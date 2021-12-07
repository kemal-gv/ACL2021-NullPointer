package tiles;

public class Tile {


    // public static final Tile testTile = new Tile(0,"groundEarth_checkered");

    protected int id;
    protected String texture;
    protected boolean solid;
    protected boolean animated = false;

    public Tile(int id, String texture) {
        this.id = id;
        this.texture = texture;
        this.solid = false;
    }

    public void setWalls() {

    }

    public int getId() {
        return id;
    }

    public String getTexture() {
        return texture;
    }

    public Tile setSolid() {
        solid = true;
        return this;
    }

    public boolean isSolid() {
        return solid;
    }

    public boolean isAnimated() {
        return animated;
    }

    public void setAnimated(boolean animated) {
        this.animated = animated;
    }

    public Tile getTile() {
        Tile newT = new Tile(id, texture);
        if (newT.isSolid()) {
            newT.setSolid();
        }
        return newT;
    }

    public void bind() {

    }


}
