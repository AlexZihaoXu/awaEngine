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
    private final HashMap<Integer, ColorEffect> colorMap = new HashMap<>();
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

    protected UnicodeFont getUnicodeFont(float size, Color color) {
        int index = (int) ((int) (size * Settings.Video.fontResolution) / Settings.Video.fontResolution);
        if (!sizeMap.containsKey(index)) {
            try {

                UnicodeFont font = new UnicodeFont(awtFont.deriveFont(java.awt.Font.PLAIN, index));
                font.addAsciiGlyphs();
                ColorEffect effect = new ColorEffect();
                if (color != null)
                    effect.setColor(new java.awt.Color(color.r, color.g, color.b, color.a));

                //noinspection unchecked
                font.getEffects().add(effect);
                colorMap.put(index, effect);
                font.loadGlyphs();
                sizeMap.put(index, font);
            } catch (Exception ignored) {
                return null;
            }
        }
        if (color != null)
            colorMap.get(index).setColor(new java.awt.Color(color.r, color.g, color.b, color.a));
        return sizeMap.get(index);
    }

    protected void render(String text, float x, float y, float size, Color color) {
        getUnicodeFont(size, color).drawString(x, y, text);

        glBindTexture(GL_TEXTURE_2D, 0);
    }

}
