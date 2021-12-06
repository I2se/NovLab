package fr.novlab.bot.commands;

/**
 * Represents when an error occurs. This is used in order to avoid a lot of if conditions, so that when we think an
 * error is occurring (for example, requiring a player, or reading an integer but the specified argument is not a number),
 * we can throw this error and handle it in the {@link CommandErrorHandler}.
 */
public class CommandException extends RuntimeException {

    /**
     * Constructor of {@link CommandException}.
     *
     * @param errorName The name of the error
     */
    public CommandException(String errorName) {
        super(errorName);
    }

    /**
     * Constructor of {@link CommandException}. This constructor is mostly called when an unexpected error occurs.
     *
     * @param errorName The name of the error
     * @param cause     The cause of the error
     */
    public CommandException(String errorName, Throwable cause) {
        super(errorName, cause);
    }
}
