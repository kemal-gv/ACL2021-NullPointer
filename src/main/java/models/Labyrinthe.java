package models;

import collision.AABB;
import framerate.Timer;
import tiles.*;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import render.Camera;
import render.Shader;
import windows.Window;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F;


public class Labyrinthe {



    private Tile[] tiles;
    private int width;
    private int height;
    private Matrix4f world;
    private AABB[] boundingBoxes;
    private byte[] tilesByte;
    private int scale;

    private ArrayList<Chest> chests=new ArrayList<>();

    private static Joueur joueur;
    private Window window;
    public Labyrinthe(String worldMap,Joueur j,Window w){
        width=64;
        this.window=w;
        this.joueur=j;
        height=64;
        scale=32;
        tiles=new Tile[width*height];
        for(int i=0;i<tiles.length;i++){
            tiles[i]=GestionnaireTile.tiles[0];
        }

        world=new Matrix4f().setTranslation(new Vector3f(0));
        world.scale(scale);

        boundingBoxes=new AABB[width*height];


        try {

            String url= Labyrinthe.class.getClassLoader().getResource("assets/levels/"+worldMap+"_tiles.png").getFile();

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
                        t= GestionnaireTile.tiles[red];

                        if(red!=0){
                            //System.out.println("RED = "+red);
                        }

                    } catch(ArrayIndexOutOfBoundsException e){
                        t=null;
                    }

                    if(t!=null){

                        if(t.getId()==15){
                            AnimatedChest tilesTmp= (AnimatedChest) new AnimatedChest(t.getId(),"assets/chests/full/0","assets/chests/full",3,1,false).setSolid();

                            Chest c=new Chest(x,y,"Item n°"+x,tilesTmp);
                            chests.add(c);
                           // AnimatedChest newAc=(AnimatedChest)t.getTile();
                            //newAc.setC(c);
                            //t=newAc;
                            //System.out.println("X = "+x+" Y = "+y);
                        }


                        setTile(t,x,y);
                    }

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }



    }


    public Labyrinthe(){
        width=64;
        height=64;
        scale=32;
        tiles=new Tile[width*height];

        world=new Matrix4f().setTranslation(new Vector3f(0));
        world.scale(scale);

        //Initialiser le niveau avec des murs tout autour




        //tiles[x + y * width]=tile.getId();



    }

    public void render(TileRenderer tileRenderer, Shader shader, Camera cam){


        //Verifier collision
        double x=Math.ceil(((((joueur.getPosX()/2)+0.5f)-(5/2)))+1.25);
        double y =Math.ceil(((((-joueur.getPosY()/2)+0.5f)-(5/2)))+1.25);

        //System.out.println("Le x : "+x+" le y : "+y);
        Tile t=getElementPlateau((int)x,(int)y);
       // System.out.println("Pos x = "+(Math.ceil(joueur.getPosX() -1)+" y="+Math.ceil(Math.abs(joueur.getPosY()+1))));
        //System.out.println("Pos xXX = "+((int)(joueur.getPosX() -1)+" y="+((joueur.getPosY()+1))));
        if(t.getId()==11){
            //Ici il y a des spikes on vérifie si ils sont "ouverts"
            AnimatedTile spike= (AnimatedTile) t;
            if(spike.isOpen()){
                joueur.setVie(joueur.getVie()-2);
            }
        }


        x=Math.ceil(((((joueur.getPosX()/2)+0.5f)-(5/2)))+1);
        y =Math.ceil(((((-joueur.getPosY()/2)+0.5f)-(5/2)))+1.15);
        t=getElementPlateau((int)x,(int)y);
         if(t.getId()==12){
            //Verif pièce


            //Piece ici
            System.out.println("Pièce rammasé, un peu de vie pour toi <3");
            setTile(GestionnaireTile.tiles[0],(int)(x),(int)(y) );
            joueur.setVie(joueur.getVie()+30);
        }




         //Verification si il y a une porte aux alentours
        int posx=(int)x;
         int posy=(int)y;

         //Verification pour les portes
        takeLoot(posx,posy);

        verifChest();



        openDoor(posx,posy);

        openChest(posx,posy);

        //Verification pour les coffres




        ////
        if(t.getId()!=0)
            System.out.println("T = "+t.getId());


        for(int i = 0; i<height;i++){
            for(int j=0 ; j<width;j++){
                // tileRenderer.renderTile(tiles[j + i * width] , j,-i,shader,world,cam);

                tileRenderer.renderTile(tiles[j + i * width].getId() , j,-i,shader,world,cam);

            }
        }

    }

    public void verifChest(){
        for(Chest c : chests){
            if(  c.isOpen() && !c.isEmpty()) {
                //System.out.println("C = "+c.getItem()+" est ouvert ?");
                //AnimatedChest ac = (AnimatedChest) getElementPlateau(c.getPosx(), c.getPosy());
                //if (ac.isOpened()) {
                  //  System.out.println("Le chest c:" + c.getItem() + "  est définitevement ouvert");
                    setTile(GestionnaireTile.tiles[18], c.getPosx(), c.getPosy());
                    animationChest=false;
                //}
            }
        }
    }



    private void takeLoot(int posx, int posy) {

        Tile[] porteProximite=new Tile[]{getElementPlateau(posx+1,posy) , getElementPlateau(posx-1,posy)
                , getElementPlateau(posx,posy-1) , getElementPlateau(posx,posy+1)};
        int[][] pos=new int[][]{{posx+1,posy},
                {posx-1,posy} ,
                {posx,posy-1},
                {posx,posy+1}
        };
        for(int i=0;i<porteProximite.length;i++){
            if(porteProximite[i].getId()==18) {
                if(window.getInput().isKeyDown(GLFW_KEY_F)) {
                    //System.out.println("LOOOT");
                    Chest c =getChest(pos[i][0],pos[i][1]);
                    if(!c.isEmpty()) {
                        c.setEmpty(true);
                        System.out.println("Le coffre : " + c.getItem() + " est ouvert. *loot*");
                        System.out.println("Le coffre vide ici : "+pos[i][0] +" et "+pos[i][1]);
                        setTile(GestionnaireTile.tiles[17], pos[i][0], pos[i][1]);

                        //Verif si l'endroit est vide :
                        //Posé le loot a recup dans la classe CHEST
                        setTile(GestionnaireTile.tiles[19],pos[i][0],pos[i][1]-1 );
                        setTile(GestionnaireTile.tiles[19],pos[i][0]+1,pos[i][1] );
                        setTile(GestionnaireTile.tiles[19],pos[i][0]-1,pos[i][1] );

                    }
                    //AnimatedChest ac= (AnimatedChest) porteProximite[i];
                    //ac.setOpen();

                   // Tile t= GestionnaireTile.tiles[16];
                   // if(ac.isOpened())
                   //     setTile(t, pos[i][0],pos[i][1]);
                }
            }
        }


    }

    private boolean animationChest=false;//Si false il y a aucune anim de chest mais si true on peut pas en faire dautres

    public void openChest(int posx,int posy){

        Tile[] porteProximite=new Tile[]{getElementPlateau(posx+1,posy) , getElementPlateau(posx-1,posy)
                , getElementPlateau(posx,posy-1) , getElementPlateau(posx,posy+1)};
        int[][] pos=new int[][]{{posx+1,posy},
                {posx-1,posy} ,
                {posx,posy-1},
                {posx,posy+1}
        };
        for(int i=0;i<porteProximite.length;i++){
            if(porteProximite[i].getId()==15) {
                if(window.getInput().isKeyDown(GLFW_KEY_F) && !animationChest) {
                    animationChest=true;
                    System.out.println("OEPNNE");
                    //AnimatedChest ac= (AnimatedChest) porteProximite[i];
                    //ac.setOpen();
                    //AnimatedChest ac=(AnimatedChest) GestionnaireTile.tiles[16];

                    Chest c = getChest(pos[i][0],pos[i][1]);


                    System.out.println("Item number : "+c.getItem());
                    AnimatedChest ac=c.getAc();
                    //ac.setOpen();
                    AnimatedChest ac2= (AnimatedChest) GestionnaireTile.tiles[16];
                    ac2.setOpen();


                   // System.out.println("Pos x du coffre : "+pos[i][0]+" , "+pos[i][1]+ " et le coffre est ici : "+c.getPosx()+" , "+c.getPosy());
                    ac2.setC(c);
                    c.setAnimation(true);
                    setTile(ac2, pos[i][0],pos[i][1]);
                    //c.setOpen(true);

                    /*
                    if(ac.isOpened()){
                        System.out.println("OVUERT");
                        //setTile(GestionnaireTile.tiles[18], pos[i][0],pos[i][1]);
                    }
                    else{
                        //setTile(ac, pos[i][0],pos[i][1]);

                    }

                     */


                }
            }
        }



    }

    public Chest getChest(int x,int y){
        for(Chest c: chests){
            if(c.getPosx()==x && c.getPosy()==y) return c;
        }
        return null;
    }


    public void openDoor(int posx,int posy){

        Tile[] porteProximite=new Tile[]{getElementPlateau(posx+1,posy) , getElementPlateau(posx-1,posy)
                , getElementPlateau(posx,posy-1) , getElementPlateau(posx,posy+1)};
        int[][] pos=new int[][]{{posx+1,posy},
                {posx-1,posy} ,
                {posx,posy-1},
                {posx,posy+1}
                            };
        for(int i=0;i<porteProximite.length;i++){
            if(porteProximite[i].getId()==13) {
                if(window.getInput().isKeyDown(GLFW_KEY_F)) {
                    setTile(GestionnaireTile.tiles[14],pos[i][0],pos[i][1]);
                }
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


        if(pos.y <  (win.getHeight()/2)+scale*5+152 ){
            pos.y= (win.getHeight()/2)+scale*5+152;
        }

        if(pos.y > h- (win.getHeight()/2)-324-(scale*2)){
            pos.y=h- (win.getHeight()/2)-324-(scale*2);
        }




    }


    public void setTile(Tile tile, int x,int y){
        tiles[x + y *width]=tile;
        if(tile.isSolid()){
            boundingBoxes[x+y*width]=new AABB(new Vector2f(x*2,-y*2),new Vector2f(1,1));
        }else{
            boundingBoxes[x+y*width]=null;
        }
    }

    public int getScale(){
        return scale;
    }

    public Tile getElementPlateau(int x,int y){
        try {
            return tiles[x+y*width];
            //return GestionnaireTile.tiles[tiles[x + y * width]];
        }catch (ArrayIndexOutOfBoundsException e){
            return null;
        }
    }

    public AABB verifierCollision(int x, int y){
        try {

            return boundingBoxes[x + y * width];
        }catch (ArrayIndexOutOfBoundsException e){
            return null;
        }
    }
}
