package site.alex_xu.dev.frameworks.awaengine.audio;

import org.lwjgl.openal.AL10;

import java.util.HashSet;

import static org.lwjgl.openal.AL10.*;

public class SoundSource {
    private static final HashSet<Integer> sourceIds = new HashSet<>();
    private final int sourceId;
    private final float[] position = new float[3];
    private boolean deleted = false;
    private float volume;
    private float pitch;
    private Audio currentAudio;
    private boolean canResume = false;
    private boolean relative = false;

    public SoundSource() {
        sourceId = AL10.alGenSources();
        setPitch(1);
        setVolume(1);
        setPosition(0, 0, 0);
        sourceIds.add(sourceId);
    }

    protected static void cleanUp() {
        for (Integer sourceId : sourceIds) {
            AL10.alDeleteSources(sourceId);
        }
    }

    public boolean isRelative() {
        return relative;
    }

    public void setRelative(boolean relative) {
        if (relative == this.relative)
            return;
        this.relative = relative;
        if (relative) {
            alSourcei(sourceId, AL_SOURCE_RELATIVE, AL_TRUE);
            alSource3f(sourceId, AL_POSITION, 0.0f, 0.0f, 0.0f);
        } else {
            alSourcei(sourceId, AL_SOURCE_RELATIVE, AL_FALSE);
            alSource3f(sourceId, AL_POSITION, position[0], position[1], position[2]);
        }
    }

    public void setPosition(float x, float y, float z) {
        if (!isRelative())
            alSource3f(sourceId, AL10.AL_POSITION, x, y, z);
        position[0] = x;
        position[1] = y;
        position[2] = z;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        AL10.alSourcef(sourceId, AL10.AL_PITCH, pitch);
        this.pitch = pitch;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        AL10.alSourcef(sourceId, AL10.AL_GAIN, volume);
        this.volume = volume;
    }

    public void play(Audio audio) {
        if (currentAudio != audio) {
            currentAudio = audio;
            alSourcei(sourceId, AL10.AL_BUFFER, audio.buffer);
        }
        stop();
        canResume = true;
        resume();
    }

    public boolean isPlaying() {
        return AL10.alGetSourcei(sourceId, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
    }

    public void pause() {
        AL10.alSourcePause(sourceId);
        canResume = true;
    }

    public void resume() {
        if (canResume) {
            setVolume(volume);
            setPosition(position[0], position[1], position[2]);
            setPitch(pitch);
            AL10.alSourcePlay(sourceId);
            canResume = false;
        }
    }

    public void stop() {
        AL10.alSourceStop(sourceId);
        canResume = false;
    }

    public void free() {
        if (deleted) return;
        deleted = true;
        AL10.alDeleteSources(sourceId);
        sourceIds.remove(sourceId);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        free();
    }
}
