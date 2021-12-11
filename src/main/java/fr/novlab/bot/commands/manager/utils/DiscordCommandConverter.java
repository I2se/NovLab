package fr.novlab.bot.commands.manager.utils;

import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

public class DiscordCommandConverter {

    public CommandData toCommandData(Command command) {
        CommandData commandData = new CommandData(command.getName(), command.getDescription());
        commandData.setDefaultEnabled(command.isDefaultEnabled());

        for (Command.Option option : command.getOptions()) {
            commandData.addOption(option.getType(), option.getName(), option.getDescription(), option.isRequired());
        }

        for (Command.Subcommand subcommand : command.getSubcommands()) {
            SubcommandData subcommandData = new SubcommandData(subcommand.getName(), subcommand.getDescription());

            for (Command.Option option : subcommand.getOptions()) {
                subcommandData.addOption(option.getType(), option.getName(), option.getDescription(), option.isRequired());
            }

            commandData.addSubcommands(subcommandData);
        }

        for (Command.SubcommandGroup subcommandGroup : command.getSubcommandGroups()) {
            SubcommandGroupData subcommandGroupData = new SubcommandGroupData(subcommandGroup.getName(), subcommandGroup.getDescription());

            for (Command.Subcommand subcommand : subcommandGroup.getSubcommands()) {
                SubcommandData subcommandData = new SubcommandData(subcommand.getName(), subcommand.getDescription());

                for (Command.Option option : subcommand.getOptions()) {
                    subcommandData.addOption(option.getType(), option.getName(), option.getDescription(), option.isRequired());
                }

                subcommandGroupData.addSubcommands(subcommandData);
            }

            commandData.addSubcommandGroups(subcommandGroupData);
        }

        return commandData;
    }
}
