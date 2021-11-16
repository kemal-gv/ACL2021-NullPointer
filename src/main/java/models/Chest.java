package models;

import tiles.AnimatedChest;

public class Chest {
    private int posx;
    private int posy;
    private String item;
    private boolean isOpen=false;
    private AnimatedChest ac;
    private boolean empty=false;


    public Chest(int posx, int posy, String item, AnimatedChest ac){
        this.posx=posx;
        this.posy=posy;
        this.item=item;
        this.ac=ac;
    }

    public AnimatedChest getAc(){
        return ac;
    }

    public int getPosx() {
        return posx;
    }

    public int getPosy() {
        return posy;
    }

    public void setPosy(int posy) {
        this.posy = posy;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }
}
