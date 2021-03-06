package site.alex_xu.dev.frameworks.awaengine.controls;

import site.alex_xu.dev.frameworks.awaengine.core.Core;
import site.alex_xu.dev.frameworks.awaengine.video.Window;
import site.alex_xu.dev.utils.Vec2D;

import java.util.ArrayList;
import java.util.HashSet;

public class Mouse extends Core {

    public static final Vec2D position = new Vec2D();
    private static final boolean[] buttonStates = new boolean[16];
    private static final HashSet<Mouse> registeredListeners = new HashSet<>();
    private boolean listenerCreated = false;

    public Mouse() {

    }

    public static float getX() {
        return position.x;
    }

    public static float getY() {
        return position.y;
    }

    public static boolean pressed(int button) {
        if (button < 0 || button > 15)
            return false;
        return buttonStates[button];
    }

    public static boolean released(int button) {
        return !pressed(button);
    }

    protected static void _internalMouseEvents() {
        while (org.lwjgl.input.Mouse.next()) {
            float eventX = org.lwjgl.input.Mouse.getEventX();
            float eventY = Window.getInstance().getHeight() - org.lwjgl.input.Mouse.getEventY();
            float eventDX = org.lwjgl.input.Mouse.getEventDX();
            float eventDY = -org.lwjgl.input.Mouse.getEventDY();
            int eventDWheel = org.lwjgl.input.Mouse.getEventDWheel();
            int eventButton = org.lwjgl.input.Mouse.getEventButton();
            boolean eventButtonState = org.lwjgl.input.Mouse.getEventButtonState();

            position.x = eventX;
            position.y = eventY;

            if (eventDWheel != 0) {
                _internalOnWheel(eventDWheel / 120f);
            }
            if (eventButton != -1 && buttonStates[eventButton] != eventButtonState) {
                buttonStates[eventButton] = eventButtonState;
                if (eventButtonState) {
                    _internalOnPress(eventX, eventY, eventButton);
                } else {
                    _internalOnRelease(eventX, eventY, eventButton);
                }
            }
            if (eventDX != 0 || eventDY != 0) {  // motionEvent
                _internalOnMotion(eventX, eventY, eventDX, eventDY);
            }

        }
    }

    private static void _internalOnMotion(float x, float y, float dx, float dy) {
        ArrayList<Mouse> removingList = new ArrayList<>();
        registeredListeners.forEach(mouse -> {
            try {
                mouse.onMotion(x, y, dx, dy);
            } catch (Exception e) {
                e.printStackTrace();
                removingList.add(mouse);
                System.err.println("A Mouse Listener has been un-registered due to an exception occurred.");
            }
        });
        removingList.forEach(registeredListeners::remove);
    }

    private static void _internalOnWheel(float delta) {
        ArrayList<Mouse> removingList = new ArrayList<>();
        registeredListeners.forEach(mouse -> {
            try {
                mouse.onWheel(delta);
            } catch (Exception e) {
                e.printStackTrace();
                removingList.add(mouse);
                System.err.println("A Mouse Listener has been un-registered due to an exception occurred.");
            }
        });
        removingList.forEach(registeredListeners::remove);
    }

    private static void _internalOnPress(float x, float y, int button) {
        ArrayList<Mouse> removingList = new ArrayList<>();
        registeredListeners.forEach(mouse -> {
            try {
                mouse.onPress(x, y, button);
            } catch (Exception e) {
                e.printStackTrace();
                removingList.add(mouse);
                System.err.println("A Mouse Listener has been un-registered due to an exception occurred.");
            }
        });
        removingList.forEach(registeredListeners::remove);
    }

    private static void _internalOnRelease(float x, float y, int button) {
        ArrayList<Mouse> removingList = new ArrayList<>();
        registeredListeners.forEach(mouse -> {
            try {
                mouse.onRelease(x, y, button);
            } catch (Exception e) {
                e.printStackTrace();
                removingList.add(mouse);
            }
        });
        removingList.forEach(registeredListeners::remove);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        destroyListener();
    }

    public void createListener() {
        if (!listenerCreated) {
            //noinspection SynchronizationOnStaticField
            synchronized (registeredListeners) {
                registeredListeners.add(this);
            }
            listenerCreated = true;
        }
    }

    public void destroyListener() {
        if (listenerCreated) {
            //noinspection SynchronizationOnStaticField
            synchronized (registeredListeners) {
                registeredListeners.remove(this);
            }
            listenerCreated = false;
        }
    }

    public void onWheel(float delta) {
    }

    public void onMotion(float x, float y, float dx, float dy) {

    }

    public void onPress(float x, float y, int button) {

    }

    public void onRelease(float x, float y, int button) {

    }
}
