/*
 *Brendan Aucoin
 *04/03/2020
 *a particle is a type of entity that has a colour and a certain amount of life(time) associated with it
 * */
package entities;

import java.awt.Color;
import java.awt.Graphics2D;

public class Particle extends Entity{
	private float life;//how long it will last for (in ticks)
	private Color col;//the colour
	public Particle(float x,float y,int width,int height,float velX,float velY,float life,Color col) {
		super(x,y,EntityID.PARTICLE);
		setWidth(width);
		setHeight(height);
		setVelX(velX);
		setVelY(velY);
		this.life = life;
		this.col = col;
	}
	/*add the velocities to the position and decrement the life(time) if the life is 0 then remove it*/
	public void update() {
		addVelX();
		addVelY();
		life--;
		if(life <= 0) {
			life = 0;
			remove();
		}
	}
	
	public void render(Graphics2D g) {
		g.setColor(col);
		g.fillRect((int)getX(),(int)getY(),getWidth(),getHeight());
	}

	public float getLife() {return life;}
	public void setLife(float life) {this.life = life;}
	public Color getCol() {return col;}
	public void setCol(Color col) {this.col = col;}
}
