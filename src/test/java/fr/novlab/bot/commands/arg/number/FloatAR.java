package fr.novlab.bot.commands.arg.number;

/**
 * Represents the {@link Float} argument parser.
 */
public class FloatAR extends NumberAR<Float> {

    /**
     * Parse a {@link String} as a {@link Float}
     *
     * @param string The {@link String} to parse
     * @return The {@link Float} value
     */
    @Override
    public Float valueOf(String string) {
        return Float.parseFloat(string);
    }

    /**
     * Return the name of the error. <br>
     * With a {@link FloatAR}, the errors can be : <br>
     * - "FLOAT_NON_VALID_FORMAT" : The argument is not a valid {@link Float}
     *
     * @param arg The argument
     * @return The error name
     */
    @Override
    public String errorCause(String arg) {
        return "FLOAT_NON_VALID_FORMAT";
    }

    /**
     * @return The display name
     */
    @Override
    public String getDisplayName() {
        return "Float";
    }
}
