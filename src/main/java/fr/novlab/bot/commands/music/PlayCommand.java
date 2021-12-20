package fr.novlab.bot.commands.music;

import fr.novlab.bot.commands.manager.Command;
import fr.novlab.bot.commands.manager.CommandExist;
import fr.novlab.bot.commands.manager.CommandInfo;
import fr.novlab.bot.commands.music.utils.Verification;
import fr.novlab.bot.music.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

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
        Member bot = event.getGuild().getSelfMember();
        Member member = event.getMember();
        GuildVoiceState botVS = bot.getVoiceState();
        GuildVoiceState memberVS = member.getVoiceState();

        if(!Verification.isInTheSameChannel(botVS, memberVS, event)) {
            return;
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
