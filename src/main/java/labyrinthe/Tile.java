package labyrinthe;

public class Tile {


   // public static final Tile testTile = new Tile(0,"groundEarth_checkered");

    private int id;
    private String texture;

    public Tile(int id, String texture){
        this.id=id;
        this.texture=texture;
    }

    public void setWalls(){

    }

    public int getId() {
        return id;
    }

    public String getTexture() {
        return texture;
    }
}
