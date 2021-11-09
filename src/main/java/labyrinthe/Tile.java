package labyrinthe;

public class Tile {


   // public static final Tile testTile = new Tile(0,"groundEarth_checkered");

    private int id;
    private String texture;
    private boolean solid;


    public Tile(int id, String texture){
        this.id=id;
        this.texture=texture;
        this.solid=false;
    }

    public void setWalls(){

    }

    public int getId() {
        return id;
    }

    public String getTexture() {
        return texture;
    }

    public Tile setSolid() {
        solid = true;return this;
    }
    public boolean isSolid(){
        return solid;
    }
}
