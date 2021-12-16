package fr.novlab.bot.music;

import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class AudioHelper {

    public static AudioTrack verifyTrack(String link, LinkType linkType, SlashCommandEvent event) {
        if(linkType.equals(LinkType.SPOTIFY)) {
            if(link.contains("track")) {
                List<String> listId = SpotifyConverter.identify(link, event);
                AudioPlayerManager audioPlayerManager = new DefaultAudioPlayerManager();
                AudioSourceManagers.registerRemoteSources(audioPlayerManager);
                AudioSourceManagers.registerLocalSource(audioPlayerManager);
                for (String id : listId) {
                    String search = "ytsearch:" + id;
                    final AudioTrack[] audioTrack = {null};
                    try {
                        audioPlayerManager.loadItem(search, new AudioLoadResultHandler() {
                            @Override
                            public void trackLoaded(AudioTrack track) {
                                audioTrack[0] = track;
                            }
                            @Override
                            public void playlistLoaded(AudioPlaylist playlist) {
                                audioTrack[0] = playlist.getTracks().get(0);
                            }
                            @Override
                            public void noMatches() {
                                System.out.println("No Matches");
                            }
                            @Override
                            public void loadFailed(FriendlyException exception) {
                                System.out.println("Load Failed");
                            }
                        }).get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    return audioTrack[0];
                }
            } else {
                return null;
            }
        } else if(linkType.equals(LinkType.YOUTUBE)) {
            AudioPlayerManager audioPlayerManager = new DefaultAudioPlayerManager();
            AudioSourceManagers.registerRemoteSources(audioPlayerManager);
            AudioSourceManagers.registerLocalSource(audioPlayerManager);
            final AudioTrack[] audioTrack = {null};
            try {
                audioPlayerManager.loadItem(link, new AudioLoadResultHandler() {
                    @Override
                    public void trackLoaded(AudioTrack track) {
                        audioTrack[0] = track;
                    }
                    @Override
                    public void playlistLoaded(AudioPlaylist playlist) {
                        audioTrack[0] = playlist.getTracks().get(0);
                    }
                    @Override
                    public void noMatches() {
                        System.out.println("No Matches");
                    }
                    @Override
                    public void loadFailed(FriendlyException exception) {
                        System.out.println("Load Failed");
                    }
                }).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return audioTrack[0];
        }
        return null;
    }
}
