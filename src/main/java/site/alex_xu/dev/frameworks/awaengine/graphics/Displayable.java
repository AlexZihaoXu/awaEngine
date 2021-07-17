package site.alex_xu.dev.frameworks.awaengine.graphics;

import site.alex_xu.dev.frameworks.awaengine.core.Core;
import site.alex_xu.dev.frameworks.awaengine.core.Drawable;
import site.alex_xu.dev.utils.Size2i;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public abstract class Displayable extends Core implements Drawable {

    abstract protected void bind();

    abstract protected void unbind();

    protected void draw(float x, float y, float w, float h) {
        draw(x, y, w, h, 0, 0, w, h);
    }

    protected void draw(float x, float y, float w, float h, float srcX, float srcY, float srcW, float srcH) {

        glColor4f(1, 1, 1, 1);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_TEXTURE_2D);
        glBegin(GL_QUADS);

        if (srcW == -1) {
            srcW = w - srcX;
        }
        if (srcH == -1) {
            srcH = h - srcY;
        }

        float texL = srcX / w;
        float texR = (srcX + srcW) / w;
        float texT = srcY / h;
        float texB = (srcY + srcH) / h;

        // 00, 10, 11, 01
        glTexCoord2f(texL, texT);
        glVertex2f(x, y);

        glTexCoord2f(texR, texT);
        glVertex2f(x + srcW, y);

        glTexCoord2f(texR, texB);
        glVertex2f(x + srcW, y + srcH);

        glTexCoord2f(texL, texB);
        glVertex2f(x, y + srcH);

        glEnd();
        glDisable(GL_TEXTURE_2D);
    }

    public abstract int getWidth();

    public abstract int getHeight();

    public Size2i getSize() {
        return new Size2i(getWidth(), getHeight());
    }

    public abstract Image convertAwtImage();
}
