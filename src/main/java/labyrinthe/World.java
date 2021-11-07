package labyrinthe;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import render.Camera;
import render.Shader;
import windows.Window;

public class World {
    private int[] tiles;
    private int width;
    private int height;
    private Matrix4f world;

    private int scale;

    public World(){
        width=64;
        height=64;
        scale=32;
        tiles=new int[width*height];

        world=new Matrix4f().setTranslation(new Vector3f(0));
        world.scale(scale);
    }

    public void render(TileRenderer tileRenderer, Shader shader, Camera cam){
        for(int i = 0; i<height;i++){
            for(int j=0 ; j<width;j++){
                tileRenderer.renderTile(tiles[j + i * width] , j,-i,shader,world,cam);
            }
        }
    }

    public void correctCamera(Camera camera, Window win){
        Vector3f pos = camera.getPosition();
        int w = -width * scale * 2;
        int h = height * scale * 2;

        if(pos.x > -(win.getWidth()/2)+scale){
            pos.x =  -(win.getWidth()/2)+scale;
        }
        if(pos.x < w+ (win.getWidth()/2)+scale){
            pos.x=w+ (win.getWidth()/2)+scale;
        }


        if(pos.y <  (win.getHeight()/2)+314 ){
            pos.y= (win.getHeight())-(scale*3)-40 ;
        }
        if(pos.y > h- (win.getHeight()/2)-324-(scale*2)){
            pos.y=h- (win.getHeight())+(scale*2) ;
        }


    }

    public void setTile(Tile tile, int x,int y){
        tiles[x + y * width]=tile.getId();
    }

}
