package fr.novlab.bot.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;

public class GuildData {

    private String guildId;
    private String name;
    private Language language;
    private String roleIdDJ;
    private Date creationDate;
    private PlaylistData[] playlists;

    public void fromJson(JSONObject json) {
        try {
            this.guildId = json.getString("guildId");
            this.name = json.getString("name");
            this.language = Language.getFromId(json.getString("language")).orElse(null);
            this.roleIdDJ = json.getString("roleIdDJ");
            this.creationDate = UserData.JS_DATE_FORMAT.parse(json.getString("creationDate"));

            if (json.has("playlists")) {
                JSONArray playlistsArray = json.getJSONArray("playlists");
                this.playlists = new PlaylistData[playlistsArray.length()];
                for (int i = 0; i < playlistsArray.length(); i++) {
                    PlaylistData playlistData = new PlaylistData();
                    playlistData.fromJson(playlistsArray.getJSONObject(i));
                    this.playlists[i] = playlistData;
                }
            } else {
                this.playlists = new PlaylistData[0];
            }
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    public String getGuildId() {
        return guildId;
    }

    public String getName() {
        return name;
    }

    public Language getLanguage() {
        return language;
    }

    public String getRoleIdDJ() {
        return roleIdDJ;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public PlaylistData[] getPlaylists() {
        return playlists;
    }
}
