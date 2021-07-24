package site.alex_xu.dev.frameworks.awaengine.video;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.opengl.ImageIOImageData;
import site.alex_xu.dev.frameworks.awaengine.audio.Audio;
import site.alex_xu.dev.frameworks.awaengine.audio.Listener;
import site.alex_xu.dev.frameworks.awaengine.audio.SoundSource;
import site.alex_xu.dev.frameworks.awaengine.controls.Keyboard;
import site.alex_xu.dev.frameworks.awaengine.controls.Mouse;
import site.alex_xu.dev.frameworks.awaengine.core.BaseLoader;
import site.alex_xu.dev.frameworks.awaengine.core.Settings;
import site.alex_xu.dev.frameworks.awaengine.exceptions.DuplicateWindowCreationException;
import site.alex_xu.dev.frameworks.awaengine.exceptions.DuplicateWindowLaunchException;
import site.alex_xu.dev.frameworks.awaengine.graphics.Renderable;
import site.alex_xu.dev.frameworks.awaengine.graphics.TextureType;
import site.alex_xu.dev.frameworks.awaengine.scene.Scene;
import site.alex_xu.dev.utils.Clock;
import site.alex_xu.dev.utils.Size2i;
import site.alex_xu.dev.utils.Vec2D;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.lwjgl.opengl.GL11.*;

class NativesManager {
    public static final int BUFFER_SIZE = 4096;


    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

    public static void unzip(String zipFilePath, String destDirectory) throws IOException {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            destDir.mkdir();
        }
        InputStream stream = BaseLoader.getResourceAsStream(zipFilePath);
        ZipInputStream zipIn = new ZipInputStream(stream);
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null) {
            String filePath = destDirectory + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                extractFile(zipIn, filePath);
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                //noinspection ResultOfMethodCallIgnored
                dir.mkdirs();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
        stream.close();
    }

    public static void extractNatives(String dest) {
        String OSType = "Unknown";
        String nativePath = "<Unknown>";
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.startsWith("linux")) {
            OSType = "Linux";
            nativePath = "site/alex_xu/dev/frameworks/awaengine/natives/natives-linux.zip";
        }
        if (osName.startsWith("win")) {
            OSType = "Windows";
            nativePath = "site/alex_xu/dev/frameworks/awaengine/natives/natives-windows.zip";
        }
        if (osName.startsWith("mac") || osName.startsWith("apple")) {
            OSType = "macOS";
            nativePath = "site/alex_xu/dev/frameworks/awaengine/natives/natives-mac.zip";
        }

        if (OSType.equals("Unknown")) {
            throw new RuntimeException("Unknown OS Type detected: " + System.getProperty("os.name"));
        }

        try {

            unzip(nativePath, dest);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Couldn't extract lwjgl natives. ");
        }

    }
}

public abstract class Window extends Renderable {
    private static Window instance;
    private static boolean _logged = false;
    private final LinkedList<Float> fpsQueue = new LinkedList<>();
    private final LinkedList<Float> fpsAddedTimeQueue = new LinkedList<>();
    private final LinkedList<Float> tpsQueue = new LinkedList<>();
    private final LinkedList<Float> tpsAddedTimeQueue = new LinkedList<>();
    private boolean s_vSyncEnabled;
    private boolean launched = false;
    private Size2i previousSize;
    private Vec2D previousPos;
    private Scene scene;
    private float fpsSum = 0;
    private float lastFpsRangeCheck = 0;
    private float tpsSum = 0;
    private float lastTpsRangeCheck = 0;

    public Window(String title, int width, int height, boolean vSyncEnabled, boolean resizable) {

        if (instance != null) {
            throw new DuplicateWindowCreationException("Only one window can be created!");
        }

        instance = this;
        try {

            NativesManager.extractNatives(Settings.LWJGL.nativesPath);
            System.setProperty("org.lwjgl.librarypath", Paths.get(Settings.LWJGL.nativesPath).toAbsolutePath().toString());
            Display.setTitle(title);

            Display.setInitialBackground(0, 0, 0);
            Display.setDisplayMode(new DisplayMode(width, height));
            Display.setResizable(resizable);
            Display.setVSyncEnabled(vSyncEnabled);
            s_vSyncEnabled = vSyncEnabled;

            if (!_logged) {
                String doLog = System.getProperty("site.alex_xu.dev.frameworks.awaengine.disable_startup_hello");
                doLog = doLog == null ? "false" : doLog;
                if (!doLog.equals("true"))
                    System.out.println("Hello from awaEngine (LWJGL " + org.lwjgl.Sys.getVersion() + ")!");
                _logged = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Window(String title, int width, int height, boolean resizable) {
        this(title, width, height, true, resizable);
    }

    public Window(String title, int width, int height) {
        this(title, width, height, true);
    }

    public Window(String title) {
        this(title, 800, 480);
    }

    public Window() {
        this("awaEngine");
    }

    public static Window getInstance() {
        if (instance == null) {
            throw new IllegalStateException("No window has been created yet!");
        }
        return instance;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        if (this.scene != null)
            this.scene.destroy();
        if (scene != null)
            scene.setup();
        this.scene = scene;
    }

    @Override
    protected void bind() {

    }

    @Override
    protected void unbind() {

    }

    @Override
    protected void beginRender() {
        prepare();
    }

    @Override
    protected void endRender() {

    }

    public int getWidth() {
        return Display.getWidth();
    }

    public int getHeight() {
        return Display.getHeight();
    }

    public boolean isVSyncEnabled() {
        return s_vSyncEnabled;
    }

    public void setVSyncEnabled(boolean enabled) {
        if (enabled != s_vSyncEnabled) {
            s_vSyncEnabled = enabled;
            Display.setVSyncEnabled(enabled);
        }
    }

    public boolean isFocused() {
        return Display.isActive();
    }

    public void setPosition(float x, float y) {
        Display.setLocation((int) x, (int) y);
    }

    public Vec2D getPosition() {
        return new Vec2D(Display.getX(), Display.getY());
    }

    public boolean isResizable() {
        return Display.isResizable();
    }

    public void setResizable(boolean resizable) {
        Display.setResizable(resizable);
    }

    public String getTitle() {
        return Display.getTitle();
    }

    public void setTitle(String title) {
        Display.setTitle(title);
    }

    public void setIcon(TextureType texture) {
        try {
            Display.setIcon(
                    new ByteBuffer[]{
                            new ImageIOImageData().imageToByteBuffer((BufferedImage) texture.convertAwtImage(), false, false, null)
                    }
            );
        } catch (NullPointerException ignored) {
        }
    }

    public boolean isFullscreen() {
        return Display.isFullscreen();
    }

    public void setFullscreen(boolean fullscreen) {
        if (fullscreen == isFullscreen())
            return;
        try {
            if (fullscreen) {
                previousSize = getSize();
                previousPos = new Vec2D(Display.getX(), Display.getY());
                Display.setDisplayModeAndFullscreen(Display.getDesktopDisplayMode());
            } else {
                Display.setFullscreen(false);
                Display.setDisplayMode(new DisplayMode(previousSize.width, previousSize.height));
                Display.setLocation((int) previousPos.x, (int) previousPos.y);
            }
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }


    abstract public void setup();

    abstract public void destroy();

    abstract public void draw();

    abstract public void update();

    @Override
    protected void prepare() {
        glViewport(0, 0, Display.getWidth(), Display.getHeight());
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, getWidth(), getHeight(), 0, -1, 1);
        glMatrixMode(GL_MODELVIEW);
        glViewport(0, 0, getWidth(), getHeight());
        glDisable(GL_DEPTH_TEST);

        applyBlendMode();
        glEnable(GL_BLEND);
        glEnable(GL_LINE_SMOOTH);
    }

    @Override
    public void popMatrix() {
        super.popMatrix();
        prepare();
    }

    private void handleEvents() {
        KeyboardEventHandler.handle();
        MouseEventHandler.handle();
    }

    private void calculateVideoStatistics() {
        if (Video.fps > 25565)
            return;

        fpsSum += Video.fps;
        fpsQueue.addLast(Video.fps);
        fpsAddedTimeQueue.addLast(Time.now);
        while (Time.now - fpsAddedTimeQueue.getFirst() >= 1f) {
            fpsSum -= fpsQueue.getFirst();
            fpsAddedTimeQueue.removeFirst();
            fpsQueue.removeFirst();
        }
        Video.fpsAvg = fpsSum / fpsQueue.size();

        if (Time.now - lastFpsRangeCheck >= 0.1f) {
            fpsSum = 0;
            Video.fpsMin = fpsQueue.getFirst();
            Video.fpsMax = fpsQueue.getFirst();

            for (float fps : fpsQueue) {
                if (fps > Video.fpsMax)
                    Video.fpsMax = fps;
                if (fps < Video.fpsMin)
                    Video.fpsMin = fps;
                fpsSum += fps;
                lastFpsRangeCheck = Time.now;
            }
        }
    }

    private void calculateUpdateStatistics() {
        if (Time.tps > 25565)
            return;

        tpsSum += Time.tps;
        tpsQueue.addLast(Time.tps);
        tpsAddedTimeQueue.addLast(Time.now);
        while (Time.now - tpsAddedTimeQueue.getFirst() >= 1f) {
            tpsSum -= tpsQueue.getFirst();
            tpsAddedTimeQueue.removeFirst();
            tpsQueue.removeFirst();
        }
        Time.tpsAvg = tpsSum / tpsQueue.size();

        if (Time.now - lastTpsRangeCheck >= 0.1f) {
            tpsSum = 0;
            Time.tpsMin = tpsQueue.getFirst();
            Time.tpsMax = tpsQueue.getFirst();

            for (float tps : tpsQueue) {
                if (tps > Time.tpsMax)
                    Time.tpsMax = tps;
                if (tps < Time.tpsMin)
                    Time.tpsMin = tps;
                tpsSum += tps;
                lastTpsRangeCheck = Time.now;
            }
        }
    }


    public void launch() {

        if (launched) {
            throw new DuplicateWindowLaunchException("Window is already launched!");
        }

        launched = true;

        try {
            Display.create();
            org.lwjgl.input.Keyboard.enableRepeatEvents(true);
        } catch (LWJGLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create Window");
        }

        try {
            AL.create();
            Listener.setListenerPos(0, 0, 0);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        try {
            setup();
            Clock globalClock = new Clock(true);
            Clock deltaClock = new Clock();
            Clock videoClock = new Clock();

            while (!Display.isCloseRequested()) {
                Time.now = globalClock.getElapsedTime();

                if (Settings.Update.reduceUpdatesWhenLostFocus && !Display.isActive()) {
                    //noinspection BusyWait
                    Thread.sleep((long) (1000 / Settings.Update.reducedTps));
                }

                Time.delta = deltaClock.getElapsedTime();
                Time.safe_delta = Math.max(0, Math.min(Time.delta, 1 / 20f));
                deltaClock.setNanoClock(Time.delta < 0.005f);
                deltaClock.reset();
                Time.tps = 1f / Time.delta;
                calculateUpdateStatistics();
                if (Time.delta <= 0) continue;
                Memory.used = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                Memory.free = Memory.total - Memory.used;
                Memory.usage = Memory.used / (float) Memory.total;
                handleEvents();
                update();
                SceneManager.updateScene(scene);

                if (Settings.Video.reduceFpsWhenLostFocus && !Display.isActive() && videoClock.getElapsedTime() < 1f / Settings.Video.reducedFps)
                    continue;
                if (videoClock.getElapsedTime() < 1f / Settings.Video.fpsLimit) continue;
                Video.delta = videoClock.getElapsedTime();
                videoClock.setNanoClock(Video.delta < 0.005f);
                videoClock.reset();
                Video.fps = 1f / Video.delta;
                calculateVideoStatistics();

                // Rendering
                Display.makeCurrent();
                // Setup 2D view
                prepare();

                draw();
                SceneManager.drawScene(scene, this);

                Display.update(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            destroy();
            if (scene != null)
                scene.destroy();
            SoundSourceManager.performCleanUp();
            AudioManager.performCleanUp();
            AL.destroy();
        }

    }

    @Override
    public Image convertAwtImage(boolean flipped) {
        return super.convertAwtImage(!flipped);
    }

    private static class MouseEventHandler extends Mouse {
        public static void handle() {
            _internalMouseEvents();
        }
    }

    private static class KeyboardEventHandler extends Keyboard {
        public static void handle() {
            _internalKeyboardEvents();
        }
    }

    private static class AudioManager extends Audio {
        protected AudioManager(int buffer) {
            super(buffer);
        }

        public static void performCleanUp() {
            cleanUp();
        }
    }

    private static class SoundSourceManager extends SoundSource {
        public static void performCleanUp() {
            cleanUp();
        }
    }

    private abstract static class SceneManager extends Scene {
        public static void updateScene(Scene scene) {
            if (scene != null) {
                _updateInstance(scene);
            }
        }

        public static void drawScene(Scene scene, Renderable renderable) {
            if (scene != null) {
                _drawScene(scene, renderable);
            }
        }
    }
}
