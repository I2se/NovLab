package fr.novlab.bot.commands.arg.number;

import fr.novlab.bot.commands.arg.ArgReader;

/**
 * Represents the {@link Number} argument parser.
 */
public abstract class NumberAR<T extends Number> implements ArgReader<T> {

    /**
     * Parse an argument as a {@link Number}.
     *
     * @param arg The argument
     * @return The {@link Number} value
     */
    @Override
    public T read(String arg) {
        try {
            return valueOf(arg);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Parse a {@link String} as a {@link Number}
     *
     * @param string The {@link String} to parse
     * @return The {@link Number} value
     */
    public abstract T valueOf(String string);
}
