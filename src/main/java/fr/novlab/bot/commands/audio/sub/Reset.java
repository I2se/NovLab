package fr.novlab.bot.commands.audio.sub;

import fr.novlab.bot.commands.audio.FilterCommand;
import fr.novlab.bot.commands.manager.CommandInfo;
import fr.novlab.bot.commands.manager.SubCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

@CommandInfo(
        name = "reset",
        description = "Reset all effects"
)
public class Reset extends SubCommand<FilterCommand> {

    @Override
    public void execute(SlashCommandEvent event) {

    }
}
