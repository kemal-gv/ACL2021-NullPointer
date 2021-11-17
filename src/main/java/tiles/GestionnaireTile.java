package tiles;

public class GestionnaireTile {

    public static Tile tiles[] = new Tile[256];

    private int cptTiles=0;

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


        //Animated tiles
        tiles[cptTiles]=new AnimatedTile(cptTiles,"fontaine/bas/0","fontaine/bas/",3,15,false).setSolid();
        System.out.println(cptTiles+" <<<<<<<<<<<");
        cptTiles++;


        tiles[cptTiles]=new AnimatedTile(cptTiles,"fontaine/haut/0","fontaine/haut/",3,15,false).setSolid();
        System.out.println(cptTiles+" <<<<<<<<<<<");
        cptTiles++;

        tiles[cptTiles]=new AnimatedTile(cptTiles,"spikes/0","spikes",4,5,true);
        System.out.println(cptTiles+" <<<<<<<<<<<");
        cptTiles++;

        tiles[cptTiles]=new AnimatedTile(cptTiles,"pieces/0","pieces",4,5,false);
        System.out.println(cptTiles+" <<<<<<<<<<<");
        cptTiles++;

        ajoutTiles("assets/doors/door1",cptTiles,true);
        ajoutTiles("assets/doors/door2",cptTiles,false);


       ajoutTiles("assets/chests/full/0",cptTiles,true);

        tiles[cptTiles]=new AnimatedChest(cptTiles,"assets/chests/full/0","assets/chests/full",3,1,false).setSolid();
        System.out.println(cptTiles+" <<<<<<<<<<<");
        cptTiles++;

        ajoutTiles("assets/chests/empty/2",cptTiles,true);

        ajoutTiles("assets/chests/full/2",cptTiles,true);

        ajoutTiles("assets/loot/flask_1",cptTiles,false);
        ajoutTiles("assets/loot/weapon_1",cptTiles,false);



    }

    public void ajoutTiles(String nom,int id,boolean solid){
        if(tiles[id] != null){
            throw new IllegalStateException("Tile n°"+id+" est deja utilise");
        }

        Tile res=new Tile(id,nom);
        if(solid) res.setSolid();
        tiles[id]=res;
        cptTiles++;
    }

    public Tile getTile(int i) {
        return tiles[i];
    }

    public int getNbTiles(){
        return tiles.length;
    }
}
