package fr.novlab.bot.commands.music;

import fr.novlab.bot.commands.manager.Command;
import fr.novlab.bot.commands.manager.CommandExist;
import fr.novlab.bot.commands.manager.CommandInfo;
import fr.novlab.bot.commands.music.utils.Verification;
import fr.novlab.bot.config.Message;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.managers.AudioManager;

@CommandExist
@CommandInfo(
        name = "leave",
        description = "This command is use to leave the bot"
)
public class LeaveCommand extends Command {

    @Override
    public void execute(SlashCommandEvent event) {
        Member bot = event.getGuild().getSelfMember();
        Member member = event.getMember();
        GuildVoiceState botVS = bot.getVoiceState();
        GuildVoiceState memberVS = member.getVoiceState();
        
        if(!Verification.isInTheSameChannel(botVS, memberVS, event)) {
            return;
        }

        AudioManager audioManager = event.getGuild().getAudioManager();

        audioManager.closeAudioConnection();
        event.reply(Message.getMessage(Message.DISCONNECTFROMCHANNEL, event.getGuild(), botVS.getChannel().getName())).queue();
    }
}
