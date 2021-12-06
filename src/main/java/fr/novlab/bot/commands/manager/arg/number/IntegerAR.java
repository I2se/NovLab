package fr.novlab.bot.commands.manager.arg.number;

import fr.novlab.bot.commands.manager.arg.ArgReader;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class IntegerAR implements ArgReader<Integer> {

    @Override
    public Integer read(OptionMapping optionValue) {
        return (int) optionValue.getAsLong();
    }
}
