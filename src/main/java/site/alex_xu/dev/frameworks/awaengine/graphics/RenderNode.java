package site.alex_xu.dev.frameworks.awaengine.graphics;

import site.alex_xu.dev.frameworks.awaengine.core.Core;
import site.alex_xu.dev.utils.Vec2D;

import java.awt.*;
import java.util.HashSet;

public abstract class RenderNode extends Renderable {
    public final Vec2D position = new Vec2D();
    public final Vec2D origin = new Vec2D();
    public final Vec2D scale = new Vec2D(1, 1);
    public float rotation = 0;
    private final HashSet<RenderNode> childrenNodes = new HashSet<>();
    private Renderable boundRenderable = null;

    public void attachChild(RenderNode node) {
        childrenNodes.add(node);
    }

    public void detachChild(RenderNode node) {
        childrenNodes.remove(node);
    }

    abstract protected void draw();

    public void drawTo(Renderable renderable) {
        renderable.pushMatrix();
        renderable.translate(position.x, position.y);
        renderable.rotate(rotation);
        renderable.scale(scale.x, scale.y);
        renderable.translate(-origin.x, -origin.y);

        boundRenderable = renderable;
        draw();
        renderable.translate(origin.x, origin.y);
        for (RenderNode node : childrenNodes) {
            node.drawTo(renderable);
        }

        renderable.popMatrix();
    }

    @Override
    protected void bind() {
        boundRenderable.bind();
    }

    @Override
    protected void unbind() {
        boundRenderable.unbind();
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
        boundRenderable.prepare();
    }

    @Override
    protected void beginRender() {
        boundRenderable.beginRender();
    }

    @Override
    protected void endRender() {
        boundRenderable.endRender();
    }
}
