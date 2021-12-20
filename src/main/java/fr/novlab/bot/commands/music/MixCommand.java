package fr.novlab.bot.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.novlab.bot.commands.manager.Command;
import fr.novlab.bot.commands.manager.CommandExist;
import fr.novlab.bot.commands.manager.CommandInfo;
import fr.novlab.bot.commands.music.utils.Verification;
import fr.novlab.bot.config.Message;
import fr.novlab.bot.music.GuildMusicManager;
import fr.novlab.bot.music.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@CommandExist
@CommandInfo(
        name = "mix",
        description = "This command mix the queue"
)
public class MixCommand extends Command {

    @Override
    public void execute(SlashCommandEvent event) {
        Member bot = event.getGuild().getSelfMember();
        Member member = event.getMember();
        GuildVoiceState botVS = bot.getVoiceState();
        GuildVoiceState memberVS = member.getVoiceState();

        if(!Verification.isInTheSameChannel(botVS, memberVS, event)) {
            return;
        }

        GuildMusicManager musicManager = PlayerManager.getINSTANCE().getMusicManager(event.getGuild());
        AudioPlayer audioPlayer = musicManager.audioPlayer;

        if(audioPlayer.getPlayingTrack() == null) {
            event.reply(Message.getMessage(Message.NOTRACKCURRENTLY, event.getGuild())).queue();
            return;
        }

        if(musicManager.scheduler.queue.isEmpty()) {
            event.reply(Message.getMessage(Message.QUEUEEMPTY, event.getGuild())).queue();
        }

        List<AudioTrack> playlist = new ArrayList<>();
        playlist.addAll(musicManager.scheduler.queue);
        int size = playlist.size();
        musicManager.scheduler.queue.clear();
        for (int i = 0; i < size; i++) {
            Random random = new Random();
            int rdm = random.nextInt(size - i);
            musicManager.scheduler.queue.offer(playlist.get(rdm));
            playlist.remove(rdm);
        }

        event.reply(Message.getMessage(Message.MIXMUSIC, event.getGuild(), size+"")).queue();

    }
}
