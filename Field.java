/*
 *Brendan Aucoin
 *07/06/2019
 *holds the lists for the monsters and magic cards on one persons field and a list of all the cards
 * */
package dueling;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;

import cards.Card;
import cards.CompareByAttack;
import cards.CompareByDefense;
import cards.MagicCard;
import cards.Monster;

public class Field {
	private ArrayList<Card> field;
	private ArrayList<Spot> monsterSpots;
	private ArrayList<Spot> magicSpots;
	private Rectangle bounds;
	private ArrayList<Monster> monsterCards;
	private ArrayList<MagicCard> magicCards;
	public Field(Rectangle bounds) {
		field = new ArrayList<Card>();
		monsterSpots = new ArrayList<Spot>();
		magicSpots = new ArrayList<Spot>();
		monsterCards = new ArrayList<Monster>();
		magicCards = new ArrayList<MagicCard>();
		this.bounds = bounds;
	}
	/*adds a card to the field and adds the card casted as a monster to the monsters list 
	 * and the card casted as a magic card to the magic cards list*/
	public void addCardToField(Card c) {
		field.add(c);
		if(c instanceof Monster) {
			monsterCards.add((Monster)c);
		}
		else if(c instanceof MagicCard) {
			magicCards.add((MagicCard)c);
		}
	}
	/*same thing as addCardToField but removing it from all 3 lists*/
	public void removeCardFromField(Card c) {
		field.remove(c);
		if(c instanceof Monster) {
			monsterCards.remove(c);
		}
		else if(c instanceof MagicCard) {
			magicCards.remove(c);
		}
	}
	
	/*returns the card with the highestAttack on the field*/
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
	/*returns the card with the highest defense on the field*/
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
	/*a sorted version of the monsters array based on attack*/
	public ArrayList<Monster> getSortedMonstersByAttack(){
		ArrayList<Monster> tempCards = new ArrayList<Monster>(monsterCards);
		Collections.sort(tempCards,new CompareByAttack());
		return tempCards;
	}
	
	public ArrayList<Monster> getRevealedSortedMonstersByAttack(){
		ArrayList<Monster> tempCards = new ArrayList<Monster>();
		ArrayList<Monster> sortedMonsters = getSortedMonstersByAttack();
		for(int i =0; i < sortedMonsters.size();i++) {
			if(sortedMonsters.get(i).isRevealed()) {
				tempCards.add(sortedMonsters.get(i));
			}
		}
		return tempCards;		
	}
	/*a sorted version of the monsters array based on defense*/
	public ArrayList<Monster> getSortedMonstersByDefense(){
		ArrayList<Monster> tempCards = new ArrayList<Monster>(monsterCards);
		Collections.sort(tempCards,new CompareByDefense());
		return tempCards;
	}
	/*returns all the spots with monsters in them*/
	public ArrayList<Spot> getFilledMonsterSpots(){
		ArrayList<Spot> tempSpots = new ArrayList<Spot>();
		for(int i =0; i< monsterSpots.size(); i++) {
			if(monsterSpots.get(i).getCard() != null) {
				tempSpots.add(monsterSpots.get(i));
			}
		}
		return tempSpots;
	}
	/*returns a sorted version of the spots array for all the monsters*/
	public ArrayList<Spot> getSortedMonsterSpots(){
		ArrayList<Spot> tempSpots = new ArrayList<Spot>(getFilledMonsterSpots());
		Collections.sort(tempSpots,new CompareSpotByCardAttack());
		return tempSpots;
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
	public void setMonsterCards(ArrayList<Monster> monsterCards) {this.monsterCards = monsterCards;}
	public void setMagicCards(ArrayList<MagicCard> magicCards) {this.magicCards = magicCards;}
	public ArrayList<Monster> getMonsterCards(){return monsterCards;}
	public ArrayList<MagicCard> getMagicCards(){return magicCards;}
}
