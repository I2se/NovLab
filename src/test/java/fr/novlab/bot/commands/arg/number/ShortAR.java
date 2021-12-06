package fr.novlab.bot.commands.arg.number;

/**
 * Represents the {@link Short} argument parser.
 */
public class ShortAR extends NumberAR<Short> {

    /**
     * Parse a {@link String} as a {@link Short}
     *
     * @param string The {@link String} to parse
     * @return The {@link Short} value
     */
    @Override
    public Short valueOf(String string) {
        return Short.parseShort(string);
    }

    /**
     * Return the name of the error. <br>
     * With a {@link ShortAR}, the errors can be : <br>
     * - "SHORT_NON_VALID_FORMAT" : The argument is not a valid {@link Short}
     *
     * @param arg The argument
     * @return The error name
     */
    @Override
    public String errorCause(String arg) {
        return "SHORT_NON_VALID_FORMAT";
    }

    /**
     * @return The display name
     */
    @Override
    public String getDisplayName() {
        return "Short";
    }
}
