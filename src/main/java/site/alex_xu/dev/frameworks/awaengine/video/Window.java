package site.alex_xu.dev.frameworks.awaengine.video;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import site.alex_xu.dev.frameworks.awaengine.controls.Keyboard;
import site.alex_xu.dev.frameworks.awaengine.controls.Mouse;
import site.alex_xu.dev.frameworks.awaengine.core.Loader;
import site.alex_xu.dev.frameworks.awaengine.core.Settings;
import site.alex_xu.dev.frameworks.awaengine.exceptions.DuplicateWindowCreationException;
import site.alex_xu.dev.frameworks.awaengine.exceptions.DuplicateWindowLaunchException;
import site.alex_xu.dev.frameworks.awaengine.graphics.Renderable;
import site.alex_xu.dev.utils.Clock;
import site.alex_xu.dev.utils.Size2i;
import site.alex_xu.dev.utils.Vec2D;

import java.awt.*;
import java.io.*;
import java.nio.file.Paths;
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
        InputStream stream = Loader.getResourceAsStream(zipFilePath);
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
    private boolean s_vSyncEnabled;
    private static boolean _logged = false;
    private boolean launched = false;
    private Size2i previousSize;
    private Vec2D previousPos;

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
        s_vSyncEnabled = enabled;
        Display.setVSyncEnabled(enabled);
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

    public boolean isFullscreen() {
        return Display.isFullscreen();
    }

    @Override
    public Image convertAwtImage() {
        return null;
    }

    public static Window getInstance() {
        if (instance == null) {
            throw new IllegalStateException("No window has been created yet!");
        }
        return instance;
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

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
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
            setup();
            Clock deltaClock = new Clock();
            while (!Display.isCloseRequested()) {
                Time.delta = deltaClock.getElapsedTime();
                deltaClock.setNanoClock(Time.delta < 0.005f);
                deltaClock.reset();
                handleEvents();
                update();

                // Rendering
                Display.makeCurrent();
                // Setup 2D view
                prepare();

                draw();

                Display.update();
                Display.processMessages();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            destroy();
        }

    }
}
