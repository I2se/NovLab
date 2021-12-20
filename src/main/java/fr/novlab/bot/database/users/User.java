package fr.novlab.bot.database.users;

public class User {

    private String discordId;

    public User(String discordId) {
        this.discordId = discordId;
    }

    public String getDiscordId() {
        return discordId;
    }

    public void setDiscordId(String discordId) {
        this.discordId = discordId;
    }
}
