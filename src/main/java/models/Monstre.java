package models;

import collision.AABB;
import collision.Collision;
import framerate.Timer;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.util.zstd.ZSTDOutBuffer;
import render.Animation;
import render.Camera;
import render.Shader;
import windows.Window;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;

public class Monstre {
	private int vie;
	private int attaque;
	private float posX;
	private float posY;


	private Model model;
	private Animation texture;
	private models.Transform tr;

	private AABB boundingBox;
	private double currentTime;
	private double lastTime;
	private double elapsedTime;


	public Monstre(int vie, int posX, int posY) {
		this.vie = vie;

		float[] vertices=new float[]{
				-1f,1f,0,//TOP LEFT     0
				1f,1f,0,//TOP RIGHT     1
				1f,-1f,0,//BOTTOM RIGHT 2

				// -0.5f,0.5f,0,//TOP LEFT
				// 0.5f,-0.5f,0,//BOTTOM RIGHT
				-1,-1f,0//BOTTOM LEFT  3

		};


		float[] texture=new float[]{
				0,0,
				1,0,
				1,1,
				// 0,0,
				// 1,1,
				0,1

		};


		int[] indices = new int[]{
				0,1,2,
				2,3,0
		};

		model=new Model(vertices,texture,indices);
		this.texture = new Animation(4,10, "muddy");


		AABB box=null;

		tr = new Transform();
		tr.scale = new Vector3f(32,32,1);
		tr.pos.x=posX;
		tr.pos.y=posY;
		this.posX = posX;
		this.posY = posY;
		boundingBox=new AABB(new Vector2f(posX,posY),new Vector2f(1,1));
	}
	
	public Monstre(int vie, int attaque, int posX, int posY) {
		this.vie = vie;
		this.attaque = attaque;
		this.posX = posX;
		this.posY = posY;
	}
	
	/*public void attaqueJoueur(Joueur j) {
		j.reduireVie(attaque);
	}*/
	
	public void reduireVie(int vie) {
		setVie(this.vie - vie);
	}
	
	public void deplacementAleatoire(float delta, Window win, Camera camera, Labyrinthe world) {
		Random random = new Random();
		int choixDeplacement = random.nextInt(4);
		/*
		switch(choixDeplacement) {
			case 0 :
				tr.pos.add(new Vector3f(15*delta,0,0));
				break;
			case 1:
				tr.pos.add(new Vector3f(0,15*delta,0));
				break;
			case 2 :
				tr.pos.add(new Vector3f(-15*delta,0,0));
				break;
			case 3:
				tr.pos.add(new Vector3f(0,-15*delta,0));
				break;
			default:
				break;
		}

		 */








		posX = tr.pos.x;
		posY = tr.pos.y;
		boundingBox.getCenter().set(posX,posY);

		AABB[] boxes=new AABB[25];
		for(int i=0;i<5;i++){
			for(int j=0;j<5;j++){
				boxes[i+j*5]=world.verifierCollision((int)(((posX/2)+0.5f)-(5/2))+i,(int)(((-posY/2)+0.5f)-(5/2))+j);
				//  boxes[i+j*5]=world.verifierCollision((int)posX/3,(int)posY/3);

			}
		}

		AABB box=null;
		for(int i=0;i<boxes.length;i++){
			if(boxes[i]!=null){
				if(box==null){
					box=boxes[i];
				}
				Vector2f length1 = box.getCenter().sub(posX,posY,new Vector2f());
				Vector2f length2 = boxes[i].getCenter().sub(posX,posY,new Vector2f());

				if(length1.lengthSquared() > length2.lengthSquared()){
					box = boxes[i];
				}
			}
		}

		if(box!=null) {
			Collision data = boundingBox.getCollision(box);
			if (data.isIntersecting) {
				// System.out.println("collision");
				//System.out.println((int)(box.getCenter().x/2)+" ====" +(int)(box.getCenter().y/2));
				//System.out.println("Collision avec : "+world.getElementPlateau((int)(box.getCenter().x/2),(int)Math.abs(box.getCenter().y/2)).getId());
				//System.out.println("Box x:"+box.getCenter().x+" //// box y = "+box.getCenter().y);
				//System.out.println("Joueur x "+posX+" joueur y ="+posY);

				boundingBox.correctPosition(box, data);
				tr.pos.set(boundingBox.getCenter(),0);
			}



		}



		//camera.setPosition(tr.pos.mul(-world.getScale() /* /2f -> -16*/,new Vector3f()));

	}

	public int getVie() {
		return vie;
	}

	public void setVie(int vie) {
		this.vie = vie;
	}

	public int getAttaque() {
		return attaque;
	}

	public void setAttaque(int attaque) {
		this.attaque = attaque;
	}

	public float getPosX() {
		return posX;
	}

	public void setPosX(float posX) {
		this.posX = posX;
	}

	public float getPosY() {
		return posY;
	}

	public void setPosY(float posY) {
		this.posY = posY;
	}
	
	@Override
	public String toString() {
		return "Monstre [vie=" + vie + ", attaque=" + attaque + ", posX=" + posX + ", posY=" + posY + "]";
	}

	public void render(Shader shader, Camera camera) {
		shader.bind();
		shader.setUniform("sampler",0 );
		shader.setUniform("projection", tr.getProjection(camera.getProjection()));
		texture.bind(0);
		model.render();
	}



	public void pathFinding(int x,int y,Labyrinthe laby){
		this.currentTime = Timer.getTime();
		this.elapsedTime += currentTime - lastTime;


		for(int i = 0; i < 1; i++) {

		///	System.out.println("Pos : "+posX+","+posY+"elaps:"+elapsedTime);

			boolean[][] tab=new boolean[64][64];
			for(int a=0;a<64;a++){
				for(int b=0;b<64;b++){
					if(laby.getElementPlateau(a,b).isSolid()) {
						tab[a][b] = true;
					}
					else{
						tab[a][b]=false;
					}

				}
			}
			int posx= (int) Math.ceil((((getPosX()/2)+0.5f)-(5/2)));
			int posy = (int) Math.ceil(((((-getPosY()/2)+0.5f)-(5/2))));


			if(x!=0 && y!=0) {
				x -= 1;
				y -= 1;
			}
			Direction d=findShortestPathToMouse(tab,posx,posy,x,y);


			if(elapsedTime>=0.1 && d!=null) {

				Random random = new Random();
				int choixDeplacement = random.nextInt(15)+15;
				//UP
				if(d.getDx()==0 && d.getDy()==-1)
					tr.pos.add(new Vector3f(0, (float) (choixDeplacement*0.02), 0));
				else if(d.getDx()==1 && d.getDy()==0)//R
					tr.pos.add(new Vector3f( (float) (choixDeplacement*0.02),0, 0));
				else if(d.getDx()==0 && d.getDy()==1)//D
					tr.pos.add(new Vector3f(0, -(float) (choixDeplacement*0.02), 0));
				else if(d.getDx()==-1 && d.getDy()==0)//L
					tr.pos.add(new Vector3f(-(float) (choixDeplacement*0.02), 0, 0));
				/*
				if(posX>xTmp)
					//tr.pos.sub(new Vector3f((float) (xTmp*0.02), 0, 0));
				else{
					//tr.pos.add(new Vector3f((float) (xTmp*0.02), 0, 0));
				}
				if(posY>yTmp)
					//tr.pos.sub(new Vector3f(0, (float) (yTmp*0.02), 0));
				else{
					//tr.pos.add(new Vector3f(0, (float) (yTmp*0.02), 0));
				}


				 */

				elapsedTime=0;
			}


			//points.add(new Point(x,y));
		}
		this.lastTime = currentTime;

	}

	public enum Direction {
		UP(0, -1),
		RIGHT(1, 0),
		DOWN(0, 1),
		LEFT(-1, 0);

		private final int dx;
		private final int dy;

		Direction(int dx, int dy) {
			this.dx = dx;
			this.dy = dy;
		}

		public int getDx() {
			return dx;
		}

		public int getDy() {
			return dy;
		}
	}

	/**
	 * Finds the shortest path from cat to mouse in the given labyrinth.
	 *
	 * @param lab the labyrinth's matrix with walls indicated by {@code true}
	 * @param cx the cat's X coordinate
	 * @param cy the cat's Y coordinate
	 * @param mx the mouse's X coordinate
	 * @param my the mouse's Y coordinate
	 * @return the direction of the shortest path
	 */
	private Direction findShortestPathToMouse
	(boolean[][] lab, int cx, int cy, int mx, int my) {
		// Create a queue for all nodes we will process in breadth-first order.
		// Each node is a data structure containing the cat's position and the
		// initial direction it took to reach this point.
		Queue<Node> queue = new ArrayDeque<>();

		// Matrix for "discovered" fields
		// (I know we're wasting a few bytes here as the cat and mouse can never
		// reach the outer border, but it will make the code easier to read. Another
		// solution would be to not store the outer border at all - neither here nor
		// in the labyrinth. But then we'd need additional checks in the code
		// whether the outer border is reached.)
		boolean[][] discovered = new boolean[64][64];

		// "Discover" and enqueue the cat's start position
		//discovered[cy][cx] = true;
		int posx= (int) Math.ceil(((((getPosX()/2)+0.5f)-(5/2))));
		int posy = (int) Math.ceil(((((-getPosY()/2)+0.5f)-(5/2))));

		discovered[posy][posx] = true;
		queue.add(new Node(cx, cy, null));

		while (!queue.isEmpty()) {
			Node node = queue.poll();

			// Go breath-first into each direction
			for (Direction dir : Direction.values()) {
				int newX = node.x + dir.getDx();
				int newY = node.y + dir.getDy();
				Direction newDir = node.initialDir == null ? dir : node.initialDir;

				// Mouse found?
				if (newX == mx && newY == my) {
					return newDir;
				}

				// Is there a path in the direction (= is it a free field in the labyrinth)?
				// And has that field not yet been discovered?
				if(newY>=0 && newX>=0)
				if (!lab[newY][newX] && !discovered[newY][newX]) {
					// "Discover" and enqueue that field
					discovered[newY][newX] = true;
					queue.add(new Node(newX, newY, newDir));
				}
			}
		}
		return null;
		//throw new IllegalStateException("No path found");
	}

	private static class Node {
		final int x;
		final int y;
		final Direction initialDir;

		public Node(int x, int y, Direction initialDir) {
			this.x = x;
			this.y = y;
			this.initialDir = initialDir;
		}
	}


}
