package labyrinthe;

import models.Model;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import render.Camera;
import render.Shader;
import render.Texture;

import java.util.HashMap;

public class TileRenderer {
    private HashMap<String , Texture> tileTexture;
    private Model model;

    private GestionnaireTile gestionnaireTile;

    public TileRenderer(){
         this.gestionnaireTile=new GestionnaireTile();


        tileTexture=new HashMap<>();

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


         for (int i=0;i<gestionnaireTile.getNbTiles();i++){
             if(gestionnaireTile.getTile(i) != null) {
                 if (!tileTexture.containsKey(gestionnaireTile.getTile(i).getTexture())) {
                     //Si il y a pas la texture on l'ajoute
                     String txt = gestionnaireTile.getTile(i).getTexture();
                     tileTexture.put(txt, new Texture(txt + ".png"));
                 }
             }
         }



    }


    public void renderTile(int id, int x, int y, Shader sh, Matrix4f world, Camera cam){
        sh.bind();
        if(tileTexture.containsKey(gestionnaireTile.getTile(id).getTexture())){
            tileTexture.get(gestionnaireTile.getTile(id).getTexture()).bind(0);
        }

        Matrix4f tilePosition=new Matrix4f().translate(new Vector3f(x*2,y*2,0) );
        Matrix4f target = new Matrix4f();

        cam.getProjection().mul(world,target);//Projection dans le monde
        target.mul(tilePosition);//on place le tile


        sh.setUniform("sampler",0);
        sh.setUniform("projection",target);

        model.render();
    }

    public GestionnaireTile getGestionnaireTile(){
        return gestionnaireTile;
    }
}

