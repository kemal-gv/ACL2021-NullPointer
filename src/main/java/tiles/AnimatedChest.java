package tiles;

import framerate.Timer;
import models.Chest;

public class AnimatedChest extends AnimatedTile{

    private String item;

    private Chest c;



    public AnimatedChest(int id, String texture, String filename, int amount, int fps, boolean cooldown) {
        super(id, texture, filename, amount, fps, cooldown);
        item="Waw";
        this.texturePointer=0;
    }

    public String getItem(){
        return item;
    }



    private boolean isOpened=false;
    @Override
    public void bind(int sampler) {
        this.currentTime = Timer.getTime();
        this.elapsedTime += currentTime - lastTime;

        if (elapsedTime >= fps) {
            if(open && c.getAnimation()) {

                elapsedTime = 0;
                texturePointer++;

                if(texturePointer==frames.length-1){
                    c.setOpen(true);
                    c.setAnimation(false);
                    isOpened=true;
                    System.out.println("IS OPEN");
                }
            }
        }

        if (texturePointer >= frames.length) {
            texturePointer = frames.length-1;
        }

        this.lastTime = currentTime;

        frames[texturePointer].bind(sampler);
    }

    public void setOpen(){
        texturePointer=0;
            open=true;
    }


    public boolean isOpened() {
        return isOpened;
    }

    public AnimatedChest getTile()  {
        AnimatedChest tile=new  AnimatedChest(id,  texture,  fileName,  amount,  (int)fps,  cooldown);
        if(tile.isSolid()){
            tile.setSolid();
        }
        return tile;
    }

    public Chest getC() {
        return c;
    }

    public void setC(Chest c) {
        this.c = c;
    }
}
