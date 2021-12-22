package fr.novlab.bot.data;

import java.util.Arrays;
import java.util.Optional;

public enum Language {

    FRENCH("FRENCH"),
    ENGLISH("ENGLISH");

    private final String id;

    Language(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static Optional<Language> getFromId(String id) {
        return Arrays.stream(values()).filter(language -> language.getId().equals(id)).findFirst();
    }
}
