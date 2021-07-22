package site.alex_xu.dev.frameworks.awaengine.scene;

import site.alex_xu.dev.frameworks.awaengine.graphics.BaseRenderable;
import site.alex_xu.dev.frameworks.awaengine.graphics.Renderable;
import site.alex_xu.dev.frameworks.awaengine.video.Window;
import site.alex_xu.dev.utils.FastMath;
import site.alex_xu.dev.utils.Vec2D;

import java.awt.*;

public abstract class Scene extends Renderable {
    private final Camera camera = new Camera();
    private final Node rootNode = new Node();
    private final CamNode camNode = new CamNode(this);
    private BaseRenderable renderTarget;

    public Scene(BaseRenderable renderTarget) {
        this.renderTarget = renderTarget;
    }

    public Scene() {
        this(Window.getInstance());
    }

    protected static void _updateInstance(Scene scene) {
        scene.camNode.position.set(scene.getWidth() / 2f, scene.getHeight() / 2f);
        scene.camNode.origin.set(scene.camera.position.x, scene.camera.position.y);
        scene.camNode.rotation = -scene.camera.rotation;
        scene.camNode.scale.set(scene.camera.scale, scene.camera.scale);
        scene.update();
        scene.camNode.updateTree();
    }

    protected static void _drawScene(Scene scene, Renderable target) {
        target.blit(scene.camNode);
        scene.drawUI();
    }

    public int getWidth() {
        return renderTarget.getWidth();
    }

    public int getHeight() {
        return renderTarget.getHeight();
    }

    public Node getRootNode() {
        return rootNode;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setRenderTarget(Renderable renderTarget) {
        this.renderTarget = renderTarget;
    }

    public Vec2D toViewPos(float frameX, float frameY) {
        float horizontal = (frameX - getWidth() / 2f) / camera.scale;
        float vertical = (frameY - getHeight() / 2f) / camera.scale;
        float vhx = FastMath.cos(camera.rotation) * horizontal;
        float vhy = FastMath.sin(camera.rotation) * horizontal;
        float vvx = FastMath.cos(camera.rotation + 90) * vertical;
        float vvy = FastMath.sin(camera.rotation + 90) * vertical;

        return new Vec2D(vhx + vvx + camera.position.x, vhy + vvy + camera.position.y);
    }

    public Vec2D toFramePos(float viewX, float viewY) {

        float y = (viewX - camera.position.x) * camera.scale;
        float x = (viewY - camera.position.y) * camera.scale;

        float resultX = (x * FastMath.sin(camera.rotation) + y * FastMath.cos(camera.rotation));
        float resultY = (x * FastMath.cos(camera.rotation) - y * FastMath.sin(camera.rotation));

        return new Vec2D(resultX + getWidth() / 2f, resultY + getHeight() / 2f);
    }

    abstract public void setup();

    abstract public void destroy();

    abstract public void update();

    abstract public void draw();

    abstract public void drawUI();

    private static class CamNode extends Node {
        Scene scene;

        CamNode(Scene scene) {
            this.scene = scene;
        }

        @Override
        public void draw() {
            scene.draw();
            scene.blit(scene.rootNode);
        }

        @Override
        public void update() {
            super.update();
            scene.rootNode.updateTree();
        }
    }


    @Override
    protected void bind() {
        _instanceBind(renderTarget);
    }

    @Override
    protected void unbind() {
        _instanceUnbind(renderTarget);
    }

    @Override
    public Image convertAwtImage() {
        return renderTarget.convertAwtImage();
    }

    @Override
    protected void prepare() {
        _instancePrepare(renderTarget);
    }

    @Override
    protected void beginRender() {
        _instanceBeginRender(renderTarget);
    }

    @Override
    protected void endRender() {
        _instanceEndRender(renderTarget);
    }
}
