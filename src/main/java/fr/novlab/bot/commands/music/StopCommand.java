package fr.novlab.bot.commands.music;

import fr.novlab.bot.commands.manager.Command;
import fr.novlab.bot.commands.manager.CommandExist;
import fr.novlab.bot.commands.manager.CommandInfo;
import fr.novlab.bot.commands.music.utils.Verification;
import fr.novlab.bot.config.Message;
import fr.novlab.bot.music.GuildMusicManager;
import fr.novlab.bot.music.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

@CommandExist
@CommandInfo(
        name = "stop",
        description = "This command is use to stop the music"
)
public class StopCommand extends Command {

    @Override
    public void execute(SlashCommandEvent event) {
        Member bot = event.getGuild().getSelfMember();
        Member member = event.getMember();
        GuildVoiceState botVS = bot.getVoiceState();
        GuildVoiceState memberVS = member.getVoiceState();

        if(!Verification.isInTheSameChannel(botVS, memberVS, event)) {
            return;
        }

        GuildMusicManager musicManager = PlayerManager.getINSTANCE().getMusicManager(event.getGuild());

        musicManager.scheduler.player.stopTrack();
        musicManager.scheduler.queue.clear();

        event.reply(Message.getMessage(Message.STOPMUSIC, event.getGuild())).queue();
    }
}
