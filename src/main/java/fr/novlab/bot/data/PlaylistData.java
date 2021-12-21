package fr.novlab.bot.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;

public class PlaylistData {

    private String id;
    private String name;
    private String description;
    private String authorType;
    private String authorId;
    private String[] content;
    private Date creationDate;
    private Date lastUpdateDate;

    public void fromJson(JSONObject json) {
        try {
            this.id = json.getString("_id");
            this.name = json.getString("name");
            this.description = json.getString("description");

            JSONObject authorJson = json.getJSONObject("author");
            this.authorType = authorJson.getString("type");
            this.authorId = authorJson.getString("id");

            JSONArray contentArray = json.getJSONArray("content");
            this.content = new String[contentArray.length()];
            for (int i = 0; i < contentArray.length(); i++) {
                this.content[i] = contentArray.getString(i);
            }

            this.creationDate = UserData.JS_DATE_FORMAT.parse(json.getString("creationDate"));
            this.lastUpdateDate = UserData.JS_DATE_FORMAT.parse(json.getString("lastUpdateDate"));
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthorType() {
        return authorType;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String[] getContent() {
        return content;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }
}
