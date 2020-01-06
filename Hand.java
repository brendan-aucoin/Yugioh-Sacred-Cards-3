/*
 *Brendan Acoin
 *07/06/2019
 *holds a list of cards in your hand and the spots that will appear on the screen
 * */
package dueling;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import cards.Card;

public class Hand {
	private ArrayList<Card> hand;
	private ArrayList<Spot> spots;
	private Rectangle bounds;
	public static final int MAX_CARDS = 5;
	public Hand() {
		hand = new ArrayList<Card>();
		spots = new ArrayList<Spot>();
	}
	
	public void draw(Deck deck) {
		if(hand.size() < MAX_CARDS) {
			Card c = deck.draw();
			for(int i=0;i < spots.size();i++) {
				if(spots.get(i).isOpen()) {
					spots.get(i).setCard(c);
					spots.get(i).setOpen(false);
					hand.add(c);
					break;
				}
			}
		}
		
	}
	
	public void removeCard(Spot spot) {
		
		Card c = spot.getCard();
		spot.setOpen(true);
		spot.setCard(null);
		hand.remove(c);
		
	}
	public void render(Graphics2D g) {
		g.setColor(new Color(178,34,34));
		g.fill(bounds);
		g.setColor(Color.YELLOW);
		renderSpots(g);
	}
	public void init() {
		createSpots();
	}
	private void createSpots() {
		for(int i =0; i < MAX_CARDS; i++) {
			Spot s = new Spot(new Rectangle(
			getBounds().width/15 + getBounds().x + (((getBounds().width/15) *3) * i),getBounds().y + getBounds().height/10,
			getBounds().width/15, (int) (getBounds().height/1.35)
			));
			spots.add(s);
		}
	}
	private void renderSpots(Graphics2D g) {
		g.setStroke(new BasicStroke(1));
		for(int i =0 ; i < spots.size();i++) {
			g.draw(spots.get(i).getBounds());
		}
	}
	
	public ArrayList<Card> getHand() {return hand;}
	public void setHand(ArrayList<Card> hand) {this.hand = hand;}
	public ArrayList<Spot> getSpots() {return spots;}
	public void setSpots(ArrayList<Spot> spots) {this.spots = spots;}
	public void setBounds(Rectangle bounds) {this.bounds = bounds;}
	public Rectangle getBounds() {return bounds;}
}
