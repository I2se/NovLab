package fr.novlab.bot.commands.manager.arg;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class CharAR implements ArgReader<Character> {

    @Override
    public Character read(OptionMapping optionValue) {
        return optionValue.getAsString().length() > 0 ? optionValue.getAsString().charAt(0) : null;
    }
}
