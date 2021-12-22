package fr.novlab.bot.socket.subscribers.playlists;

import fr.novlab.bot.socket.APIConnection;
import fr.novlab.bot.socket.Subscriber;
import org.json.JSONException;
import org.json.JSONObject;

public class PlaylistsDelete implements Subscriber {

    @Override
    public void on(APIConnection connection, JSONObject response) throws JSONException {
        if (response.getInt("statusCode") == 200) {
            connection.getCache().getPlaylists().remove(response.getJSONObject("content").getString("playlistId"));
        }
    }

    @Override
    public String getChannel() {
        return "playlists:delete";
    }
}

