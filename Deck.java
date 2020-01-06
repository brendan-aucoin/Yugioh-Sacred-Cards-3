/*
 *Brendan Acoin
 *07/06/2019
 *holds the name of a deck and the list of cards in the deck
 *can shuffle and draw
 * */
package dueling;

import java.util.Collections;
import java.util.LinkedList;

import cards.Card;

public class Deck {
	private String name;
	private LinkedList<Card> deck;
	
	public Deck(String name) {
		deck = new LinkedList<Card>();
	}

	public void shuffle() {
		Collections.shuffle(deck);
	}
	
	public Card draw() {
		Card c = deck.removeFirst();
		return c;
	}
	
	public boolean lost() {
		return deck.size() == 0;
	}
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	public LinkedList<Card> getDeck() {return deck;}
	public void setDeck(LinkedList<Card> deck) {this.deck = deck;}
	
	
}
