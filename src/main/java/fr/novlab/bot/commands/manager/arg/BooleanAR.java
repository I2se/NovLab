package fr.novlab.bot.commands.manager.arg;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class BooleanAR implements ArgReader<Boolean> {

    @Override
    public Boolean read(OptionMapping optionValue) {
        return optionValue.getAsBoolean();
    }
}
