package inputs;


import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class Input {
    private boolean keys[];
    private int[] validKeys = {
            32, 39, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 59, 61, 65, 66, 67, 68, 69, 70,
            71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 96,
            161, 162, 256, 257, 258, 259, 260, 261, 262, 263, 264, 265, 266, 267, 268, 269, 280, 281, 282,
            283, 284, 290, 291, 292, 293, 294, 295, 296, 297, 298, 299, 300, 301, 302, 303, 304, 305, 306,
            307, 308, 309, 310, 311, 312, 313, 314, 320, 321, 322, 323, 324, 325, 326, 327, 328, 329, 330,
            331, 332, 333, 334, 335, 336, 340, 341, 342, 343, 344, 345, 346, 347, 348
    };

    List<Integer> validKeyList = new ArrayList<Integer>();

    private long window;

    public Input(long win) {
        for (int i = 0; i < validKeys.length; i++) {
            validKeyList.add(validKeys[i]);
        }


        this.window = win;
        keys = new boolean[GLFW_KEY_LAST];
        for (int i = 0; i < keys.length; i++) {
            keys[i] = false;
        }
    }

    public boolean isKeyDown(int key) {
        return glfwGetKey(window, key) == 1;
    }

    public boolean isMouseButtonDown(int button) {
        return glfwGetMouseButton(window, button) == 1;
    }

    public boolean isKeyPressed(int key) {
        return (isKeyDown(key) && !keys[key]);
    }

    public void update() {
        for (int i = 0; i < keys.length; i++) {
            if (validKeyList.contains(i))
                keys[i] = isKeyDown(i);
        }
    }

    public boolean isKeyReleased(int key) {
        return (!isKeyDown(key) && keys[key]);
    }
}
