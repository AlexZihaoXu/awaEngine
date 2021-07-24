package site.alex_xu.dev.frameworks.awaengine.audio;

import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.HashSet;

public class Audio {
    private static final HashSet<Integer> buffers = new HashSet<>();
    protected final int buffer;

    protected Audio(int buffer) {
        this.buffer = buffer;
    }

    public static Audio fromStream(InputStream stream) {
        return new Audio(loadBuffer(stream));
    }

    private static int loadBuffer(InputStream stream) {
        int buffer = AL10.alGenBuffers();
        buffers.add(buffer);
        WaveData waveData = WaveData.create(new BufferedInputStream(stream));
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
