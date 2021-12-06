package fr.novlab.bot.commands.manager.arg;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class StringAR implements ArgReader<String> {

    @Override
    public String read(OptionMapping optionValue) {
        return optionValue.getAsString();
    }
}
