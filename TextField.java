package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.function.Consumer;

public class TextField extends Aesthetics{
	private String text;
	private Rectangle bounds;
	private Color borderCol,backgroundCol,textCol,hoverCol,cursorCol;
	private int mouseX,mouseY;
	private boolean canType;
	private Consumer<KeyEvent> keyPress;
	private FontRenderContext frc;
	private AffineTransform affinetransform;
	public TextField(Rectangle bounds,Color borderCol,Color backgroundCol,Color textCol,Color hoverCol,Color cursorCol,Consumer<KeyEvent>keyPress) {
		this.bounds = bounds;
		this.borderCol = borderCol;
		this.backgroundCol = backgroundCol;
		this.textCol = textCol;
		this.hoverCol = hoverCol;
		this.cursorCol = cursorCol;
		this.text = "";
		this.mouseX =0;
		this.mouseY=0;
		this.canType = false;
		this.keyPress = keyPress;
		affinetransform = new AffineTransform();   
		frc = new FontRenderContext(affinetransform,true,true);
	}
	public void update(int mouseX,int mouseY) {
		this.mouseX = mouseX;
		this.mouseY = mouseY;
	}
	
	public void render(Graphics2D g) {
		g.setColor(backgroundCol);
		g.fill(bounds);
		changeColour(g,mouseX, mouseY,bounds,hoverCol, borderCol);
		if(mouseOver(mouseX,mouseY,bounds) || canType) {
			g.setStroke(new BasicStroke(3));
		}
		g.draw(bounds);
		g.setColor(textCol);
		g.setFont(new Font("ariel",Font.PLAIN,bounds.getBounds().height/2));
		g.drawString(text, bounds.x, bounds.y + bounds.height/2 + (int)(g.getFont().getStringBounds(text, frc).getHeight()/4));
		if(canType) {
			g.setColor(this.cursorCol);
			g.drawLine(
			(int)(bounds.x + g.getFont().getStringBounds(text, frc).getWidth() + bounds.width/125), 
			bounds.y+bounds.height/8, 
			(int) (bounds.x + g.getFont().getStringBounds(text, frc).getWidth() + bounds.width/125), 
			(int) (bounds.y+bounds.height/1.2)
			
					
			);
		}
	}
	
	public void mousePressed(MouseEvent e) {
		if(mouseOver(mouseX,mouseY,bounds)) {
			canType = true;
		}
		else {
			canType = false;
		}
	}
	
	public void keyPressed(KeyEvent e) {
		if(canType && keyPress!=null) {
			keyPress.accept(e);
		}
	}
	
	public void addText(String text) {
		this.text += text;
	}
	public void backspace() {
		if(text.length() == 0) {return;}
		this.text = text.substring(0,text.length()-1);
	}

	public String getText() {return text;}
	public void setText(String text) {this.text = text;}
	public Rectangle getBounds() {return bounds;}
	public void setBounds(Rectangle bounds) {this.bounds = bounds;}
	public Color getBorderCol() {return borderCol;}
	public void setBorderCol(Color borderCol) {this.borderCol = borderCol;}
	public Color getBackgroundCol() {return backgroundCol;}
	public void setBackgroundCol(Color backgroundCol) {this.backgroundCol = backgroundCol;}
	public Color getTextCol() {return textCol;}
	public void setTextCol(Color textCol) {this.textCol = textCol;}
	public Color getHoverCol() {return hoverCol;}
	public void setHoverCol(Color hoverCol) {this.hoverCol = hoverCol;}
	public Color getCursorCol() {return cursorCol;}
	public void setCursorCol(Color cursorCol) {this.cursorCol = cursorCol;}
	
	
	
	
}
