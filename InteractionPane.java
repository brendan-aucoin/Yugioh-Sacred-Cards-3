/*
 *Brendan Aucoin
 *06/30/2019
 *the super class of any state or anything you see in the game because this holds the bounds for that state
 */
package interaction_pane;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

import game.Game;
import gui.Aesthetics;

public abstract class InteractionPane extends Aesthetics{
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
	public int makeRectangleWidth(Font font,String str) {
		return (int)(font.getStringBounds(str, frc).getWidth());
	}
	public int makeRectangleHeight(Font font,String str) {
		return (int)(font.getStringBounds(str, frc).getHeight());
	}
	 
	  /*moving mouse*/
	  public void mouseMoved(MouseEvent e){
	    this.mouseX = e.getX();
	    this.mouseY = e.getY();
	  }
	  protected boolean mouseOver(Rectangle bounds) {
		  return mouseOver(mouseX,mouseY,bounds.x,bounds.y,bounds.width,bounds.height);
	  }
	  protected void changeColour(Graphics2D g,Rectangle bounds,Color newCol,Color oldCol){
		  if(mouseOver(mouseX,mouseY,bounds.x,bounds.y,bounds.width,bounds.height)) {g.setColor(newCol);}
		  else {g.setColor(oldCol);}
	  }
	
	  
	  public void drawStringOverflow(Graphics2D g,String str,Rectangle bounds) {
		  int x = bounds.x + bounds.width/50;
			int y = bounds.y + bounds.height/40;
			String [] overflowString = str.split(" ");
			for(int i =0;i < overflowString.length;i++) {
				if(x + makeRectangleWidth(g.getFont(),overflowString[i]) > bounds.x + bounds.width) {
					x = bounds.x + bounds.width/50;
					y = y + g.getFont().getSize();
				}
				g.drawString(overflowString[i], x, y);
				x+=this.makeRectangleWidth(g.getFont(), overflowString[i])+5;
			}
	  }
	
	public void mouseDragged(MouseEvent e) {}
	public void mouseClicked(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}
	public void mouseWheelMoved(MouseWheelEvent arg0) {}
	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
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
