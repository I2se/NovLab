package fr.novlab.bot.commands.audio;

import fr.novlab.bot.commands.manager.Command;
import fr.novlab.bot.commands.manager.CommandExist;
import fr.novlab.bot.commands.manager.CommandInfo;
import fr.novlab.bot.config.Message;
import fr.novlab.bot.config.Perms;
import fr.novlab.bot.music.GuildMusicManager;
import fr.novlab.bot.music.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

@CommandExist
@CommandInfo(
        name = "volume",
        description = "Fix the volume of the bot",
        usage = "<vol='Percent':int>",
        permission = Perms.DJ
)
public class VolumeCommand extends Command {
    @Override
    public void execute(SlashCommandEvent event) {
        Member bot = event.getGuild().getSelfMember();
        GuildVoiceState botVoiceState = bot.getVoiceState();
        Member member = event.getMember();

        if (!botVoiceState.inAudioChannel()) {
            event.reply(Message.getMessage(Message.BOTNOTINVOICECHANNEL, event.getGuild())).queue();
            return;
        }

        GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inAudioChannel()) {
            event.reply(Message.getMessage(Message.NOTINVOICECHANNEL, event.getGuild())).queue();
            return;
        }

        if (!memberVoiceState.getChannel().equals(botVoiceState.getChannel())) {
            event.reply(Message.getMessage(Message.NOTINSAMEVOICECHANNEL, event.getGuild())).queue();
            return;
        }

        GuildMusicManager musicManager = PlayerManager.getINSTANCE().getMusicManager(event.getGuild());
        int volume = this.readRequiredArg("vol", Integer.class);
        musicManager.audioPlayer.setVolume(volume);

        event.reply(Message.getMessage(Message.VOLUMESET, event.getGuild(), volume+"")).queue();
    }
}
