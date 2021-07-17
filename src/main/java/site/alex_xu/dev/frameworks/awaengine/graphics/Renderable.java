package site.alex_xu.dev.frameworks.awaengine.graphics;

import site.alex_xu.dev.frameworks.awaengine.core.Settings;
import site.alex_xu.dev.utils.FastMath;

import static org.lwjgl.opengl.GL11.*;

public abstract class Renderable extends Displayable {

    private final Color fillColor = new Color(255);
    private final Color strokeColor = new Color(0);
    private float strokeWeight = 1f;
    protected static Renderable renderingSlot = null;
    private Font font = Font.getDefaultFont();
    private float fontSize = 24f;

    abstract protected void prepare();

    protected void _beginRender() {
        if (renderingSlot != this) {
            if (renderingSlot != null) {
                renderingSlot._endRender();
                renderingSlot = null;
            }
            renderingSlot = this;

            beginRender();
        }
    }

    protected void _endRender() {
        endRender();
    }

    abstract protected void beginRender();

    abstract protected void endRender();


    // Settings
    public void stroke(float r, float g, float b, float a) {
        strokeColor.set(r, g, b, a);
    }

    public void strokeWeight(float w) {
        strokeWeight = w;
    }

    public void fill(float r, float g, float b, float a) {
        fillColor.set(r, g, b, a);
    }

    public void textSize(float size) {
        fontSize = size;
    }

    public float getTextSize() {
        return fontSize;
    }

    public void textFont(Font font) {
        this.font = font;
    }

    public float getTextAdditionalScale() {
        float trueSize = fontSize * Settings.Video.fontResolution;
        return 1 + (trueSize - (int) trueSize) / (int) trueSize;
    }

    public float getTextWidth(String text) {

        return font.getUnicodeFont(fontSize).getWidth(text) * getTextAdditionalScale();
    }

    public float getTextHeight(String text) {
        return font.getUnicodeFont(fontSize).getHeight(text) * getTextAdditionalScale();
    }

    // Basic Draws
    public void clear(float r, float g, float b, float a) {
        _beginRender();
        glClearColor(Math.max(0, Math.min(r, 255)) / 255f, Math.max(0, Math.min(g, 255)) / 255f, Math.max(0, Math.min(b, 255)) / 255f, Math.max(0, Math.min(a, 255)) / 255f);
        glClear(GL_COLOR_BUFFER_BIT);
    }

    public void rect(float x, float y, float w, float h) {
        _beginRender();
        fillColor.glSetColor();
        glEnable(GL_LINE_SMOOTH);
        glLineWidth(strokeWeight);
        glBegin(GL_QUADS);

        glVertex2f(x, y);
        glVertex2f(x + w, y);
        glVertex2f(x + w, y + h);
        glVertex2f(x, y + h);

        glEnd();

        if (strokeColor.visible()) {
            strokeColor.glSetColor();
            glBegin(GL_LINE_LOOP);

            glVertex2f(x, y);
            glVertex2f(x + w, y);
            glVertex2f(x + w, y + h);
            glVertex2f(x, y + h);

            glEnd();
        }
    }

    public void circle(float x, float y, float radius, int pointCount) {
        _beginRender();
        glLineWidth(strokeWeight);
        glEnable(GL_LINE_SMOOTH);
        fillColor.glSetColor();

        glBegin(GL_POLYGON);

        for (int i = 0; i < pointCount; i++) {
            float cx = FastMath.cos(360f * i / pointCount) * radius + x;
            float cy = FastMath.sin(360f * i / pointCount) * radius + y;
            glVertex2f(cx, cy);
        }

        glEnd();

        if (strokeColor.visible()) {
            strokeColor.glSetColor();

            glBegin(GL_LINE_LOOP);

            for (int i = 0; i < pointCount; i++) {
                float cx, cy;
                cx = FastMath.cos(360f * i / pointCount) * radius + x;
                cy = FastMath.sin(360f * i / pointCount) * radius + y;
                glVertex2f(cx, cy);
            }

            glEnd();
        }

    }

    public void line(float x1, float y1, float x2, float y2) {
        strokeColor.glSetColor();
        glEnable(GL_LINE_SMOOTH);
        glLineWidth(strokeWeight);
        glBegin(GL_LINES);
        glVertex2f(x1, y1);
        glVertex2f(x2, y2);
        glEnd();
    }

    public void polygon(float[] poses) {
        _beginRender();
        glEnable(GL_LINE_SMOOTH);
        glLineWidth(strokeWeight);
        fillColor.glSetColor();
        glBegin(GL_POLYGON);
        for (int i = 0; i < poses.length / 2; i++) {
            glVertex2f(poses[i * 2], poses[i * 2 + 1]);
        }
        glEnd();

        strokeColor.glSetColor();
        glBegin(GL_LINE_LOOP);
        for (int i = 0; i < poses.length / 2; i++) {
            glVertex2f(poses[i * 2], poses[i * 2 + 1]);
        }
        glEnd();
    }

    public void text(float x, float y, String text) {
        glPushMatrix();
        float trueSize = fontSize * Settings.Video.fontResolution;
        float additionalScale = 1 + (trueSize - (int) trueSize) / (int) trueSize;
        glScalef(additionalScale, additionalScale, 1);
        font.render(text, x, y, fontSize, fillColor);
        glPopMatrix();
    }

    public void blit(float x, float y, Displayable displayable) {
        _beginRender();
        displayable.bind();
        displayable.draw(x, y, displayable.getWidth(), displayable.getHeight());
        displayable.unbind();
    }

    public void blit(float x, float y, Displayable displayable, float srcX, float srcY, float srcW, float srcH) {
        _beginRender();
        displayable.bind();
        displayable.draw(x, y, displayable.getWidth(), displayable.getHeight(), srcX, srcY, srcW, srcH);
        displayable.unbind();
    }

    // Transform
    public void translate(float x, float y) {
        _beginRender();
        glTranslatef(x, y, 0);
    }

    public void scale(float x, float y) {
        _beginRender();
        glScalef(x, y, 1);
    }

    public void rotate(float angle) {
        _beginRender();
        glRotatef(angle, 0, 0, 1);
    }

    public void pushMatrix() {
        _beginRender();
        glPushMatrix();
    }

    public void popMatrix() {
        _beginRender();
        glPopMatrix();
    }

}
