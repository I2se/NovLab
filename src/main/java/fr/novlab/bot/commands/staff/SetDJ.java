package fr.novlab.bot.commands.staff;

import fr.novlab.bot.commands.manager.Command;
import fr.novlab.bot.commands.manager.CommandExist;
import fr.novlab.bot.commands.manager.CommandInfo;
import fr.novlab.bot.commands.manager.arg.discord.RoleAR;
import fr.novlab.bot.config.Message;
import fr.novlab.bot.config.Perms;
import fr.novlab.bot.database.guilds.GuildService;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

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
        GuildService.updateGuild(event.getGuild().getId(), guildData -> {
            guildData.setRoleIdDJ(role.getId());
        });
        event.reply("Set the role " + role.getName() + " has the DJ").queue();
    }
}
