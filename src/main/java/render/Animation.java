package render;

import framerate.Timer;

public class Animation {
    private final Texture[] frames;
    private int texturePointer;

    private double elapsedTime;
    private double currentTime;
    private double lastTime;
    private double fps;

    public Animation(int amount, int fps, String filename) {
        this.texturePointer = 0;
        this.elapsedTime = 0;
        this.currentTime = 0;
        this.lastTime = Timer.getTime();
        this.fps = 1.0 / fps;

        this.frames = new Texture[amount];
        for (int i = 0; i < amount; i++) {
            System.out.println(filename + "/" + i + ".png");
            this.frames[i] = new Texture(filename + "/" + i + ".png");
        }
    }

    private boolean weapon = false;

    public Animation(String filename) {
        this.texturePointer = 0;
        this.elapsedTime = 0;
        this.currentTime = 0;
        this.lastTime = Timer.getTime();
        this.fps = 1.0 / 10;


        this.frames = new Texture[1];
        this.frames[0] = new Texture(filename + ".png");
        weapon = true;
    }

    public void bind() {
        bind(0);
    }

    public void bind(int sampler) {
        if (weapon) {
            frames[0].bind(sampler);

        } else {
            this.currentTime = Timer.getTime();
            this.elapsedTime += currentTime - lastTime;

            if (elapsedTime >= fps) {
                elapsedTime = 0;
                texturePointer++;
            }

            if (texturePointer >= frames.length) texturePointer = 0;

            this.lastTime = currentTime;

            frames[texturePointer].bind(sampler);
        }
    }

    public Texture getTexture() {
        return frames[0];
    }
}