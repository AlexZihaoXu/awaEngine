package site.alex_xu.dev.frameworks.awaengine.audio;

import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;
import site.alex_xu.dev.frameworks.awaengine.core.Core;

import java.io.InputStream;
import java.util.HashSet;

public class Audio {
    private static final HashSet<Integer> buffers = new HashSet<>();
    protected final int buffer;

    public static void setListenerPos(float x, float y, float z) {
        AL10.alListener3f(AL10.AL_POSITION, x, y, z);
        AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);
    }

    protected Audio(int buffer) {
        this.buffer = buffer;
    }

    public static Audio fromStream(InputStream stream) {
        return new Audio(loadBuffer(stream));
    }

    private static int loadBuffer(InputStream stream) {
        int buffer = AL10.alGenBuffers();
        buffers.add(buffer);
        WaveData waveData = WaveData.create(stream);
        AL10.alBufferData(buffer, waveData.format, waveData.data, waveData.samplerate);
        waveData.dispose();
        return buffer;
    }

    protected static void cleanUp() {
        for (Integer buffer : buffers) {
            AL10.alDeleteBuffers(buffer);
        }
    }
}
