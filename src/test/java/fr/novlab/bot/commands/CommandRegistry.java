package fr.novlab.bot.commands;

import fr.novlab.bot.commands.arg.ArgReader;
import fr.novlab.bot.commands.arg.BooleanAR;
import fr.novlab.bot.commands.arg.CharAR;
import fr.novlab.bot.commands.arg.StringAR;
import fr.novlab.bot.commands.arg.number.*;
import fr.novlab.bot.commands.manager.CommandParent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CommandRegistry extends ListenerAdapter implements EventListener {

    // <arg_name='description':arg_type> or [arg_name='description':arg_type]
    private static final Pattern OPTION_PATTERN = Pattern.compile("([<\\[])([a-zA-Z0-9]+)(?:[=]'([^'\\\\]*(?:\\\\.[^'\\\\]*)*)')?(?:[:]([a-zA-Z0-9]+))?[>\\]]");

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandRegistry.class);

    private static final Map<String, OptionType> OPTION_TYPES = new HashMap<>() {{
        put("str", OptionType.STRING);
        put("int", OptionType.INTEGER);
        put("bool", OptionType.BOOLEAN);
        put("user", OptionType.USER);
        put("channel", OptionType.CHANNEL);
        put("role", OptionType.ROLE);
        put("mentionable", OptionType.MENTIONABLE);
        put("number", OptionType.NUMBER);
    }};

    /**
     * The jda's instance
     *
     * @see CommandRegistry#CommandRegistry(JDA)
     * @see CommandRegistry#getJDA()
     */
    private final JDA jda;

    /**
     * Registry of {@link ArgReader}.
     *
     * @see CommandRegistry#registerArgReader(Class, ArgReader)
     * @see CommandRegistry#registerDefaults()
     * @see CommandRegistry#getArgReader(Object)
     * @see CommandRegistry#getArgReaderRegistry()
     */
    private final Map<Class<?>, ArgReader<?>> argReaderRegistry;

    /**
     * Template of the default {@link CommandErrorHandler}.
     *
     * @see CommandRegistry#getErrorHandlerTemplate()
     * @see CommandRegistry#registerDefaults()
     */
    private final CommandErrorHandler errorHandlerTemplate;

    /**
     * The commands registry
     */
    private final Map<String, Command> commands;

    /**
     * Main constructor of {@link CommandRegistry}.
     *
     * @param jda The jda's instance
     */
    public CommandRegistry(JDA jda) {
        this.jda = jda;

        this.argReaderRegistry = new HashMap<>();
        this.errorHandlerTemplate = new CommandErrorHandler();
        this.commands = new HashMap<>();
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        if (this.commands.containsKey(event.getName())) {
            Command command = this.commands.get(event.getName());

            System.out.println(event.getCommandString());

            try {
                command.internallyExecute(event, new String[0]);
            } catch (CommandException e) {
                command.getErrorHandler().whenError(command.getContext().setCurrentError(e.getMessage()));
            } catch (Exception e) {
                command.getErrorHandler().whenError(command.getContext().setCurrentError("EXCEPTION"), e);
            }
        }
    }

    public void registerDefaults() {

        // Register default argument readers
        this.registerArgReader(byte.class, new ByteAR());
        this.registerArgReader(Byte.class, new ByteAR());
        this.registerArgReader(short.class, new ShortAR());
        this.registerArgReader(Short.class, new ShortAR());
        this.registerArgReader(int.class, new IntegerAR());
        this.registerArgReader(Integer.class, new IntegerAR());
        this.registerArgReader(long.class, new LongAR());
        this.registerArgReader(Long.class, new LongAR());
        this.registerArgReader(float.class, new FloatAR());
        this.registerArgReader(Float.class, new FloatAR());
        this.registerArgReader(double.class, new DoubleAR());
        this.registerArgReader(Double.class, new DoubleAR());

        this.registerArgReader(boolean.class, new BooleanAR());
        this.registerArgReader(Boolean.class, new BooleanAR());
        this.registerArgReader(char.class, new CharAR());
        this.registerArgReader(Character.class, new CharAR());
        this.registerArgReader(String.class, new StringAR());

        // Register default sentence readers


        // Register default error actions
        this.errorHandlerTemplate.registerErrorMessage("EXCEPTION", "§cException happened while reading the arg \"%2$s\" at index %1$d of the command \"%3$s\".");
        this.errorHandlerTemplate.registerErrorMessage("CRITICAL", "§cCritical error happened at arg \"%2$s\" of index %1$d with the command \"%3$s\".");

        this.errorHandlerTemplate.registerErrorMessage("ARG_NULL", "§cThe arg \"%2$s\" at index %1$d in the command \"%3$s\" is null!");
        this.errorHandlerTemplate.registerErrorMessage("EMPTY_STRING", "§cThe arg \"%2$s\" at index %1$d in the command \"%3$s\" is an empty String!");

        this.errorHandlerTemplate.registerErrorMessage("BOOLEAN_NON_VALID_FORMAT", "§cThe arg \"%2$s\" at index %1$d in the command \"%3$s\" is not a valid Boolean!");
        this.errorHandlerTemplate.registerErrorMessage("BYTE_NON_VALID_FORMAT", "§cThe arg \"%2$s\" at index %1$d in the command \"%3$s\" is not a valid Byte!");
        this.errorHandlerTemplate.registerErrorMessage("SHORT_NON_VALID_FORMAT", "§cThe arg \"%2$s\" at index %1$d in the command \"%3$s\" is not a valid Short!");
        this.errorHandlerTemplate.registerErrorMessage("INTEGER_NON_VALID_FORMAT", "§cThe arg \"%2$s\" at index %1$d in the command \"%3$s\" is not a valid Integer!");
        this.errorHandlerTemplate.registerErrorMessage("LONG_NON_VALID_FORMAT", "§cThe arg \"%2$s\" at index %1$d in the command \"%3$s\" is not a valid Long!");
        this.errorHandlerTemplate.registerErrorMessage("FLOAT_NON_VALID_FORMAT", "§cThe arg \"%2$s\" at index %1$d in the command \"%3$s\" is not a valid Float!");
        this.errorHandlerTemplate.registerErrorMessage("DOUBLE_NON_VALID_FORMAT", "§cThe arg \"%2$s\" at index %1$d in the command \"%3$s\" is not a valid Double!");

        this.errorHandlerTemplate.registerErrorMessage("WRONG_USAGE", "§cWrong usage : \"%4$s\"");
        this.errorHandlerTemplate.registerErrorMessage("NOT_ENOUGH_PERMISSION", "§cYou don't have enough permissions to do that!");
    }

    /**
     * Register a {@link ArgReader}.
     *
     * @param typeClass      The type class
     * @param argReaderClass The argument reader instance
     * @param <T>            The type parameter
     * @see CommandRegistry#getArgReader(Object)
     */
    public <T> void registerArgReader(Class<T> typeClass, ArgReader<T> argReaderClass) {
        this.argReaderRegistry.put(typeClass, argReaderClass);
    }


    /**
     * Get a {@link ArgReader} from a type.
     *
     * @param typeClass The type class
     * @param <T>       The type parameter
     * @return The argument reader instance for the specified type
     */
    @SuppressWarnings("all")
    public <T> ArgReader<T> getArgReader(T typeClass) {
        return (ArgReader<T>) this.argReaderRegistry.get(typeClass);
    }

    /**
     * Get a {@link SentenceReader} from a type.
     *
     * @param typeClass The type class
     * @param <T>       The type paramter
     * @return The sentence reader instance for the specified type
     */
    @SuppressWarnings("all")

    /**
     * Register all commands of a specified package. It will search for all class in the package which are subtype of
     * {@link Command}. Before trying to register a command, it will check these conditions : <br>
     * - The class is not PSubCommand <br>
     * - The class is not abstract and is not an interface <br>
     * - The annotation {@link CommandExist} is present <br>
     * - The class is not a sub-command (the class doesn't have {@link SubCommand} in his superclasses) <br>
     * - The class has an empty constructor <br>
     * If these conditions are met, the command will be registered.
     *
     * @param packageName The package name
     */
    public void registerAllCommandsIn(String packageName) {
        Reflections reflections = new Reflections(packageName);

        reflections.getSubTypesOf(Command.class).forEach(commandClass -> {
            try {
                if (commandClass.getPackage().getName().startsWith(packageName) && commandClass != SubCommand.class
                        && !Modifier.isAbstract(commandClass.getModifiers()) && !Modifier.isInterface(commandClass.getModifiers())
                        && commandClass.isAnnotationPresent(CommandExist.class)) {
                    try {
                        commandClass.asSubclass(SubCommand.class); // check if it's not a sub command
                        return;
                    } catch (ClassCastException ignored) {
                    }
                    commandClass.getDeclaredConstructor(); // check if it has an empty constructor

                    this.registerCommand(commandClass);
                }
            } catch (NoSuchMethodException ignored) {
            }
        });
    }

    private List<OptionData> parseOptionsFromCommand(Command command) {
        List<OptionData> options = new ArrayList<>();
        String usage = command.getUsage();

        List<String> args = new ArrayList<>();
        for (int i = 0; i < usage.length(); i++) {
            int j = i + 1;

            for (; j <= usage.length(); j++) {
                Matcher matcher = OPTION_PATTERN.matcher(usage.substring(i, j));

                if (matcher.find()) {
                    System.out.println(usage.substring(i, j));
                    args.add(usage.substring(i, j));

                    i = j;
                    break;
                }
            }
        }

        for (String arg : args) {
            Matcher matcher = OPTION_PATTERN.matcher(arg);
            System.out.println(arg);
            System.out.println(matcher.find());

            boolean required = matcher.group(1).equals("<");
            String argName = matcher.group(2);
            String description = matcher.group(3) != null ? matcher.group(3).replaceAll("\\\\'", "'").replaceAll("\\\\\\\\", "\\\\") : "unknown";
            OptionType argType = OPTION_TYPES.getOrDefault(matcher.group(4).toLowerCase(), OptionType.STRING);

            options.add(new OptionData(argType, argName, description, required));
        }

        return options;
    }

    public boolean areOptionsEqual(List<OptionData> options1, List<OptionData> options2) {
        if (options1.size() != options2.size())
            return false;

        for (OptionData option1 : options1) {
            boolean result = false; // are equals

            for (OptionData option2 : options2) {
                if (option1.getName().equals(option2.getName())) {
                    if (option1.getType().equals(option2.getType()) &&
                            option1.getDescription().equals(option2.getDescription()) &&
                            option1.isRequired() == option2.isRequired()) {
                        result = true;
                    }
                    break;
                }
            }

            if (!result) {
                return false;
            }
        }

        return true;
    }

    public void updateDiscord() {
        LOGGER.info("Update Discord commands");

        List<net.dv8tion.jda.api.interactions.commands.Command> discordCommands = this.jda.retrieveCommands().complete();
        for (net.dv8tion.jda.api.interactions.commands.Command discordCommand : discordCommands) {
            if (this.commands.containsKey(discordCommand.getName())) {
                Command command = this.commands.get(discordCommand.getName());

                List<OptionData> options = this.parseOptionsFromCommand(command);
                if (!discordCommand.getDescription().equals(command.getDescription()) || !this.areOptionsEqual(discordCommand.getOptions().stream()
                        .map(option -> new OptionData(option.getType(), option.getName(), option.getDescription(), option.isRequired())).collect(Collectors.toList()),
                        options)) {

                    LOGGER.info("Command \"" + command.getName() + "\" need to be edited!");
                    this.jda.editCommandById(discordCommand.getId())
                            .setDescription(command.getDescription())
                            .clearOptions()
                            .addOptions(options)
                            .queue();
                }
            } else {
                LOGGER.info("Command \"" + discordCommand.getName() + "\" need to be deleted!");
                this.jda.deleteCommandById(discordCommand.getId()).queue();
            }
        }

        this.commands.forEach((name, command) -> {
            if (discordCommands.stream().noneMatch(discordCommand -> discordCommand.getName().equals(name))) {
                LOGGER.info("Command \"" + command.getName() + "\" need to be created!");
                this.jda.upsertCommand(command.getName(), command.getDescription())
                        .addOptions(this.parseOptionsFromCommand(command))
                        .queue();
            }
        });
    }

    /**
     * Register a command.
     *
     * @param clazz The command class
     */
    public void registerCommand(Class<? extends CommandParent> clazz) {
        try {
            CommandParent command = clazz.getDeclaredConstructor().newInstance();
            command.setCommandRegistry(this);
            command.init();

            this.commands.put(command.getName(), command);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException("Couldn't instantiate Command class " + clazz.getName(), e);
        }
    }
    /**
     * Getter of {@link CommandRegistry#argReaderRegistry}.
     *
     * @return The {@link ArgReader} registry
     */
    public Map<Class<?>, ArgReader<?>> getArgReaderRegistry() {
        return argReaderRegistry;
    }

    /**
     * Getter of {@link CommandRegistry#errorHandlerTemplate}.
     *
     * @return The error handler template
     */
    public CommandErrorHandler getErrorHandlerTemplate() {
        return errorHandlerTemplate;
    }

    /**
     * Getter of {@link CommandRegistry#jda}
     *
     * @return The jda instance
     */
    public JDA getJDA() {
        return jda;
    }
}
