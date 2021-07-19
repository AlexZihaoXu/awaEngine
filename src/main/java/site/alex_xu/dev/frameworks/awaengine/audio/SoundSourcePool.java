package site.alex_xu.dev.frameworks.awaengine.audio;

public class SoundSourcePool {

    private final SoundSource[] sources;
    private int index = 0;

    public SoundSourcePool(int poolSize) {
        sources = new SoundSource[poolSize];

        for (int i = 0; i < poolSize; i++) {
            sources[i] = new SoundSource();
        }
    }

    public void play(Audio audio, float volume, float pitch, float x, float y, float z) {
        sources[index].setVolume(volume);
        sources[index].setPitch(pitch);
        sources[index].setPosition(x, y, z);
        sources[index].play(audio);
        index++;
        if (index >= sources.length)
            index = 0;
    }

    public void play(Audio audio, float volume, float pitch) {
        play(audio, volume, pitch, 0, 0, 0);
    }

    public void play(Audio audio) {
        play(audio, 1, 1);
    }

    public void play(Audio audio, float x, float y, float z) {
        play(audio, 1, 1, x, y, z);
    }

    public void pauseAll() {
        for (SoundSource source : sources) {
            source.pause();
        }
    }

    public void resumeAll() {
        for (SoundSource source : sources) {
            source.resume();
        }
    }

    public void stopAll() {
        for (SoundSource source : sources) {
            source.stop();
        }
    }
}
