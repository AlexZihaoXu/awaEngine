package site.alex_xu.dev.frameworks.awaengine.graphics;

import site.alex_xu.dev.utils.Size2f;
import site.alex_xu.dev.utils.Size2i;
import site.alex_xu.dev.utils.Vec2D;
import site.alex_xu.dev.utils.geometry.Rect;

public abstract class Renderable extends BaseRenderable {
    // Settings
    public void stroke(int r, int g, int b, int a) {
        stroke((float) r, (float) g, (float) b, (float) a);
    }

    public void stroke(float r, float g, float b) {
        stroke(r, g, b, 255);
    }

    public void stroke(int r, int g, int b) {
        stroke(r, g, b, 255);
    }

    public void stroke(float brightness, float alpha) {
        stroke(brightness, brightness, brightness, alpha);
    }

    public void stroke(int brightness, int alpha) {
        stroke((float) brightness, (float) alpha);
    }

    public void stroke(float brightness) {
        stroke(brightness, 255);
    }

    public void stroke(int brightness) {
        stroke((float) brightness);
    }

    public void strokeWeight(int w) {
        strokeWeight((float) w);
    }

    public void fill(int r, int g, int b, int a) {
        fill((float) r, (float) g, (float) b, (float) a);
    }

    public void fill(float r, float g, float b) {
        fill(r, g, b, 255);
    }

    public void fill(int r, int g, int b) {
        fill((float) r, (float) g, (float) b);
    }

    public void fill(float brightness, float alpha) {
        fill(brightness, brightness, brightness, alpha);
    }

    public void fill(int brightness, int alpha) {
        fill((float) brightness, (float) alpha);
    }

    public void fill(float brightness) {
        fill(brightness, 255);
    }

    public void fill(int brightness) {
        fill((float) brightness);
    }

    public void fill(Color color) {
        fill(color.r, color.g, color.b, color.a);
    }

    public void fill(java.awt.Color color) {
        fill(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public void textSize(int size) {
        textSize((float) size);
    }

    // Basic Drawings
    public void clear(int r, int g, int b, int a) {
        clear((float) r, (float) g, (float) b, (float) a);
    }

    public void clear(float r, float g, float b) {
        clear(r, g, b, 255);
    }

    public void clear(int r, int g, int b) {
        clear((float) r, (float) g, (float) b);
    }

    public void clear(float brightness, float alpha) {
        clear(brightness, brightness, brightness, alpha);
    }

    public void clear(int brightness, int alpha) {
        clear((float) brightness, (float) alpha);
    }

    public void clear(float brightness) {
        clear(brightness, 255);
    }

    public void clear(int brightness) {
        clear((float) brightness);
    }

    public void clear(Color color) {
        clear(color.r, color.g, color.b, color.a);
    }

    public void clear(java.awt.Color color) {
        clear(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public void rect(int x, int y, int w, int h) {
        rect((float) x, (float) y, (float) w, (float) h);
    }

    public void rect(float w, float h) {
        rect(0, 0, w, h);
    }

    public void rect(int w, int h) {
        rect((float) w, (float) h);
    }

    public void rect(Size2f size) {
        rect(size.width, size.height);
    }

    public void rect(Size2i size) {
        rect(size.width, size.height);
    }

    public void rect(Vec2D pos, float w, float h) {
        rect(pos.x, pos.y, w, h);
    }

    public void rect(Vec2D pos, int w, int h) {
        rect(pos, (float) w, (float) h);
    }

    public void rect(Vec2D pos, Vec2D size) {
        rect(pos, size.x, size.y);
    }

    public void rect(Vec2D pos, Size2i size) {
        rect(pos, size.width, size.height);
    }

    public void rect(Vec2D pos, Size2f size) {
        rect(pos, size.width, size.height);
    }

    public void rect(Rect rect) {
        rect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }

    public void circle(float x, float y, float radius) {
        circle(x, y, radius, 360);
    }

    public void circle(int x, int y, int radius, int pointCount) {
        circle((float) x, (float) y, (float) radius, pointCount);
    }

    public void circle(int x, int y, int radius) {
        circle((float) x, (float) y, (float) radius);
    }

    public void circle(Vec2D pos, float radius, int pointCount) {
        circle(pos.x, pos.y, radius, pointCount);
    }

    public void circle(Vec2D pos, float radius) {
        circle(pos.x, pos.y, radius);
    }

    public void circle(float radius, int pointCount) {
        circle(0, 0, radius, pointCount);
    }

    public void circle(int radius, int pointCount) {
        circle((float) radius, pointCount);
    }

    public void circle(float radius) {
        circle(0, 0, radius);
    }

    public void circle(int radius) {
        circle((float) radius);
    }

    public void line(int x1, int y1, int x2, int y2) {
        line((float) x1, (float) y1, (float) x2, (float) y2);
    }

    public void line(float x1, float y1, Vec2D pos2) {
        line(x1, y1, pos2.x, pos2.y);
    }

    public void line(int x1, int y1, Vec2D pos2) {
        line((float) x1, (float) y1, pos2);
    }

    public void line(Vec2D pos1, float x2, float y2) {
        line(pos1.x, pos1.y, x2, y2);
    }

    public void line(Vec2D pos1, int x2, int y2) {
        line(pos1, (float) x2, (float) y2);
    }

    public void line(Vec2D pos1, Vec2D pos2) {
        line(pos1.x, pos1.y, pos2.x, pos2.y);
    }

    public void text(int x, int y, String text) {
        text((float) x, (float) y, text);
    }

    public void text(Vec2D pos, String text) {
        text(pos.x, pos.y, text);
    }

    public void blit(Displayable displayable) {
        blit(0, 0, displayable);
    }
    public void blit(int x, int y, Displayable displayable) {
        blit((float) x, (float) y, displayable);
    }

    public void blit(Vec2D pos, Displayable displayable) {
        blit(pos.x, pos.y, displayable);
    }

    public void blit(int x, int y, Displayable displayable, int srcX, int srcY, int srcW, int srcH) {
        blit((float) x, (float) y, displayable, (float) srcX, (float) srcY, (float) srcW, (float) srcH);
    }

    public void blit(Vec2D pos, Displayable displayable, float srcX, float srcY, float srcW, float srcH) {
        blit(pos.x, pos.y, displayable, srcX, srcY, srcW, srcH);
    }

    public void blit(Vec2D pos, Displayable displayable, int srcX, int srcY, int srcW, int srcH) {
        blit(pos, displayable, (float) srcX, (float) srcY, (float) srcW, (float) srcH);
    }

    public void blit(Vec2D pos, Displayable displayable, Vec2D srcPos, float srcW, float srcH) {
        blit(pos, displayable, srcPos.x, srcPos.y, srcW, srcH);
    }

    public void blit(Vec2D pos, Displayable displayable, Vec2D srcPos, int srcW, int srcH) {
        blit(pos, displayable, srcPos, (float) srcW, (float) srcH);
    }

    public void blit(Vec2D pos, Displayable displayable, Vec2D srcPos, Vec2D srcSize) {
        blit(pos, displayable, srcPos, srcSize.x, srcSize.y);
    }

    public void blit(Vec2D pos, Displayable displayable, Vec2D srcPos, Size2f srcSize) {
        blit(pos, displayable, srcPos, srcSize.width, srcSize.height);
    }

    public void blit(Vec2D pos, Displayable displayable, Vec2D srcPos, Size2i srcSize) {
        blit(pos, displayable, srcPos, srcSize.width, srcSize.height);
    }

    // Transform
    public void translate(int x, int y) {
        translate((float) x, (float) y);
    }

    public void translate(Vec2D vec) {
        translate(vec.x, vec.y);
    }

    public void translateX(float x) {
        translate(x, 0);
    }

    public void translateX(int x) {
        translateX((float) x);
    }

    public void translateY(float y) {
        translate(0, y);
    }

    public void translateY(int y) {
        translateY((float) y);
    }

    public void scale(int x, int y) {
        scale((float) x, (float) y);
    }

    public void scale(float scale) {
        scale(scale, scale);
    }

    public void scale(int scale) {
        scale((float) scale);
    }

    public void rotate(int angle) {
        rotate((float) angle);
    }

}
