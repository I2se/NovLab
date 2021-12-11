package fr.novlab.bot.commands.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import fr.novlab.bot.commands.manager.Command;
import fr.novlab.bot.commands.manager.CommandExist;
import fr.novlab.bot.commands.manager.CommandInfo;
import fr.novlab.bot.config.Message;
import fr.novlab.bot.music.GuildMusicManager;
import fr.novlab.bot.music.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

@CommandExist
@CommandInfo(
        name = "filter",
        description = "This command apply filter",
        usage = ""
)
public class FilterCommand extends Command {

    @Override
    public void execute(SlashCommandEvent event) {
        Member bot = event.getGuild().getSelfMember();
        GuildVoiceState botVoiceState = bot.getVoiceState();
        Member member = event.getMember();

        if (!botVoiceState.inVoiceChannel()) {
            event.reply(Message.getMessage(Message.BOTNOTINVOICECHANNEL, event.getGuild())).queue();
            return;
        }

        GuildVoiceState memberVoiceState = member.getVoiceState();

        if(!memberVoiceState.inVoiceChannel()) {
            event.reply(Message.getMessage(Message.NOTINVOICECHANNEL, event.getGuild())).queue();
            return;
        }

        if(!memberVoiceState.getChannel().equals(botVoiceState.getChannel())) {
            event.reply(Message.getMessage(Message.NOTINSAMEVOICECHANNEL, event.getGuild())).queue();
            return;
        }

        GuildMusicManager musicManager = PlayerManager.getINSTANCE().getMusicManager(event.getGuild());
        AudioPlayer audioPlayer = musicManager.audioPlayer;

        if(audioPlayer.getPlayingTrack() == null) {
            event.reply(Message.getMessage(Message.NOTRACKCURRENTLY, event.getGuild())).queue();
            return;
        }

    }
}
