package fr.novlab.bot.database.playlists;

import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.List;

public class PlaylistData {

    @BsonProperty(value = "userID")
    private String userID;
    @BsonProperty(value = "userTag")
    private String userTag;
    @BsonProperty(value = "guilds")
    private List<String> guilds;
    @BsonProperty(value = "playlists")
    private List<String> playlist;

    public PlaylistData(String userID, String userTag, List<String> guilds, List<String> playlist) {
        this.userID = userID;
        this.userTag = userTag;
        this.guilds = guilds;
        this.playlist = playlist;
    }

    public PlaylistData() {

    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserTag() {
        return userTag;
    }

    public void setUserTag(String userTag) {
        this.userTag = userTag;
    }

    public List<String> getGuilds() {
        return guilds;
    }

    public void setGuilds(List<String> guilds) {
        this.guilds = guilds;
    }

    public List<String> getPlaylist() {
        return playlist;
    }

    public void setPlaylist(List<String> playlist) {
        this.playlist = playlist;
    }
}
