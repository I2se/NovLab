package fr.novlab.bot.commands.manager.arg.discord;

import fr.novlab.bot.commands.manager.arg.ArgReader;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class MemberAR implements ArgReader<Member> {

    @Override
    public Member read(OptionMapping optionValue) {
        return optionValue.getAsMember();
    }
}
