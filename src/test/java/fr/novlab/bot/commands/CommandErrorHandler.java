package fr.novlab.bot.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Represents the error handler. This is where you can modify the behaviours of when an error occurs.
 */
public class CommandErrorHandler {

    /**
     * Registry of the errors actions.
     *
     * @see CommandErrorHandler#registerErrorMessage(String, String)
     * @see CommandErrorHandler#registerErrorAction(String, Consumer)
     * @see CommandErrorHandler#getErrorsActions()
     */
    private final Map<String, Consumer<CommandContext>> errorsActions;

    /**
     * Do we need to print in the console the exception if one occurs? By default, it's set in {@link Command#Command()}
     * with the value specified in {@link CommandInfo}.
     */
    private boolean doesPrintException;

    /**
     * Main constructor of {@link CommandErrorHandler}.
     */
    public CommandErrorHandler() {
        this.errorsActions = new HashMap<>();
        this.doesPrintException = true;
    }

    /**
     * Copying the errors actions of the {@link CommandRegistry#getErrorHandlerTemplate()}.
     *
     * @param registry The registry which holds the error handler template
     */
    public void registerDefaults(CommandRegistry registry) {
        registry.getErrorHandlerTemplate().getErrorsActions().forEach(this::registerErrorAction);
    }

    /**
     * Register an error message sent to the current command sender using String format :
     * 1 : current index (int)
     * 2 : current arg (String)
     * 3 : current command name (String)
     * 4 : current command usage (String)
     *
     * @param errorName The name of the error
     * @param message   The message which will be formatted and sent
     */
    public void registerErrorMessage(String errorName, String message) {
        this.registerErrorAction(errorName, context -> context.getCurrentEvent().reply(String.format(
                message,
                context.getCurrentIndex(),
                context.getCurrentIndex() < context.getCurrentArgs().length ? context.getCurrentArgs()[context.getCurrentIndex()] : "",
                context.getCurrentCommand().getFullName(),
                context.getCurrentCommand().getFullUsage()
        )).queue());
    }

    /**
     * Register an error action for an error name.
     *
     * @param errorName The name of the error
     * @param action    The action which will be executed when the error occurs
     */
    public void registerErrorAction(String errorName, Consumer<CommandContext> action) {
        this.errorsActions.put(errorName, action);
    }

    /**
     * When an error occurs.
     *
     * @param context The command context
     */
    public void whenError(CommandContext context) {
        if (this.errorsActions.containsKey(context.getCurrentError())) {
            this.errorsActions.get(context.getCurrentError()).accept(context);
        }
    }

    /**
     * When an error happened caused by an exception.
     *
     * @param context   The command context
     * @param exception The exception which occurred
     */
    public void whenError(CommandContext context, Exception exception) {
        this.whenError(context);

        if (doesPrintException) {
            exception.printStackTrace();
        }
    }

    /**
     * Setter of {@link CommandErrorHandler#doesPrintException}
     *
     * @param doesPrintException Will we now print the exception?
     */
    public void setDoesPrintException(boolean doesPrintException) {
        this.doesPrintException = doesPrintException;
    }

    /**
     * Getter of {@link CommandErrorHandler#errorsActions}
     *
     * @return The errors action's registry
     */
    public Map<String, Consumer<CommandContext>> getErrorsActions() {
        return errorsActions;
    }

    /**
     * Getter of {@link CommandErrorHandler#doesPrintException}
     *
     * @return Do we need to print the exception?
     */
    public boolean doesPrintException() {
        return doesPrintException;
    }
}
