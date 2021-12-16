package fr.novlab.bot.commands.audio.sub;

import fr.novlab.bot.commands.audio.FilterCommand;
import fr.novlab.bot.commands.manager.CommandInfo;
import fr.novlab.bot.commands.manager.SubCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

@CommandInfo(
        name = "distortion",
        description = "Effect Distortion"
)
public class Distortion extends SubCommand<FilterCommand> {

    @Override
    public void execute(SlashCommandEvent event) {

    }
}
