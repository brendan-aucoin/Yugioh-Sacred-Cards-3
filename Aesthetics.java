package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Aesthetics {
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
	  
	  /*if you are over a certain bounds change its colour of the graphics*/
	  protected void changeColour(Graphics2D g,int mouseX,int mouseY,Rectangle bounds,Color newCol,Color oldCol)
	 {
	  if(mouseOver(mouseX,mouseY,bounds.x,bounds.y,bounds.width,bounds.height)) {g.setColor(newCol);}
	  else {g.setColor(oldCol);}
	 }
	  /*if you are over a certain bounds change its colour of the graphics*/
	  protected void changeColour(Graphics2D g,int mouseX,int mouseY,int x,int y, int width,int height,Color newCol,Color oldCol)
	 {
	  if(mouseOver(mouseX,mouseY,x,y,width,height)) {g.setColor(newCol);}
	  else {g.setColor(oldCol);}
	 }
	  /*if you are over a certain bounds change its colour of the graphics*/
	  protected void changeColour(Graphics g,int mouseX,int mouseY,Rectangle bounds,Color newCol,Color oldCol)
	 {
	  if(mouseOver(mouseX,mouseY,bounds.x,bounds.y,bounds.width,bounds.height)) {g.setColor(newCol);}
	  else {g.setColor(oldCol);}
	 }
	  /*if you are over a certain bounds change its colour of the graphics*/
	  protected void changeColour(Graphics g,int mouseX,int mouseY,int x,int y, int width,int height,Color newCol,Color oldCol)
	 {
	  if(mouseOver(mouseX,mouseY,x,y,width,height)) {g.setColor(newCol);}
	  else {g.setColor(oldCol);}
	 }
}
