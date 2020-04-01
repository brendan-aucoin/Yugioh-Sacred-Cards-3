/*
 *Brendan Aucoin
 *07/06/2019
 *holds the lists for the monsters and magic cards on one persons field and a list of all the cards
 * */
package dueling;

import java.awt.Rectangle;
import java.util.ArrayList;

import cards.Card;
import cards.MagicCard;
import cards.Monster;

public class Field {
	private ArrayList<Card> field;
	private ArrayList<Spot> monsterSpots;
	private ArrayList<Spot> magicSpots;
	private Rectangle bounds;
	private ArrayList<Card> monsterCards;
	private ArrayList<Card> magicCards;
	public Field(Rectangle bounds) {
		field = new ArrayList<Card>();
		monsterSpots = new ArrayList<Spot>();
		magicSpots = new ArrayList<Spot>();
		monsterCards = new ArrayList<Card>();
		magicCards = new ArrayList<Card>();
		this.bounds = bounds;
	}
	
	public void addCardToField(Card c) {
		field.add(c);
		if(c instanceof Monster) {
			monsterCards.add(c);
		}
		else if(c instanceof MagicCard) {
			magicCards.add(c);
		}
	}
	public void removeCardFromField(Card c) {
		field.remove(c);
		if(c instanceof Monster) {
			monsterCards.remove(c);
		}
		else if(c instanceof MagicCard) {
			magicCards.remove(c);
		}
	}
	
	public Card getHighestAttackCard() {
		int max = 0;
		Card maxCard = null;
		for(int i =0; i < monsterCards.size();i++) {
			Card c = monsterCards.get(i);
			if(c instanceof Monster) {
				Monster monster = (Monster)c;
				if(monster.getAttack() > max) {
					max = monster.getAttack();
					maxCard = monster;
				}
			}
		}
		return maxCard;
	}
	public Card getHighestDefenseCard() {
		int max = 0;
		Card maxCard = null;
		for(int i =0; i < monsterCards.size();i++) {
			Card c = monsterCards.get(i);
			if(c instanceof Monster) {
				Monster monster = (Monster)c;
				if(monster.getDefense() > max) {
					max = monster.getDefense();
					maxCard = monster;
				}
			}
		}
		return maxCard;
	}
	
	
	/*add a spot to the monster spots*/
	public void addSpotToMonsters(Spot sp) {
		monsterSpots.add(sp);
	}
	/*add a spot to the magic spots*/
	public void addSpotToMagic(Spot sp) {
		magicSpots.add(sp);
	}
	/*add a spot to the whichever list of spots you specify*/
	public void addSpotTo(Spot sp,ArrayList<Spot> list) {
		list.add(sp);
	}
	
	/*returns a list of all the monster and magic spots*/
	public ArrayList<Spot> allCardSpots(){
		ArrayList<Spot> allSpots= new ArrayList<Spot>();
		for(Spot spot : monsterSpots) {
			allSpots.add(spot);
		}
		for(Spot spot : magicSpots) {
			allSpots.add(spot);
		}
		return allSpots;
	}
	/*getters and setters*/
	public ArrayList<Spot> getMonsterSpots() {	return monsterSpots;}
	public void setMonsterSpots(ArrayList<Spot> monsterSpots) {this.monsterSpots = monsterSpots;}
	public ArrayList<Spot> getMagicSpots() {return magicSpots;}
	public void setMagicSpots(ArrayList<Spot> magicSpots) {this.magicSpots = magicSpots;}
	public void setBounds(Rectangle bounds) {this.bounds = bounds;}
	public Rectangle getBounds() {return bounds;}
	public ArrayList<Card> getField() {return field;}
	public void setField(ArrayList<Card> field) {this.field = field;}
	public ArrayList<Card> getMonsterCards() {return monsterCards;}
	public void setMonsterCards(ArrayList<Card> monsterCards) {this.monsterCards = monsterCards;}
	public ArrayList<Card> getMagicCards() {return magicCards;}
	public void setMagicCards(ArrayList<Card> magicCards) {this.magicCards = magicCards;}
	
}
