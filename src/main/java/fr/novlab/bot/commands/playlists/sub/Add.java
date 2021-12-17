package fr.novlab.bot.commands.playlists.sub;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.novlab.bot.commands.manager.CommandInfo;
import fr.novlab.bot.commands.manager.SubCommand;
import fr.novlab.bot.commands.playlists.PlaylistCommand;
import fr.novlab.bot.config.Message;
import fr.novlab.bot.database.playlists.PlaylistData;
import fr.novlab.bot.database.playlists.PlaylistService;
import fr.novlab.bot.music.AudioHelper;
import fr.novlab.bot.music.GuildMusicManager;
import fr.novlab.bot.music.LinkType;
import fr.novlab.bot.music.PlayerManager;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.List;

@CommandInfo(
        name = "add",
        description = "Add A Music To Your Playlist",
        usage = "[link='Link':str]"
)
public class Add extends SubCommand<PlaylistCommand> {

    @Override
    public void execute(SlashCommandEvent event) {
        if(!this.isArgPresent("link")) {
            GuildMusicManager guildMusicManager = PlayerManager.getINSTANCE().getMusicManager(event.getGuild());
            AudioPlayer audioPlayer = guildMusicManager.audioPlayer;
            if(audioPlayer.getPlayingTrack() != null) {
                AudioTrack audioTrack = audioPlayer.getPlayingTrack();
                User user = event.getUser();
                if(PlaylistService.isRegistered(user.getId())) {
                    PlaylistService.updatePlaylist(user.getId(), playlistData -> {
                        playlistData.getPlaylist().add(audioTrack.getInfo().uri);
                        if(!playlistData.getGuilds().contains(event.getGuild().getId())) {
                            playlistData.getGuilds().add(event.getGuild().getId());
                        }
                    });
                } else {
                    PlaylistData playlistData = new PlaylistData(user.getId(), user.getAsTag(), List.of(event.getGuild().getId()), List.of(audioTrack.getInfo().uri));
                    PlaylistService.addPlaylist(playlistData);
                }
                event.reply(Message.getMessage(Message.PLADD, event.getGuild(), audioTrack.getInfo().title)).queue();
            } else {
                event.reply(Message.getMessage(Message.NOTRACKCURRENTLY, event.getGuild())).queue();
            }
        } else {
            String link = this.readOptionalArg("link", "");
            AudioTrack audioTrack = null;

            if(link.contains("open.spotify.com")) {
                audioTrack = AudioHelper.verifyTrack(link, LinkType.SPOTIFY, event);
                if(audioTrack == null) {
                    event.reply(Message.getMessage(Message.INCORRECTLINK, event.getGuild())).queue();
                    return;
                }
            } else if(link.contains("youtube.com")) {
                audioTrack = AudioHelper.verifyTrack(link, LinkType.YOUTUBE, event);
                if(audioTrack == null) {
                    event.reply(Message.getMessage(Message.INCORRECTLINK, event.getGuild())).queue();
                    return;
                }
            }

            User user = event.getUser();

            if(PlaylistService.isRegistered(user.getId())) {
                AudioTrack finalAudioTrack = audioTrack;
                PlaylistService.updatePlaylist(user.getId(), playlistData -> {
                    playlistData.getPlaylist().add(finalAudioTrack.getInfo().uri);
                    if(!playlistData.getGuilds().contains(event.getGuild().getId())) {
                        playlistData.getGuilds().add(event.getGuild().getId());
                    }
                });
            } else {
                PlaylistData playlistData = new PlaylistData(user.getId(), user.getAsTag(), List.of(event.getGuild().getId()), List.of(audioTrack.getInfo().uri));
                PlaylistService.addPlaylist(playlistData);
            }

            event.reply(Message.getMessage(Message.PLADD, event.getGuild(), audioTrack.getInfo().title)).queue();
        }
    }
}
