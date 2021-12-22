package fr.novlab.bot.socket;

import fr.novlab.bot.data.GuildData;
import fr.novlab.bot.data.PlaylistData;
import fr.novlab.bot.data.UserData;
import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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

    public CompletableFuture<Optional<UserData>> requestUser(String discordId) {
        CompletableFuture<Optional<UserData>> future = new CompletableFuture<>();

        if (this.users.containsKey(discordId)) {
            future.complete(Optional.of(this.users.get(discordId)));
        } else {
            this.apiConnection.sendFuture("users:get", discordId).thenAccept((response) -> {
                if (response.getStatusCode() == 200) {
                    UserData userData = new UserData();
                    try {
                        userData.fromJson(response.getContent().getJSONObject("user"));
                    } catch (JSONException e) {
                        future.complete(Optional.empty());
                        return;
                    }
                    future.complete(Optional.of(userData));
                } else {
                    future.complete(Optional.empty());
                }
            });
        }

        return future;
    }

    public CompletableFuture<Optional<GuildData>> requestGuild(String guildId) {
        CompletableFuture<Optional<GuildData>> future = new CompletableFuture<>();

        if (this.guilds.containsKey(guildId)) {
            future.complete(Optional.of(this.guilds.get(guildId)));
        } else {
            this.apiConnection.sendFuture("guilds:get", guildId).thenAccept((response) -> {
                if (response.getStatusCode() == 200) {
                    GuildData guildData = new GuildData();
                    try {
                        guildData.fromJson(response.getContent().getJSONObject("guild"));
                    } catch (JSONException e) {
                        future.complete(Optional.empty());
                        return;
                    }
                    future.complete(Optional.of(guildData));
                } else {
                    future.complete(Optional.empty());
                }
            });
        }

        return future;
    }

    public CompletableFuture<Optional<PlaylistData>> requestPlaylist(String playlistId) {
        CompletableFuture<Optional<PlaylistData>> future = new CompletableFuture<>();

        if (this.playlists.containsKey(playlistId)) {
            future.complete(Optional.of(this.playlists.get(playlistId)));
        } else {
            this.apiConnection.sendFuture("playlists:get", playlistId).thenAccept((response) -> {
                if (response.getStatusCode() == 200) {
                    PlaylistData playlistData = new PlaylistData();
                    try {
                        playlistData.fromJson(response.getContent().getJSONObject("playlist"));
                    } catch (JSONException e) {
                        future.complete(Optional.empty());
                        return;
                    }
                    future.complete(Optional.of(playlistData));
                } else {
                    future.complete(Optional.empty());
                }
            });
        }

        return future;
    }

    public void pushPlaylists(PlaylistData[] playlists) {
        for (PlaylistData playlist : playlists) {
            this.playlists.put(playlist.getId(), playlist);
        }
    }

    public void requestPlaylistsFor(String type, String id, Consumer<Optional<List<PlaylistData>>> callback) {
        if (type.equals("user") && !this.users.containsKey(id)) {
            //this.requestUser(id, userData -> callback.accept(userData.map(data -> Arrays.asList(data.getPlaylists()))));
        } else if (type.equals("guild") && !this.guilds.containsKey(id)) {
            //this.requestGuild(id, guildData -> callback.accept(guildData.map(data -> Arrays.asList(data.getPlaylists()))));
        } else {
            callback.accept(Optional.of(this.playlists.values()
                .stream()
                .filter(playlistData ->
                    playlistData.getAuthorType().equals(type) && playlistData.getAuthorId().equals(id))
                .collect(Collectors.toList())));
        }
    }

    public Map<String, UserData> getUsers() {
        return users;
    }

    public Map<String, GuildData> getGuilds() {
        return guilds;
    }

    public Map<String, PlaylistData> getPlaylists() {
        return playlists;
    }
}
