package models;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;

public class Audio{
    private String name = "ruins";
    private Clip clip = null;

    public Audio(){

    }

    public void play() throws Exception{
        if (clip != null && clip.isOpen()) clip.close();
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src/main/resources/music/" + name + ".wav").getAbsoluteFile());
        clip = AudioSystem.getClip();

        clip.open(audioInputStream);
        FloatControl gainControl =
                (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(0f);

        System.out.println(clip.getFrameLength() + " | " + clip.getFramePosition());
        clip.start();
    }
}
