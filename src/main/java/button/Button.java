package button;

import models.Model;
import models.Transform;
import org.joml.Vector3f;
import render.Camera;
import render.Shader;
import render.Texture;

public class Button {
    private Model model;
    private models.Transform tr;
    private Texture texture;

    public Button(String str,float dec) {
        this.texture = new Texture(str);
        float[] vertices=new float[]{
                7f,-5f-dec,0,//TOP LEFT     0
                8f,-5f-dec,0,//TOP RIGHT     1
                8f,-6f-dec,0,//BOTTOM RIGHT 2

                // -0.5f,0.5f,0,//TOP LEFT
                // 0.5f,-0.5f,0,//BOTTOM RIGHT
                7f,-6f-dec,0//BOTTOM LEFT  3

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
        tr.scale = new Vector3f(100,200,1);
        tr.pos.x=0;
        tr.pos.y=0;
    }

    public void render(Shader shader, Camera camera){
        shader.bind();
        shader.setUniform("sampler",0 );
        shader.setUniform("projection", tr.getProjection(camera.getProjection()));
        texture.bind(0);

        model.render();
    }

}
