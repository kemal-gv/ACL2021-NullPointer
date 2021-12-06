package models;

public class HoleTp {
    private int tpX;
    private int tpY;
    private int posX;
    private int posY;

    public HoleTp(int tpX, int tpY, int posX, int posY) {
        this.tpX = tpX;
        this.tpY = tpY;
        this.posX = posX;
        this.posY = posY;
    }

    public int getTpX() {
        return tpX;
    }

    public void setTpX(int tpX) {
        this.tpX = tpX;
    }

    public int getTpY() {
        return tpY;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }
}
