import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.GLFW.*;


public class Main {
    private static long window;


    private static final int WIDTH=1280;
    private static final int LENGTH=800;
    public static void main(String[] args) {
        //On initialise GLFW
        if(!glfwInit()) {
            throw new IllegalStateException("Erreur dans l'initialisation de  GLFW");
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

        while(!glfwWindowShouldClose(window)){
            //Update tant que la fenetre ne veut pas se fermer
            glfwPollEvents();
        }

        glfwTerminate();
    }


}
