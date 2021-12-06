package fr.novlab.bot.commands.manager.arg.number;

import fr.novlab.bot.commands.manager.arg.ArgReader;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class DoubleAR implements ArgReader<Double> {

    @Override
    public Double read(OptionMapping optionValue) {
        return optionValue.getAsDouble();
    }
}
