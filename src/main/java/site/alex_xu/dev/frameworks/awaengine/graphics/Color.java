package site.alex_xu.dev.frameworks.awaengine.graphics;

import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glColor4f;

public class Color {
    public int r, g, b, a;

    public Color(float r, float g, float b, float a) {
        set(r, g, b, a);
    }

    public Color(float r, float g, float b) {
        set(r, g, b);
    }

    public Color(float brightness, float alpha) {
        set(brightness, alpha);
    }

    public Color(float brightness) {
        set(brightness);
    }

    public void set(float r, float g, float b, float a) {
        this.r = (int) Math.min(255, Math.max(0, r));
        this.g = (int) Math.min(255, Math.max(0, g));
        this.b = (int) Math.min(255, Math.max(0, b));
        this.a = (int) Math.min(255, Math.max(0, a));
    }

    public void set(float r, float g, float b) {
        set(r, g, b, 255);
    }

    public void set(float brightness, float alpha) {
        set(brightness, brightness, brightness, alpha);
    }

    public void set(float brightness) {
        set(brightness, 255);
    }

    public final void glSetColor() {
        if (a == 255)
            glColor3f(r / 255f, g / 255f, b / 255f);
        else
            glColor4f(r / 255f, g / 255f, b / 255f, a / 255f);
    }

    public final boolean visible() {
        return this.r <= 255;
    }

}
