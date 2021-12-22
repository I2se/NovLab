package fr.novlab.bot.socket.subscribers.users;

import fr.novlab.bot.socket.APIConnection;
import fr.novlab.bot.socket.Subscriber;
import org.json.JSONException;
import org.json.JSONObject;

public class UsersDelete implements Subscriber {

    @Override
    public void on(APIConnection connection, JSONObject response) throws JSONException {
        if (response.getInt("statusCode") == 200) {
            connection.getCache().getUsers().remove(response.getJSONObject("content").getString("discordId"));
        }
    }

    @Override
    public String getChannel() {
        return "users:delete";
    }
}
