package fr.novlab.bot.socket.subscribers.guilds;

import fr.novlab.bot.data.GuildData;
import fr.novlab.bot.socket.APIConnection;
import fr.novlab.bot.socket.Subscriber;
import org.json.JSONException;
import org.json.JSONObject;

public class GuildsCreate implements Subscriber {

    @Override
    public void on(APIConnection connection, JSONObject response) throws JSONException {
        if (response.getInt("statusCode") == 200) {
            GuildData guildData = new GuildData();
            guildData.fromJson(response.getJSONObject("content").getJSONObject("guild"));

            connection.getCache().getGuilds().put(guildData.getGuildId(), guildData);
            connection.getCache().pushPlaylists(guildData.getPlaylists());
        }
    }

    @Override
    public String getChannel() {
        return "guilds:create";
    }
}
