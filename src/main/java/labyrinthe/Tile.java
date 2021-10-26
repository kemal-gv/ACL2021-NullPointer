package labyrinthe;

public class Tile {
    public static Tile tiles[] = new Tile[32];


   // public static final Tile testTile = new Tile(0,"groundEarth_checkered");
    public static final Tile testTile = new Tile(0,"floor_1");
    public static final Tile testTile2 = new Tile(1,"floor_2");


    private int id;
    private String texture;

    public Tile(int id, String texture){
        this.id=id;
        this.texture=texture;
        if(tiles[id] != null){
            throw new IllegalStateException("Tile n°"+id+" est deja utilise");
        }

        tiles[id]= this;
    }

    public int getId() {
        return id;
    }

    public String getTexture() {
        return texture;
    }
}
