package tiles;

public class GestionnaireTile {

    public static Tile tiles[] = new Tile[32];


    public GestionnaireTile(){
        //Set floor
         Tile floor1 = new Tile(0,"floor_1");
         Tile floor2 = new Tile(1,"floor_2");


        ajoutTiles("floor_1",0,false);
        ajoutTiles("floor_2",1,false);



        //Set walls
        for(int i=0;i<7;i++){
            ajoutTiles("assets/walls/wall_"+(i+1),i+2,true);
        }

    }

    public void ajoutTiles(String nom,int id,boolean solid){
        if(tiles[id] != null){
            throw new IllegalStateException("Tile n°"+id+" est deja utilise");
        }

        Tile res=new Tile(id,nom);
        if(solid) res.setSolid();
        tiles[id]=res;
    }

    public Tile getTile(int i) {
        return tiles[i];
    }

    public int getNbTiles(){
        return tiles.length;
    }
}
