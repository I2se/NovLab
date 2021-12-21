package fr.novlab.bot.socket;

import fr.novlab.bot.data.GuildData;
import fr.novlab.bot.data.PlaylistData;
import fr.novlab.bot.data.UserData;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Cache {

    private final APIConnection apiConnection;

    private final Map<String, UserData> users;
    private final Map<String, GuildData> guilds;
    private final Map<String, PlaylistData> playlists;

    public Cache(APIConnection apiConnection) {
        this.apiConnection = apiConnection;

        this.users = new HashMap<>();
        this.guilds = new HashMap<>();
        this.playlists = new HashMap<>();
    }

    public void requestUser(String discordId, Consumer<UserData> callback) {
        if (this.users.containsKey(discordId)) {
            callback.accept(this.users.get(discordId));
        } else {
            this.apiConnection.send("users:get", response -> {
                if (response.getStatusCode() == 200) {
                    
                }
            }, discordId);
        }
    }
}
