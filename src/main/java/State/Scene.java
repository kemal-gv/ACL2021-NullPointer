package State;

import button.ManagerButton;
import models.Transform;
import org.joml.Vector3f;
import render.Camera;
import render.Shader;
import render.Texture;
import models.Model;
import windows.Window;

import static org.lwjgl.glfw.GLFW.*;

public class Scene {
    private Texture background;
    private Model model;
    private models.Transform tr;
    private ManagerButton playManagerButton;
    private ManagerButton quitManagerButton;

    public Scene(String str){
        background =  new Texture(str);
        playManagerButton = new ManagerButton("button/jouer_select.png", "button/jouer.png",true,0);
        quitManagerButton = new ManagerButton("button/quitter_select.png", "button/quitter.png",false,0.8f);
        float[] vertices=new float[]{
                0f,0f,0,//TOP LEFT     0
                1.7f,0f,0,//TOP RIGHT     1
                1.7f,-1.7f,0,//BOTTOM RIGHT 2

                // -0.5f,0.5f,0,//TOP LEFT
                // 0.5f,-0.5f,0,//BOTTOM RIGHT
                0,-1.7f,0//BOTTOM LEFT  3

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
        tr.scale = new Vector3f(900,1600,1);
        tr.pos.x=0;
        tr.pos.y=0;

    }

    public boolean input(Window win){
        boolean bool = false;
        if(win.getInput().isKeyPressed(GLFW_KEY_DOWN) || win.getInput().isKeyPressed(GLFW_KEY_UP)){
            playManagerButton.setState(!playManagerButton.isState());
            quitManagerButton.setState(!quitManagerButton.isState());
        }
        if(win.getInput().isKeyPressed(GLFW_KEY_ENTER)){
            if(quitManagerButton.isState()){
                glfwSetWindowShouldClose(win.getWindow(),true);
            }else{
                bool = true;
            }
        }
        return bool;
    }

    public void render(Shader shader, Camera camera){
        shader.bind();
        shader.setUniform("sampler",0 );
        shader.setUniform("projection", tr.getProjection(camera.getProjection()));

        background.bind(0);
        model.render();
        playManagerButton.render(shader,camera);
        quitManagerButton.render(shader,camera);
    }
}
