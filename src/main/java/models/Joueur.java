package models;

import collision.AABB;
import collision.Collision;
import org.joml.Vector2f;
import org.joml.Vector3f;
import render.Animation;
import render.Camera;
import render.Shader;
import render.Texture;
import windows.Window;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;

public class Joueur {
    private int vie;
    private int attaque;
    private float posX;
    private float posY;
    private Model model;
    private Animation texture;
    private models.Transform tr;

    private AABB boundingBox;

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
        this.texture = new Animation(2,10,"joueur");


        AABB box=null;

        tr = new Transform();
        tr.scale = new Vector3f(16,16,1);
        boundingBox=new AABB(new Vector2f(tr.pos.x,tr.pos.y),new Vector2f(1,1));

    }
    public void setPos(int x, int y) {
        tr.pos.x = x;
        tr.pos.y = y;
        posX = tr.pos.x;
        posY = tr.pos.y;
    }


    public void deplacement(float delta, Window win, Camera camera, Labyrinthe world){
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
        posX = tr.pos.x;
        posY = tr.pos.y;
        boundingBox.getCenter().set(tr.pos.x,tr.pos.y);

        AABB[] boxes=new AABB[25];
        for(int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                boxes[i+j*5]=world.verifierCollision((int)(((tr.pos.x/2)+0.5f)-(5/2))+i,(int)(((-tr.pos.y/2)+0.5f)-(5/2))+j);
            }
        }

        AABB box=null;
        for(int i=0;i<boxes.length;i++){
            if(boxes[i]!=null){
                if(box==null){
                    box=boxes[i];
                }
                Vector2f length1 = box.getCenter().sub(tr.pos.x,tr.pos.y,new Vector2f());
                Vector2f length2 = boxes[i].getCenter().sub(tr.pos.x,tr.pos.y,new Vector2f());

                if(length1.lengthSquared() > length2.lengthSquared()){
                    box = boxes[i];
                }else{

                }
            }
        }

        if(box!=null) {
            Collision data = boundingBox.getCollision(box);
            if (data.isIntersecting) {
                boundingBox.correctPosition(box, data);
                tr.pos.set(boundingBox.getCenter(),0);
            }
        }
        //System.out.println("POS X du joueur : " + posX + "\nPOS X caméra : " +camera.getPosition().x + "\nwindows diviser par 2 : "+win.getWidth()/2);
        //if (posX>=camera.getPosition().x)
            camera.setPosition(tr.pos.mul(-world.getScale()/2f/*-16*/,new Vector3f()));
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


    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }
}
