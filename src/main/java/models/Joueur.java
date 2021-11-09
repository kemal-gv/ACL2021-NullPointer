package models;

import labyrinthe.World;
import org.joml.Vector3f;
import render.Camera;
import render.Shader;
import render.Texture;
import windows.Window;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;

public class Joueur {
    private int vie;
    private int attaque;
    private int posX;
    private int posY;
    private Model model;
    private Texture texture;
    private models.Transform tr;

    public Joueur(int vie){
        this.vie = vie;

        float[] vertices=new float[]{
                -1f,1f,0,//TOP LEFT     0
                1f,1f,0,//TOP RIGHT     1
                1f,-1f,0,//BOTTOM RIGHT 2

                // -0.5f,0.5f,0,//TOP LEFT
                // 0.5f,-0.5f,0,//BOTTOM RIGHT
                -1,-1f,0//BOTTOM LEFT  3

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
        this.texture = new Texture("test.png");

        tr = new Transform();
        tr.scale = new Vector3f(16,16,1);
    }

    public void setPos(int x, int y) {
        posX = x;
        posY = y;
    }

    public void update(float delta, Window win, Camera camera, World world){
        if(win.getInput().isKeyDown(GLFW_KEY_LEFT)){
            tr.pos.add(new Vector3f(-10*delta,0,0));
        }
        if(win.getInput().isKeyDown(GLFW_KEY_RIGHT)){
            tr.pos.add(new Vector3f(10*delta,0,0));
        }
        if(win.getInput().isKeyDown(GLFW_KEY_UP)){
            tr.pos.add(new Vector3f(0,10*delta,0));
        }
        if(win.getInput().isKeyDown(GLFW_KEY_DOWN)){
            tr.pos.add(new Vector3f(0,-10*delta,0));
        }
        if(win.getInput().isKeyDown(GLFW_KEY_D)){
            setVie(vie-1);
        }
        System.out.println("POS X du joueur : " + posX + "\nPOS X caméra : " +camera.getPosition().x + "\nwindows diviser par 2 : "+win.getWidth()/2);
        //if (posX>=camera.getPosition().x)
            camera.setPosition(tr.pos.mul(-world.getScale(),new Vector3f()));
    }

    public void render(Shader shader, Camera camera){
        shader.bind();
        shader.setUniform("sampler",0 );
        shader.setUniform("projection", tr.getProjection(camera.getProjection()));
        texture.bind(0);
        model.render();

    }

    public int getVie(){
        return this.vie;
    }

    public void setVie(int vie){
        this.vie = vie;
    }


}
