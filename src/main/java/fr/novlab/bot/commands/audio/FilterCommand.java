package fr.novlab.bot.commands.audio;

import fr.novlab.bot.commands.audio.sub.*;
import fr.novlab.bot.commands.manager.CommandExist;
import fr.novlab.bot.commands.manager.CommandInfo;
import fr.novlab.bot.commands.manager.CommandParent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

@CommandExist
@CommandInfo(
        name = "filter",
        description = "This command apply filter",
        subCommands = {
                BassBoost.class,
                Reset.class,
                Distortion.class,
                Karaoke.class,
                Timescale.class,
                Tremolo.class,
                Vibrato.class
        }
)
public class FilterCommand extends CommandParent {

    @Override
    public void execute(SlashCommandEvent event) {
    }
}
