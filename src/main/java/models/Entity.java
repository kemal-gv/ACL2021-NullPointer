package models;

import collision.AABB;
import collision.Collision;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import render.Animation;
import render.Camera;
import render.Shader;
import render.Texture;
import tiles.*;
import windows.Window;

import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;

public class Entity {
    private double vie;
    private int attaque;
    protected float posX;
    protected float posY;
    private static Model model;
    protected Tile tile;
    protected final Transform tr;

    private int id;
    private AABB boundingBox;

    public Tile getTile(){
        return tile;
    }

    public void rotate(){
        model.rotate();
    }
    public Entity(float posX, float posY, Tile tile){

        this.id=tile.getId();
        this.tile=tile;

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
        //this.texture = new Animation(2,10,"joueur");


        AABB box=null;

        tr = new Transform();
        tr.scale = new Vector3f(32,32,1);
        tr.pos.x=posX;
        tr.pos.y=posY;
        this.posX = posX;
        this.posY = posY;
        boundingBox=new AABB(new Vector2f(posX,posY),new Vector2f(tr.scale.x,tr.scale.y));

    }
    public void setPos(int x, int y) {
        tr.pos.x = x;
        tr.pos.y = y;
        posX = tr.pos.x;
        posY = tr.pos.y;
    }


    public void render(int id, double x, double y, Shader sh, Matrix4f world, Camera cam, GestionnaireTile gestionnaireTile,TileRenderer tl){

        HashMap<String , Texture> tileTexture=tl.getTileTexture();
        sh.bind();
        /*
        if(tileTexture.containsKey(gestionnaireTile.getTile(id).getTexture())){
            Tile t=gestionnaireTile.getTile(id);
            if(t.isAnimated()){
                AnimatedTile a=(AnimatedTile)t;
                a.bind();


            }
            else {
                tileTexture.get(gestionnaireTile.getTile(id).getTexture()).bind(0);
            }
        }


         */
        /*
        if(tile.getId()==15){
            AnimatedChest acTile= (AnimatedChest)tile;
            acTile.bind(0);
        }

         */
        tileTexture.get(tile.getTexture()).bind(0);

        tile.bind();

        Matrix4f tilePosition=new Matrix4f().translate(new Vector3f((float)x*2,(float)y*2,0) );
        Matrix4f target = new Matrix4f();

        cam.getProjection().mul(world,target);//Projection dans le monde
        target.mul(tilePosition);//on place le tile


        sh.setUniform("sampler",0);
        sh.setUniform("projection",target);

        model.render();


    }

    public double getVie(){
        return this.vie;
    }

    public void setVie(double vie){
        this.vie = vie;
    }


    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }


    public static void initAsset(){

    }
    public static void deleteAsset(){
        model=null;
    }

    public int getId() {
        return id;
    }
}
