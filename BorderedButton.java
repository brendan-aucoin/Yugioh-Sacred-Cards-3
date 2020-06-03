package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class BorderedButton extends Button{
	private Color textCol,textHoverCol;
	private int arcWidth,arcHeight;
	public BorderedButton(Rectangle bounds,Color baseCol,Color hoverCol,String text,Color textCol,Color textHoverCol,Font font,Consumer<MouseEvent> mousePressed,int arcWidth,int arcHeight) {
		super(bounds,baseCol,hoverCol,text,font,mousePressed);
		this.textCol = textCol;
		this.arcWidth = arcWidth;
		this.arcHeight = arcHeight;
		this.textHoverCol = textHoverCol;
	}
	
	@Override
	public void render(Graphics2D g) {
		super.render(g);
		g.fillRoundRect(getBounds().x, getBounds().y, getBounds().width, getBounds().height, arcWidth, arcHeight);
		this.changeColour(g, getMouseX(), getMouseY(), getBounds(), textHoverCol, textCol);
		g.drawString(getText(), 
		(getBounds().x + getBounds().width/2) - (int)(getFont().getStringBounds(getText(), frc).getWidth()/2),
		(getBounds().y + getBounds().height/2) + (int)(getFont().getStringBounds(getText(), frc).getHeight()/6)
		);
	}

	public Color getTextCol() {return textCol;}
	public void setTextCol(Color textCol) {this.textCol = textCol;}
	public int getArcWidth() {return arcWidth;}
	public void setArcWidth(int arcWidth) {	this.arcWidth = arcWidth;}
	public int getArcHeight() {return arcHeight;}
	public void setArcHeight(int arcHeight) {this.arcHeight = arcHeight;}
	public Color getTextHoverCol() {return textHoverCol;}
	public void setTextHoverCol(Color textHoverCol) {this.textHoverCol = textHoverCol;}
	
}
