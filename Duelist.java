/*
 *Brendan Aucoin
 *03/30/2020
 *The super class of a player and an ai
 *the player has more data than just dueling a player also moves around and can talk to people
 *a duelist only has information for duels
 * */
package player;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Map;

import dueling.CardHolder;
import dueling.Deck;
import dueling.Hand;
import dueling.Spot;

public class Duelist {
	private String name;
	private int health;
	private CardHolder trunk;
	private ArrayList<Deck> decks;
	private Deck deck;
	private Hand hand;
	private int numTributes;
	private boolean playedCard;
	private double money;
	public static final double STARTING_MONEY = 5000.0;
	public static final int STARTING_HEALTH = 8000;
	
	public Duelist(String name) {
		this.name = name;
		health = STARTING_HEALTH;
		trunk = new CardHolder();
		decks = new ArrayList<Deck>();
		hand = new Hand();
		deck = new Deck("Deck # " + decks.size() + 1);//make basic deck
		money = STARTING_MONEY;
		//variables that are affected constantly during the duel but start out as 0, and false.
		numTributes = 0;
		playedCard =  false;
	}
	
	/*draw a card from your deck and put it in your hand*/
	public void draw() {
		hand.draw(deck);
	}
	
	/*randomize the order of cards in your deck*/
	public void shuffle() {
		deck.shuffle();
	}
	
	/*draw a certain amount of cards from your hand for instance at the begining of every duel you draw 5 cards*/
	public void draw(int amount) {
		for(int i =0; i < amount; i++) {
			hand.draw(deck);
		}
	}
	
	public void addMoney(double money) {
		setMoney(getMoney() + money);
	}
	
	/*remove the card from your hand*/
	public void playCardFromHand(Spot spot) {
		hand.removeCard(spot);
	}
	
	/*create the hands bounds */
	public void createHandBounds(Rectangle bounds) {
		hand.setBounds(bounds);
	}
	
	/*getters and setters*/
	public Map<String,Integer> getCardFrequency(){return deck.getCardFrequency();}
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	public int getHealth() {return health;}
	public void setHealth(int health) {	this.health = health;}
	public CardHolder getTrunk() {return trunk;}
	public void setTrunk(CardHolder trunk) {this.trunk = trunk;}
	public ArrayList<Deck> getDecks() {return decks;}
	public void setDecks(ArrayList<Deck> decks) {this.decks = decks;}
	public Deck getDeck() {	return deck;}
	public void setDeck(Deck deck) {this.deck = deck;}
	public int getNumTributes() {return numTributes;}
	public void setNumTributes(int numTributes) {this.numTributes = numTributes;}
	public boolean hasPlayedCard() {return playedCard;}
	public void setPlayedCard(boolean playedCard) {this.playedCard = playedCard;}
	public Hand getHand() {return hand;}
	public void setHand(Hand hand) {this.hand = hand;}
	public void setMoney(double money) {this.money = money;}
	public double getMoney() {return money;}
}
