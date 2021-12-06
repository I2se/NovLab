package fr.novlab.bot.commands.manager.arg.number;

import fr.novlab.bot.commands.manager.arg.ArgReader;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class FloatAR implements ArgReader<Float> {

    @Override
    public Float read(OptionMapping optionValue) {
        return (float) optionValue.getAsDouble();
    }
}
