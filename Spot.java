/*
 *Brendan Acoin
 *06/30/2019
 *holds data for a rectangle if that rectangle is filled and what card is filling the rectangle.
 * */
package dueling;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import cards.Card;

public class Spot {
	private Rectangle bounds;
	private boolean open;
	private Card card;
	public Spot(Rectangle bounds) {
		this.bounds = bounds;
		open = true;
		card =null;
	}
	
	public void render(Graphics2D g) {
		if(card != null) {
			g.drawImage(card.getImage(), bounds.x,bounds.y,bounds.width,bounds.height,null);
		}
	}
	public String toString() {
		if(card != null) {
		return card.toString() + ", Available: " + open;
		}
		else {
			return "No card";
		}
	}
	
	public boolean hasCard() {
		if(card == null) {
			return false;
		}
		else {
			return true;
		}
	}
	public boolean equals(Spot spot) {
		if(this.bounds == spot.bounds && this.open == spot.open && this.card == spot.card) {
			return true;
		}
		else {
			return false;
		}
	}
	public Rectangle getBounds() {
		return bounds;
	}
	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}
	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
	}
	public Card getCard() {
		return card;
	}
	public void setCard(Card card) {
		this.card = card;
		if(card != null) {
			open = false;
		}
	}
	
	
}
