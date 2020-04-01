/*
 *Brendan Aucoin
 *06/30/2019
 *the super class of any state or anything you see in the game because this holds the bounds for that state
 */
package interaction_pane;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

import game.Game;

public abstract class InteractionPane {
	private int mouseX,mouseY;
	private Game game;
	private Rectangle bounds;
	protected AffineTransform affinetransform;
	protected FontRenderContext frc;
	public InteractionPane(Game game,Rectangle bounds) {
		this.game = game;
		this.bounds=  bounds;
		init();
	}
	/*this is to overload it in the state classes that inherit from this class*/
	public void mousePressed(MouseEvent e) {}
	
	/*sets the variables to 0 and intializes the objects*/
	protected void init() {
		mouseX =  mouseY = 0;
		affinetransform = new AffineTransform();   
		frc = new FontRenderContext(affinetransform,true,true);
	}
	 
	  /*moving mouse*/
	  public void mouseMoved(MouseEvent e){
	    this.mouseX = e.getX();
	    this.mouseY = e.getY();
	  }
	  
	  /*checks if an x and y position is over a rectangle defined by the individual recatangle components*/
	 protected boolean mouseOver(int mx,int my,int x,int y,int width,int height)
	 {
	   if(mx > x && mx < x+width)
	   {
	     if(my > y && my <y +height)
	     {
	       return true;
	     }
	   }
	   return false;
	 }
	  /*checks if an x and y position is over a rectangle*/
	  protected boolean mouseOver(int mx,int my,Rectangle bounds)
	 {
	   return mouseOver(mx,my,bounds.x,bounds.y,bounds.width,bounds.height);
	 }
	  /*checks if your mouse if over a rectangle*/
	  protected boolean mouseOver(Rectangle bounds)
	 {
		  return mouseOver(mouseX,mouseY,bounds);
	 }
	  
	  /*if you are over a certain bounds change its colour of the graphics*/
	  protected void changeColour(Graphics2D g,Rectangle bounds,Color newCol,Color oldCol)
	 {
	  if(mouseOver(mouseX,mouseY,bounds.x,bounds.y,bounds.width,bounds.height)) {g.setColor(newCol);}
	  else {g.setColor(oldCol);}
	 }
	  /*if you are over a certain bounds change its colour of the graphics*/
	  protected void changeColour(Graphics2D g,int x,int y, int width,int height,Color newCol,Color oldCol)
	 {
	  if(mouseOver(mouseX,mouseY,x,y,width,height)) {g.setColor(newCol);}
	  else {g.setColor(oldCol);}
	 }
	  /*if you are over a certain bounds change its colour of the graphics*/
	  protected void changeColour(Graphics g,Rectangle bounds,Color newCol,Color oldCol)
	 {
	  if(mouseOver(mouseX,mouseY,bounds.x,bounds.y,bounds.width,bounds.height)) {g.setColor(newCol);}
	  else {g.setColor(oldCol);}
	 }
	  /*if you are over a certain bounds change its colour of the graphics*/
	  protected void changeColour(Graphics g,int x,int y, int width,int height,Color newCol,Color oldCol)
	 {
	  if(mouseOver(mouseX,mouseY,x,y,width,height)) {g.setColor(newCol);}
	  else {g.setColor(oldCol);}
	 }
	
	
	public void mouseDragged(MouseEvent e) {}
	public void mouseClicked(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}
	public void mouseWheelMoved(MouseWheelEvent arg0) {}
	public int getMouseX() {return mouseX;}
	public void setMouseX(int mouseX) {this.mouseX = mouseX;}
	public int getMouseY() {return mouseY;}
	public void setMouseY(int mouseY) {this.mouseY = mouseY;}
	public Game getGame() {return game;}
	public void setGame(Game game) {this.game = game;}
	public void setBounds(Rectangle bounds) {this.bounds = bounds;}
	public Rectangle getBounds() {return bounds;}
	public AffineTransform getAffinetransform() {return affinetransform;}
	public void setAffinetransform(AffineTransform affinetransform) {this.affinetransform = affinetransform;}
	public FontRenderContext getFrc() {return frc;}
	public void setFrc(FontRenderContext frc) {this.frc = frc;}
	
	
}
