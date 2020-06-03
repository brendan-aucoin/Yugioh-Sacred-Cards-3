package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class TextButton extends Button{
	public TextButton(Rectangle bounds,Color baseCol,Color hoverCol,String text,Font font,Consumer<MouseEvent> mousePressed) {
		super(bounds,baseCol,hoverCol,text,font,mousePressed);
	}
	
	@Override
	public void render(Graphics2D g) {
		super.render(g);
		g.drawString(getText(),getBounds().x , getBounds().y + getBounds().height);
	}
}
