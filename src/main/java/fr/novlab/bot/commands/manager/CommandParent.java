package fr.novlab.bot.commands.manager;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.lang.reflect.InvocationTargetException;
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
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    public CommandData getCommandData() {
        return new CommandData(this.getCommandInfo().name(), this.getCommandInfo().description())
                .addOptions(this.parseOptions())
                .addSubcommands(this.subCommands.values().stream().map(SubCommand::getCommandData).collect(Collectors.toList()));
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
