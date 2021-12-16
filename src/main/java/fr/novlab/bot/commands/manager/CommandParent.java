package fr.novlab.bot.commands.manager;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class CommandParent extends Command {

    private final Map<String, SubCommand<?>> subCommands;

    public CommandParent() {
        this.subCommands = new LinkedHashMap<>();

        for (Class<? extends SubCommand<?>> clazz : this.getCommandInfo().subCommands()) {
            try {
                SubCommand<?> subCommand = clazz.getDeclaredConstructor().newInstance();
                subCommand.setParent(this);

                this.subCommands.put(subCommand.getCommandInfo().name(), subCommand);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void execute(SlashCommandEvent event) {}

    public CommandData getCommandData() {
        if (this.getCommandInfo().hasSubcommandGroups()) {
            Map<String, SubcommandGroupData> subcommandGroups = new HashMap<>();
            for (SubCommandDefinition definition : this.getCommandInfo().subcommandGroups()) {
                subcommandGroups.put(definition.name(), new SubcommandGroupData(definition.name(), definition.description()));
            }

            for (SubCommand<?> subCommand : this.subCommands.values()) {
                String groupName = subCommand.getCommandInfo().name().contains(":") ? subCommand.getCommandInfo().name().split(":")[0] : subCommand.getCommandInfo().name();

                if (!subcommandGroups.containsKey(groupName)) {
                    subcommandGroups.put(groupName, new SubcommandGroupData(groupName, "unknown").addSubcommands(subCommand.getSubCommandData()));
                } else {
                    subcommandGroups.get(groupName).addSubcommands(subCommand.getSubCommandData());
                }
            }

            return new CommandData(this.getCommandInfo().name(), this.getCommandInfo().description())
                    .addSubcommandGroups(subcommandGroups.values());
        } else {
            return new CommandData(this.getCommandInfo().name(), this.getCommandInfo().description())
                    .addSubcommands(this.subCommands.values().stream().map(SubCommand::getSubCommandData).collect(Collectors.toList()));
        }
    }

    public boolean executeSubCommands(SlashCommandEvent event) {
        if (event.getSubcommandName() != null && this.subCommands.containsKey(event.getSubcommandName())) {
            this.subCommands.get(event.getSubcommandName()).internallyExecute(event);
            return true;
        }
        return false;
    }

    @Override
    public void setCommandRegistry(CommandRegistry commandRegistry) {
        super.setCommandRegistry(commandRegistry);

        this.subCommands.values().forEach(subCommand -> subCommand.setCommandRegistry(commandRegistry));
    }

    public Map<String, SubCommand<?>> getSubCommands() {
        return subCommands;
    }
}
