/*
 *Brendan Aucoin
 *07/06/2019
 *holds the name of a deck and the list of cards in the deck
 *can shuffle and draw
 * */
package dueling;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.LinkedList;

import cards.Card;
import player.Player;

public class Deck extends CardHolder{
	private String name;
	public static final int MAX_SIZE = 40;
	private int capacity;
	public Deck(String name) {
		super();
		this.name = name;
		this.capacity = 0;
	}
	public Deck(Deck d,String name) {
		super(d);
		this.name = name;
		this.capacity = d.capacity;
	}
	/*randomizes the order of the deck*/
	public void shuffle() {
		Collections.shuffle(getDeck());
	}
	
	/* add a card to the deck if the size is less than 40 and you dont have the max amount of that card already
	 * if so add the card to the deck and update the frequency of that card in the map then return true */
	@Override
	public boolean addCard(Card c) {
		if(canAddCard(c)) {
			//getDeck().add(c);
			//updateCardFrequency(c);
			super.addCard(c);
			capacity += c.getCapacity();
			return true;
		}
		return false;
	}
	@Override
	public void removeCard(Card c) {
		super.removeCard(c);
		capacity-=c.getCapacity();
	}
	
	
	public boolean canAddCard(Card c) {
		return getDeck().size() < MAX_SIZE && (getCardFrequency().get(c.getName()) == null || getCardFrequency().get(c.getName()) < c.getMaxFrequency());
	}
	public void createDeck(String filePath) throws FileNotFoundException {
		createList(filePath);
		createCapacity();
	}
	
	/*updates the specific frequency for whatver card you pass in*/
	@Override
	public void updateCardFrequency(Card c) {
		Integer val = getCardFrequency().get(c.getName());
		if(val == null) {
			getCardFrequency().put(c.getName(),1);
		}
		else {
			if(val < c.getMaxFrequency()) {
				getCardFrequency().put(c.getName(), val + 1);
			}
		}
	}
	/*creates a deck based on the text in a file you give it the path too*/
	/*public void createDeck(String filePath) throws FileNotFoundException{
		File file = new File(filePath);
		Scanner tempDeckScanner = new Scanner(file);
		while(tempDeckScanner.hasNextLine()) {
			String line  = tempDeckScanner.nextLine();
			CardList cardName = CardList.valueOf(line.toUpperCase());
			try {
				Card card = CardHandler.getCard(cardName);
				addCard(card);
			} catch (CloneNotSupportedException e) {
				System.err.print("Card was not able to be created");
				System.exit(0);
				}
			}
	}*/
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
	
	public int createCapacity() {
		return getDeck().stream().map(x -> x.getCapacity()).reduce(0, Integer::sum);
	}
	
	/*returns all of your cards printed on seperate lines*/
	public String toString() {
		String result = "";
		for(int i =0; i < this.getDeck().size();i++) {
			result += getDeck().get(i).getName() + "\n";
		}
		return result;
	}
	/*getters and setters*/
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	private LinkedList<Card> getDeck() {return getList();}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
		if(this.capacity == 0) {this.capacity = 0;}
	}
	public int getCapacity() {return capacity;}
	
}
