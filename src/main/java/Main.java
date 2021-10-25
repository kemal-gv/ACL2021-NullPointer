import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.GL;
import textures.Texture;

public class Main {
    private static long window;


    private static final int WIDTH=1280;
    private static final int LENGTH=800;

    public static void main(String[] args) {
        //On initialise GLFW
        if(!glfwInit()) {
            //throw new IllegalStateException("Erreur dans l'initialisation de  GLFW");
            System.err.println("Erreur dans l'initialisation de  GLFW");
            System.exit(1);
        }

        glfwWindowHint(GLFW_VISIBLE,GLFW_FALSE);
        //Creation de la fenetre
        window=glfwCreateWindow(WIDTH,LENGTH,"NullPointer'",0,0);

        if(window==0) {
            throw new IllegalStateException("Erreur dans la création de la fenêtre");
        }

        //
        GLFWVidMode videoMode= glfwGetVideoMode(glfwGetPrimaryMonitor());
        //On place la fenêtre au milieu
        glfwSetWindowPos(window,(videoMode.width() - WIDTH)/2, (videoMode.height() - LENGTH)/2);

        glfwShowWindow(window);

        //Create a context
        glfwMakeContextCurrent(window);//I need a context to display graphics
        GL.createCapabilities();

        glEnable(GL_TEXTURE_2D);
        //On doit creer les textures ici après le context

        Texture tex=new Texture(("groundEarth_checkered.png"));



        while(!glfwWindowShouldClose(window)){
            //Test input
            //testInput();
            //Fin test input

            //Update tant que la fenetre ne veut pas se fermer
            glfwPollEvents();

            glClear(GL_COLOR_BUFFER_BIT);// ? Set every pixel to black ? pas sur

            tex.bind();


            testSquare();




            glfwSwapBuffers(window);
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
