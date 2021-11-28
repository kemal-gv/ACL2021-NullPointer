package tiles;

import framerate.Timer;
import render.Animation;
import render.Camera;
import render.Shader;
import render.Texture;

public class AnimatedTile extends Tile {
    protected Texture[] frames;
    protected int texturePointer;

    protected double elapsedTime;
    protected double currentTime;
    protected double lastTime;
    protected double fps;
    protected boolean cooldown = false;

    protected String fileName;
    protected int amount;


    public AnimatedTile(int id, String texture,String filename,int amount,int fps,boolean cooldown) {
        super(id, texture);
        this.amount=amount;
        this.fileName=filename;
        this.setAnimated(true);
        this.texturePointer = 0;
        this.elapsedTime = 0;
        this.currentTime = 0;
        this.lastTime = Timer.getTime();
        this.fps = 1.0 / fps;

        this.cooldown=cooldown;
        this.frames = new Texture[amount];
        for (int i = 0; i < amount; i++) {
            //System.out.println(filename + "/" + i + ".png");
            this.frames[i] = new Texture(filename + "/" + i + ".png");
        }
    }

    public void bind() {
        bind(0);
    }

    protected boolean open=false;
    protected int cpt=0;

    public void bind(int sampler) {
            this.currentTime = Timer.getTime();
            this.elapsedTime += currentTime - lastTime;

            if (elapsedTime >= fps) {
                if (cooldown) {
                    if (texturePointer == 0) {
                        cpt = 1;
                        open = false;
                    }
                    if (elapsedTime > fps * 10) cpt = 0;
                    if (cpt == 0) {
                        open = true;
                        elapsedTime = 0;
                        texturePointer++;
                    }
                } else {
                    elapsedTime = 0;
                    texturePointer++;
                }
            }

            if (texturePointer >= frames.length) texturePointer = 0;

            this.lastTime = currentTime;

            frames[texturePointer].bind(sampler);

    }


    public boolean isOpen() {
        return open;
    }

    public AnimatedChest getTile()  {
        AnimatedChest newT=new AnimatedChest(id,  texture,  fileName,  amount,  (int)fps,  cooldown);
        if(newT.isSolid())
            newT.setSolid();

        return newT;

    }

}
