package models;


import org.joml.Vector3f;
import render.Camera;
import render.Shader;
import render.Texture;
import windows.Window;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_Q;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;

public class GameOver {
    Texture texture;
    Model model;
    Transform tr;
    Boolean continuer;


    public GameOver() {
        float[] vertices = new float[]{
                0f, 0f, 0,//TOP LEFT     0
                640f, 0f, 0,//TOP RIGHT     1
                640f, -480f, 0,//BOTTOM RIGHT 2

                // -0.5f,0.5f,0,//TOP LEFT
                // 0.5f,-0.5f,0,//BOTTOM RIGHT
                0f, -480f, 0//BOTTOM LEFT  3

        };


        float[] texture = new float[]{
                0, 0,
                1, 0,
                1, 1,
                // 0,0,
                // 1,1,
                0, 1

        };


        int[] indices = new int[]{
                0, 1, 2,
                2, 3, 0
        };
        model = new Model(vertices, texture, indices);
        this.texture = new Texture("die_screen.png");
        continuer = true;
        tr = new Transform();
        tr.scale = new Vector3f(1, 1, 1);

    }

    public void update(Window win) {
        if (win.getInput().isKeyPressed(GLFW_KEY_R)) {
            continuer = true;
            //System.out.println("RRRR");
        }
        if (win.getInput().isKeyPressed(GLFW_KEY_Q)) {
            continuer = false;
            //System.out.println("QQQQ");
        }
    }

    public void render(Shader shader, Camera cam) {
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", tr.getProjection(cam.getProjection()));
        texture.bind(0);

        model.render();

    }

    public boolean getContinuer() {
        return continuer;
    }
}
