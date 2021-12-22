package fr.novlab.bot.listeners;

import fr.novlab.bot.Main;
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

        Main.getApiConnection().getCache().requestGuild(guild.getId()).thenAccept(opt -> {
            if (opt.isPresent()) {
                fr.novlab.bot.data.GuildData guildData = opt.get();

                Main.getApiConnection().sendFuture("guilds:edit", guild.getId(), guild.getName(), guildData.getLanguage().getId(), guildData.getRoleIdDJ());;
                LOGGER.info("Connected to guild : " + guild.getName());
            } else {
                Main.getApiConnection().sendFuture("guilds:create", guild.getId());
                LOGGER.info("Database creation for guild : " + guild.getName());
            }
        });
    }
}
