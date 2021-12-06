package models;

import windows.Window;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;

public class Audio {

    private Clip clip = null;

    public Audio() {

    }

    public void play(String audioTitle) throws Exception {
        if (clip != null && clip.isOpen()) clip.close();
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src/main/resources/audios/" + audioTitle + ".wav").getAbsoluteFile());
        clip = AudioSystem.getClip();

        clip.open(audioInputStream);
        FloatControl gainControl =
                (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(0f);

        System.out.println(clip.getFrameLength() + " | " + clip.getFramePosition());
        clip.start();
    }

    public void soundSword(Window win) {
        clip = null;
        if (win.getInput().isKeyPressed(GLFW_KEY_S)) {
            try {
                play("sword");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void soundDyingMonster(Monstre m) {
        clip = null;
        if (m.getVie() <= 0) {
            try {
                play("dying_monster");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void soundDoor() {
        clip = null;
        try {
            play("door");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void soundChest() {
        clip = null;
        try {
            play("chest");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void soundPotion() {
        clip = null;
        try {
            play("potion");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void soundChangeSword() {
        clip = null;
        try {
            play("change_sword");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void soundCoin() {
        clip = null;
        try {
            play("coin");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
