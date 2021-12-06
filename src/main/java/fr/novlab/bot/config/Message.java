package fr.novlab.bot.config;

import fr.novlab.bot.database.guilds.GuildService;
import fr.novlab.bot.database.guilds.Language;
import net.dv8tion.jda.api.entities.Guild;

public enum Message {

    // %s

    SPOTIFYLINKINVALID("Le lien spotify est invalid ou le bot ne supporte pas la demande",
            "The Spotify link is invalid or the bot doesn't support the request");

    private String messageFrench;
    private String messageEnglish;

    Message(String messageFrench, String messageEnglish) {
        this.messageFrench = messageFrench;
        this.messageEnglish = messageEnglish;
    }

    public String getMessageEnglish() {
        return messageEnglish;
    }

    public String getMessageFrench() {
        return messageFrench;
    }

    public static String getMessage(Message message, Guild guild, String... args) {
        Language language = GuildService.getGuild(guild.getId()).getLanguage();
        if(language.equals(Language.ENGLISH)) {
            if(message.getMessageEnglish().contains("%s")) {
                return String.format(message.getMessageEnglish(), args);
            }
            return message.getMessageEnglish();
        }
        if(language.equals(Language.FRENCH)) {
            if(message.getMessageFrench().contains("%s")) {
                return String.format(message.getMessageFrench(), args);
            }
            return message.getMessageFrench();
        }
        return "Error";
    }
}
