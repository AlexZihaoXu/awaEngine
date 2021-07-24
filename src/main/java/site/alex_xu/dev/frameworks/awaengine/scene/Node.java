package site.alex_xu.dev.frameworks.awaengine.scene;

import site.alex_xu.dev.frameworks.awaengine.exceptions.DuplicateAttachingNodeException;
import site.alex_xu.dev.frameworks.awaengine.graphics.Renderable;
import site.alex_xu.dev.utils.Vec2D;

import java.awt.*;
import java.util.HashSet;

public class Node extends Renderable {
    public final Vec2D position = new Vec2D();
    public final Vec2D origin = new Vec2D();
    public final Vec2D scale = new Vec2D(1, 1);
    private final HashSet<Node> childrenNodes = new HashSet<>();
    public float rotation = 0;
    private boolean isAttached = false;
    private Renderable boundRenderable = null;

    protected static void instanceDrawTo(Node node, Renderable renderable) {
        node.drawTo(renderable);
    }

    public void attachChild(Node node) {
        if (node.isAttached) {
            throw new DuplicateAttachingNodeException("Each node can only be attached to one other node. ");
        }
        node.isAttached = true;
        childrenNodes.add(node);
    }

    public void detachChild(Node node) {
        childrenNodes.remove(node);
        node.isAttached = false;
    }

    public void update() {
    }

    public void draw() {

    }

    protected void updateTree() {
        update();
        for (Node node : childrenNodes) {
            node.updateTree();
        }
    }

    protected void drawTo(Renderable renderable) {
        boundRenderable = renderable;
        pushMatrix();
        translate(position.x, position.y);
        rotate(rotation);
        scale(scale.x, scale.y);
        translate(-origin.x, -origin.y);

        draw();
        translate(origin.x, origin.y);
        for (Node node : childrenNodes) {
            node.drawTo(renderable);
        }

        popMatrix();
    }

    @Override
    protected void bind() {
        _instanceBind(boundRenderable);
    }

    @Override
    protected void unbind() {
        _instanceUnbind(boundRenderable);
    }

    @Override
    public int getWidth() {
        return boundRenderable.getWidth();
    }

    @Override
    public int getHeight() {
        return boundRenderable.getHeight();
    }

    @Override
    public Image convertAwtImage() {
        return boundRenderable.convertAwtImage();
    }

    @Override
    protected void prepare() {
        _instancePrepare(boundRenderable);
    }

    @Override
    protected void beginRender() {
        _instanceBeginRender(boundRenderable);
    }

    @Override
    protected void endRender() {
        _instanceEndRender(boundRenderable);
    }
}
