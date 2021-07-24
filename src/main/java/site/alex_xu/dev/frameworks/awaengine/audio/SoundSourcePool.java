package site.alex_xu.dev.frameworks.awaengine.audio;

public class SoundSourcePool {

    private SoundSource[] sources;
    private int index = 0;

    public SoundSourcePool(int poolSize) {
        sources = new SoundSource[poolSize];
    }

    public void extendSize(int size) {
        SoundSource[] newSources = new SoundSource[size + sources.length];
        System.arraycopy(sources, 0, newSources, 0, sources.length);
        sources = newSources;
    }

    public int capacity() {
        return sources.length;
    }

    public SoundSource play(Audio audio, float volume, float pitch, float x, float y, float z) {

        for (int i = 0; i < sources.length; i++) {
            if (sources[index] == null || !sources[index].isPlaying())
                break;
        }
        if (sources[index] == null)
            sources[index] = new SoundSource();
        sources[index].setVolume(volume);
        sources[index].setPitch(pitch);
        sources[index].setPosition(x, y, z);
        sources[index].play(audio);
        index++;
        if (index >= sources.length)
            index = 0;
        return sources[index];
    }

    public SoundSource play(Audio audio, float volume, float pitch) {
        return play(audio, volume, pitch, 0, 0, 0);
    }

    public SoundSource play(Audio audio) {
        return play(audio, 1, 1);
    }

    public SoundSource play(Audio audio, float x, float y, float z) {
        return play(audio, 1, 1, x, y, z);
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
