/*
 *Brendan Aucoin
 *06/30/2019
 *The player holds all information about the players position, and all the cards, money and other info the player needs
 */
package player;

import java.util.ArrayList;

import dueling.Deck;

public class Player extends Duelist{
	public static final int STARTING_CAPACITY = 10000;
	private int totalCapacity;
	private ArrayList<Deck> decks;
	private double money;
	public static final double STARTING_MONEY = 5000.0;
	//make area enum later
	//add instance variables for overworld
	public Player(String name) {
		super(name);
		this.totalCapacity = STARTING_CAPACITY;
		decks = new ArrayList<Deck>();
		this.money = STARTING_MONEY;
	}
	
	public void addMoney(double money) {
		setMoney(getMoney() + money);
	}
	
	
	public int getTotalCapacity() {return totalCapacity;}
	public void setTotalCapacity(int totalCapacity) {this.totalCapacity = totalCapacity;}
	public ArrayList<Deck> getDecks() {return decks;}
	public void setDecks(ArrayList<Deck> decks) {this.decks = decks;}
	public void setMoney(double money) {this.money = money;}
	public double getMoney() {return money;}
	
}
