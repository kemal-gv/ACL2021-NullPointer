package models;

import java.util.Random;

public class Monstre {
	private int vie;
	private int attaque;
	private int posX;
	private int posY;
	
	public Monstre(int posX, int posY) {
		vie = 1;
		attaque = 1;
		this.posX = posX;
		this.posY = posY;
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
	
	public void deplacementAleatoire() {
		Random random = new Random();
		int value = random.nextInt(4);
		switch(value) {
			case 0 : 
				setPosX(posX-1); 
				break;
			case 1:
				setPosX(posX+1);
				break;
			case 2 :
				setPosY(posY-1);
				break;
			case 3 :
				setPosY(posY+1);
				break;
			default:
				break;
		}
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

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}
	
	@Override
	public String toString() {
		return "Monstre [vie=" + vie + ", attaque=" + attaque + ", posX=" + posX + ", posY=" + posY + "]";
	}

}
