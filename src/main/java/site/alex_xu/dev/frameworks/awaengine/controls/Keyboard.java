package site.alex_xu.dev.frameworks.awaengine.controls;

import site.alex_xu.dev.frameworks.awaengine.core.Core;

import java.util.ArrayList;
import java.util.HashSet;

public class Keyboard extends Core {

    private static final HashSet<Keyboard> registeredListeners = new HashSet<>();
    protected boolean acceptRepeatKeys;
    private boolean listenerCreated = false;

    public Keyboard(boolean acceptRepeatKeys) {
        this.acceptRepeatKeys = acceptRepeatKeys;
    }

    public Keyboard() {
        this(false);
    }

    protected static void _internalKeyboardEvents() {
        while (org.lwjgl.input.Keyboard.next()) {
            boolean isRepeat = org.lwjgl.input.Keyboard.isRepeatEvent();
            int eventKey = org.lwjgl.input.Keyboard.getEventKey();
            boolean eventKeyState = org.lwjgl.input.Keyboard.getEventKeyState();
            char character = org.lwjgl.input.Keyboard.getEventCharacter();

            if (eventKeyState) {
                _internalOnPress(isRepeat, eventKey, character);
            } else {
                _internalOnRelease(eventKey);
            }
        }
    }

    private static void _internalOnPress(boolean isRepeat, int key, char character) {
        ArrayList<Keyboard> removingList = new ArrayList<>();
        registeredListeners.forEach(keyboard -> {
            try {
                if (!(isRepeat && !keyboard.acceptRepeatKeys))
                    keyboard.onPress(key, isRepeat, character);
            } catch (Exception e) {
                e.printStackTrace();
                removingList.add(keyboard);
                System.err.println("A Keyboard Listener has been un-registered due to an exception occurred.");
            }
        });
        removingList.forEach(registeredListeners::remove);
    }

    private static void _internalOnRelease(int key) {
        ArrayList<Keyboard> removingList = new ArrayList<>();
        registeredListeners.forEach(keyboard -> {
            try {
                keyboard.onRelease(key);
            } catch (Exception e) {
                e.printStackTrace();
                removingList.add(keyboard);
                System.err.println("A Keyboard Listener has been un-registered due to an exception occurred.");
            }
        });
        removingList.forEach(registeredListeners::remove);
    }

    public static int getKeyIndex(String keyName) {
        return org.lwjgl.input.Keyboard.getKeyIndex(keyName);
    }

    public static String getKeyName(int key) {
        return org.lwjgl.input.Keyboard.getKeyName(key);
    }

    public static boolean isKeyPressed(int key) {
        return org.lwjgl.input.Keyboard.isKeyDown(key);
    }

    public static boolean isKeyReleased(int key) {
        return !isKeyPressed(key);
    }

    public boolean isKeyDown(int key) {
        return isKeyPressed(key);
    }

    public boolean isKeyUp(int key) {
        return isKeyReleased(key);
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

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        destroyListener();
    }

    public void onPress(int key, boolean isRepeat, char character) {

    }

    public void onRelease(int key) {

    }

    public static class Key {

        public static final int KEY_ESCAPE = 1;
        public static final int KEY_1 = 2;
        public static final int KEY_2 = 3;
        public static final int KEY_3 = 4;
        public static final int KEY_4 = 5;
        public static final int KEY_5 = 6;
        public static final int KEY_6 = 7;
        public static final int KEY_7 = 8;
        public static final int KEY_8 = 9;
        public static final int KEY_9 = 10;
        public static final int KEY_0 = 11;
        public static final int KEY_MINUS = 12;
        public static final int KEY_EQUALS = 13;
        public static final int KEY_BACK = 14;
        public static final int KEY_TAB = 15;
        public static final int KEY_Q = 16;
        public static final int KEY_W = 17;
        public static final int KEY_E = 18;
        public static final int KEY_R = 19;
        public static final int KEY_T = 20;
        public static final int KEY_Y = 21;
        public static final int KEY_U = 22;
        public static final int KEY_I = 23;
        public static final int KEY_O = 24;
        public static final int KEY_P = 25;
        public static final int KEY_LBRACKET = 26;
        public static final int KEY_RBRACKET = 27;
        public static final int KEY_RETURN = 28;
        public static final int KEY_LCONTROL = 29;
        public static final int KEY_A = 30;
        public static final int KEY_S = 31;
        public static final int KEY_D = 32;
        public static final int KEY_F = 33;
        public static final int KEY_G = 34;
        public static final int KEY_H = 35;
        public static final int KEY_J = 36;
        public static final int KEY_K = 37;
        public static final int KEY_L = 38;
        public static final int KEY_SEMICOLON = 39;
        public static final int KEY_APOSTROPHE = 40;
        public static final int KEY_GRAVE = 41;
        public static final int KEY_LSHIFT = 42;
        public static final int KEY_BACKSLASH = 43;
        public static final int KEY_Z = 44;
        public static final int KEY_X = 45;
        public static final int KEY_C = 46;
        public static final int KEY_V = 47;
        public static final int KEY_B = 48;
        public static final int KEY_N = 49;
        public static final int KEY_M = 50;
        public static final int KEY_COMMA = 51;
        public static final int KEY_PERIOD = 52;
        public static final int KEY_SLASH = 53;
        public static final int KEY_RSHIFT = 54;
        public static final int KEY_MULTIPLY = 55;
        public static final int KEY_LMENU = 56;
        public static final int KEY_SPACE = 57;
        public static final int KEY_CAPITAL = 58;
        public static final int KEY_F1 = 59;
        public static final int KEY_F2 = 60;
        public static final int KEY_F3 = 61;
        public static final int KEY_F4 = 62;
        public static final int KEY_F5 = 63;
        public static final int KEY_F6 = 64;
        public static final int KEY_F7 = 65;
        public static final int KEY_F8 = 66;
        public static final int KEY_F9 = 67;
        public static final int KEY_F10 = 68;
        public static final int KEY_NUMLOCK = 69;
        public static final int KEY_SCROLL = 70;
        public static final int KEY_NUMPAD7 = 71;
        public static final int KEY_NUMPAD8 = 72;
        public static final int KEY_NUMPAD9 = 73;
        public static final int KEY_SUBTRACT = 74;
        public static final int KEY_NUMPAD4 = 75;
        public static final int KEY_NUMPAD5 = 76;
        public static final int KEY_NUMPAD6 = 77;
        public static final int KEY_ADD = 78;
        public static final int KEY_NUMPAD1 = 79;
        public static final int KEY_NUMPAD2 = 80;
        public static final int KEY_NUMPAD3 = 81;
        public static final int KEY_NUMPAD0 = 82;
        public static final int KEY_DECIMAL = 83;
        public static final int KEY_F11 = 87;
        public static final int KEY_F12 = 88;
        public static final int KEY_F13 = 100;
        public static final int KEY_F14 = 101;
        public static final int KEY_F15 = 102;
        public static final int KEY_F16 = 103;
        public static final int KEY_F17 = 104;
        public static final int KEY_F18 = 105;
        public static final int KEY_KANA = 112;
        public static final int KEY_F19 = 113;
        public static final int KEY_CONVERT = 121;
        public static final int KEY_NOCONVERT = 123;
        public static final int KEY_YEN = 125;
        public static final int KEY_NUMPADEQUALS = 141;
        public static final int KEY_CIRCUMFLEX = 144;
        public static final int KEY_AT = 145;
        public static final int KEY_COLON = 146;
        public static final int KEY_UNDERLINE = 147;
        public static final int KEY_KANJI = 148;
        public static final int KEY_STOP = 149;
        public static final int KEY_AX = 150;
        public static final int KEY_UNLABELED = 151;
        public static final int KEY_NUMPADENTER = 156;
        public static final int KEY_RCONTROL = 157;
        public static final int KEY_SECTION = 167;
        public static final int KEY_NUMPADCOMMA = 179;
        public static final int KEY_DIVIDE = 181;
        public static final int KEY_SYSRQ = 183;
        public static final int KEY_RMENU = 184;
        public static final int KEY_FUNCTION = 196;
        public static final int KEY_PAUSE = 197;
        public static final int KEY_HOME = 199;
        public static final int KEY_UP = 200;
        public static final int KEY_PRIOR = 201;
        public static final int KEY_LEFT = 203;
        public static final int KEY_RIGHT = 205;
        public static final int KEY_END = 207;
        public static final int KEY_DOWN = 208;
        public static final int KEY_NEXT = 209;
        public static final int KEY_INSERT = 210;
        public static final int KEY_DELETE = 211;
        public static final int KEY_CLEAR = 218;
        public static final int KEY_LMETA = 219;
        public static final int KEY_LWIN = 219;
        public static final int KEY_RMETA = 220;
        public static final int KEY_RWIN = 220;
        public static final int KEY_APPS = 221;
        public static final int KEY_POWER = 222;
        public static final int KEY_SLEEP = 223;
    }
}
