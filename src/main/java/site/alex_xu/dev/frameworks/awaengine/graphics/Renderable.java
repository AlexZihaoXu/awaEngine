package site.alex_xu.dev.frameworks.awaengine.graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import site.alex_xu.dev.frameworks.awaengine.scene.Node;
import site.alex_xu.dev.frameworks.awaengine.core.Settings;
import site.alex_xu.dev.utils.FastMath;
import site.alex_xu.dev.utils.Vec2D;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_SUBTRACT;
import static org.lwjgl.opengl.GL14.*;

public abstract class Renderable extends Displayable {
    protected static Renderable renderingSlot = null;
    // Blend Funcs
    protected final int BLEND_ADD = GL_ADD;
    protected final int BLEND_SUBTRACT = GL_SUBTRACT;
    protected final int BLEND_MULT = GL_MULT;
    protected final int BLEND_MIN = GL_MIN;
    protected final int BLEND_MAX = GL_MAX;
    // Blend Factors
    protected final int BLEND_ZERO = GL_ZERO;
    protected final int BLEND_ONE = GL_ONE;
    protected final int BLEND_SRC_COLOR = GL_SRC_COLOR;
    protected final int BLEND_ONE_MINUS_SRC_COLOR = GL_ONE_MINUS_SRC_COLOR;
    protected final int BLEND_DST_COLOR = GL_DST_COLOR;
    protected final int BLEND_ONE_MINUS_DST_COLOR = GL_ONE_MINUS_DST_COLOR;
    protected final int BLEND_SRC_ALPHA = GL_SRC_ALPHA;
    protected final int BLEND_ONE_MINUS_SRC_ALPHA = GL_ONE_MINUS_SRC_ALPHA;
    protected final int BLEND_DST_ALPHA = GL_DST_ALPHA;
    protected final int BLEND_ONE_MINUS_DST_ALPHA = GL_ONE_MINUS_DST_ALPHA;
    protected final int BLEND_CONSTANT_COLOR = GL_CONSTANT_COLOR;
    protected final int BLEND_ONE_MINUS_CONSTANT_COLOR = GL_ONE_MINUS_CONSTANT_COLOR;
    protected final int BLEND_CONSTANT_ALPHA = GL_CONSTANT_ALPHA;
    protected final int BLEND_ONE_MINUS_CONSTANT_ALPHA = GL_ONE_MINUS_CONSTANT_ALPHA;
    private final Color fillColor = new Color(255);
    private final Color strokeColor = new Color(0);
    private final Color blendColor = new Color(0, 0, 0, 0);
    // Defaults
    protected int defaultBlendEquation = BLEND_ADD;
    protected int defaultSrcFactor = BLEND_SRC_ALPHA;
    protected int defaultDstFactor = BLEND_ONE_MINUS_SRC_ALPHA;
    protected int blendEquation = defaultBlendEquation;
    protected int srcFactor = defaultSrcFactor;
    protected int dstFactor = defaultDstFactor;
    private float strokeWeight = 1f;
    private Font font = Font.getDefaultFont();
    private float fontSize = 24f;

    protected static void _instancePrepare(Renderable renderable) {
        renderable.prepare();
    }
    protected static void _instanceBind(Renderable renderable) {
        renderable.bind();
    }
    protected static void _instanceUnbind(Renderable renderable) {
        renderable.unbind();
    }
    protected static void _instanceBeginRender(Renderable renderable) {
        renderable.beginRender();
    }
    protected static void _instanceEndRender(Renderable renderable) {
        renderable.endRender();
    }

    private static class NodeManager extends Node {
        public static void drawNode(Node node, Renderable renderable) {
            instanceDrawTo(node, renderable);
        }
    }


    abstract protected void prepare();

    public void blendReset() {
        blendEquation = defaultBlendEquation;
        srcFactor = defaultSrcFactor;
        dstFactor = defaultDstFactor;
        blendColor.set(0, 0, 0, 0);
    }

    public void blendColor(float r, float g, float b, float a) {
        blendColor.set(r, g, b, a);
    }

    public void blendEquation(int blendEquation) {
        bindContext();
        this.blendEquation = blendEquation;
    }

    public void blendFactors(int srcFactor, int dstFactor) {
        bindContext();
        this.srcFactor = srcFactor;
        this.dstFactor = dstFactor;
    }

    protected void applyBlendMode() {
        GL11.glBlendFunc(srcFactor, dstFactor);
        GL14.glBlendEquation(blendEquation);
        glBlendColor(blendColor.r / 255f, blendColor.g / 255f, blendColor.b / 255f, blendColor.a / 255f);
    }

    public void bindContext() {
        if (renderingSlot != this) {
            if (renderingSlot != null) {
                renderingSlot.unbindContext();
                renderingSlot = null;
            }
            renderingSlot = this;

            beginRender();
        }
        applyBlendMode();
    }

    public void unbindContext() {
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
        bindContext();
        glClearColor(Math.max(0, Math.min(r, 255)) / 255f, Math.max(0, Math.min(g, 255)) / 255f, Math.max(0, Math.min(b, 255)) / 255f, Math.max(0, Math.min(a, 255)) / 255f);
        glClear(GL_COLOR_BUFFER_BIT);
    }

    public void rect(float x, float y, float w, float h) {
        bindContext();
        glEnable(GL_LINE_SMOOTH);

        if (fillColor.visible()) {
            fillColor.glSetColor();
            glLineWidth(strokeWeight);
            glBegin(GL_QUADS);

            glVertex2f(x, y);
            glVertex2f(x + w, y);
            glVertex2f(x + w, y + h);
            glVertex2f(x, y + h);

            glEnd();
        }

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

        if (fillColor.visible()) {
            bindContext();
            glEnable(GL_LINE_SMOOTH);
            fillColor.glSetColor();

            glBegin(GL_POLYGON);

            for (int i = 0; i < pointCount; i++) {
                float cx = FastMath.cos(360f * i / pointCount) * radius + x;
                float cy = FastMath.sin(360f * i / pointCount) * radius + y;
                glVertex2f(cx, cy);
            }

            glEnd();
        }

        if (strokeColor.visible() && strokeWeight > 0) {
            glLineWidth(strokeWeight);
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
        if (strokeColor.visible() && strokeWeight > 0) {
            strokeColor.glSetColor();
            glEnable(GL_LINE_SMOOTH);
            glLineWidth(strokeWeight);
            glBegin(GL_LINES);
            glVertex2f(x1, y1);
            glVertex2f(x2, y2);
            glEnd();
        }
    }

    public void polygon(float[] poses) {
        bindContext();
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
        if (text.isEmpty())
            return;
        glPushMatrix();
        float trueSize = fontSize * Settings.Video.fontResolution;
        float additionalScale = 1 + (trueSize - (int) trueSize) / (int) trueSize;
        glScalef(additionalScale, additionalScale, 1);
        font.render(text, x, y, fontSize, fillColor);
        glPopMatrix();
    }

    public void blit(float x, float y, Displayable displayable) {
        if (displayable == null)
            return;
        bindContext();
        displayable.bind();
        displayable.draw(x, y, displayable.getWidth(), displayable.getHeight());
        displayable.unbind();
    }

    public void blit(Node node) {
        if (node == null)
            return;
        NodeManager.drawNode(node, this);
    }

    public void blit(float x, float y, Displayable displayable, float srcX, float srcY, float srcW, float srcH) {
        if (displayable == null)
            return;
        bindContext();
        displayable.bind();
        displayable.draw(x, y, displayable.getWidth(), displayable.getHeight(), srcX, srcY, srcW, srcH);
        displayable.unbind();
    }

    // Transform
    public void translate(float x, float y) {
        bindContext();
        glTranslatef(x, y, 0);
    }

    public void scale(float x, float y) {
        bindContext();
        glScalef(x, y, 1);
    }

    public void rotate(float angle) {
        bindContext();
        glRotatef(angle, 0, 0, 1);
    }

    public void pushMatrix() {
        bindContext();
        glPushMatrix();
    }

    public void popMatrix() {
        bindContext();
        glPopMatrix();
    }

}
