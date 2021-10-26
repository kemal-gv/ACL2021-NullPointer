package windows;

import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;

public class Window {
    private static long window;

    private boolean fullscreen=false;
    private int width,height;

    public Window(){
        setSize(640,480);
    }

    public void createWindow(String title){
        //FULL SCREEN =  window = glfwCreateWindow(width,height,title,glfwGetPrimaryMonitor(),0);

        //If fullscreen = true alors glfwGetPrimaryMonitor sinon 0
        window = glfwCreateWindow(width,height,title,fullscreen ? glfwGetPrimaryMonitor() : 0 ,0);

        if(window==0){
            System.out.println("d = "+nglfwGetError(window));
            throw new IllegalStateException("erreur dans la création de la fenetre : "+title);
        }
        if(!fullscreen){
            //On place la fenêtre au milieu
            GLFWVidMode videoMode= glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwSetWindowPos(window,(videoMode.width() - width)/2, (videoMode.height() - height)/2);
        }

        glfwShowWindow(window);

        glfwMakeContextCurrent(window);



    }

    public void setSize(int width,int height){
        this.width=width;
        this.height=height;
    }


    public boolean shouldClose(){
        return glfwWindowShouldClose(window);
    }

    public void swapBuffers(){
        glfwSwapBuffers(window);
    }
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    public long getWindow(){
        return window;
    }
}
