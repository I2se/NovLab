package fr.novlab.bot.commands.playlists.sub;

import fr.novlab.bot.commands.manager.CommandInfo;
import fr.novlab.bot.commands.manager.SubCommand;
import fr.novlab.bot.commands.playlists.PlaylistCommand;
import fr.novlab.bot.config.Message;
import fr.novlab.bot.database.playlists.PlaylistService;
import fr.novlab.bot.music.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.List;

@CommandInfo(
        name = "play",
        description = "Play The Playlist"
)
public class Play extends SubCommand<PlaylistCommand> {

    @Override
    public void execute(SlashCommandEvent event) {
        TextChannel channel = (TextChannel) event.getChannel();
        Member bot = event.getGuild().getSelfMember();
        Member member = event.getMember();
        GuildVoiceState botVoiceState = bot.getVoiceState();
        GuildVoiceState memberVoiceState = member.getVoiceState();

        if(!memberVoiceState.inAudioChannel()) {
            event.reply(Message.getMessage(Message.NOTINVOICECHANNEL, event.getGuild())).queue();
            return;
        }

        AudioManager audioManager = channel.getGuild().getAudioManager();
        VoiceChannel memberChannel = (VoiceChannel) memberVoiceState.getChannel();

        if(!channel.getGuild().getAudioManager().isConnected()) {
            audioManager.openAudioConnection(memberChannel);
        } else {
            if(!memberVoiceState.getChannel().equals(botVoiceState.getChannel())) {
                event.reply(Message.getMessage(Message.NOTINSAMEVOICECHANNEL, event.getGuild())).queue();
                return;
            }
        }

        List<String> allTracks = PlaylistService.getPlaylist(event.getUser().getId()).getPlaylist();
        for (String track : allTracks) {
            PlayerManager.getINSTANCE().loadAndPlay(event, track);
        }
        event.reply("All your musics has been added to the queue").queue();
    }
}
