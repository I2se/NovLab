package fr.novlab.bot.socket.subscribers.users;

import fr.novlab.bot.data.UserData;
import fr.novlab.bot.socket.APIConnection;
import fr.novlab.bot.socket.Subscriber;
import org.json.JSONException;
import org.json.JSONObject;

public class UsersGet implements Subscriber {

    @Override
    public void on(APIConnection connection, JSONObject response) throws JSONException {
        if (response.getInt("statusCode") == 200) {
            UserData userData = new UserData();
            userData.fromJson(response.getJSONObject("content").getJSONObject("user"));

            connection.getCache().getUsers().put(userData.getDiscordId(), userData);
            connection.getCache().pushPlaylists(userData.getPlaylists());
        }
    }

    @Override
    public String getChannel() {
        return "users:get";
    }
}
