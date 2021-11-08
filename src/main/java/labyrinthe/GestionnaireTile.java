package labyrinthe;

public class GestionnaireTile {

    public static Tile tiles[] = new Tile[32];


    public GestionnaireTile(){
        //Set floor
         Tile floor1 = new Tile(0,"floor_1");
         Tile floor2 = new Tile(1,"floor_2");


        ajoutTiles("floor_1",0);
        ajoutTiles("floor_2",1);



        //Set walls
        for(int i=0;i<7;i++){
            ajoutTiles("assets/walls/wall_"+(i+1),i+2);
        }

    }

    public void ajoutTiles(String nom,int id){
        if(tiles[id] != null){
            throw new IllegalStateException("Tile n°"+id+" est deja utilise");
        }

        tiles[id]=new Tile(id,nom);
    }

    public Tile getTile(int i) {
        return tiles[i];
    }

    public int getNbTiles(){
        return tiles.length;
    }
}
