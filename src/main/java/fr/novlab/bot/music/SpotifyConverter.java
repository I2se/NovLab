package fr.novlab.bot.music;

import fr.novlab.bot.config.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.michaelthelin.spotify.model_objects.specification.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SpotifyConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpotifyConverter.class);

    // Track
    // https://open.spotify.com/track/4fouWK6XVHhzl78KzQ1UjL?si=33827e88cf5240f8

    // Album
    // https://open.spotify.com/album/3jsZSRV7pLrjFCD8FObfok?si=v4-jV9xkRoG37QW27gdyIA

    // Playlist
    // https://open.spotify.com/playlist/37i9dQZEVXbNG2KDcFcKOF?si=361f48fd5c194b51

    public static List<String> identify(String url, SlashCommandEvent event) {
        if(url.contains("?si=")) {
            if(url.contains("track")) {
                return Collections.singletonList(trackSpotify(returnId(url)));
            } else if(url.contains("album")) {
                return albumSpotify(returnId(url));
            } else if(url.contains("playlist")) {
                return playlistSpotify(returnId(url));
            } else {
                event.reply(Message.getMessage(Message.SPOTIFYLINKINVALID, event.getGuild())).queue();
                return null;
            }
        } else {
            event.reply(Message.getMessage(Message.SPOTIFYLINKINVALID, event.getGuild())).queue();
        }
        return null;
    }

    public static String trackSpotify(String id) {
        LOGGER.info("Convert Track for : " + id);
        try {
            Track track = SpotifyHelper.doRequest(spotifyApi -> spotifyApi.getTrack(id).build().execute());
            StringBuilder musicStrId = new StringBuilder(track.getName() + " ");
            for (ArtistSimplified artist : track.getArtists()) {
                musicStrId.append(artist.getName()).append(" ");
            }
            LOGGER.info(musicStrId.toString());
            return musicStrId.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Error";
    }

    public static List<String> playlistSpotify(String id) {
        try {
            Playlist playlist = SpotifyHelper.doRequest(spotifyApi -> spotifyApi.getPlaylist(id).build().execute());
            List<PlaylistTrack> playlistTrackList = Arrays.asList(playlist.getTracks().getItems());
            List<String> list = new ArrayList<>();
            for (PlaylistTrack playlistTrack : playlistTrackList) {
                list.add(trackSpotify(playlistTrack.getTrack().getId()));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of("Error");
    }

    public static List<String> albumSpotify(String id) {
        try {
            Album album = SpotifyHelper.doRequest(spotifyApi -> spotifyApi.getAlbum(id).build().execute());
            List<TrackSimplified> trackSimplifieds = Arrays.asList(album.getTracks().getItems());
            List<String> list = new ArrayList<>();
            for (TrackSimplified trackSimplified : trackSimplifieds) {
                list.add(trackSpotify(trackSimplified.getId()));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of("Error");
    }

    public static String returnId(String url) {
        int index = url.indexOf('?');
        int lastIndex = url.lastIndexOf('/');
        String id = (String) url.subSequence(lastIndex + 1, index);
        return id;
    }
}
