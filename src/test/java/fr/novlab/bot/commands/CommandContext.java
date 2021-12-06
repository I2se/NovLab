package fr.novlab.bot.commands;

import fr.novlab.bot.commands.arg.ArgReader;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

/**
 * Represents the context of the command. This keep track of a lot of information.
 */
public class CommandContext {

    /**
     * The current command.
     *
     * @see CommandContext#setCurrentCommand(Command)
     * @see CommandContext#getCurrentCommand()
     */
    private Command currentCommand;

    /**
     * The current event.
     *
     * @see CommandContext#setCurrentEvent(SlashCommandEvent)
     * @see CommandContext#getCurrentEvent()
     */
    private SlashCommandEvent currentEvent;

    /**
     * The current arguments.
     *
     * @see CommandContext#setCurrentArgs(String[])
     * @see CommandContext#getCurrentArgs()
     */
    private String[] currentArgs;

    /**
     * The current argName.
     *
     * @see CommandContext#setCurrentArgName(String)
     * @see CommandContext#getCurrentArgName()
     */
    private String currentArgName;

    /**
     * The current {@link Action}.
     *
     * @see CommandContext#setCurrentAction(Action)
     * @see CommandContext#getCurrentAction()
     */
    private Action currentAction;

    /**
     * The current permission.
     *
     * @see CommandContext#setCurrentPermission(String)
     * @see CommandContext#getCurrentPermission()
     */
    private String currentPermission;

    /**
     * The current {@link ArgReader}.
     *
     * @see CommandContext#setCurrentArgReader(ArgReader)
     * @see CommandContext#getCurrentArgReader()
     */
    private ArgReader<?> currentArgReader;

    /**
     * The current error.
     *
     * @see CommandContext#setCurrentError(String)
     * @see CommandContext#getCurrentError()
     */
    private String currentError;

    /**
     * Getter of {@link CommandContext#currentCommand}.
     *
     * @return The current command
     */
    public Command getCurrentCommand() {
        return currentCommand;
    }

    /**
     * Setter of {@link CommandContext#currentCommand}.
     *
     * @param currentCommand The new current command
     * @return The context for chaining
     */
    public CommandContext setCurrentCommand(Command currentCommand) {
        this.currentCommand = currentCommand;
        return this;
    }

    /**
     * Getter of {@link CommandContext#currentEvent}.
     *
     * @return The current command sender
     */
    public SlashCommandEvent getCurrentEvent() {
        return currentEvent;
    }

    /**
     * Setter of {@link CommandContext#currentEvent}.
     *
     * @param currentEvent The new current command event
     * @return The context for chaining
     */
    public CommandContext setCurrentEvent(SlashCommandEvent currentEvent) {
        this.currentEvent = currentEvent;
        return this;
    }

    /**
     * Getter of {@link CommandContext#currentArgs}.
     *
     * @return The current arguments
     */
    public String[] getCurrentArgs() {
        return currentArgs;
    }

    /**
     * Setter of {@link CommandContext#currentArgs}.
     *
     * @param currentArgs The new current arguments
     * @return The context for chaining
     */
    public CommandContext setCurrentArgs(String[] currentArgs) {
        this.currentArgs = currentArgs;
        return this;
    }

    /**
     * Getter of {@link CommandContext#currentArgName}.
     *
     * @return The current argName
     */
    public String getCurrentArgName() {
        return currentArgName;
    }

    /**
     * Setter of {@link CommandContext#currentArgName}.
     *
     * @param currentArgName The new current argName
     * @return The context for chaining
     */
    public CommandContext setCurrentArgName(String currentArgName) {
        this.currentArgName = currentArgName;
        return this;
    }

    /**
     * Getter of {@link CommandContext#currentAction}.
     *
     * @return The current {@link Action}
     */
    public Action getCurrentAction() {
        return currentAction;
    }

    /**
     * Setter of {@link CommandContext#currentAction}.
     *
     * @param currentAction The new current {@link Action}
     * @return The context for chaining
     */
    public CommandContext setCurrentAction(Action currentAction) {
        this.currentAction = currentAction;
        return this;
    }

    /**
     * Getter of {@link CommandContext#currentPermission}.
     *
     * @return The current permission
     */
    public String getCurrentPermission() {
        return currentPermission;
    }

    /**
     * Setter of {@link CommandContext#currentPermission}.
     *
     * @param currentPermission The new current permission
     * @return The context for chaining
     */
    public CommandContext setCurrentPermission(String currentPermission) {
        this.currentPermission = currentPermission;
        return this;
    }

    /**
     * Getter of {@link CommandContext#currentArgReader}.
     *
     * @return The current {@link ArgReader}
     */
    public ArgReader<?> getCurrentArgReader() {
        return currentArgReader;
    }

    /**
     * Setter of {@link CommandContext#currentArgReader}.
     *
     * @param currentArgReader The new current {@link ArgReader}
     * @return The context for chaining
     */
    public CommandContext setCurrentArgReader(ArgReader<?> currentArgReader) {
        this.currentArgReader = currentArgReader;
        return this;
    }

    /**
     * Getter of {@link CommandContext#currentError}.
     *
     * @return The current error
     */
    public String getCurrentError() {
        return currentError;
    }

    /**
     * Setter of {@link CommandContext#currentError}.
     *
     * @param currentError The new current error
     * @return The context for chaining
     */
    public CommandContext setCurrentError(String currentError) {
        this.currentError = currentError;
        return this;
    }

    public enum Action {

        NONE,
        READING_ARGUMENT,
        READING_SENTENCE,
        REQUIRING_PLAYER,
        CHECKING_PERMISSION
    }
}
