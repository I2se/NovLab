package fr.novlab.bot.database.playlists;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import fr.novlab.bot.Main;
import fr.novlab.bot.database.guilds.GuildData;
import org.bson.Document;

import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.eq;

public class PlaylistService {

    private static final MongoCollection<PlaylistData> collection = Main.getCollectionPlaylists();

    public static PlaylistData getPlaylist(String id) {
        return collection.find(eq("userID", id)).first();
    }

    public static void updatePlaylist(String id, Consumer<PlaylistData> consumer) {
        PlaylistData guildData = getPlaylist(id);
        consumer.accept(guildData);
        updatePlaylist(guildData);
    }

    public static void updatePlaylist(PlaylistData playlistData) {
        Document filterById = new Document("userID", playlistData.getUserID());
        FindOneAndReplaceOptions returnDocAfterReplace = new FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER);
        PlaylistData updatedPlaylistData = collection.findOneAndReplace(filterById, playlistData, returnDocAfterReplace);
    }

    public static boolean isRegistered(String id) {
        if(getPlaylist(id) != null) {
            return true;
        }
        return false;
    }

    public static void addPlaylist(PlaylistData playlistData) {
        collection.insertOne(playlistData);
    }
}
