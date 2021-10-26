package framerate;

public class Timer {
    //Classe timer basique

    public static double getTime(){
        return (double)System.nanoTime() / (double)1000000000L;//On met nano en secondes
    }
}
