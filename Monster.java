/*
 *Brendan Acoin
 *06/30/2019
 *a basic monster card with no effect
 */
package cards;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import attributes.Attribute;
import types.CardType;

public  class Monster extends Card{
	private int attack;
	private int defense;
	private int tributeCost;
	private boolean inDefense;//if they are set in defense mode for the turn
	
	private Attribute attribute;//only monsters have an attribute
	private ArrayList<Attribute> weaknesses;//they might have multiple weaknesses like in pokemon
	
	public Monster(int cardId,Attribute attribute) throws FileNotFoundException {
		super(cardId,CardType.MONSTER);
		this.attribute = attribute;
		init();
	}
	
	public Monster(Monster m) throws FileNotFoundException {
		super(m);
		this.attack = m.attack;
		this.defense = m.defense;
		this.tributeCost = m.tributeCost;
		this.inDefense = m.inDefense;
		this.attribute = m.attribute;
		this.weaknesses = m.weaknesses;
		init();
	}
	
	protected void init() throws FileNotFoundException {
		weaknesses = new ArrayList<Attribute>();
		createBaseStats();
		determineWeaknesses();
		normalizeName();
	}
	@Override
	protected Scanner createBaseStats() throws FileNotFoundException {
		Scanner word = super.createBaseStats();
		setAttack(word.nextInt());
		setDefense(word.nextInt());
		setTributeCost(word.nextInt());
		return null;
	}
	
	@Override
	public String toString() {
		return super.toString() + ", attack: " + attack + ", defense: " + defense;
	}
	/*the weaknesses for all monsters are pre determined*/
	private void determineWeaknesses() {
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
	

	public int getAttack() {return attack;}

	public void setAttack(int attack) {this.attack = attack;}

	public int getDefense() {return defense;}

	public void setDefense(int defense) {this.defense = defense;}

	public int getTributeCost() {return tributeCost;}

	public void setTributeCost(int tributeCost) {this.tributeCost = tributeCost;}

	public boolean isInDefense() {return inDefense;}

	public void setInDefense(boolean inDefense) {this.inDefense = inDefense;}

	public Attribute getAttribute() {return attribute;}

	public void setAttribute(Attribute attribute) {this.attribute = attribute;}

	public ArrayList<Attribute> getWeaknesses() {return weaknesses;}

	public void setWeaknesses(ArrayList<Attribute> weaknesses) {this.weaknesses = weaknesses;}
	
	
	
}
