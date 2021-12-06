package fr.novlab.bot.commands.manager.arg.discord;

import fr.novlab.bot.commands.manager.arg.ArgReader;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class UserAR implements ArgReader<User> {

    @Override
    public User read(OptionMapping optionValue) {
        return optionValue.getAsUser();
    }
}
