package site.alex_xu.dev.frameworks.awaengine.core;

import site.alex_xu.dev.frameworks.awaengine.graphics.Font;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class BaseLoader {

    public static InputStream getResourceAsStream(String path) {
        return BaseLoader.class.getClassLoader().getResourceAsStream(path);
    }

    public static InputStream getFileStream(String path) {
        try {
            return new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
