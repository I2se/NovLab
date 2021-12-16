package fr.novlab.bot.music.equalizer;

public enum Filter {

    BASS_BOOST(new float[]{0.2f, 0.15f, 0.1f, 0.05f, 0.0f, -0.05f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f}),
    DISTORTION(),
    KARAOKE(),
    TIMESCALE(),
    TREMOLO(),
    VIBRATO();


    private float[] bandMultipliers;
    private Class<?> pcmfilter;

    Filter(float[] band_multipliers) {
        this.bandMultipliers = band_multipliers;
    }

    Filter() {
    }

    public float[] getBandMultipliers() {
        return bandMultipliers;
    }
}
