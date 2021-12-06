package fr.novlab.bot.commands;

import fr.novlab.bot.commands.CommandContext.Action;
import fr.novlab.bot.commands.arg.ArgReader;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public abstract class Command {

    private final CommandInfo commandInfo;
    private final Map<String, SubCommand<?>> subCommands;
    private CommandContext context;
    private CommandErrorHandler errorHandler;
    private CommandRegistry commandRegistry;
    private String[] currentArgs;

    public Command() {
        this.subCommands = new LinkedHashMap<>();
        this.setErrorHandler(new CommandErrorHandler());

        if (this.getClass().isAnnotationPresent(CommandInfo.class)) {
            this.commandInfo = this.getClass().getAnnotation(CommandInfo.class);

            // Register all sub-commands...
            for (Class<? extends SubCommand<?>> clazz : this.commandInfo.subCommands()) {
                try {
                    SubCommand<?> subCommand = clazz.getDeclaredConstructor().newInstance();
                    subCommand.setParent(this); //

                    // We also register aliases, so that it's easier to get the sub-command by its alias
                    this.subCommands.put(subCommand.getName().toLowerCase(), subCommand);
                    for (String alias : subCommand.getAliases()) {
                        this.subCommands.put(alias.toLowerCase(), subCommand);
                    }
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new IllegalStateException("Couldn't instantiate SubCommand class " + clazz.getName(), e);
                }
            }

            this.errorHandler.setDoesPrintException(this.commandInfo.doesPrintException());
        } else {
            throw new IllegalStateException("The class " + this.getClass().getName() + " needs to have a CommandInfo" +
                    " annotation in order to get infos");
        }

        // Set the context after the sub-commands have been registered, so that this also set the same context for
        // the sub-commands
        this.setContext(new CommandContext());
    }

    @SuppressWarnings("unchecked")
    public <T> T readOptionalArg(String name, T defaultValue) {
        return (T) this.readArg(name, defaultValue.getClass(), defaultValue);
    }

    @SuppressWarnings("unchecked")
    public <T> T readRequiredArg(String name, Class<T> typeClass) {
        return (T) this.readArg(name, typeClass, null);
    }

    /**
     * Parse an argument in the current args as a usable value. If the default value passed is null, this is considered
     * as a required argument, and will throw an exception if it can't read the argument. <br>
     * <br>
     * If the default value is null and the user didn't specify enough arguments, a {@link CommandException} with error
     * "WRONG_USAGE" will be thrown. Otherwise, if the {@link ArgReader} couldn't parse the argument and so return null,
     * a {@link CommandException} will be thrown with an exception computed by the {@link ArgReader} according to the
     * argument passed. The {@link CommandException} will then be caught by the {@link CommandRegistry}<br>
     * <br>
     * This is also where the context is updated.
     *
     * @param name        The name of the argument
     * @param typeClass    The type of the wanted value
     * @param defaultValue The default value if the argument couldn't be parsed
     * @return The value that could be read
     */
    public Object readArg(String name, Class<?> typeClass, Object defaultValue) {
        // Update context
        this.resetPartiallyContext()
                .setCurrentAction(Action.READING_ARGUMENT)
                .setCurrentArgName(name);

        // Parse the argument
        String arg = null;
        ArgReader<?> argReader = this.commandRegistry.getArgReader(typeClass);
        this.context.setCurrentArgReader(argReader);
        Object value = argReader.read(arg);

        this.context.getCurrentEvent().getSubcommandName();
        if (value != null) { // Was the parsing successful?
            return value;
        } else if (defaultValue != null) { // Is it required?
            return defaultValue; // Optional
        } else {
            throw new CommandException(argReader.errorCause(arg)); // Required
        }
    }

    public void checkPermission(SlashCommandEvent event, String permission) {
        // TODO ADD PERMISSION CHECK

        /*// Update context
        this.resetPartiallyContext()
                .setCurrentAction(Action.CHECKING_PERMISSION)
                .setCurrentPermission(permission);

        // Check permission
        if (!sender.hasPermission(permission) && !permission.equals("")) {
            throw new CommandException("NOT_ENOUGH_PERMISSION");
        }*/
    }

    /**
     * This function after the CommandRegistry was set. This is where we register the default messages.
     * <p>
     * You can listen to this function in order to replace the error handler
     */
    public void init() {
        this.errorHandler.registerDefaults(this.commandRegistry);
    }

    public void internallyExecute(SlashCommandEvent event, String[] args) {
        // Set current args
        this.currentArgs = args;

        // Check permission
        if (this.commandInfo.autoCheckPermission()) {
            this.checkPermission(event, this.getPermission());
        }

        // Auto-manage sub-commands if it is enabled
        if (this.commandInfo.autoManagingSubCommands() && this.commandInfo.subCommandIndex() == 0) {

            // Update the context
            this.updateContext(event, args);

            boolean result = this.executeSubCommands(event);

            if (result) { // Has a sub-command been executed?
                return; // Yes!
            }
        }

        // Update the context
        this.updateContext(event, args);

        // Called the main execute function
        this.execute(event);
    }

    public boolean executeSubCommands(SlashCommandEvent event) {
        int index = this.commandInfo.subCommandIndex();

        if (this.currentArgs.length > index && index >= 0) { // Check if the command sender specifies enough arguments

            // Update the context
            String arg = this.currentArgs[index];

            if (this.subCommands.containsKey(arg)) { // Tries to find a sub-command

                // Execute the sub-command
                this.subCommands.get(arg).internallyExecute(
                        event,
                        this.currentArgs.length > index + 1 ? Arrays.copyOfRange(this.currentArgs, index + 1,
                                this.currentArgs.length) : new String[0]
                );

                return true; // A sub-command have been executed
            }
        }

        return false; // No sub-command have been executed
    }

    /**
     * Called when the command is executed.
     *
     * @param event The command event
     */
    public void execute(SlashCommandEvent event) {
    }

    public void updateContext(SlashCommandEvent event, String[] args) {
        this.resetPartiallyContext()
                .setCurrentCommand(this)
                .setCurrentEvent(event)
                .setCurrentArgs(args);
    }

    /**
     * Reset partially the context.
     *
     * @return The context
     */
    public CommandContext resetPartiallyContext() {
        return this.context
                .setCurrentArgs(this.currentArgs)
                .setCurrentAction(Action.NONE)
                .setCurrentPermission("")
                .setCurrentArgReader(null);
    }

    /**
     * Getter of all sub-commands recursively. So that, it also calls this function with the sub-commands. Be aware that
     * the returned {@link Set} will not contain the command itself.
     *
     * @return All the sub-commands
     */
    public Set<SubCommand<?>> getAllSubCommands() {
        Set<SubCommand<?>> subCommandsSet = new HashSet<>();

        this.subCommands.values().forEach(subCommand -> {
            subCommandsSet.add(subCommand);
            subCommandsSet.addAll(subCommand.getAllSubCommands());
        });

        return subCommandsSet;
    }

    /**
     * Get the main command name. This is mainly used by {@link SubCommand#getFullUsage()} in order to just have the
     * root name. If this is sub-command, this will call this method on his parent.
     *
     * @return The root command name
     * @see SubCommand#getRootCommandName()
     */
    public String getRootCommandName() {
        return this.getFullName();
    }

    /**
     * Get he full name of the command. If this is called on a sub-command, the sub-command name will be added with
     * his parent name. Be aware that this is not made to be displayed with the usage, this will not take into account
     * {@link CommandInfo#subCommandIndex()} of the command. See {@link Command#getFullUsage()}, which is more
     * suitable for this usage.
     *
     * @return The full name of the command
     * @see SubCommand#getFullName()
     */
    public String getFullName() {
        return "/" + this.getName();
    }

    /**
     * Get the full usage, with the command name included. If this is called on a sub-command, the sub-command usage
     * will be inserted in the usage at the index {@link CommandInfo#subCommandIndex()}.
     *
     * @return The full usage of the command
     */
    public String getFullUsage() {
        return this.getFullName() + (!this.getUsage().equals("") ? " " + this.getUsage() : "");
    }

    /**
     * Getter of {@link Command#commandRegistry}.
     *
     * @return The command registry of the command
     * @see Command#setCommandRegistry(CommandRegistry)
     */
    public CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }

    /**
     * Setter of the {@link Command#commandRegistry}. This will also set the command registry for the sub-commands.
     * Use only this method if you know what you are doing.
     *
     * @param commandRegistry The new command registry
     * @see Command#getCommandRegistry()
     */
    public void setCommandRegistry(CommandRegistry commandRegistry) {
        this.commandRegistry = commandRegistry;

        this.subCommands.values().forEach(subCommand -> subCommand.setCommandRegistry(commandRegistry));
    }

    /**
     * @return The length of the current args
     * @see Command#getCurrentArgs()
     */
    public int getCurrentArgsLength() {
        return this.getCurrentArgs().length;
    }

    /**
     * Getter of {@link Command#currentArgs}.
     *
     * @return The current args of the command
     */
    public String[] getCurrentArgs() {
        return currentArgs;
    }

    /**
     * Overloading getter of {@link CommandInfo#name()}.
     *
     * @return The name of the command
     */
    public String getName() {
        return this.getCommandInfo().name();
    }

    /**
     * Overloading getter of {@link CommandInfo#usage()}.
     *
     * @return The usage of the command
     */
    public String getUsage() {
        return this.getCommandInfo().usage();
    }

    /**
     * Overloading getter of {@link CommandInfo#aliases()}.
     *
     * @return The aliases of the command
     */
    public String[] getAliases() {
        return this.getCommandInfo().aliases();
    }

    /**
     * Overloading getter of {@link CommandInfo#description()}.
     *
     * @return The description of the command
     */
    public String getDescription() {
        return this.getCommandInfo().description();
    }

    /**
     * Overloading getter of {@link CommandInfo#permission()}.
     *
     * @return The permission of the command
     */
    public String getPermission() {
        return this.getCommandInfo().permission();
    }
    
    public Map<String, SubCommand<?>> getSubCommands() {
        return subCommands;
    }

    public CommandInfo getCommandInfo() {
        return commandInfo;
    }

    public CommandContext getContext() {
        return context;
    }

    public void setContext(CommandContext context) {
        this.context = context;

        this.subCommands.values().forEach(subCommand -> subCommand.setContext(context));
    }

    public CommandErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public void setErrorHandler(CommandErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }
}
