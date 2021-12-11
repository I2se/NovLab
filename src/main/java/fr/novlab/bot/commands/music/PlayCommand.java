package fr.novlab.bot.commands.music;

import fr.novlab.bot.commands.manager.Command;
import fr.novlab.bot.commands.manager.CommandExist;
import fr.novlab.bot.commands.manager.CommandInfo;
import fr.novlab.bot.config.Message;
import fr.novlab.bot.music.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.net.URI;
import java.net.URISyntaxException;

@CommandExist
@CommandInfo(
        name = "play",
        description = "This command is for playing music",
        usage = "<song='Url/Title':str>"
)
public class PlayCommand extends Command {

    @Override
    public void execute(SlashCommandEvent event) {
        TextChannel channel = (TextChannel) event.getChannel();
        Member bot = event.getGuild().getSelfMember();
        Member member = event.getMember();
        GuildVoiceState botVoiceState = bot.getVoiceState();
        GuildVoiceState memberVoiceState = member.getVoiceState();

        if(!memberVoiceState.inVoiceChannel()) {
            event.reply(Message.getMessage(Message.NOTINVOICECHANNEL, event.getGuild())).queue();
            return;
        }

        AudioManager audioManager = channel.getGuild().getAudioManager();
        VoiceChannel memberChannel = memberVoiceState.getChannel();

        if(!channel.getGuild().getAudioManager().isConnected()) {
            audioManager.openAudioConnection(memberChannel);
            event.reply(Message.getMessage(Message.CONNECTEDTOCHANNEL, event.getGuild(), memberChannel.getName())).queue();
        } else {
            if(!memberVoiceState.getChannel().equals(botVoiceState.getChannel())) {
                event.reply(Message.getMessage(Message.NOTINSAMEVOICECHANNEL, event.getGuild())).queue();
                return;
            }
        }

        String song = this.readRequiredArg("song", String.class);
        String link = String.join(" ", song);

        if(!isUrl(link)) {
            link = "ytsearch:" + link;
        }

        PlayerManager.getINSTANCE().loadAndPlay(event, link);
    }

    private boolean isUrl(String url) {
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
