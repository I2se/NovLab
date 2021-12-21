package fr.novlab.bot.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserData {

    // From https://stackoverflow.com/a/33989369
    public static final DateFormat JS_DATE_FORMAT = new SimpleDateFormat("EE MMM d y H:m:s 'GMT'Z (zz)");

    private String discordId;
    private Date creationDate;
    private PlaylistData[] playlists;

    public void fromJson(JSONObject json) {
        try {
            this.discordId = json.getString("discordId");
            this.creationDate = JS_DATE_FORMAT.parse(json.getString("creationDate"));

            JSONArray playlistsArray = json.getJSONArray("playlists");
            this.playlists = new PlaylistData[playlistsArray.length()];
            for (int i = 0; i < playlistsArray.length(); i++) {
                PlaylistData playlistData = new PlaylistData();
                playlistData.fromJson(playlistsArray.getJSONObject(i));
                this.playlists[i] = playlistData;
            }
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    public JSONObject toJson() {
        try {
            JSONObject json = new JSONObject();

            json.put("discordId", this.discordId);
            json.put("creationDate", JS_DATE_FORMAT.format(this.creationDate));

            return json;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getDiscordId() {
        return discordId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public PlaylistData[] getPlaylists() {
        return playlists;
    }
}
