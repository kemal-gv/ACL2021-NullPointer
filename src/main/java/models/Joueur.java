package models;

import labyrinthe.World;
import org.joml.Vector3f;
import render.Camera;
import render.Shader;
import render.Texture;
import windows.Window;

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

    public void update(float delta, Window window, Camera camera, World world){

    }

    public void render(Shader shader, Camera camera){
        shader.bind();
        shader.setUniform("sampler",0 );
        shader.setUniform("projection", tr.getProjection(camera.getProjection()));
        texture.bind(0);
        model.render();
    }


}
