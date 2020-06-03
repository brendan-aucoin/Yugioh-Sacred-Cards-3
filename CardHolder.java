/*
 *Brendan Aucoin
 *03/17/2020
 *holds the list of cards and the frequency of that card in the list
 */
package dueling;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

import cards.Card;
import cards.CardHandler;
import cards.CardList;
//should make abstract
public class CardHolder {
	private LinkedList<Card> list;
	private Map<String,Integer> cardFrequency;//maps the cards name to the amount of that card in the list
	
	public CardHolder() {
		list = new LinkedList<Card>();
		cardFrequency = new HashMap<String,Integer>();
	}
	public CardHolder(CardHolder ch) {
		this.list = new LinkedList<Card>();
		//you must add the clone of the card
		for(int i =0; i < ch.list.size();i++) {
			try {this.list.add(ch.list.get(i).clone());} catch (CloneNotSupportedException e) {e.printStackTrace();}
		}
		this.cardFrequency = new HashMap<String,Integer>(ch.getCardFrequency());
	}
	
	/*goes through every card in the list and adds it to the map*/
	public void createCardFrequency() {
		cardFrequency.clear();
		for(int i =0; i < list.size(); i++) {
			Integer val = cardFrequency.get(list.get(i).getName());
			if(val == null) {
				cardFrequency.put(list.get(i).getName(),1);
			}
			else {
				cardFrequency.put(list.get(i).getName(), val + 1);
			}
		}
	}
	/*updates the specific frequency for whatver card you pass in*/
	public void updateCardFrequency(Card c) {
		Integer val = cardFrequency.get(c.getName());
		if(val == null) {
			cardFrequency.put(c.getName(),1);
		}
		else {
			cardFrequency.put(c.getName(), val + 1);
		}
	}
	/*creates a deck based on the text in a file you give it the path too*/
	public void createList(String filePath) throws FileNotFoundException{
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
			}
		}
	}
	public boolean addCard(Card c) {
		list.add(c);
		updateCardFrequency(c);
		return true;
	}
	public void removeCard(Card c) {
		list.remove(c);
		Integer val = cardFrequency.get(c.getName());
		if(val == null || val == 0) {
			cardFrequency.put(c.getName(),0);
		}
		else {
			cardFrequency.put(c.getName(), val - 1);
		}
	}
	/*returns the size of the list*/
	public int size() {
		return list.size();
	}
	/*a string representation of every card in that list*/
	public String toString() {
		String result = "";
		for(int i =0; i < list.size();i++) {
			result += list.get(i).toString() + "\n";
		}
		return result;
	}
	
	public int getFrequency(Card c) {
		if(cardFrequency.get(c.getName()) == null) {return 0;}
		return cardFrequency.get(c.getName());
	}
	public Card get(int index) {
		return list.get(index);
	}
	/*getters and setters*/
	public void setList(LinkedList<Card> list) {this.list = list;}
	public LinkedList<Card> getList(){return list;}
	public Map<String, Integer> getCardFrequency() {return cardFrequency;}
	public void setCardFrequency(Map<String, Integer> cardFrequency) {this.cardFrequency = cardFrequency;}
	
}
