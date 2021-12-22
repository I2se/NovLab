package fr.novlab.bot.socket.subscribers.guilds;

import fr.novlab.bot.socket.APIConnection;
import fr.novlab.bot.socket.Subscriber;
import org.json.JSONException;
import org.json.JSONObject;

public class GuildsDelete implements Subscriber {

    @Override
    public void on(APIConnection connection, JSONObject response) throws JSONException {
        if (response.getInt("statusCode") == 200) {
            connection.getCache().getGuilds().remove(response.getJSONObject("content").getString("guildId"));
        }
    }

    @Override
    public String getChannel() {
        return "guilds:delete";
    }
}
