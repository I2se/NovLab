package fr.novlab.bot.commands.playlists;

import fr.novlab.bot.commands.manager.CommandExist;
import fr.novlab.bot.commands.manager.CommandInfo;
import fr.novlab.bot.commands.manager.CommandParent;
import fr.novlab.bot.commands.playlists.sub.Add;
import fr.novlab.bot.commands.playlists.sub.Play;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

@CommandExist
@CommandInfo(
        name = "pl",
        description = "Settings your playlist",
        subCommands = {
                Add.class,
                Play.class
        }
)
public class PlaylistCommand extends CommandParent {

    @Override
    public void execute(SlashCommandEvent event) {
    }
}
