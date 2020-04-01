/*
 *Brendan Aucoin
 *07/06/2019
 *holds the name of a deck and the list of cards in the deck
 *can shuffle and draw
 * */
package dueling;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

import cards.Card;
import cards.CardHandler;
import cards.CardList;

public class Deck extends CardHolder{
	private String name;
	public static final int MAX_SIZE = 40;
	public Deck(String name) {
		super();
		this.name = name;
	}
	public Deck(Deck d,String name) {
		super(d);
		this.name = name;
	}
	/*randomizes the order of the deck*/
	public void shuffle() {
		Collections.shuffle(getDeck());
	}
	/*
	 * add a card to the deck if the size is less than 40 and you dont have the max amount of that card already
	 * if so add the card to the deck and update the frequency of that card in the map then return true
	 * */
	public boolean addCard(Card c) {
		if(getDeck().size() < MAX_SIZE && (getCardFrequency().get(c.getName()) == null || getCardFrequency().get(c.getName()) < c.getMaxFrequency())) {
			getDeck().add(c);
			updateCardFrequency(c);
			return true;
		}
		return false;
	}
	
	public void createDeck(String filePath) throws FileNotFoundException{
		File file = new File(filePath);
		Scanner tempDeckScanner = new Scanner(file);
		while(tempDeckScanner.hasNextLine()) {
			String line  = tempDeckScanner.nextLine();
			CardList cardName = CardList.valueOf(line.toUpperCase());
			Card card = CardHandler.getCard(cardName);
			try {
				addCard(card.clone());
			} catch (CloneNotSupportedException e) {
				System.err.print("Card was not able to be created");
				System.exit(0);
			}
				//this is if you want all your cards as a single arraylist.
				/*for(int i =0; i < allCards.size();i++) {
					String cardName = allCards.get(i).getName().replace(" ", "_");
					if(line.equalsIgnoreCase(cardName)) {
						 try {
							tempDeck.addCard(allCards.get(i).clone());
						} catch (CloneNotSupportedException e) {
							e.printStackTrace();
						}
					}
				}*/
			}
	}
	/*remove the first card from the deck and return it*/
	public Card draw() {
		if(empty()) {
			return null;
		}
		Card c = getDeck().removeFirst();
		return c;
	}
	
	/*returns if the deck is empty*/
	public boolean empty() {
		return size() == 0;
	}
	
	/*getters and setters*/
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	private LinkedList<Card> getDeck() {return getList();}
	
}
