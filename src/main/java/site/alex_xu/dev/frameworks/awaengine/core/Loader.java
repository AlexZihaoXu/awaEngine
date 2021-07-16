package site.alex_xu.dev.frameworks.awaengine.core;

import site.alex_xu.dev.frameworks.awaengine.graphics.Font;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

class FontLoader extends Font {

    public FontLoader(java.awt.Font awtFont) {
        super(awtFont);
    }

    public static Font load(java.awt.Font awtFont) {
        return create(awtFont);
    }
}

public class Loader {
    private static Loader instance;
    private static final Map<String, java.awt.Font> awtFonts = new HashMap<>();
    private static final Map<String, Font> awaFonts = new HashMap<>();

    public static InputStream getResourceAsStream(String path) {
        return Loader.class.getClassLoader().getResourceAsStream(path);
    }

    public static InputStream getFileStream(String path) {
        try {
            return new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static java.awt.Font loadAwtFontFromResources(String path) {
        if (!awtFonts.containsKey(path)) {
            try {
                awtFonts.put(path, java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, getResourceAsStream(path)));
            } catch (Exception ignored) {
                return null;
            }
        }
        return awtFonts.get(path);
    }

    public static Font loadFontFromResources(String path) {
        if (!awaFonts.containsKey(path)) {
            java.awt.Font font = loadAwtFontFromResources(path);
            if (font == null)
                return null;
            awaFonts.put(path, FontLoader.load(font));
        }
        return awaFonts.get(path);
    }
}
