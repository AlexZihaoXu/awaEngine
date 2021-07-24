package site.alex_xu.dev.frameworks.awaengine.audio;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import site.alex_xu.dev.utils.FastMath;

import java.nio.FloatBuffer;

public class Listener {
    private static final FloatBuffer listenerOrientation = BufferUtils.createFloatBuffer(6);
    private static float x = 0;
    private static float y = 0;
    private static float z = 0;

    static {
        listenerOrientation.put(2, 0); // lookZ
        listenerOrientation.put(3, 0); // upX
        listenerOrientation.put(4, 0); // upY
        listenerOrientation.put(5, 1); // upZ
    }

    public static float getX() {
        return x;
    }

    public static float getY() {
        return y;
    }

    public static float getZ() {
        return z;
    }

    public static void setListenerPos(float x, float y, float z) {
        Listener.x = x;
        Listener.y = y;
        Listener.z = z;
        AL10.alListener3f(AL10.AL_POSITION, x, y, z);
        AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);
    }

    public static void setListenerRotation(float angle) {

        listenerOrientation.put(0, FastMath.cos(angle + 90)); // lookX
        listenerOrientation.put(1, FastMath.sin(angle + 90)); // lookY

        AL10.alListener(AL10.AL_ORIENTATION, listenerOrientation);
    }
}
