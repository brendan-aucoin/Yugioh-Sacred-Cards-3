/*
 *Brendan Aucoin
 *04/03/2020
 *any object in the game that has a location a bounding box and a speed essentially any thing you can see on the 
 *overworld as well as particles
 * */
package entities;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public abstract class Entity {
	private float x,y,velX,velY;
	private int width,height;
	private EntityID id;
	public Entity(float x,float y,EntityID id) {
		this.x = x;
		this.y = y;
		this.id = id;
	}
	/*every entity updates and renders*/
	public abstract void update();
	public abstract void render(Graphics2D g);
	
	/*add your velX to your current x position*/
	public void addVelX() {
		this.x += velX;
	}
	/*add your velX to your current x position*/
	public void addVelY() {
		this.y += velY;
	}
	/*returns a rectangle of your x,y,width,and height*/
	public Rectangle getBounds() {
		return new Rectangle((int)x,(int)y,width,height);
	}
	/*temp*/
	public void remove() {
		x = 0;
		y =0;
		velX = 0;
		velY= 0;
		width = 0;
		height = 0;
	}
	/*getters and setters*/
	public float getX() {return x;}
	public void setX(float x) {this.x = x;}
	public float getY() {return y;}
	public void setY(float y) {this.y = y;}
	public float getVelX() {return velX;}
	public void setVelX(float velX) {this.velX = velX;}
	public float getVelY() {return velY;}
	public void setVelY(float velY) {this.velY = velY;}
	public int getWidth() {return width;}
	public void setWidth(int width) {this.width = width;}
	public int getHeight() {return height;}
	public void setHeight(int height) {this.height = height;}
	public EntityID getId() {return id;}
	public void setId(EntityID id) {this.id = id;}
}
