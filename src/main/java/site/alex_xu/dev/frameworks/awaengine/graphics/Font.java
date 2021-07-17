package site.alex_xu.dev.frameworks.awaengine.graphics;

import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import site.alex_xu.dev.frameworks.awaengine.core.Core;
import site.alex_xu.dev.frameworks.awaengine.core.Settings;

import java.util.HashMap;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

public class Font extends Core {
    private final HashMap<Integer, UnicodeFont> sizeMap = new HashMap<>();
    private final java.awt.Font awtFont;
    private static Font defaultFont = null;

    public static Font getDefaultFont() {
        if (defaultFont == null)
            defaultFont = create(new java.awt.Font("Serif", java.awt.Font.PLAIN, 24));
        return defaultFont;
    }

    protected Font(java.awt.Font awtFont) {
        this.awtFont = awtFont;
    }

    protected static Font create(java.awt.Font awtFont) {
        return new Font(awtFont);
    }

    protected UnicodeFont getUnicodeFont(float size) {
        int index = (int) ((int) (size * Settings.Video.fontResolution) / Settings.Video.fontResolution);
        if (!sizeMap.containsKey(index)) {
            try {
                UnicodeFont font = new UnicodeFont(awtFont.deriveFont(java.awt.Font.PLAIN, index));
                font.addAsciiGlyphs();
                //noinspection unchecked
                font.getEffects().add(new ColorEffect());
                font.loadGlyphs();
                sizeMap.put(index, font);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return sizeMap.get(index);
    }

    protected void render(String text, float x, float y, float size, Color color) {
        try {
            getUnicodeFont(size).drawString(x, y, text, new org.newdawn.slick.Color(color.r, color.g, color.b, color.a));
            glBindTexture(GL_TEXTURE_2D, 0);
        } catch (NullPointerException e) {
            System.err.println("Unable to create font");
            throw e;
        }
    }

}
