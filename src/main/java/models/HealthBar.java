package models;

import models.Model;
import models.Transform;
import org.joml.Vector3f;
import render.Shader;
import render.Texture;


public class HealthBar {
    private int vie_total;
    private Model model_red;
    private Texture texture_red;
    private Model model_green;
    private Texture texture_green;
    private models.Transform tr;


    public HealthBar(int vie){
        this.vie_total = vie;

        float[] vertices=new float[]{
                -2f,-3f,0,//TOP LEFT     0
                2f,-3f,0,//TOP RIGHT     1
                2f,-4f,0,//BOTTOM RIGHT 2

                // -0.5f,0.5f,0,//TOP LEFT
                // 0.5f,-0.5f,0,//BOTTOM RIGHT
                -2f,-4f,0//BOTTOM LEFT  3

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
        model_red =new Model(vertices,texture,indices);
        this.texture_red = new Texture("healthbar.png");
        model_green = new Model(vertices,texture,indices);
        this.texture_green = new Texture("healthbar_full.png");
        tr = new Transform();
        tr.scale = new Vector3f(16,16,1);
    }

    public void update(float vie){
        float pourcentage = vie/vie_total;
        float[] vertices=new float[]{
                -2f*pourcentage,-3f,0,//TOP LEFT     0
                2f*pourcentage,-3f,0,//TOP RIGHT     1
                2f*pourcentage,-4f,0,//BOTTOM RIGHT 2

                // -0.5f,0.5f,0,//TOP LEFT
                // 0.5f,-0.5f,0,//BOTTOM RIGHT
                -2f*pourcentage,-4f,0//BOTTOM LEFT  3
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
        model_green = new Model(vertices,texture,indices);
        //this.texture_green = new Texture("healthbar_full.png");
    }

    public void render(Shader shader){
        shader.bind();
        shader.setUniform("sampler",0 );

        texture_red.bind(0);
        model_red.render();
        texture_green.bind(0);
        model_green.render();
    }
}
