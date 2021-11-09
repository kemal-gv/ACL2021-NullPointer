package labyrinthe;

import collision.AABB;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import render.Camera;
import render.Shader;
import render.Texture;
import windows.Window;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;

public class World {
    private int[] tiles;
    private int width;
    private int height;
    private Matrix4f world;
   private AABB[] boundingBoxes;
    private byte[] tilesByte;
    private int scale;

    public World(String worldMap){
        width=64;
        height=64;
        scale=32;
        tiles=new int[width*height];

        world=new Matrix4f().setTranslation(new Vector3f(0));
        world.scale(scale);

        boundingBoxes=new AABB[width*height];


        try {

            String url= World.class.getClassLoader().getResource("assets/levels/"+worldMap+"_tiles.png").getFile();

            BufferedImage tileSheet= ImageIO.read(new File(url));
           // BufferedImage entitySheet= ImageIO.read(new File("./levels/"+world+"_entity.png"));


            int width=tileSheet.getWidth();
            int height=tileSheet.getHeight();

            int[] colorTileSheet = tileSheet.getRGB(0,0,width,height,null,0,width);

            tilesByte = new byte[width*height];
            //boundingBoxes=new AABB[width*height]

            for(int y=0;y<height;y++){

                for(int x=0;x<width;x++){
                    int red=(colorTileSheet[x+y*width] >> 16) & 0xFF;
                    int green=(colorTileSheet[x+y*width] >> 8) & 0xFF;
                    int blue=(colorTileSheet[x + y * width]) & 0xFF;

                    Tile t;
                    try {
                        t=GestionnaireTile.tiles[red];

                        if(red!=0){
                            //System.out.println("RED = "+red);
                        }

                    } catch(ArrayIndexOutOfBoundsException e){
                        t=null;
                    }

                    if(t!=null){
                        setTile(t,x,y);
                    }

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public World(){
        width=64;
        height=64;
        scale=32;
        tiles=new int[width*height];

        world=new Matrix4f().setTranslation(new Vector3f(0));
        world.scale(scale);

        //Initialiser le niveau avec des murs tout autour




        //tiles[x + y * width]=tile.getId();



    }

    public void render(TileRenderer tileRenderer, Shader shader, Camera cam){
        for(int i = 0; i<height;i++){
            for(int j=0 ; j<width;j++){
               // tileRenderer.renderTile(tiles[j + i * width] , j,-i,shader,world,cam);
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
        tiles[x + y *width]=tile.getId();
        if(tile.isSolid()){
            boundingBoxes[x+y*width]=new AABB(new Vector2f(x*2,-y*2),new Vector2f(1,1));
        }else{
            boundingBoxes[x+y*width]=null;
        }
    }

    public int getScale(){
        return scale;
    }

    public Tile getTile(int x,int y){
        try {
            return GestionnaireTile.tiles[tiles[x + y * width]];
        }catch (ArrayIndexOutOfBoundsException e){
            return null;
        }
    }

    public AABB getTileBoundingBox(int x,int y){
        try {
            return boundingBoxes[x + y * width];
        }catch (ArrayIndexOutOfBoundsException e){
            return null;
        }
    }
}
