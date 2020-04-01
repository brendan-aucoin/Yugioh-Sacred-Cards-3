/*
 *Brendan Aucoin
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
	public static final int MAX_CARDS = 5;//you can only ever have a max of 5 cards in your hand
	public Hand() {
		hand = new ArrayList<Card>();
		spots = new ArrayList<Spot>();
	}
	
	public void init() {
		createSpots();
	}
	
	/*if your hand isnt full then go through all the spots in your hand and find the first open one and add the card you
	 * drew from the deck into your hand and set the spots card to whatever you drew and the availability to false*/
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
	/*remove card from your list and set the spots availability to true and the spots card to null*/
	public void removeCard(Spot spot) {
		Card c = spot.getCard();
		spot.setOpen(true);
		spot.setCard(null);
		hand.remove(c);
	}
	
	/*render all the spots and all the cards in those spots*/
	public void render(Graphics2D g) {
		g.setColor(new Color(178,34,34));
		g.fill(bounds);
		g.setColor(Color.YELLOW);
		renderSpots(g);
	}
	
	/*add all the spots you create to the spots list*/
	private void createSpots() {
		for(int i =0; i < MAX_CARDS; i++) {
			Spot s = new Spot(new Rectangle(
			getBounds().width/15 + getBounds().x + (((getBounds().width/15) *3) * i),getBounds().y + getBounds().height/10,
			getBounds().width/15, (int) (getBounds().height/1.35)
			));
			spots.add(s);
		}
	}
	/*draw the outline of a spot*/
	private void renderSpots(Graphics2D g) {
		g.setStroke(new BasicStroke(2));
		for(int i =0 ; i < spots.size();i++) {
			g.draw(spots.get(i).getBounds());
		}
	}
	
	/*getters and setters*/
	public ArrayList<Card> getHand() {return hand;}
	public void setHand(ArrayList<Card> hand) {this.hand = hand;}
	public ArrayList<Spot> getSpots() {return spots;}
	public void setSpots(ArrayList<Spot> spots) {this.spots = spots;}
	public void setBounds(Rectangle bounds) {this.bounds = bounds;}
	public Rectangle getBounds() {return bounds;}
}
