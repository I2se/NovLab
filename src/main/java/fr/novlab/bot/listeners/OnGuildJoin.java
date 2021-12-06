package fr.novlab.bot.listeners;

import fr.novlab.bot.database.guilds.GuildData;
import fr.novlab.bot.database.guilds.GuildService;
import fr.novlab.bot.database.guilds.Language;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OnGuildJoin extends ListenerAdapter implements EventListener {

    public Logger LOGGER = LoggerFactory.getLogger(OnGuildJoin.class);

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        Guild guild = event.getGuild();
        if(GuildService.isRegistered(guild.getId())) {
            GuildService.updateGuild(guild.getId(), guildData -> {
                guildData.setName(guild.getName());
            });
            LOGGER.info("Connected to guild : " + guild.getName());
        } else {
            GuildData guildData = new GuildData(guild.getId(), guild.getName(), Language.ENGLISH);
            GuildService.addGuild(guildData);
            LOGGER.info("Database creation for guild : " + guild.getName());
        }
    }
}
