package fr.novlab.bot.commands.audio.sub;

import fr.novlab.bot.commands.audio.FilterCommand;
import fr.novlab.bot.commands.manager.CommandInfo;
import fr.novlab.bot.commands.manager.SubCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

@CommandInfo(
        name = "bassboost",
        description = "Effect Bass Boost"
)
public class BassBoost extends SubCommand<FilterCommand> {

    @Override
    public void execute(SlashCommandEvent event) {

    }
}
