package fr.novlab.bot.commands.manager.arg.discord;

import fr.novlab.bot.commands.manager.arg.ArgReader;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class RoleAR implements ArgReader<Role> {

    @Override
    public Role read(OptionMapping optionValue) {
        return optionValue.getAsRole();
    }
}
