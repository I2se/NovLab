package fr.novlab.bot.music.equalizer;

import com.github.natanbc.lavadsp.distortion.DistortionPcmAudioFilter;
import com.github.natanbc.lavadsp.karaoke.KaraokePcmAudioFilter;
import com.github.natanbc.lavadsp.timescale.TimescalePcmAudioFilter;
import com.github.natanbc.lavadsp.tremolo.TremoloPcmAudioFilter;
import com.github.natanbc.lavadsp.vibrato.VibratoPcmAudioFilter;
import com.sedmelluq.discord.lavaplayer.filter.equalizer.EqualizerFactory;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import java.util.Collections;

public class NovEqualizer {

    private static float[] BassBoost = Filter.BASS_BOOST.getBandMultipliers();

    public void resetFilter(AudioPlayer player) {
        setDistortion(player, 1.0F);
        setKaraoke(player, 1.0F);
        setTimescale(player, 1.0D);
        setTremolo(player, 0.5F);
        setVibrato(player, 0.5F);
        resetBassBoost(player);
    }

    public void setDistortion(AudioPlayer player, float scale) {
        player.setFilterFactory((track, format, output) -> {
            DistortionPcmAudioFilter distortionPcmAudioFilter = new DistortionPcmAudioFilter(output, format.channelCount);
            distortionPcmAudioFilter.setScale(scale);
            return Collections.singletonList(distortionPcmAudioFilter);
        });
    }

    public void setKaraoke(AudioPlayer player, float level) {
        player.setFilterFactory((track, format, output) -> {
            KaraokePcmAudioFilter karaokePcmAudioFilter = new KaraokePcmAudioFilter(output, format.channelCount, format.sampleRate);
            karaokePcmAudioFilter.setLevel(level);
            return Collections.singletonList(karaokePcmAudioFilter);
        });
    }

    public void setTimescale(AudioPlayer player, double speed) {
        player.setFilterFactory((track, format, output) -> {
            TimescalePcmAudioFilter timescalePcmAudioFilter = new TimescalePcmAudioFilter(output, format.channelCount, format.sampleRate);
            timescalePcmAudioFilter.setSpeed(speed);
            return Collections.singletonList(timescalePcmAudioFilter);
        });
    }

    public void setTremolo(AudioPlayer player, float depth) {
        player.setFilterFactory((track, format, output) -> {
            TremoloPcmAudioFilter tremoloPcmAudioFilter = new TremoloPcmAudioFilter(output, format.channelCount, format.sampleRate);
            tremoloPcmAudioFilter.setDepth(depth);
            return Collections.singletonList(tremoloPcmAudioFilter);
        });
    }

    public void setVibrato(AudioPlayer player, float depth) {
        player.setFilterFactory((track, format, output) -> {
            VibratoPcmAudioFilter vibratoPcmAudioFilter = new VibratoPcmAudioFilter(output, format.channelCount, format.sampleRate);
            vibratoPcmAudioFilter.setDepth(depth);
            return Collections.singletonList(vibratoPcmAudioFilter);
        });
    }

    public void setBassBoost(AudioPlayer player, float diff) {
        EqualizerFactory equalizerFactory = new EqualizerFactory();
        for (int i = 0; i < BassBoost.length; i++) {
            equalizerFactory.setGain(i, BassBoost[i] + diff);
        }
        player.setFilterFactory(equalizerFactory);
    }

    public void resetBassBoost(AudioPlayer player) {
        EqualizerFactory equalizerFactory = new EqualizerFactory();
        for (int i = 0; i < BassBoost.length; i++) {
            equalizerFactory.setGain(i, BassBoost[i]);
        }
        player.setFilterFactory(equalizerFactory);
    }
}
