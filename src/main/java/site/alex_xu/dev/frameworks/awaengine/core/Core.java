package site.alex_xu.dev.frameworks.awaengine.core;

public class Core {
    public static class Time {
        public static float now = 0;
        public static float tps = 0;
        public static float tpsMin = 0;
        public static float tpsMax = 0;
        public static float tpsAvg = 0;
        public static float delta = 0;
        public static float safe_delta = 0f;
    }

    public static class Video {
        public static float fps = 0;
        public static float fpsMin = 0;
        public static float fpsMax = 0;
        public static float fpsAvg = 0;
        public static float delta = 0;
    }
}
