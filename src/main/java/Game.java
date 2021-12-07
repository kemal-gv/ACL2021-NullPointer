import State.CutScene;
import State.Digit;
import State.Scene;
import collision.AABB;
import models.*;
import text.Text;
import text.TrueTypeFont;
import tiles.TileRenderer;
import render.Camera;
import framerate.Timer;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.GL;
import render.Shader;
import windows.Window;
import java.util.ArrayList;
import java.util.Random;

public class Game {
    private static Digit digit1,digit2,digit3,digit4,slash;
    private static int piecesC;

    public static enum GameState{
        MAINMENU,GAME, WIN, GAMEOVER, CUTSCENE
    }
    public static GameState gameState = GameState.MAINMENU;
    public static Scene sceneMainMenu,sceneWin,sceneGameOver;
    public static CutScene cutScene;
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
    public static void main(String[] args) throws Exception {
        Window.setCallBacks();

        init_vue();
        init_game();
        init_scene();
        game_loop();

        //Clean glfw
        glfwTerminate();
    }

    public static void init_scene(){
        sceneMainMenu = new Scene("scene/mn.png");
        sceneGameOver = new Scene("scene/game_over.png");
        sceneWin = new Scene("scene/succees.png");
        cutScene = new CutScene("scene/cut_scene.png");
    }

    public static void init_game(){
        //On doit creer les textures ici après le context

        joueur = new Joueur(100, 3, -3);
        monstres = new ArrayList<>();
        text = new Text();
        random = new Random();
        hbMonstres = new ArrayList<>();
        for (int i = 0; i<4; i++){
            int randX = random.nextInt(50 + 10) + 10; // max 50 ; min 10
            int randY = -random.nextInt(50 + 10) - 10; // max -10 ; min -50
            int vie = random.nextInt(100 + 1) + 1;
            //System.out.println("vie monstre" +vie);
            Monstre monstre = new Monstre(vie, randX,randY);
            HealthBar hbMonstre = new HealthBar(vie);

            monstres.add(monstre);
            hbMonstres.add(hbMonstre);
        }

        //placement des monstres dans la salle piège
        int X = 34;
        int Y = -94;
        for (int i = 0; i < 10; i++) {
            int vie = 100;
            //System.out.println("vie monstre" + vie);
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

        world = new Labyrinthe("level2", joueur, win);

        world.setMonstre(monstres);

        hb = new HealthBar((int)joueur.getVie());

        //Creation d'un shader
        shader= new Shader("shader");

        scale = new Matrix4f()
                .scale(16);
        target = new Matrix4f();

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
        //Creation de la fenetre
        win.createWindow("SkullQuest");

        //Create a context
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

    public static void game_loop() throws Exception {
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
                    glfwSetWindowShouldClose(win.getWindow(),true);
                }

                //Tout ce qui n'a rien a voir avec le rendering est ici
                unprocessed-=frameCap;
                canRender=true;
                target=scale;

                text.drawString(0,0,"HELLO TOI");

                switch (gameState){
                    case MAINMENU:

                        if(sceneMainMenu.input(win))
                            gameState=GameState.CUTSCENE;
                        break;
                    case CUTSCENE:
                        if(cutScene.input(win))
                            gameState=GameState.GAME;
                        break;
                    case GAME:
                        if(joueur.getPiecesCollectees()==world.getPieces()) {
                            piecesC = joueur.getPiecesCollectees();
                            gameState = GameState.WIN;
                        }
                        else
                        {
                            if(joueur.getVie()<=0) {
                                piecesC = joueur.getPiecesCollectees();
                                gameState = GameState.GAMEOVER;
                            }
                            if (i%17==0) {
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

                world.correctCamera(camera,win);
                win.update();

                if(frameTime >= 1.0){//Every secon we print how much frame we have
                    frameTime=0;
                    frames=0;

                }

            }

            if(canRender){
                /*if(monstres.size()==0){
                    monstres = new ArrayList<>();
                    random = new Random();
                    hbMonstres = new ArrayList<>();
                    for (i = 0; i < 20; i++) {
                        int randX = random.nextInt(50 + 10) + 10; // max 50 ; min 10
                        int randY = -random.nextInt(50 + 10) - 10; // max -10 ; min -50
                        int vie = random.nextInt(100 + 1) + 1;
                        Monstre monstre = new Monstre(vie, randX,randY);
                        HealthBar hbMonstre = new HealthBar(vie);
                        monstres.add(monstre);
                        hbMonstres.add(hbMonstre);
                    }

                    world = new Labyrinthe("level2",joueur,win);
                    //SEt point de spawn joueur
                    joueur.setPos(3,-3);
                    world.setMonstre(monstres);
                }*/
                glClear(GL_COLOR_BUFFER_BIT);// ? Set every pixel to black ? pas sur

                switch (gameState){
                    case MAINMENU:
                        sceneMainMenu.render(shader,camera);
                        break;
                    case CUTSCENE:
                        cutScene.render(shader,camera);
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
                        test_text();
                        break;
                    case WIN:
                        score();
                        camera= new Camera(win.getWidth(),win.getWidth());
                        world.correctCamera(camera,win);
                        sceneWin.render(shader,camera);
                        score_render(shader,camera);
                        break;
                    case GAMEOVER:
                        score();
                        camera= new Camera(win.getWidth(),win.getWidth());
                        world.correctCamera(camera,win);
                        sceneGameOver.render(shader,camera);
                        score_render(shader,camera);
                        break;
                }
                win.swapBuffers();
                frames++;
            }
        }
    }

    public static void test_text() throws Exception {
        TrueTypeFont font = new TrueTypeFont("C:\\Users\\admin\\IdeaProjects\\ACL2021-NullPointer\\src\\main\\resources\\font\\Minecraft.ttf");
        font.drawBitmap(0, 0);
        font.drawText("HALLO World ! ;-)", 50, 100);
    }

    public static void score(){
        digit1 = new Digit(piecesC/10,0);
        digit2 = new Digit(piecesC%10,5f);
        System.out.println(piecesC);
        slash = new Digit("slash",10f);
        digit3 = new Digit(world.getPieces()/10,15f);
        digit4 = new Digit(world.getPieces()%10,20f);
    }

    public static void score_render(Shader shader, Camera camera){
        digit1.render(shader,camera);
        digit2.render(shader,camera);
        slash.render(shader, camera);
        digit3.render(shader, camera);
        digit4.render(shader, camera);
    }
}
