package fr.novlab.bot.commands;

public class SubCommand<T extends Command> extends Command {

    /**
     * The parent of this sub-command
     */
    private T parent;

    /**
     * {@inheritDoc}
     *
     * @return The root command name
     */
    @Override
    public String getRootCommandName() {
        return this.getParent().getRootCommandName();
    }

    @Override
    public String getFullName() {
        return this.getParent().getFullName() + " " + this.getName();
    }

    /**
     * {@inheritDoc}
     *
     * @return The full usage of the command
     */
    @Override
    public String getFullUsage() { // TODO : Fix this, surely not working for subcommand of subcommand
        String[] parentUsage = this.getParent() instanceof SubCommand ?
                this.getParent().getFullUsage().split(" ") :
                this.getParent().getCommandInfo().subCommandUsage().split(" ");
        int subCommandIndex = this.getParent().getCommandInfo().subCommandIndex();

        StringBuilder finalUsage = new StringBuilder();

        finalUsage.append(this.getRootCommandName()).append(" ");

        for (int i = 0; i < subCommandIndex; i++) {
            finalUsage.append(parentUsage[i]).append(" ");
        }

        finalUsage.append(this.getName()).append(" ");
        finalUsage.append(this.getUsage());

        if (finalUsage.charAt(finalUsage.length() - 1) == ' ') {
            return finalUsage.substring(0, finalUsage.length() - 1);
        } else {
            return finalUsage.toString();
        }
    }

    /**
     * Getter of {@link SubCommand#parent}.
     *
     * @return The parent of the sub-command
     */
    public T getParent() {
        return parent;
    }

    /**
     * Setter of {@link SubCommand#parent}. Be aware that is just to set the instance of the sub-command,
     * you will need to do more work in order to actually relocate a sub-command to another command.
     *
     * @param parent The new parent of the sub-command
     */
    @SuppressWarnings("unchecked")
    public void setParent(Command parent) {
        this.parent = (T) parent;
    }
}
