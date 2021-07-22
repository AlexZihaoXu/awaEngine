package site.alex_xu.dev.frameworks.awaengine.core;

public @interface Settings {
    class LWJGL {
        public static String nativesPath = "natives";
    }

    class Video {
        public static float fontResolution = 1f / 4f;
        public static float fpsLimit = 300;
        public static boolean reduceFpsWhenLostFocus = true;
        public static float reducedFps = 20;
    }

    class Update {
        public static boolean reduceUpdatesWhenLostFocus = true;
        public static float reducedTps = 30;
    }
}
