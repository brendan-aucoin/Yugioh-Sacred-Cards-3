package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.function.Consumer;

public class Button extends Aesthetics{
	private Rectangle bounds;
	private Color baseCol,hoverCol;
	private String text;
	private Font font;
	private Consumer<MouseEvent> mousePressed;
	private int mouseX,mouseY;
	protected FontRenderContext frc;
	protected AffineTransform affinetransform;
	public Button(Rectangle bounds,Color baseCol,Color hoverCol,String text,Font font,Consumer<MouseEvent> mousePressed) {
		this.bounds = bounds;
		this.baseCol = baseCol;
		this.hoverCol = hoverCol;
		this.text = text;
		this.font = font;
		this.mousePressed = mousePressed;
		affinetransform = new AffineTransform();   
		frc = new FontRenderContext(affinetransform,true,true);
	}
	public void update(int mouseX,int mouseY) {
		this.mouseX = mouseX;
		this.mouseY = mouseY;
	}
	

	public void render(Graphics2D g) {
		g.setFont(font);
		this.changeColour(g,mouseX,mouseY,bounds, hoverCol, baseCol);
	}
	
	public void mousePressed(MouseEvent e) {
		if(mousePressed != null) {
			if(mouseOver(mouseX,mouseY,bounds)) {
				mousePressed.accept(e);
			}
		}
	}
	
	/*getters and setters*/
	public Rectangle getBounds() {return bounds;}
	public void setBounds(Rectangle bounds) {this.bounds = bounds;}
	public void setX(int x) {this.bounds.x = x;}
	public void setY(int y) {this.bounds.y = y;}
	public void setWidth(int width) {this.bounds.width = width;}
	public void setHeight(int height) {this.bounds.height = height;}
	public int getX() {return this.bounds.x;}
	public int getY() {return this.bounds.y;}
	public int getWidth() {return this.bounds.width;}
	public int getHeight() {return this.bounds.height;}
	public Color getBaseCol() {return baseCol;}
	public void setBaseCol(Color baseCol) {this.baseCol = baseCol;}
	public Color getHoverCol() {return hoverCol;}
	public void setHoverCol(Color hoverCol) {this.hoverCol = hoverCol;}
	public String getText() {return text;}
	public void setText(String text) {this.text = text;}
	public Font getFont() {	return font;}
	public void setFont(Font font) {this.font = font;}	
	public Consumer<MouseEvent> getMousePressed() {return mousePressed;}
	public void setMousePressed(Consumer<MouseEvent> mousePressed) {this.mousePressed = mousePressed;}
	public int getMouseX() {return mouseX;}
	public void setMouseX(int mouseX) {this.mouseX = mouseX;}
	public int getMouseY() {return mouseY;}
	public void setMouseY(int mouseY) {	this.mouseY = mouseY;}
	
	
	
	
	
	
	
	
}
