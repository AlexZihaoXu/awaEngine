package site.alex_xu.dev.frameworks.awaengine.graphics;

import site.alex_xu.dev.frameworks.awaengine.exceptions.TextureException;
import site.alex_xu.dev.utils.Size2i;

import java.awt.*;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

public final class BufferedTexture extends Renderable {
    private final int fboID;
    private final int texID;
    private final int depthBuff;
    private boolean deleted = false;

    private final Size2i size;

    public BufferedTexture(Texture texture) {
        this(texture.getWidth(), texture.getHeight());
        blit(0, 0, texture);
    }

    public BufferedTexture(int width, int height) {
        fboID = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fboID);

        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        depthBuff = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, depthBuff);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthBuff);

        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texID, 0);

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            throw new RuntimeException("Failed to create BufferedTexture!");
        }
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        size = new Size2i(width, height);
    }

    public BufferedTexture getSubTexture(int x, int y, int w, int h) {
        if (w == -1)
            w = getWidth() - x;
        if (h == -1)
            h = getHeight() - y;
        w = Math.max(0, Math.min(w, getWidth() - x));
        h = Math.max(0, Math.min(h, getHeight() - y));
        BufferedTexture result = new BufferedTexture(w, h);
        result.clear(0, 0, 0, 0);

        result.blit(0, 0, this, x, y, w, h);
        return result;
    }

    public void free() {
        System.out.println("Finalized " + getWidth() + getHeight());
        if (deleted) return;
        deleted = true;
        glDeleteFramebuffers(fboID);
        glDeleteTextures(texID);
        glDeleteRenderbuffers(depthBuff);
    }

    private void enableEditing() {
        if (deleted)
            throw new TextureException("Trying to edit a buffered texture that has already been freed. ");

        glPushMatrix();
        glDisable(GL_TEXTURE_2D);
        glBindFramebuffer(GL_FRAMEBUFFER, fboID);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, getWidth(), 0, getHeight(), -1, 1);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glDisable(GL_DEPTH_TEST);

        glViewport(0, 0, getWidth(), getHeight());

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);
        glEnable(GL_LINE_SMOOTH);
    }

    @Override
    protected void prepare() {
        enableEditing();
    }

    private void disableEditing() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glPopMatrix();
    }

    @Override
    protected void bind() {
        if (deleted)
            throw new TextureException("Trying to access a texture that has already been freed. ");
        glBindTexture(GL_TEXTURE_2D, texID);
    }

    @Override
    protected void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    @Override
    public int getWidth() {
        return size.width;
    }

    @Override
    public int getHeight() {
        return size.height;
    }

    @Override
    public Image convertAwtImage() {
        return null;
    }


    @Override
    protected void finalize() throws Throwable {
        free();
        super.finalize();
    }

    @Override
    protected void beginRender() {
        enableEditing();
    }

    @Override
    protected void endRender() {
        disableEditing();
    }
}
