package fr.novlab.bot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManager {

    private static PlayerManager INSTANCE;
    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerManager.class);

    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    public PlayerManager() {
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    public GuildMusicManager getMusicManager(Guild guild) {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);
            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
            return guildMusicManager;
        });
    }

    public void loadAndPlay(SlashCommandEvent event, String trackURL) {
        final GuildMusicManager musicManager = this.getMusicManager(event.getGuild());

        if(trackURL.contains("open.spotify.com")) {
            List<String> listTrack = SpotifyConverter.identify(trackURL, event);
            if(listTrack.size() >= 2) {
                for (AudioTrack audioTrack : TrackManager.getTrack(listTrack, LinkType.SPOTIFY, true, event, musicManager, audioPlayerManager)) {
                    musicManager.scheduler.queue(audioTrack);
                }
            } else {
                for (AudioTrack audioTrack : TrackManager.getTrack(listTrack, LinkType.SPOTIFY, false, event, musicManager, audioPlayerManager)) {
                    musicManager.scheduler.queue(audioTrack);
                }
            }
        } else {
            for (AudioTrack audioTrack : TrackManager.getTrack(List.of(trackURL), LinkType.YOUTUBE, false, event, musicManager, audioPlayerManager)) {
                musicManager.scheduler.queue(audioTrack);
            }
        }
    }

    public static PlayerManager getINSTANCE() {

        if(INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }

        return INSTANCE;
    }
}
