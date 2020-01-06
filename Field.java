/*
 *Brendan Acoin
 *07/06/2019
 *holds the lists for the monsters and magic cards on one persons field and a list of all the cards
 * */
package dueling;

import java.awt.Rectangle;
import java.util.ArrayList;

import cards.Card;

public class Field {
	private ArrayList<Card> field;
	private ArrayList<Spot> monsterSpots;
	private ArrayList<Spot> magicSpots;
	private Rectangle bounds;
	public Field(Rectangle bounds) {
		field = new ArrayList<Card>();
		monsterSpots = new ArrayList<Spot>();
		magicSpots = new ArrayList<Spot>();
		this.bounds = bounds;
	}
	
	
	public void addSpotToMonsters(Spot sp) {
		monsterSpots.add(sp);
	}
	
	public void addSpotToMagic(Spot sp) {
		magicSpots.add(sp);
	}
	
	public void addSpotTo(Spot sp,ArrayList<Spot> list) {
		list.add(sp);
	}
	
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
	public ArrayList<Card> getMonsterField() {return field;}
	public void setMonsterField(ArrayList<Card> field) {this.field = field;}
	public ArrayList<Spot> getMonsterSpots() {	return monsterSpots;}
	public void setMonsterSpots(ArrayList<Spot> monsterSpots) {this.monsterSpots = monsterSpots;}
	public ArrayList<Spot> getMagicSpots() {return magicSpots;}
	public void setMagicSpots(ArrayList<Spot> magicSpots) {this.magicSpots = magicSpots;}
	public void setBounds(Rectangle bounds) {this.bounds = bounds;}
	public Rectangle getBounds() {return bounds;}
	public ArrayList<Card> getField() {return field;}
	public void setField(ArrayList<Card> field) {this.field = field;}
	
}
