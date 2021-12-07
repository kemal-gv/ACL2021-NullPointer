package models;

import framerate.Timer;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import render.Camera;
import render.Shader;
import tiles.AnimatedTile;

public class Weapon extends AnimatedTile {

    private static Model model;
    protected float posX;
    protected float posY;

    private int attaqueDegat = 2;

    public Weapon(int id, String texture, String filename, int amount, int fps, boolean cooldown, float posX, float posY) {
        super(id, texture, filename, amount, fps, cooldown);
        this.posX = posX;
        this.posY = posY;

        float[] vertices = new float[]{
                -1f, 1f, 0,//TOP LEFT     0
                1f, 1f, 0,//TOP RIGHT     1
                1f, -1f, 0,//BOTTOM RIGHT 2

                // -0.5f,0.5f,0,//TOP LEFT
                // 0.5f,-0.5f,0,//BOTTOM RIGHT
                -1, -1f, 0//BOTTOM LEFT  3

        };


        float[] textureT = new float[]{
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
        model = new Model(vertices, textureT, indices);
        // this.texture = new Animation("assets/loot/sword_1");


    }


    public void setPos(double x, double y) {
        posX = (float) x;
        posY = (float) y;
    }

    public boolean attaque = false;


    public void attaque() {
        //setPos(getPosX()+10,getPosY()+10);
        attaque = true;
    }


    public void bind() {
        bind(0);
    }

    protected boolean open = false;
    protected int cpt = 0;

    public void bind(int sampler) {

        if (attaque) {
            this.currentTime = Timer.getTime();
            this.elapsedTime += currentTime - lastTime;

            if (elapsedTime >= fps) {
                if (cooldown) {
                    if (texturePointer == 0) {
                        cpt = 1;
                        open = false;
                        attaque = false;
                    }
                    if (elapsedTime > fps) cpt = 0;
                    if (cpt == 0) {
                        open = true;
                        elapsedTime = 0;
                        texturePointer++;
                    }
                } else {
                    elapsedTime = 0;
                    texturePointer++;
                }
            }

            if (texturePointer >= frames.length) texturePointer = 0;

            this.lastTime = currentTime;

            frames[texturePointer].bind(sampler);
        } else {
            frames[0].bind(sampler);
        }

    }


    public void render(float x, float y, Shader sh, Matrix4f world, Camera cam) {

        sh.bind();
        // if(tileTexture.containsKey(gestionnaireTile.getTile(id).getTexture())){
        bind();

        // }

        Matrix4f tilePosition = new Matrix4f().translate(new Vector3f(x * 2, y * 2, 0));
        Matrix4f target = new Matrix4f();

        cam.getProjection().mul(world, target);//Projection dans le monde
        target.mul(tilePosition);//on place le tile


        sh.setUniform("sampler", 0);
        sh.setUniform("projection", target);

        model.render();
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public int getAttaqueDegat() {
        return attaqueDegat;
    }

    public void setAttaqueDegat(int a) {
        attaqueDegat = a;
    }


}
