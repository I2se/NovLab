package fr.novlab.bot.commands.staff;

import fr.novlab.bot.Main;
import fr.novlab.bot.commands.manager.Command;
import fr.novlab.bot.commands.manager.CommandExist;
import fr.novlab.bot.commands.manager.CommandInfo;
import fr.novlab.bot.config.Perms;
import fr.novlab.bot.data.GuildData;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.concurrent.ExecutionException;

@CommandExist
@CommandInfo(
        name = "dj",
        description = "This command set the role DJ",
        usage = "<roleid='Set The Role Who Control The DJ':role>",
        permission = Perms.ADMIN
)
public class SetDJ extends Command {

    @Override
    public void execute(SlashCommandEvent event) {
        Role role = this.readRequiredArg("roleid", Role.class);
        Main.getApiConnection().getCache().requestGuild(event.getGuild().getId()).thenAccept(opt -> {
            if (opt.isPresent()) {
                GuildData guildData = opt.get();

                Main.getApiConnection().sendSyncFuture("guilds:edit", guildData.getGuildId(), guildData.getName(), guildData.getLanguage().getId(), role.getId());
                event.reply("Change DJ role id!").queue();
            } else {
                event.reply("Internal error (SetDJ#execute() -> request guild data)").queue();
            }
        });
    }
}
