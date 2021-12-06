package tiles;

public class GestionnaireTile {

    public static Tile tiles[] = new Tile[256];

    private int cptTiles = 0;

    public GestionnaireTile() {
        //Set floor
        Tile floor1 = new Tile(0, "floor_1");
        Tile floor2 = new Tile(1, "floor_2");


        ajoutTiles("floor_1", 0, false);
        ajoutTiles("floor_2", 1, false);


        //Set walls
        for (int i = 0; i < 7; i++) {
            ajoutTiles("assets/walls/wall_" + (i + 1), i + 2, true);
        }


        //Animated tiles
        tiles[cptTiles] = new AnimatedTile(cptTiles, "fontaine/bas/0", "fontaine/bas/", 3, 15, false).setSolid();
        cptTiles++;


        tiles[cptTiles] = new AnimatedTile(cptTiles, "fontaine/haut/0", "fontaine/haut/", 3, 15, false).setSolid();
        cptTiles++;

        tiles[cptTiles] = new AnimatedTile(cptTiles, "spikes/0", "spikes", 4, 5, true);
        cptTiles++;

        tiles[cptTiles] = new AnimatedTile(cptTiles, "pieces/0", "pieces", 4, 5, false);
        cptTiles++;

        ajoutTiles("assets/doors/door1", cptTiles, true);
        ajoutTiles("assets/doors/door2", cptTiles, false);


        ajoutTiles("assets/chests/full/0", cptTiles, true);

        tiles[cptTiles] = new AnimatedChest(cptTiles, "assets/chests/full/0", "assets/chests/full", 3, 1, false).setSolid();
        cptTiles++;

        ajoutTiles("assets/chests/empty/2", cptTiles, true);

        ajoutTiles("assets/chests/full/2", cptTiles, true);

        ajoutTiles("assets/loot/flask_1", cptTiles, false);
        ajoutTiles("assets/loot/sword_0", cptTiles, false);//20

        ajoutTiles("assets/teleportation/hole_for_tp", cptTiles, false);//21
        ajoutTiles("assets/loot/axe_0", cptTiles, false);//22

        ajoutTiles("assets/sword_anim/0", cptTiles, false);//23
        ajoutTiles("assets/sword_anim_1/0", cptTiles, false);//24


    }

    public void ajoutTiles(String nom, int id, boolean solid) {
        if (tiles[id] != null) {
            throw new IllegalStateException("Tile n°" + id + " est deja utilise");
        }

        Tile res = new Tile(id, nom);
        if (solid) res.setSolid();
        tiles[id] = res;
        cptTiles++;
    }

    public Tile getTile(int i) {
        return tiles[i];
    }

    public int getNbTiles() {
        return tiles.length;
    }
}
