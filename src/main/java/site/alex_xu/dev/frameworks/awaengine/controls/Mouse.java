package site.alex_xu.dev.frameworks.awaengine.controls;

import site.alex_xu.dev.frameworks.awaengine.core.Core;
import site.alex_xu.dev.frameworks.awaengine.video.Window;

import java.util.ArrayList;
import java.util.HashSet;

public class Mouse extends Core {

    private static final boolean[] buttonStates = new boolean[16];
    private static final HashSet<Mouse> registeredListeners = new HashSet<>();
    private boolean listenerCreated = false;

    protected static void _internalMouseEvents() {
        while (org.lwjgl.input.Mouse.next()) {
            float eventX = org.lwjgl.input.Mouse.getEventX();
            float eventY = Window.getInstance().getHeight() - org.lwjgl.input.Mouse.getEventY();
            float eventDX = org.lwjgl.input.Mouse.getEventDX();
            float eventDY = -org.lwjgl.input.Mouse.getEventDY();
            int eventDWheel = org.lwjgl.input.Mouse.getEventDWheel();
            int eventButton = org.lwjgl.input.Mouse.getEventButton();
            boolean eventButtonState = org.lwjgl.input.Mouse.getEventButtonState();


            if (eventDX != 0 || eventDY != 0) {  // motionEvent
                _internalOnMotion(eventX, eventY, eventDX, eventDY);
            }
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

    public Mouse() {

    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        destroyListener();
    }

    public void createListener() {
        if (!listenerCreated) {
            synchronized (registeredListeners) {
                registeredListeners.add(this);
            }
            listenerCreated = true;
        }
    }

    public void destroyListener() {
        if (listenerCreated) {
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
