package site.alex_xu.dev.frameworks.awaengine.graphics;

import org.lwjgl.BufferUtils;
import site.alex_xu.dev.frameworks.awaengine.core.Loader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;

class SubTexture extends Texture {
    private final int x, y, w, h;
    private final Texture origin;

    public SubTexture(Texture origin, int x, int y, int w, int h) {
        super(w, h);
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.origin = origin;
    }

    public Texture getSubTexture(int x, int y, int w, int h) {
        if (w == -1)
            w = this.w - x;
        if (h == -1)
            h = this.h - y;
        w = Math.max(0, Math.min(w, this.w - x));
        h = Math.max(0, Math.min(h, this.h - y));
        return origin.getSubTexture(this.x + x, this.y + y, w, h);
    }

    @Override
    public void bind() {
        origin.bind();
    }

    @Override
    public void unbind() {
        origin.unbind();
    }

    @Override
    protected void draw(float x, float y, float w, float h, float srcX, float srcY, float srcW, float srcH) {
        origin.draw(x, y, origin.getWidth(), origin.getHeight(), this.x + srcX, this.y + srcY, srcW, srcH);
    }

    @Override
    public int getWidth() {
        return w;
    }

    @Override
    public int getHeight() {
        return h;
    }

    @Override
    public Image convertAwtImage() {
        return null;
    }
}

public class Texture extends Displayable {
    private int id;
    private final int width;
    private final int height;
    private BufferedImage awtBufferedImage;
    public final boolean isOriginal;

    protected Texture(int width, int height) {
        this.width = width;
        this.height = height;
        this.isOriginal = false;
    }

    protected Texture(ByteBuffer buffer, int width, int height, BufferedImage awtBufferedImage) {
        id = glGenTextures();
        this.width = width;
        this.height = height;
        this.isOriginal = true;

        glBindTexture(GL_TEXTURE_2D, id);

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.width, this.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        this.awtBufferedImage = awtBufferedImage;
    }

    public static Texture fromFile(String path) {
        return fromStream(Loader.getFileStream(path));
    }

    public static Texture fromStream(InputStream stream) {
        Texture texture = null;
        try {
            BufferedImage image;
            image = ImageIO.read(stream);
            int[] pixels = new int[image.getWidth() * image.getHeight()];
            image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

            ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4); //4 for RGBA, 3 for RGB

            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    int pixel = pixels[y * image.getWidth() + x];
                    buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
                    buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
                    buffer.put((byte) (pixel & 0xFF));                // Blue component
                    buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
                }
            }

            buffer.flip();
            texture = new Texture(buffer, image.getWidth(), image.getHeight(), image);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return texture;
    }

    public Texture getSubTexture(int x, int y, int w, int h) {
        if (w == -1)
            w = getWidth() - x;
        if (h == -1)
            h = getHeight() - y;
        w = Math.max(0, Math.min(w, getWidth() - x));
        h = Math.max(0, Math.min(h, getHeight() - y));
        return new SubTexture(this, x, y, w, h);
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public static Texture fromResources(String path) {
        return fromStream(Loader.getResourceAsStream(path));
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        glDeleteTextures(id);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public Image convertAwtImage() {
        ColorModel cm = awtBufferedImage.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = awtBufferedImage.copyData(awtBufferedImage.getRaster().createCompatibleWritableRaster());
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
}
