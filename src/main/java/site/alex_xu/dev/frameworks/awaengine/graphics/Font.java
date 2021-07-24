package site.alex_xu.dev.frameworks.awaengine.graphics;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import site.alex_xu.dev.frameworks.awaengine.core.Core;
import site.alex_xu.dev.frameworks.awaengine.core.Settings;

import java.util.HashMap;
import java.util.HashSet;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

public class Font extends Core {
    private static Font defaultFont = null;
    private final HashMap<Integer, UnicodeFont> sizeMap = new HashMap<>();
    private final HashMap<Integer, HashSet<Character>> loadedGlyphs = new HashMap<>();
    private final java.awt.Font awtFont;

    protected Font(java.awt.Font awtFont) {
        this.awtFont = awtFont;
    }

    public static Font getDefaultFont() {
        if (defaultFont == null)
            defaultFont = create(new java.awt.Font("Serif", java.awt.Font.PLAIN, 24));
        return defaultFont;
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
                loadedGlyphs.put(index, new HashSet<>());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return sizeMap.get(index);
    }

    protected void render(String text, float x, float y, float size, Color color) {
        try {
            int index = (int) ((int) (size * Settings.Video.fontResolution) / Settings.Video.fontResolution);
            StringBuilder newGlyphs = new StringBuilder();
            UnicodeFont f = getUnicodeFont(size);
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                if (c >= 256)
                    if (!loadedGlyphs.get(index).contains(c)) {
                        newGlyphs.append(c);
                        loadedGlyphs.get(index).add(c);
                    }
            }
            if (newGlyphs.length() > 0) {
                f.addGlyphs(newGlyphs.toString());
                f.loadGlyphs();
            }
            f.drawString(x, y, text, new org.newdawn.slick.Color(color.r, color.g, color.b, color.a));
            glBindTexture(GL_TEXTURE_2D, 0);
        } catch (NullPointerException e) {
            System.err.println("Unable to create font");
            throw e;
        } catch (SlickException e) {
            e.printStackTrace();
            System.err.println("Unable load new glyphs.");
        }
    }

}
