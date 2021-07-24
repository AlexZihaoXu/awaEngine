package site.alex_xu.dev.frameworks.awaengine.core;

import site.alex_xu.dev.frameworks.awaengine.audio.Audio;
import site.alex_xu.dev.frameworks.awaengine.exceptions.AssetNotFoundException;
import site.alex_xu.dev.frameworks.awaengine.graphics.Font;
import site.alex_xu.dev.frameworks.awaengine.graphics.Texture;

import java.awt.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.GL_NEAREST;

public final class MasterLoader {
    private static class FontLoader extends Font {
        private static final HashMap<String, java.awt.Font> awtFonts = new HashMap<>();

        public FontLoader(java.awt.Font awtFont) {
            super(awtFont);
        }

        public static Font load(java.awt.Font awtFont) {
            return create(awtFont);
        }

        public static java.awt.Font loadAwtFontFromStream(InputStream stream) {
            try {
                return java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, stream);
            } catch (FontFormatException | IOException e) {
                return null;
            }
        }

    }

    public static final class resources {
        private static final HashMap<String, Texture> loadedTextures = new HashMap<>();
        private static final HashMap<String, Font> loadedFonts = new HashMap<>();
        private static final HashMap<String, Audio> loadedAudios = new HashMap<>();

        public static Texture getTexture(String path, int glTexMinFilter, int glTexMagFilter) {

            String key = glTexMinFilter + "&" + glTexMagFilter + "|" + path;

            if (!loadedTextures.containsKey(key)) {
                Texture texture = Texture.fromStream(new BufferedInputStream(BaseLoader.getResourceAsStream(path)), glTexMinFilter, glTexMagFilter);
                if (texture != null)
                    loadedTextures.put(key, texture);
                else
                    throw new AssetNotFoundException("Could not load texture from resource: " + path);
            }

            return loadedTextures.get(key);
        }

        public static Texture getTexture(String path, int glTexFilters) {
            return getTexture(path, glTexFilters, glTexFilters);
        }

        public static Texture getTexture(String path) {
            return getTexture(path, GL_NEAREST);
        }

        public static Font getFont(String path) {
            if (!loadedFonts.containsKey(path)) {
                InputStream stream = BaseLoader.getResourceAsStream(path);
                java.awt.Font awtFont = FontLoader.loadAwtFontFromStream(stream);
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new AssetNotFoundException("Could not load font from resource: " + path);
                }
                if (awtFont != null) {
                    loadedFonts.put(path, FontLoader.load(awtFont));
                } else {
                    throw new AssetNotFoundException("Could not load font from resource: " + path);
                }
            }

            return loadedFonts.get(path);
        }

        public static Audio getAudio(String path) {
            if (!loadedAudios.containsKey(path)) {
                Audio audio;
                try {
                    audio = Audio.fromStream(BaseLoader.getResourceAsStream(path));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    throw new AssetNotFoundException("Could not load audio from resource: " + path);
                }
                loadedAudios.put(path, audio);
            }
            return loadedAudios.get(path);
        }

    }

    public static final class files {
        private static final HashMap<String, Texture> loadedTextures = new HashMap<>();
        private static final HashMap<String, Font> loadedFonts = new HashMap<>();
        private static final HashMap<String, Audio> loadedAudios = new HashMap<>();

        public static Texture getTexture(String path, int glTexMinFilter, int glTexMagFilter) {
            if (!loadedTextures.containsKey(path)) {
                Texture texture = Texture.fromStream(BaseLoader.getFileStream(path), glTexMinFilter, glTexMagFilter);
                if (texture != null)
                    loadedTextures.put(path, texture);
                else
                    throw new AssetNotFoundException("Could not load texture from path: " + path);
            }

            return loadedTextures.get(path);
        }

        public static Texture getTexture(String path, int glTexFilters) {
            return getTexture(path, glTexFilters, glTexFilters);
        }

        public static Texture getTexture(String path) {
            return getTexture(path, GL_NEAREST);
        }

        public static Font getFont(String path) {
            if (!loadedFonts.containsKey(path)) {
                InputStream stream = BaseLoader.getFileStream(path);
                if (stream == null) {
                    throw new AssetNotFoundException("Could not locate font file: " + path);
                }
                java.awt.Font awtFont = FontLoader.loadAwtFontFromStream(stream);
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new AssetNotFoundException("Could not load font from path: " + path);
                }
                if (awtFont != null) {
                    loadedFonts.put(path, FontLoader.load(awtFont));
                } else {
                    throw new AssetNotFoundException("Could not load font from path: " + path);
                }
            }

            return loadedFonts.get(path);
        }

        public static Audio getAudio(String path) {
            if (!loadedAudios.containsKey(path)) {
                Audio audio;
                try {
                    audio = Audio.fromStream(BaseLoader.getFileStream(path));
                } catch (NullPointerException e) {
                    throw new AssetNotFoundException("Could not load audio from path: " + path);
                }
                loadedAudios.put(path, audio);
            }
            return loadedAudios.get(path);
        }
    }
}
