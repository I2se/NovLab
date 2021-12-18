package fr.novlab.bot.socket;

public enum AppType {

    BOT("bot"),
    PANEL("panel");

    private final String name;

    AppType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
