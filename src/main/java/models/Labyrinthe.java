package models;

import collision.AABB;
import framerate.Timer;
import render.Animation;
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
import java.util.List;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_ALPHA_TEST;
import static org.lwjgl.opengl.GL11.glAlphaFunc;
import static org.lwjgl.opengl.GL11C.GL_GREATER;
import static org.lwjgl.opengl.GL11C.glEnable;


public class Labyrinthe {


    private List<Monstre> listMonstres;

    private Tile[] tiles;
    private int width;
    private int height;
    private Matrix4f world;
    private AABB[] boundingBoxes;
    private byte[] tilesByte;
    private int scale;

    private List<Entity> listEntity;

    private ArrayList<Chest> chests=new ArrayList<>();
    private ArrayList<HoleTp> holes=new ArrayList<>();

    private static Joueur joueur;
    private Window window;


    public Labyrinthe(String worldMap,Joueur j,Window w){
        listEntity=new ArrayList<>();
        glEnable(GL_ALPHA_TEST);
        glAlphaFunc(GL_GREATER, 0.0f);


        width=64;
        this.window=w;
        this.joueur=j;
        height=64;
        scale=32;
        tiles=new Tile[width*height];
        for(int i=0;i<tiles.length;i++){
            tiles[i]=GestionnaireTile.tiles[0];
        }


        //TODO a enlever
       // listEntity.add(joueur.getW());


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
                           // Entity e =new Entity(x,y,new Animation("assets/chests/full/0"),t.getId(),tilesTmp);
                            setTile(t,x,y);
                           // listEntity.add(e);


                           // AnimatedChest newAc=(AnimatedChest)t.getTile();
                            //newAc.setC(c);
                            //t=newAc;
                            //System.out.println("X = "+x+" Y = "+y);
                        }
                        else if(t.getId()==12){
                            AnimatedTile piece=(AnimatedTile) t;
                            listEntity.add(new Entity(x,y,t));
                        }
                        else if(t.getId()==21){
                            //Ici on a un trou donc on doit tp le joueur qd il est dessus
                            holes.add(new HoleTp(blue,green,x,-y));
                            setTile(t,x,y);

                        }else{
                            setTile(t,x,y);
                        }


                    }

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        joueur.setW(new Weapon(23,"assets/sword_anim/0","assets/sword_anim/",6,15,true,joueur.getPosX(),joueur.getPosY()));




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

    public Matrix4f getWorldMatrix(){
        return world;
    }

    public void render(TileRenderer tileRenderer, Shader shader, Camera cam){



        //Verifier collision
        double x=Math.ceil(((((joueur.getPosX()/2)+0.5f)-(5/2)))+1.25);
        double y =Math.ceil(((((-joueur.getPosY()/2)+0.5f)-(5/2)))+1.25);

        //System.out.println("Le x : "+x+" le y : "+y);
        Tile t=getElementPlateau((int)x,(int)y);
       // System.out.println("Pos x = "+(Math.ceil(joueur.getPosX() -1)+" y="+Math.ceil(Math.abs(joueur.getPosY()+1))));
        //System.out.println("Pos xXX = "+((int)(joueur.getPosX() -1)+" y="+((joueur.getPosY()+1))));
        if(t!=null) {
            if (t.getId() == 11) {
                //Ici il y a des spikes on vérifie si ils sont "ouverts"
                AnimatedTile spike = (AnimatedTile) t;
                if (spike.isOpen()) {
                    joueur.setVie(joueur.getVie() - 1);
                }
            }


            x = Math.ceil(((((joueur.getPosX() / 2) + 0.5f) - (5 / 2))) + 1);
            y = Math.ceil(((((-joueur.getPosY() / 2) + 0.5f) - (5 / 2))) + 1.15);
            t = getElementPlateau((int) x, (int) y);
            if (t.getId() == 19) {
                //Verif potion


                //potion ici
                System.out.println("Potion rammasée, un peu de vie pour toi <3");
                //setTile(GestionnaireTile.tiles[0], (int) (x), (int) (y));
                if (joueur.getVie() + 30 >= 100) {
                    joueur.setVie(100);
                } else {
                    joueur.setVie(joueur.getVie() + 30);
                }
                removeEntity((int)x,(int)y);

            }
            if(t.getId()==23 || t.getId()==24){

                //ICi il a une arme ! on change l'arme du joueur
                if(t.getId()==23) {

                    joueur.setW(new Weapon(23, "assets/sword_anim/0", "assets/sword_anim/", 6, 15, true, joueur.getPosX(), joueur.getPosY()));
                    joueur.setAttaque(4);
                } else{
                    joueur.setW(new Weapon(24,"assets/sword_anim_1/0","assets/sword_anim_1/",8,15,true,joueur.getPosX(),joueur.getPosY()));
                    joueur.setAttaque(6);
                }

                removeEntity((int)x,(int)y);


            }

            if (getElementPlateau((int)x,(int)y).getId()==12) {
                //Verif pièces


                //pièce ici
                System.out.println("Pièce rammasée, un peu de point pour toi <3");
                //setTile(GestionnaireTile.tiles[0], (int) (x), (int) (y));
                removeEntity((int)x,(int)y);

            }

            joueur.setPosArme();

            //Verification si il y a une porte aux alentours
            int posx = (int) x;
            int posy = (int) y;

            //Verification pour les portes
            takeLoot(posx, posy);

            verifChest();


            openDoor(posx, posy);

            openChest(posx, posy);

            verifCollisionMonstre(posx, posy);
            verifHoles(posx, posy);
            //Verification pour les coffres


            for(Monstre m : listMonstres){
                m.pathFinding(posx,posy,this);

            }



            //Render weapon
            //Condition pr savoir quelle arme le jouer possède



            ////
            //       if(t.getId()!=0)
//            System.out.println("T = "+t.getId());

        }

        for(int i = 0; i<height;i++){
            for(int j=0 ; j<width;j++){
                // tileRenderer.renderTile(tiles[j + i * width] , j,-i,shader,world,cam);

                tileRenderer.renderTile(tiles[j + i * width].getId() , j,-i,shader,world,cam);

            }
        }

        joueur.setPosArme();
      //  joueur.getW().setPos(((((joueur.getPosX()/2)+0.5f)-(5/2)))+2,-((((joueur.getPosY()/2)+0.5f)-(5/2)))-2);

        //joueur.getW().render(shader,cam,world);
        for(Entity e:listEntity){
            if(e.getTile().getId()==13)
                //Porte
                tileRenderer.renderTile(13,(int)e.getPosX(),(int)-e.getPosY(),shader,world,cam);
            else
                e.render(e.getId(),e.getPosX(),-e.getPosY(),shader,world,cam, tileRenderer.getGestionnaireTile(), tileRenderer);
        }
        if(joueur.getW()!=null){
            Weapon w=joueur.getW();

            if(window.getInput().isKeyDown(GLFW_KEY_S)){
                joueur.animationAttaque();
                for(Monstre m : listMonstres){
                    int posx= (int) Math.ceil(((((m.getPosX()/2)+0.5f)-(5/2)))+1);
                    int posy = (int) Math.ceil(((((-m.getPosY()/2)+0.5f)-(5/2)))+1.15);

                    if((posx==x && posy==y) || (posx==x+1 && posy==y) || (posx==x+1 && posy==y-1) ){
                        m.setVie(m.getVie()-w.getAttaqueDegat());

                    }
                }
            }



            w.render(w.getPosX(),-w.getPosY(),shader,world,cam);
        }


        Monstre tmp=null;
        for(Monstre m:listMonstres){
            if(m.getVie()<=0)
                tmp=m;

        }
        listMonstres.remove(tmp);

    }

    private void verifCollisionMonstre(int x,int y) {
        for(Monstre m:listMonstres){
            int posx= (int) Math.ceil(((((m.getPosX()/2)+0.5f)-(5/2)))+1);
            int posy = (int) Math.ceil(((((-m.getPosY()/2)+0.5f)-(5/2)))+1.15);

            if(posx==x && posy==y){
                joueur.setVie(joueur.getVie()-1.5);


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


    public void verifHoles(int x,int y){

        for(HoleTp hole:holes){
            if(hole.getPosX()==x && hole.getPosY()==-y) {
                //On tp si il est usr un trou
                joueur.setPos(hole.getPosX(), -hole.getTpY());
            }
        }
    }

    public void removeEntity(int x,int y){
        Entity e=null;
        for(Entity entity:listEntity){
            if(entity.getPosX() == x && entity.getPosY()==y){
                e=entity;
            }
        }
        if(e!=null){
            listEntity.remove(e);
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
                      //  System.out.println("Le coffre : " + c.getItem() + " est ouvert. *loot*");
                       // System.out.println("Le coffre vide ici : "+pos[i][0] +" et "+pos[i][1]);
                        setTile(GestionnaireTile.tiles[17], pos[i][0], pos[i][1]);//coffre ouvert

                        //Verif si l'endroit est vide :
                        //Posé le loot a recup dans la classe CHEST
                        Random random = new Random();
                        int nb;
                        nb = random.nextInt(3);
                        if(nb==2){
                            //Il a une arme !!!
                            //Où ?
                            nb=random.nextInt(3);
                            //Laquelle ?
                            int nbArme=random.nextInt(2);

                            switch(nb){
                                case 0:
                                    listEntity.add(new Entity(pos[i][0],pos[i][1]-1,GestionnaireTile.tiles[23+nbArme]));
                                    listEntity.add(new Entity(pos[i][0]+1,pos[i][1],GestionnaireTile.tiles[19]));
                                    listEntity.add(new Entity(pos[i][0]-1,pos[i][1],GestionnaireTile.tiles[19]));
                                    break;
                                case 1:
                                    listEntity.add(new Entity(pos[i][0],pos[i][1]-1,GestionnaireTile.tiles[19]));
                                    listEntity.add(new Entity(pos[i][0]+1,pos[i][1],GestionnaireTile.tiles[23+nbArme]));
                                    listEntity.add(new Entity(pos[i][0]-1,pos[i][1],GestionnaireTile.tiles[19]));
                                    break;
                                case 2:
                                    listEntity.add(new Entity(pos[i][0],pos[i][1]-1,GestionnaireTile.tiles[19]));
                                    listEntity.add(new Entity(pos[i][0]+1,pos[i][1],GestionnaireTile.tiles[19]));
                                    listEntity.add(new Entity(pos[i][0]-1,pos[i][1],GestionnaireTile.tiles[23+nbArme]));
                                    break;
                            }
                        }
                        else{
                            listEntity.add(new Entity(pos[i][0],pos[i][1]-1,GestionnaireTile.tiles[19]));
                            listEntity.add(new Entity(pos[i][0]+1,pos[i][1],GestionnaireTile.tiles[19]));
                            listEntity.add(new Entity(pos[i][0]-1,pos[i][1],GestionnaireTile.tiles[19]));
                        }


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
                    //AnimatedChest ac= (AnimatedChest) porteProximite[i];
                    //ac.setOpen();
                    //AnimatedChest ac=(AnimatedChest) GestionnaireTile.tiles[16];

                    Chest c = getChest(pos[i][0],pos[i][1]);


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
                    listEntity.add(new Entity(pos[i][0],pos[i][1],GestionnaireTile.tiles[14]));
                    setTile(GestionnaireTile.tiles[0],pos[i][0],pos[i][1] );
                    //(GestionnaireTile.tiles[14],pos[i][0],pos[i][1]);

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

    public void setEntity(Entity entity, int x,int y){
        //tiles[x + y *width]=tile;
        if(entity.getTile().isSolid()){
            boundingBoxes[x+y*width]=new AABB(new Vector2f(x*2,-y*2),new Vector2f(1,1));
        }else{
            boundingBoxes[x+y*width]=null;
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
            for(Entity e: listEntity){
                if(e.getPosX()==x && e.getPosY()==y)
                    return e.getTile();
            }

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

    public void ajoutMonstre(Monstre m){
        listMonstres.add(m);
    }

    public void setMonstre(List<Monstre> lm){
        listMonstres=lm;
    }
}
