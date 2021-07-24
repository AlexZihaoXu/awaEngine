package site.alex_xu.dev.frameworks.awaengine.graphics;

import java.awt.*;

public interface TextureType {

    int getWidth();

    int getHeight();

    Image convertAwtImage();
}
