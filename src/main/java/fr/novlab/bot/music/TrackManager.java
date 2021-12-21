package fr.novlab.bot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.novlab.bot.config.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TrackManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrackManager.class);

    public static List<AudioTrack> getTrack(List<String> music, LinkType type, boolean isPlaylistSpotify, SlashCommandEvent event, GuildMusicManager guildMusicManager, AudioPlayerManager audioPlayerManager) {
        List<AudioTrack> audioTracks = new ArrayList<>();
        if(isPlaylistSpotify) {
            final int[] count = {music.size()};
            music.forEach(s -> {
                event.getChannel().sendTyping().queue();
                String search = "ytsearch:" + s;
                try {
                    audioPlayerManager.loadItemOrdered(guildMusicManager, search, new AudioLoadResultHandler() {
                        @Override
                        public void trackLoaded(AudioTrack track) {
                            audioTracks.add(track);
                            LOGGER.info("Track for spotify find : " + track.getInfo().title);
                        }
                        @Override
                        public void playlistLoaded(AudioPlaylist playlist) {
                            audioTracks.add(playlist.getTracks().get(0));
                            LOGGER.info("Track for spotify find : " + playlist.getTracks().get(0).getInfo().title);
                        }
                        @Override
                        public void noMatches() {
                            event.reply(Message.getMessage(Message.NOMATCH, event.getGuild(), s)).queue();
                            count[0]--;
                            LOGGER.info("No match for : " + s);
                        }
                        @Override
                        public void loadFailed(FriendlyException exception) {
                            event.reply(Message.getMessage(Message.FAILEDTOLOAD, event.getGuild(), s)).queue();
                            count[0]--;
                            LOGGER.info("Load Failed For Music : " + s);
                        }
                    }).get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            });
            event.getChannel().sendMessage(Message.getMessage(Message.LOADFROMSPOTIFY, event.getGuild(), count[0] + "")).queue();
        } else {
            if(type.equals(LinkType.SPOTIFY)) {
                String ts = music.get(0);
                music.clear();
                music.add("ytsearch:" + ts);
            }
            try {
                audioPlayerManager.loadItemOrdered(guildMusicManager, music.get(0), new AudioLoadResultHandler() {
                    @Override
                    public void trackLoaded(AudioTrack audioTrack) {
                        audioTracks.add(audioTrack);
                        StringBuilder msg = new StringBuilder("Adding to queue: `")
                                .append(audioTrack.getInfo().title)
                                .append("` by `")
                                .append(audioTrack.getInfo().author)
                                .append("`");
                        LOGGER.info(event.getGuild().getName() + " - Listen Music " + audioTrack.getInfo().title + " - " + audioTrack.getInfo().author);
                        event.reply(msg.toString()).queue();
                    }
                    @Override
                    public void playlistLoaded(AudioPlaylist playlist) {
                        if(music.get(0).contains("https://youtube.com/playlist")) {
                            List<AudioTrack> tracks = playlist.getTracks();
                            StringBuilder msg = new StringBuilder("Adding to queue: `")
                                    .append(String.valueOf(tracks.size()))
                                    .append("` musics from playlist `")
                                    .append(playlist.getName())
                                    .append("`");
                            LOGGER.info(event.getGuild().getName() + " - Listen Music " + playlist.getName() + " - " + tracks.size());
                            event.reply(msg.toString()).queue();
                            for (AudioTrack track : tracks) {
                                audioTracks.add(track);
                            }
                        } else {
                            AudioTrack audioTrack = playlist.getTracks().get(0);
                            audioTracks.add(audioTrack);
                            StringBuilder msg = new StringBuilder("Adding to queue: `")
                                    .append(audioTrack.getInfo().title)
                                    .append("` by `")
                                    .append(audioTrack.getInfo().author)
                                    .append("`");
                            LOGGER.info(event.getGuild().getName() + " - Listen Music " + playlist.getName());
                            event.reply(msg.toString()).queue();
                        }
                    }
                    @Override
                    public void noMatches() {
                        event.reply(Message.getMessage(Message.NOMATCH, event.getGuild(), music.get(0))).queue();
                        LOGGER.info("No match for : " + music.get(0));
                    }
                    @Override
                    public void loadFailed(FriendlyException e) {
                        event.reply(Message.getMessage(Message.FAILEDTOLOAD, event.getGuild(), music.get(0))).queue();
                        LOGGER.info("Load Failed For Music : " + music.get(0));
                    }
                }).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return audioTracks;
    }
}
