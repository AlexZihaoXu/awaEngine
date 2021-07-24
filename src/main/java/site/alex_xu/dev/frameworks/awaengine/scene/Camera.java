package site.alex_xu.dev.frameworks.awaengine.scene;

import site.alex_xu.dev.frameworks.awaengine.core.Core;
import site.alex_xu.dev.utils.Vec2D;

public class Camera extends Core {
    public final Vec2D position = new Vec2D();
    public float scale = 1;
    public float rotation = 0;

    public void move(float x, float y) {
        position.move(x, y);
    }

    public void move(int x, int y) {
        move((float) x, (float) y);
    }

    public void rotate(float angle) {
        rotation += angle;
    }

    public void rotate(int angle) {
        rotation += angle;
    }
}
