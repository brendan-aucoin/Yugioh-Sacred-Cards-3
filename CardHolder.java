/*
 *Brendan Acoin
 *03/17/2020
 *holds the list of cards and the frequency of that card in the list
 */
package dueling;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import cards.Card;
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
			if(val < c.getMaxFrequency()) {
				cardFrequency.put(c.getName(), val + 1);
			}
		}
	}
	//public abstract void addCard(Card c);
	//public abstract void removeCard(Card c);
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
	/*getters and setters*/
	public void setList(LinkedList<Card> list) {this.list = list;}
	protected LinkedList<Card> getList(){return list;}
	public Map<String, Integer> getCardFrequency() {return cardFrequency;}
	public void setCardFrequency(Map<String, Integer> cardFrequency) {this.cardFrequency = cardFrequency;}
	
}
