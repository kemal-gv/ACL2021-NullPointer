import State.Scene;
import collision.AABB;
import models.*;
import org.joml.Vector3f;
import org.lwjgl.system.CallbackI;
import tiles.TileRenderer;
import render.Camera;
import framerate.Timer;
import models.*;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.GL;
import render.Camera;
import render.Shader;
import render.Texture;
import tiles.TileRenderer;
import windows.Window;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Game {
    public static enum GameState{
        MAINMENU,GAME, WIN, GAMEOVER, CUTSCENE
    }
    public static GameState gameState = GameState.MAINMENU;
    public static Scene sceneMainMenu,sceneWin,sceneGameOver,cutScene;
    private static long window;
    private static Window win;
    private static Camera camera;
    private static TileRenderer tileRenderer;
    private static Joueur joueur;
    private static ArrayList<Monstre> monstres;
    private static Random random;
    private static ArrayList<HealthBar> hbMonstres;
    private static Labyrinthe world;
    private static HealthBar hb;
    private static Shader shader;
    private static Audio audio;
    private static Text text;
    private static double frameCap,time,unprocessed,frameTime;
    private static Matrix4f scale,target;
    private static int frames,i;
    private static final int WIDTH=1600;
    private static final int HEIGHT =900;
    private static final int FPS=60;
    public static void main(String[] args) {
        Window.setCallBacks();
        init_vue();
        init_game();
        init_scene();
        game_loop();
        //Clean glfw
        glfwTerminate();
    }

    /*
    public static void testInput(){
        if(glfwGetKey(window,GLFW_KEY_E) == GL_TRUE) {
            color_red=0.43f;
            color_blue=1;
        }else{
            color_blue=0.43f;
            color_red=1;
        }

        //0 et 1 pr les deux boutons
        if(glfwGetMouseButton(window,0)==GL_TRUE){
            color_blue=0;
            color_red=0;
        }

        //Echap pr fermer la fenetre
        if(glfwGetKey(window,GLFW_KEY_ESCAPE)==GL_TRUE){
            glfwSetWindowShouldClose(window,true);
        }
    }

     */

    public static void testSquare(){
        //Draw a square (test)
        glBegin(GL_QUADS);

        glTexCoord2f(0,0);
        glVertex2f(-0.5f,0.5f);

        glTexCoord2f(1,0);
        glVertex2f(0.5f,0.5f);

        glTexCoord2f(1,1);
        glVertex2f(0.5f,-0.5f);

        glTexCoord2f(0,1);
        glVertex2f(-0.5f,-0.5f);
        glEnd();
        //Fin test square
    }

    /**
     *
     */
    public static void init_scene(){
        sceneMainMenu = new Scene("mn.png");
        sceneGameOver = new Scene("game_over.png");
        sceneWin = new Scene("succees.png");
    }

    /**
     *
     */
    public static void init_game(){
        //On doit creer les textures ici après le context

        joueur = new Joueur(100, 10, -3);
        //JoueurTest jTest=new JoueurTest(100, 10, -1, new Transform());
        monstres = new ArrayList<>();
        text = new Text();
        random = new Random();
        hbMonstres = new ArrayList<>();
        for (int i = 0; i<4; i++){
            int randX = random.nextInt(50 + 10) + 10; // max 50 ; min 10
            int randY = -random.nextInt(50 + 10) - 10; // max -10 ; min -50
            int vie = random.nextInt(100 + 1) + 1;
            System.out.println("vie monstre" +vie);
            Monstre monstre = new Monstre(vie, randX,randY);
            HealthBar hbMonstre = new HealthBar(vie);

            monstres.add(monstre);
            hbMonstres.add(hbMonstre);
        }

        world = new Labyrinthe("level1",joueur,win);
        //placement des monstres dans la salle piège
        int X = 34;
        int Y = -94;
        for (int i = 0; i < 10; i++) {
            int vie = 100;
            System.out.println("vie monstre" + vie);
            Monstre monstre = new Monstre(vie, X, Y);
            HealthBar hbMonstre = new HealthBar(vie);

            monstres.add(monstre);
            hbMonstres.add(hbMonstre);
            X = X + 6;
            if (i == 4) {
                X = 36;
                Y = -104;
            }
        }

        Labyrinthe world = new Labyrinthe("level2", joueur, win);

        world.setMonstre(monstres);
        //   world.setTile(tileRenderer.getGestionnaireTile().getTile(6),3,3);
        // world.setTile(tileRenderer.getGestionnaireTile().getTile(6),0,0);
        //world.setTile(tileRenderer.getGestionnaireTile().getTile(6),0,63);


        hb = new HealthBar((int)joueur.getVie());
        //world.setTile(tileRenderer.getGestionnaireTile().getTile(1),0,0);


        //Test setup world
        // //Coin haut gauche
        //world.setTile(tileRenderer.getGestionnaireTile().getTile(4),0,0);
/*
        for(int i=0;i<20;i++){
           // world.setTile(tileRenderer.getGestionnaireTile().getTile(8),i,0);
         //   world.setTile(tileRenderer.getGestionnaireTile().getTile(8),0,i);
           // world.setTile(tileRenderer.getGestionnaireTile().getTile(8),19,i);
           // world.setTile(tileRenderer.getGestionnaireTile().getTile(8),i,19);
        }
*/
      /*  //world.setTile(tileRenderer.getGestionnaireTile().getTile(2),0,19);

        // Texture tex=new Texture(("groundEarth_checkered.png"));
        // Texture tex=new Texture(("groundExit.png"));
        // Texture tex=new Texture(("test.png"));
*/
        //Creation d'un shader
        shader= new Shader("shader");

        // Matrix4f projection= new Matrix4f().ortho2D(-WIDTH/2,WIDTH/2, -HEIGHT /2,HEIGHT /2);
        scale = new Matrix4f()
                //.translate(new Vector3f(100,0,0))//Pour modifier la position de notre image
                .scale(16);
        target = new Matrix4f();

       /* // projection.mul(scale,target);//Projection*scale = target


        //Test camera position
        //camera.setPosition(new Vector3f(-100,0,0));//pr mettre l'image de nouvea au centre (quand on a fait le translate plus haut)
*/
        //Gérer les fps
        frameCap = 1.0/FPS;// X sec / Nb fps ici 60images par 1 seconde   cb on veut de fps
        time = Timer.getTime();
        unprocessed = 0 ;

        frameTime=0;
        frames=0;


        i = 1;
    }

    public static void init_vue(){
        AABB box1= new AABB(new Vector2f(0,0),new Vector2f(1,1));
        AABB box2= new AABB(new Vector2f(0,0),new Vector2f(1,1));

        if (box1.getCollisionInter(box2))
            System.out.println("INTERSECTION");
        else
            System.out.println("pas inters");


        //On initialise GLFW
        if(!glfwInit()){
            //throw new IllegalStateException("Erreur dans l'initialisation de  GLFW");
            System.err.println("Erreur dans l'initialisation de  GLFW");
            System.exit(1);
        }

        glfwWindowHint(GLFW_VISIBLE,GLFW_FALSE);
        win= new Window();
        win.setSize(WIDTH,HEIGHT);
        // win.setFullscreen(true);
        win.createWindow("Game");
        //Creation de la fenetre
        //  window=glfwCreateWindow(WIDTH, HEIGHT,"NullPointer'",0,0);
        // glfwShowWindow(window);

        //glfwMakeContextCurrent(window);
        //if(window==0) {
        //  throw new IllegalStateException("Erreur dans la création de la fenêtre");
        // }

        //
        //GLFWVidMode videoMode= glfwGetVideoMode(glfwGetPrimaryMonitor());
        //On place la fenêtre au milieu
        //glfwSetWindowPos(window,(videoMode.width() - WIDTH)/2, (videoMode.height() - HEIGHT)/2);

        //   glfwShowWindow(window);

        //Create a context
        //glfwMakeContextCurrent(window);//I need a context to display graphics
        GL.createCapabilities();
        //Camera
        camera= new Camera(win.getWidth(),win.getWidth());
        glEnable(GL_TEXTURE_2D);
        tileRenderer=new TileRenderer();

        audio = new Audio();
        try {
            audio.play("ruins");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void game_loop(){
        while(!win.shouldClose()){

            audio.soundSword(win);

            boolean canRender = false;
            double time2 = Timer.getTime();
            double passed = time2 - time;
            unprocessed+=passed;//Temps que le jeu n'a pas ete traité
            frameTime+=passed;
            time = time2;//To avoid the game goes exponentially faster
            while(unprocessed >= frameCap){
                if(win.getInput().isKeyPressed(GLFW_KEY_ESCAPE)){
                    //if(win.getInput().isMouseButtonDown(0)){//0=left click 1=right click 2=scroll button
                    glfwSetWindowShouldClose(win.getWindow(),true);
                }
                /*
                if(win.getInput().isKeyDown(GLFW_KEY_LEFT)){
                    camera.getPosition().sub(new Vector3f(-10,0,0));
                }
                if(win.getInput().isKeyDown(GLFW_KEY_RIGHT)){
                    camera.getPosition().sub(new Vector3f(10,0,0));
                }
                if(win.getInput().isKeyDown(GLFW_KEY_UP)){
                    camera.getPosition().sub(new Vector3f(0,10,0));
                }
                if(win.getInput().isKeyDown(GLFW_KEY_DOWN)){
                    camera.getPosition().sub(new Vector3f(0,-10,0));
                }

                 */

                //Tout ce qui n'a rien a voir avec le rendering est ici
                unprocessed-=frameCap;
                canRender=true;
                target=scale;
                //Test input
                //testInput();
                //Fin test input

                //Update tant que la fenetre ne veut pas se fermer
                //glfwPollEvents();

                //joueur.update((float) frameCap, win, camera, world);

                text.drawString(0,0,"HELLO TOI");
                //
                switch (gameState){
                    case MAINMENU:
                        if(sceneMainMenu.input(win))
                            gameState=GameState.GAME;
                        break;
                    case CUTSCENE:

                        break;
                    case GAME:
                        if(joueur.getPiecesCollectees()==world.getPieces())
                            gameState=GameState.WIN;
                        else
                        {
                            if(joueur.getVie()<=0)
                                gameState=GameState.GAMEOVER;
                            if (i%17==0) {
                            /*
                            for (Map.Entry entry : monstres.entrySet()){
                                Monstre m = (Monstre) entry.getValue();
                                m.deplacementAleatoire((float) frameCap, win, camera, world);
                            }

                            */
                                for (Monstre m : monstres){
                                    if(m.getVie()>0)
                                        m.deplacementAleatoire((float) frameCap, win, camera, world);
                                }
                            }
                            i++;

                            int indMonstre = 0;
                            for(Monstre m : monstres) {
                                if (m.getVie()>0)
                                    hbMonstres.get(indMonstre).update((int)m.getVie());
                                indMonstre++;
                            }
                            joueur.deplacement((float)frameCap,win,camera,world);

                            joueur.setVie(joueur.getVie());
                            if (joueur.getVie()>=0)
                                hb.update((int)joueur.getVie());
                        }
                        break;
                    case GAMEOVER:
                        if(sceneGameOver.input(win))
                            gameState=GameState.GAME;
                            init_game();
                        break;
                    case WIN:
                        if(sceneWin.input(win))
                            gameState=GameState.GAME;
                            init_game();
                        break;

                }
                //jTest.deplacement((float)frameCap,win,camera,world);
                //vie du joueur infèrieur à 0
                world.correctCamera(camera,win);
                win.update();

                if(frameTime >= 1.0){//Every secon we print how much frame we have
                    frameTime=0;
                    //System.out.println("FPS : "+frames);
                    frames=0;

                }

            }

            if(canRender){
                if(monstres.size()==0){
                    monstres = new ArrayList<>();
                    random = new Random();
                    hbMonstres = new ArrayList<>();
                    for (i = 0; i < 20; i++) {
                        int randX = random.nextInt(50 + 10) + 10; // max 50 ; min 10
                        int randY = -random.nextInt(50 + 10) - 10; // max -10 ; min -50
                        int vie = random.nextInt(100 + 1) + 1;
                        //System.out.println("vie monstre" +vie);
                        Monstre monstre = new Monstre(vie, randX,randY);
                        HealthBar hbMonstre = new HealthBar(vie);
                        monstres.add(monstre);
                        hbMonstres.add(hbMonstre);
                    }

                    world = new Labyrinthe("level0",joueur,win);
                    //SEt point de spawn joueur
                    joueur.setPos(3,-3);
                    world.setMonstre(monstres);
                }
                glClear(GL_COLOR_BUFFER_BIT);// ? Set every pixel to black ? pas sur
                //tex.bind(0);



                //shader.bind();
                // shader.setUniform("sampler",0);
                //shader.setUniform("projection",camera.getProjection().mul(target));
                //model.render();

                // testSquare();

                //Rendering tile

                //mn.update();
                //mn.render(shader,camera);
                switch (gameState){
                    case MAINMENU:
                        sceneMainMenu.render(shader,camera);
                        break;
                    case GAME:
                        world.render(tileRenderer,shader,camera);
                        int indMonstres = 0;
                        for(Monstre m : monstres){
                            if(m.getVie()>0) {
                                m.render(shader, camera);
                                hbMonstres.get(indMonstres).render(shader);
                                indMonstres++;
                            }
                        }
                        joueur.render(shader, camera);
                        hb.render(shader);
                        break;
                    case WIN:
                        camera= new Camera(win.getWidth(),win.getWidth());
                        world.correctCamera(camera,win);
                        sceneWin.render(shader,camera);
                        break;
                    case GAMEOVER:
                        camera= new Camera(win.getWidth(),win.getWidth());
                        world.correctCamera(camera,win);
                        sceneGameOver.render(shader,camera);
                        break;

                }
                // jTest.render(shader,camera,world);
                //jTest.setPos(10,-3);
                win.swapBuffers();
                frames++;

            }

        }
    }

    /*
    public static void testInput(){
        if(glfwGetKey(window,GLFW_KEY_E) == GL_TRUE) {
            color_red=0.43f;
            color_blue=1;
        }else{
            color_blue=0.43f;
            color_red=1;
        }

        //0 et 1 pr les deux boutons
        if(glfwGetMouseButton(window,0)==GL_TRUE){
            color_blue=0;
            color_red=0;
        }

        //Echap pr fermer la fenetre
        if(glfwGetKey(window,GLFW_KEY_ESCAPE)==GL_TRUE){
            glfwSetWindowShouldClose(window,true);
        }
    }

     */

    public static void testSquare() {
        //Draw a square (test)
        glBegin(GL_QUADS);

        glTexCoord2f(0, 0);
        glVertex2f(-0.5f, 0.5f);

        glTexCoord2f(1, 0);
        glVertex2f(0.5f, 0.5f);

        glTexCoord2f(1, 1);
        glVertex2f(0.5f, -0.5f);

        glTexCoord2f(0, 1);
        glVertex2f(-0.5f, -0.5f);
        glEnd();
        //Fin test square
    }

}
