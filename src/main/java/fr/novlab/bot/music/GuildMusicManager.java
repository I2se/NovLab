package fr.novlab.bot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import fr.novlab.bot.music.equalizer.Filter;

import java.util.ArrayList;
import java.util.List;

public class GuildMusicManager {

    public final AudioPlayer audioPlayer;
    public final TrackScheduler scheduler;
    private final AudioPlayerSendHandler sendHandler;
    private final List<Filter> filters;

    public GuildMusicManager(AudioPlayerManager manager) {
        this.audioPlayer = manager.createPlayer();
        this.scheduler = new TrackScheduler(this.audioPlayer);
        this.audioPlayer.addListener(this.scheduler);
        this.sendHandler = new AudioPlayerSendHandler(this.audioPlayer);
        this.filters = new ArrayList<>();
    }

    public AudioPlayerSendHandler getSendHandler() {
        return sendHandler;
    }

    public List<Filter> getFilters() {
        return filters;
    }
}
