package fr.novlab.bot.commands.music.utils;

import fr.novlab.bot.config.Message;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class Verification {

    public static boolean isConnectedToChannel(GuildVoiceState voiceState) {
        if(voiceState.inAudioChannel()) {
            return true;
        }
        return false;
    }

    public static boolean isInTheSameChannel(GuildVoiceState voiceStateBot, GuildVoiceState voiceStateMember, SlashCommandEvent event) {
        if(isConnectedToChannel(voiceStateMember)) {
            if(isConnectedToChannel(voiceStateBot)) {
                if(voiceStateBot.getChannel() == voiceStateMember.getChannel()) {
                    return true;
                }
                event.reply(Message.getMessage(Message.NOTINSAMEVOICECHANNEL, event.getGuild())).queue();
                return false;
            }
            VoiceChannel channel = (VoiceChannel) voiceStateMember.getChannel();
            channel.getGuild().getAudioManager().openAudioConnection(channel);
            return true;
        }
        event.reply(Message.getMessage(Message.NOTINVOICECHANNEL, event.getGuild())).queue();
        return false;
    }
}
