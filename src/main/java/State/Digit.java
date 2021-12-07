package State;

import com.sun.source.doctree.TextTree;
import models.Model;
import models.Transform;
import org.joml.Vector3f;
import render.Camera;
import render.Shader;
import render.Texture;
import windows.Window;

import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class Digit {
    private Texture number;
    private Model model;
    private models.Transform tr;


    public Digit(int i,float dec) {

        number=new Texture("number/" + i + ".png");

        float[] vertices=new float[]{
                0f,0f-dec,0,//TOP LEFT     0
                1.7f,0f-dec,0,//TOP RIGHT     1
                1.7f,-1.7f-dec,0,//BOTTOM RIGHT 2

                // -0.5f,0.5f,0,//TOP LEFT
                // 0.5f,-0.5f,0,//BOTTOM RIGHT
                0,-1.7f-dec,0//BOTTOM LEFT  3

        };
        float[] texture=new float[]{
                0,0,
                1,0,
                1,1,
                // 0,0,
                // 1,1,
                0,1

        };
        int[] indices = new int[]{
                0,1,2,
                2,3,0
        };
        model=new Model(vertices,texture,indices);
        tr = new Transform();
        tr.scale = new Vector3f(64,64,1);
        tr.pos.x=0;
        tr.pos.y=0;

}

    public Digit(String str,float dec) {

        number=new Texture("number/" + str + ".png");

        float[] vertices=new float[]{
                0f,0f-dec,0,//TOP LEFT     0
                1.7f,0f-dec,0,//TOP RIGHT     1
                1.7f,-1.7f-dec,0,//BOTTOM RIGHT 2

                // -0.5f,0.5f,0,//TOP LEFT
                // 0.5f,-0.5f,0,//BOTTOM RIGHT
                0,-1.7f-dec,0//BOTTOM LEFT  3

        };
        float[] texture=new float[]{
                0,0,
                1,0,
                1,1,
                // 0,0,
                // 1,1,
                0,1

        };
        int[] indices = new int[]{
                0,1,2,
                2,3,0
        };
        model=new Model(vertices,texture,indices);
        tr = new Transform();
        tr.scale = new Vector3f(64,64,1);
        tr.pos.x=0;
        tr.pos.y=0;

    }

    public void render(Shader shader, Camera camera){
        shader.bind();
        shader.setUniform("sampler",0 );
        shader.setUniform("projection", tr.getProjection(camera.getProjection()));

        number.bind(0);
        model.render();

    }
}
