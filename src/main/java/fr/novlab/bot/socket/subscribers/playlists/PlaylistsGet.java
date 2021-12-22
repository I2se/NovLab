package fr.novlab.bot.socket.subscribers.playlists;

import fr.novlab.bot.data.PlaylistData;
import fr.novlab.bot.socket.APIConnection;
import fr.novlab.bot.socket.Subscriber;
import org.json.JSONException;
import org.json.JSONObject;

public class PlaylistsGet implements Subscriber {

    @Override
    public void on(APIConnection connection, JSONObject response) throws JSONException {
        if (response.getInt("statusCode") == 200) {
            PlaylistData playlistData = new PlaylistData();
            playlistData.fromJson(response.getJSONObject("content").getJSONObject("playlist"));

            connection.getCache().getPlaylists().put(playlistData.getId(), playlistData);
        }
    }

    @Override
    public String getChannel() {
        return "playlists:get";
    }
}

