package site.alex_xu.dev.frameworks.awaengine.graphics;

import site.alex_xu.dev.frameworks.awaengine.core.Core;
import site.alex_xu.dev.frameworks.awaengine.core.Drawable;
import site.alex_xu.dev.utils.Size2i;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public abstract class Displayable extends Core implements Drawable {

    abstract protected void bind();

    abstract protected void unbind();

    protected static void draw(float x, float y, float w, float h) {

        glColor4f(1, 1, 1, 1);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_TEXTURE_2D);
        glBegin(GL_QUADS);

        glTexCoord2f(0, 0);
        glVertex2f(x, y);

        glTexCoord2f(1, 0);
        glVertex2f(x + w, y);

        glTexCoord2f(1, 1);
        glVertex2f(x + w, y + h);

        glTexCoord2f(0, 1);
        glVertex2f(x, y + h);

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
