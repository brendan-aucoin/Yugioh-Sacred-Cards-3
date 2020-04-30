/*
 *Brendan Aucoin
 *06/30/2019
 *a basic monster card with no effect
 */
package cards;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import attributes.Attribute;
import types.CardType;

public abstract class Monster extends Card{
	private int attack;
	private int defense;
	private boolean inDefense;//if they are set in defense mode for the turn
	
	private Attribute attribute;//only monsters have an attribute
	private ArrayList<Attribute> weaknesses;//they might have multiple weaknesses like in pokemon
	private int tributeCost;
	public Monster(int cardId,Attribute attribute) throws FileNotFoundException {
		super(cardId,CardType.MONSTER);
		this.attribute = attribute;
		init();
	}
	
	public Monster(Monster m){
		super(m);
		this.attack = m.attack;
		this.defense = m.defense;
		this.attribute = m.attribute;
		this.weaknesses = m.weaknesses;
		inDefense = false;
		this.tributeCost = m.tributeCost;
		weaknesses = new ArrayList<Attribute>();//initialize the weakneses list
		determineWeaknesses();
		//init();
	}
	
	protected void init() throws FileNotFoundException {
		inDefense = false;
		weaknesses = new ArrayList<Attribute>();//initialize the weakneses list
		createBaseStats();
		determineWeaknesses();
		normalizeName();
	}
	@Override
	/*this fills in the rest of the stats for the monster from the file*/
	protected Scanner createBaseStats() throws FileNotFoundException {
		Scanner word = super.createBaseStats();//you have the scanner that you used in the create base stats method in the card class
		setAttack(word.nextInt());
		setDefense(word.nextInt());
		setTributeCost(word.nextInt());
		return null;
	}
	
	@Override
	/*a string representation of the card by the name, the type, attack, defense*/
	public String toString() {
		return super.toString() + ", attack: " + attack + ", defense: " + defense;
	}
	/*the weaknesses for all monsters are pre determined*/
	private void determineWeaknesses() {
		//right now every attribute only has one weakness but that may change
		if(attribute == Attribute.LIGHT) {weaknesses.add(Attribute.DARK);}
		else if(attribute == Attribute.DARK) {weaknesses.add(Attribute.DREAM);}
		else if(attribute == Attribute.FIEND) {weaknesses.add(Attribute.LIGHT);}
		else if(attribute == Attribute.DREAM) {weaknesses.add(Attribute.FIEND);}
		else if(attribute == Attribute.FIRE) {weaknesses.add(Attribute.WATER);}
		else if(attribute == Attribute.GRASS) {weaknesses.add(Attribute.FIRE);}
		else if(attribute == Attribute.WATER) {weaknesses.add(Attribute.ELECTRIC);}
		else if(attribute == Attribute.ELECTRIC) {weaknesses.add(Attribute.EARTH);}
		else if(attribute == Attribute.EARTH) {weaknesses.add(Attribute.WIND);}
		else if(attribute == Attribute.WIND) {weaknesses.add(Attribute.GRASS);}
		else if(attribute == Attribute.DIVINE) {weaknesses.add(Attribute.DIVINE);}
	}
	
	public boolean weakTo(Attribute attribute) {
		return (weaknesses.contains(attribute));
	}
	public boolean weakTo(Monster m) {
		return weakTo(m.getAttribute());
	}
	@Override
    public Monster clone() throws CloneNotSupportedException 
    { 
        return (Monster) super.clone(); 
    } 
	
	@Override
	public int compareTo(Card c) {
		if(c instanceof Monster) {
			Monster m = (Monster)c;
			if(this.attack > m.attack) {return 1;}
			else if(this.attack<m.attack) {return -1;}
			return 0;
		}
		return 0;
	}
	/*getters and setters*/
	public int getAttack() {return attack;}
	public void setAttack(int attack) {this.attack = attack;}
	public int getDefense() {return defense;}
	public void setDefense(int defense) {this.defense = defense;}
	public boolean isInDefense() {return inDefense;}
	public void setInDefense(boolean inDefense) {this.inDefense = inDefense;}
	public Attribute getAttribute() {return attribute;}
	public void setAttribute(Attribute attribute) {this.attribute = attribute;}
	public ArrayList<Attribute> getWeaknesses() {return weaknesses;}
	public void setWeaknesses(ArrayList<Attribute> weaknesses) {this.weaknesses = weaknesses;}
	public int getTributeCost() {return tributeCost;}
	public void setTributeCost(int tributeCost) {this.tributeCost = tributeCost;}
}
