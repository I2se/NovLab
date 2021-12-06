package fr.novlab.bot.commands.manager.arg.number;

import fr.novlab.bot.commands.manager.arg.ArgReader;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class LongAR implements ArgReader<Long> {

    @Override
    public Long read(OptionMapping optionValue) {
        return optionValue.getAsLong();
    }
}
