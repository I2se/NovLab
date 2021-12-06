package fr.novlab.bot.commands.manager;

import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public abstract class SubCommand<T extends CommandParent> extends Command {

    private T parent;

    public SubcommandData getCommandData() {
        return new SubcommandData(this.getCommandInfo().name(), this.getCommandInfo().description())
                .addOptions(this.parseOptions());
    }

    public void setParent(CommandParent parent) {
        this.parent = (T) parent;
    }

    public T getParent() {
        return parent;
    }
}
