package fr.novlab.bot.config;

import fr.novlab.bot.Main;
import fr.novlab.bot.data.GuildData;
import fr.novlab.bot.data.Language;
import net.dv8tion.jda.api.entities.Guild;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

public enum Message {

    // %s

    SPOTIFYLINKINVALID("Le lien spotify est invalid ou le bot ne supporte pas la demande",
            "The Spotify link is invalid or the bot doesn't support the request"),
    ALREADYCONNECTTOVOICECHANNEL("Je suis dejà connecté a un channel vocal",
            "I'm already in a voice channel"),
    NOTINVOICECHANNEL("Vous devez être dans un channel vocal",
            "You need to be in a voice channel"),
    CONNECTEDTOCHANNEL("Connecté à `\uD83D" + "%s" + "\uDD0A`",
            "Connected to `\uD83D" + "%s" + "\uDD0A`"),
    NOTINSAMEVOICECHANNEL("Vous devez être dans le même salon vocal",
            "You need to be in the same voice channel"),
    BOTNOTINVOICECHANNEL("Le bot a besoin d'etre dans un salon vocal pour le quitter",
            "I need to be in a voice channel"),
    DISCONNECTFROMCHANNEL("Déconnecté de `\uD83D" + "%s" + "\uDD07`",
            "Disconnect from `\uD83D" + "%s" + "\uDD07`"),
    LOOPMESSAGE("La boucle a était réglé sur **" + "%s" + "**",
            "The loop has been set to **" + "%s" + "**"),
    NOTRACKCURRENTLY("Pas de musique en cours",
            "There is no track playing currently"),
    NOWPLAYING("En cours : `" + "%s" + "` par `" + "%s" + "`",
            "Now Playing `" + "%s" + "` by `" + "%s" + "`"),
    STOPMUSIC("Musique arreté et file d'attente vidé",
            "Music stop and queue cleared"),
    QUEUEEMPTY("La file d'attente est vide",
            "The queue is empty"),
    MIXMUSIC("Un mix de %s musiques a été fait",
            "A mix of %s musics has been apply"),
    VOLUMESET("Volume mis a %s pourcent",
            "Volume fix to %s percent"),
    DONTHAVEPERMS("Vous n'avez pas les permissions",
            "You don't have permission"),
    FAILEDTOLOAD("Impossible de charger la musique : %s",
            "Failed to load music : %s"),
    NOMATCH("Aucune correspondance pour : %s",
            "No matchs for : %s"),
    LOADFROMSPOTIFY("%s musiques ont était ajouté a la file d'attente",
            "%s musics added to queue"),
    WAITINGSPOTIFY("Chargement des musiques depuis Spotify (Merci de patientez)",
            "Loading musics from Spotify... (Please Wait)");

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
        try {
            Optional<GuildData> guildData = Main.getApiConnection().getCache().requestGuild(guild.getId()).get();
            if (guildData.isEmpty()) {
                return "Error empty";
            }

            Language language = guildData.get().getLanguage();

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
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return "Error exception";
        }
    }
}
