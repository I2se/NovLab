package fr.novlab.bot;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.mongodb.client.MongoCollection;
import fr.novlab.bot.commands.manager.CommandRegistry;
import fr.novlab.bot.commands.music.JoinCommand;
import fr.novlab.bot.commands.music.VolumeCommand;
import fr.novlab.bot.commands.staff.SetDJ;
import fr.novlab.bot.config.Config;
import fr.novlab.bot.listeners.OnGuildJoin;
import fr.novlab.bot.music.SpotifyHelper;
import fr.novlab.bot.socket.APIConnection;
import fr.novlab.bot.socket.AppType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class Main {

    private static JDA jda;
    private static APIConnection apiConnection;
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws LoginException, ExecutionException, InterruptedException {
        new Main();
    }

    private Main() throws LoginException, ExecutionException, InterruptedException {
        apiConnection = new APIConnection(AppType.BOT, Config.API_KEYS);
        apiConnection.start("http://localhost:8000");

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLogger = loggerContext.getLogger("org.mongodb");
        rootLogger.setLevel(Level.OFF);
        LoggerContext loggerContextRefec = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLoggerRefec = loggerContextRefec.getLogger("org.reflections");
        rootLoggerRefec.setLevel(Level.OFF);

        try {
            jda = JDABuilder.createDefault(Config.TOKEN)
                    .build()
                    .awaitReady();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        CommandRegistry commandRegistry = new CommandRegistry(jda);
        commandRegistry.registerDefaults();
        commandRegistry.registerAllCommandsIn(JoinCommand.class.getPackageName());
        commandRegistry.registerAllCommandsIn(VolumeCommand.class.getPackageName());
        commandRegistry.registerAllCommandsIn(SetDJ.class.getPackageName());
        commandRegistry.updateDiscord();

        jda.addEventListener(commandRegistry, new OnGuildJoin());
        SpotifyHelper.login();
        printInitialize(commandRegistry);

        while (true) {
            Scanner scanner = new Scanner(System.in);
            String message = scanner.nextLine();
            if(message.equals("stop")) {
                apiConnection.end();
                jda.shutdown();
                System.exit(0);
            }
        }
    }

    public static void executeOnReady() {
        /*CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).conventions(Arrays.asList(Conventions.ANNOTATION_CONVENTION, Conventions.USE_GETTERS_FOR_SETTERS)).build();
        CodecRegistry codecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
        MongoClient mongoClient = MongoClients.create(Config.MONGOURI);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("bot");
        collectionGuilds = mongoDatabase.getCollection("guilds", GuildData.class).withCodecRegistry(codecRegistry);
        collectionPlaylists = mongoDatabase.getCollection("playlists", PlaylistData.class).withCodecRegistry(codecRegistry);
        LOGGER.info("Register Collections :");
        LOGGER.info("- Guilds");
        LOGGER.info("- Playlists");*/
        connectionToGuild();
    }

    public static void connectionToGuild() {
        LOGGER.info("Connecting to all guilds :");
        for (Guild guild : jda.getGuilds()) {
            guild.getAudioManager().closeAudioConnection();
            String guildId = guild.getId();

            apiConnection.getCache().requestGuild(guildId).thenAccept(opt -> {
                if (opt.isPresent()) {
                    fr.novlab.bot.data.GuildData guildData = opt.get();

                    LOGGER.info(" - " + guild.getName());
                    apiConnection.sendFuture("guilds:edit", guildId, guild.getName(), guildData.getLanguage().getId(), guildData.getRoleIdDJ());
                } else {
                    LOGGER.info(" - " + guild.getName() + " (Database Creation)");
                    apiConnection.sendFuture("guilds:create", guildId);
                }
            });


            /*if(GuildService.isRegistered(guildId)) {
                LOGGER.info("- " + guild.getName());
                GuildService.updateGuild(guildId, guildData -> {
                    guildData.setName(guild.getName());
                });
            } else {
                GuildData guildData = new GuildData(guildId, guild.getName(), Language.ENGLISH, "");
                GuildService.addGuild(guildData);
                LOGGER.info("- " + guild.getName() + " (Database Creation)");
            }*/
        }
    }

    public void printInitialize(CommandRegistry commandRegistry) {
        LOGGER.info("===================================");
        LOGGER.info("Start Bot " + jda.getSelfUser().getAsTag());
        LOGGER.info("Register Events :");
        jda.getEventManager().getRegisteredListeners().forEach(o -> {
            LOGGER.info("- " + o.getClass().getSimpleName());
        });
        LOGGER.info("Register Commands :");
        commandRegistry.commands.forEach((s, command) -> {
            String letter = String.valueOf(s.charAt(0));
            String maj = letter.toUpperCase();
            String name = s.replace(s.charAt(0), maj.toCharArray()[0]);
            LOGGER.info("- " + name + " : " + command.getClass().getSimpleName());
        });
        Main.executeOnReady();
        LOGGER.info("{} is ready", jda.getSelfUser().getAsTag());
        LOGGER.info("===================================");
    }

    public static JDA getJda() {
        return jda;
    }

    public static APIConnection getApiConnection() {
        return apiConnection;
    }
}
