package fr.novlab.bot.commands;

import fr.novlab.bot.commands.manager.Command;
import fr.novlab.bot.commands.manager.CommandExist;
import fr.novlab.bot.commands.manager.CommandInfo;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

@CommandExist
@CommandInfo(name = "ping",
        description = "This command is for testing the ping of the bot",
        usage = "<none>",
        permission = "ntm.perms")
public class TestCommand extends Command {

    @Override
    public void execute(SlashCommandEvent event) {

    }
}
