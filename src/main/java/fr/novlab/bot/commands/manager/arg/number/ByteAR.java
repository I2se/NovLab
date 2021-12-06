package fr.novlab.bot.commands.manager.arg.number;

import fr.novlab.bot.commands.manager.arg.ArgReader;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class ByteAR implements ArgReader<Byte> {

    @Override
    public Byte read(OptionMapping optionValue) {
        return (byte) optionValue.getAsLong();
    }
}
