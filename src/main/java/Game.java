import collision.AABB;
import models.*;
import tiles.TileRenderer;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import render.Camera;
import framerate.Timer;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.GL;
import render.Shader;
import render.Texture;
import windows.Window;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Game {
    private static long window;


    private static final int WIDTH=1600;
    private static final int HEIGHT =900;
    private static final int FPS=60;
    public static void main(String[] args) {
        Window.setCallBacks();

        AABB box1= new AABB(new Vector2f(0,0),new Vector2f(1,1));
        AABB box2= new AABB(new Vector2f(0,0),new Vector2f(1,1));

        if (box1.getCollisionInter(box2)){
            System.out.println("INTERSECTION");
        }
        else{
            System.out.println("pas inters");
        }



        //On initialise GLFW
        if(!glfwInit()) {
            //throw new IllegalStateException("Erreur dans l'initialisation de  GLFW");
            System.err.println("Erreur dans l'initialisation de  GLFW");
            System.exit(1);
        }

        glfwWindowHint(GLFW_VISIBLE,GLFW_FALSE);


        Window win= new Window();


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
        Camera camera= new Camera(win.getWidth(),win.getWidth());


        glEnable(GL_TEXTURE_2D);
        //On doit creer les textures ici après le context
        TileRenderer tileRenderer=new TileRenderer();

        Joueur joueur = new Joueur(100, 10, -3);
        //JoueurTest jTest=new JoueurTest(100, 10, -1, new Transform());

        ArrayList<Monstre> monstres = new ArrayList<>();
        Random random = new Random();
        ArrayList<HealthBar> hbMonstres = new ArrayList<>();
        for (int i = 0; i<5; i++){
            int randX = random.nextInt(50 + 10) + 10; // max 50 ; min 10
            int randY = -random.nextInt(50 + 10) - 10; // max -10 ; min -50
            int vie = random.nextInt(100 + 1) + 1;
            System.out.println("vie monstre" +vie);
            Monstre monstre = new Monstre(vie, randX,randY);
            HealthBar hbMonstre = new HealthBar(vie);

            monstres.add(monstre);
            hbMonstres.add(hbMonstre);
        }

        Labyrinthe world = new Labyrinthe("level1",joueur,win);

        world.setMonstre(monstres);
     //   world.setTile(tileRenderer.getGestionnaireTile().getTile(6),3,3);
       // world.setTile(tileRenderer.getGestionnaireTile().getTile(6),0,0);
        //world.setTile(tileRenderer.getGestionnaireTile().getTile(6),0,63);


        HealthBar hb = new HealthBar((int)joueur.getVie());
        //world.setTile(tileRenderer.getGestionnaireTile().getTile(1),0,0);


        //Test setup world
       // //Coin haut gauche
        //world.setTile(tileRenderer.getGestionnaireTile().getTile(4),0,0);

        for(int i=0;i<20;i++){
           // world.setTile(tileRenderer.getGestionnaireTile().getTile(8),i,0);
         //   world.setTile(tileRenderer.getGestionnaireTile().getTile(8),0,i);
           // world.setTile(tileRenderer.getGestionnaireTile().getTile(8),19,i);
           // world.setTile(tileRenderer.getGestionnaireTile().getTile(8),i,19);

        }

        //world.setTile(tileRenderer.getGestionnaireTile().getTile(2),0,19);

        // Texture tex=new Texture(("groundEarth_checkered.png"));
       // Texture tex=new Texture(("groundExit.png"));
        Texture tex=new Texture(("test.png"));

        //Creation d'un shader
        Shader shader= new Shader("shader");





       // Matrix4f projection= new Matrix4f().ortho2D(-WIDTH/2,WIDTH/2, -HEIGHT /2,HEIGHT /2);
        Matrix4f scale = new Matrix4f()
                //.translate(new Vector3f(100,0,0))//Pour modifier la position de notre image
                .scale(16);
        Matrix4f target = new Matrix4f();

       // projection.mul(scale,target);//Projection*scale = target


        //Test camera position
        //camera.setPosition(new Vector3f(-100,0,0));//pr mettre l'image de nouvea au centre (quand on a fait le translate plus haut)

        //Gérer les fps
        double frameCap = 1.0/FPS;// X sec / Nb fps ici 60images par 1 seconde   cb on veut de fps
        double time = Timer.getTime();
        double unprocessed = 0 ;

        double frameTime=0;
        int frames=0;



        Audio audio = new Audio();
        try {
            audio.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

        int i = 1;

        // while(!glfwWindowShouldClose(window)){
        while(!win.shouldClose()){


            boolean canRender=false;
            double time2=Timer.getTime();
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

               //
                joueur.deplacement((float)frameCap,win,camera,world);

                //jTest.deplacement((float)frameCap,win,camera,world);

                joueur.setVie(joueur.getVie());



                //vie du joueur infèrieur à 0
                if (joueur.getVie()>=0)
                    hb.update((int)joueur.getVie());
                world.correctCamera(camera,win);



                win.update();


                if(frameTime >= 1.0){//Every secon we print how much frame we have
                    frameTime=0;
                    //System.out.println("FPS : "+frames);
                    frames=0;

                }

            }



            if(canRender){
                glClear(GL_COLOR_BUFFER_BIT);// ? Set every pixel to black ? pas sur
                tex.bind(0);



                //shader.bind();
                // shader.setUniform("sampler",0);
                //shader.setUniform("projection",camera.getProjection().mul(target));
                //model.render();

                // testSquare();

                //Rendering tile

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
               // jTest.render(shader,camera,world);
                //jTest.setPos(10,-3);

                hb.render(shader);

                win.swapBuffers();
                frames++;

            }

        }

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

}
