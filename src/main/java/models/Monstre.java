package models;

import collision.AABB;
import collision.Collision;
import org.joml.Vector2f;
import org.joml.Vector3f;
import render.Animation;
import render.Camera;
import render.Shader;
import windows.Window;

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
}
