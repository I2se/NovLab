package fr.novlab.bot.commands.music;

import fr.novlab.bot.commands.manager.Command;
import fr.novlab.bot.commands.manager.CommandExist;
import fr.novlab.bot.commands.manager.CommandInfo;
import fr.novlab.bot.config.Message;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.managers.AudioManager;

@CommandExist
@CommandInfo(
        name = "join",
        description = "This command is for the bot join your channel",
        usage = ""
)
public class JoinCommand extends Command {

    @Override
    public void execute(SlashCommandEvent event) {
        TextChannel channel = (TextChannel) event.getChannel();
        Member bot = event.getGuild().getSelfMember();
        GuildVoiceState botVoiceState = bot.getVoiceState();
        Member member = event.getMember();

        if(botVoiceState.inVoiceChannel()) {
            event.reply(Message.getMessage(Message.ALREADYCONNECTTOVOICECHANNEL, event.getGuild())).queue();
            return;
        }

        GuildVoiceState memberVoiceState = member.getVoiceState();

        if(!memberVoiceState.inVoiceChannel()) {
            event.reply(Message.getMessage(Message.NOTINVOICECHANNEL, event.getGuild())).queue();
            return;
        }

        AudioManager audioManager = channel.getGuild().getAudioManager();
        VoiceChannel memberChannel = memberVoiceState.getChannel();

        audioManager.openAudioConnection(memberChannel);
        event.reply(Message.getMessage(Message.CONNECTEDTOCHANNEL, event.getGuild(), memberChannel.getName())).queue();
    }
}
