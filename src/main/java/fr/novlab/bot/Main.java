package fr.novlab.bot;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import fr.novlab.bot.commands.audio.VolumeCommand;
import fr.novlab.bot.commands.manager.CommandRegistry;
import fr.novlab.bot.commands.music.JoinCommand;
import fr.novlab.bot.commands.playlists.PlaylistCommand;
import fr.novlab.bot.commands.staff.SetDJ;
import fr.novlab.bot.config.Config;
import fr.novlab.bot.database.guilds.GuildData;
import fr.novlab.bot.database.guilds.GuildService;
import fr.novlab.bot.database.guilds.Language;
import fr.novlab.bot.database.playlists.PlaylistData;
import fr.novlab.bot.listeners.OnGuildJoin;
import fr.novlab.bot.music.SpotifyHelper;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.Arrays;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Main {

    private static JDA jda;
    private static MongoCollection<GuildData> collectionGuilds;
    private static MongoCollection<PlaylistData> collectionPlaylists;
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws LoginException {
        new Main();
    }

    private Main() throws LoginException {
        try {
            jda = JDABuilder.createDefault(Config.TOKEN)
                    .build()
                    .awaitReady();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info("{} is ready", jda.getSelfUser().getAsTag());

        CommandRegistry commandRegistry = new CommandRegistry(jda);
        commandRegistry.registerDefaults();
        commandRegistry.registerAllCommandsIn(JoinCommand.class.getPackageName());
        commandRegistry.registerAllCommandsIn(VolumeCommand.class.getPackageName());
        commandRegistry.registerAllCommandsIn(SetDJ.class.getPackageName());
        commandRegistry.registerAllCommandsIn(PlaylistCommand.class.getPackageName());
        commandRegistry.updateDiscord();

        jda.addEventListener(commandRegistry, new OnGuildJoin());

        SpotifyHelper.login();

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLogger = loggerContext.getLogger("org.mongodb");
        rootLogger.setLevel(Level.OFF);
        LOGGER.info("Disabling Log MongoDB");

        Main.executeOnReady();
    }

    public static void executeOnReady() {
        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).conventions(Arrays.asList(Conventions.ANNOTATION_CONVENTION, Conventions.USE_GETTERS_FOR_SETTERS)).build();
        CodecRegistry codecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
        MongoClient mongoClient = MongoClients.create(Config.MONGOURI);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("bot");
        collectionGuilds = mongoDatabase.getCollection("guilds", GuildData.class).withCodecRegistry(codecRegistry);
        collectionPlaylists = mongoDatabase.getCollection("playlists", PlaylistData.class).withCodecRegistry(codecRegistry);
        connectionToGuild();
    }

    public static void connectionToGuild() {
        for (Guild guild : jda.getGuilds()) {
            guild.getAudioManager().closeAudioConnection();
            String guildId = guild.getId();
            if(GuildService.isRegistered(guildId)) {
                LOGGER.info("Connected to guild : " + guild.getName());
                GuildService.updateGuild(guildId, guildData -> {
                    guildData.setName(guild.getName());
                });
            } else {
                GuildData guildData = new GuildData(guildId, guild.getName(), Language.ENGLISH, "");
                GuildService.addGuild(guildData);
                LOGGER.info("Database creation for guild : " + guild.getName());
            }
        }
    }

    public static MongoCollection<GuildData> getCollectionGuilds() {
        return collectionGuilds;
    }

    public static MongoCollection<PlaylistData> getCollectionPlaylists() {
        return collectionPlaylists;
    }

    public static JDA getJda() {
        return jda;
    }
}
