package fr.novlab.bot.database.guilds;

import org.bson.codecs.pojo.annotations.BsonProperty;

public class GuildData {

    @BsonProperty(value = "guildId")
    private String guildId;
    @BsonProperty(value = "name")
    private String name;
    @BsonProperty(value = "language")
    private Language language;
    @BsonProperty(value = "dj")
    private String roleIdDJ;

    public GuildData(String guildId, String name, Language language, String roleDj) {
        this.guildId = guildId;
        this.name = name;
        this.language = language;
        this.roleIdDJ = roleDj;
    }

    public GuildData() {

    }

    public String getGuildId() {
        return guildId;
    }

    public void setGuildId(String guildId) {
        this.guildId = guildId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public String getRoleIdDJ() {
        return roleIdDJ;
    }

    public void setRoleIdDJ(String roleIdDJ) {
        this.roleIdDJ = roleIdDJ;
    }
}
