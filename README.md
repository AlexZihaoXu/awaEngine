# awaEngine
> **awaEngine** is a free game engine under WTFPL made with LWJGL and Slick2D

## How to use
To add this library, open `build.gradle` and add the following lines:
```kts
dependencies {
    implementation 'com.github.AlexZihaoXu:awaEngine:1.0-SNAPSHOT'
    implementation 'org.lwjgl.lwjgl:lwjgl:2.9.3'
    implementation 'org.slick2d:slick2d-core:1.0.2'
    implementation 'com.github.AlexZihaoXu:LibAlexUtils:1.0-SNAPSHOT'
}
```

## Example
```java
package your.game;
import site.alex_xu.dev.frameworks.awaengine.video.Window;

public class Main {
    public static void main(String[] args) {
        Window window = new Window("awaEngine Demo", 800, 450) {
            float angle = 0;

            @Override
            public void setup() { // gets called when the window is created

            }

            @Override
            public void destroy() { // gets called when window closed

            }

            @Override
            public void draw() {

                clear(200, 200, 200, 255); // clean up the background

                pushMatrix();  // Save current transform state

                float centerX = getWidth() / 2f; // get center x of the window
                float centerY = getHeight() / 2f; // get center y of the window
                translate(centerX, centerY);  // translate to the center of the window

                rotate(angle); // rotate for angle degrees

                translate(-50, -50);  // translate back by half of the rectangle size to make it centered

                strokeWeight(2);  // set stroke weight to 2
                fill(180, 255, 180, 255);  // set filling color to green
                rect(0, 0, 100, 100);  // draw a rectangle in the center

                popMatrix();  // restore previous state

            }

            @Override
            public void update() {

                angle += 50 * Time.delta; // Rotate by 50 * delta time

            }
        };

        window.launch(); // launch the window
    }
}
```
Running this class will start a window and display a rotating green square in the center of the screen.
For more tutorial please visit our wiki.