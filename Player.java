package player;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import cards.Card;
import dueling.Deck;
import dueling.Hand;
import dueling.Spot;

public class Player {
	private String name;
	private int health;
	private LinkedList<Card>trunk;
	private ArrayList<Deck> decks;
	private Deck deck;
	private Hand hand;
	private int numTributes;
	private boolean playedCard;
	private Map<Card,Integer> cardFrequency;
	//make area enum later
	
	public Player(String name) {
		this.name = name;
		health = 8000;
		trunk = new LinkedList<Card>();
		decks = new ArrayList<Deck>();
		hand = new Hand();
		deck = new Deck("Deck # " + decks.size() + 1);//make basic deck
		numTributes = 0;
		playedCard =  false;
		cardFrequency = new TreeMap<Card,Integer>();
	}

	public void draw() {
		hand.draw(deck);
	}
	public void shuffle() {
		deck.shuffle();
	}
	public void draw(int amount) {
		for(int i =0; i < amount; i++) {
			hand.draw(deck);
		}
	}
	
	public void playCardFromHand(Spot spot) {
		hand.removeCard(spot);
	}
	
	public void createHandBounds(Rectangle bounds) {
		hand.setBounds(bounds);
	}
	
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	public int getHealth() {return health;}
	public void setHealth(int health) {	this.health = health;}
	public LinkedList<Card> getTrunk() {return trunk;}
	public void setTrunk(LinkedList<Card> trunk) {this.trunk = trunk;}
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
	public Map<Card, Integer> getCardFrequency() {return cardFrequency;}
	public void setCardFrequency(Map<Card, Integer> cardFrequency) {this.cardFrequency = cardFrequency;}
	
}
