package fr.novlab.bot.commands.manager;

import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public abstract class SubCommand<T extends CommandParent> extends Command {

    private T parent;

    public SubcommandData getSubCommandData() {
        return new SubcommandData(this.getCommandInfo().name().contains(":") ? this.getCommandInfo().name().split(":")[1] : this.getCommandInfo().name(), this.getCommandInfo().description())
                .addOptions(this.parseOptions());
    }

    public void setParent(CommandParent parent) {
        this.parent = (T) parent;
    }

    public T getParent() {
        return parent;
    }
}
