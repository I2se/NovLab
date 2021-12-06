package fr.novlab.bot.commands.manager.arg;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public interface ArgReader<T> {

    T read(OptionMapping optionValue);
}
