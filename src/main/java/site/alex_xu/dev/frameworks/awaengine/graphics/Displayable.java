package site.alex_xu.dev.frameworks.awaengine.graphics;

import org.lwjgl.BufferUtils;
import site.alex_xu.dev.frameworks.awaengine.core.Core;
import site.alex_xu.dev.frameworks.awaengine.core.Drawable;
import site.alex_xu.dev.utils.Size2i;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.*;

public abstract class Displayable extends Core implements Drawable {

    abstract protected void bind();

    abstract protected void unbind();

    protected void draw(float x, float y, float w, float h) {
        draw(x, y, w, h, 0, 0, w, h);
    }

    protected void draw(float x, float y, float w, float h, float srcX, float srcY, float srcW, float srcH) {

        glColor4f(1, 1, 1, 1);
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

    public Image convertAwtImage(boolean flipped) {
        bind();
        BufferedImage result = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        ByteBuffer buffer = BufferUtils.createByteBuffer(getWidth() * getHeight() * 4);
        glReadPixels(0, 0, getWidth(), getHeight(), GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                int ny = (flipped) ? getHeight() - y - 1 : y;
                int r = buffer.get(ny * getWidth() * 4 + x * 4) & 0xff;
                int g = buffer.get(ny * getWidth() * 4 + x * 4 + 1) & 0xff;
                int b = buffer.get(ny * getWidth() * 4 + x * 4 + 2) & 0xff;
                int a = buffer.get(ny * getWidth() * 4 + x * 4 + 3) & 0xff;

                result.setRGB(x, y, new Color(r, g, b, a).getRGB());
            }
        }
        unbind();
        return result;
    }

    public Image convertAwtImage() {
        return convertAwtImage(false);
    }

    public void saveImage(OutputStream stream, String format) throws IOException {
        ImageIO.write((RenderedImage) convertAwtImage(), format, stream);
    }

    public void saveImage(OutputStream stream) throws IOException {
        saveImage(stream, "PNG");
    }

    public void saveImage(String path) throws IOException {
        String filename = Paths.get(path).getFileName().toString();
        int index = filename.lastIndexOf('.');
        String format = "PNG";
        if (index != -1) {
            format = filename.substring(index + 1).toUpperCase();
        } else {
            System.err.println("[WARNING] No file extension was specified when saving the image, using default PNG format.");
            assert true;
        }
        File file = new File(path);
        BufferedImage image = (BufferedImage) convertAwtImage();
        try {
            ImageIO.write(image, format, file);

        } catch (IOException e) {
            BufferedImage img2 = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
            img2.getGraphics().drawImage(image, 0, 0, null);
            ImageIO.write(img2, format, file);
        }
        if (!file.exists())
            throw new IOException("Could not write image to: " + path);
    }
}
