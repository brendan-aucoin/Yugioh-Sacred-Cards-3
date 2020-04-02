/*
 *Brendan Aucoin
 *06/30/2019
 *the super class for all cards holds all general data that all cards have
 */
package cards;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.util.Scanner;

import file_loader.FileLoader;
import game.Game;
import images.Texture;
import types.CardType;

public abstract class Card implements Cloneable, Comparable<Card>{
	private BufferedImage image;
	private String name;
	private int capacity;//how much deck capacity they take up
	private double price;//their in game price
	private boolean usedAction;//if the card has done something on a turn
	private CardType type;//ie magic,trap,monster
	private int cardId;//each cards unique id code. this will be the number that it is in the sprite sheet.
	protected FileLoader fileLoader;
	protected Scanner scanner;
	private int maxFrequency;//how many of this card can appear in your deck. i.e you can only have 1 change of heart in your deck. but you can have 3 dorons.
	
	public Card(int cardId,CardType type) throws FileNotFoundException {//you must throw fileNotFoundException because all cards read from a text file to get their base stats
		this.cardId = cardId;
		this.type = type;
		this.image = Texture.cardSprites[cardId];
		this.maxFrequency = 3;//almost all cards have a frequency of 3
		init();
	}
	
	public Card(Card card) {
		this.image = card.image;
		this.name = card.name;
		this.capacity = card.capacity;
		this.price = card.price;
		this.type = card.type;
		this.cardId = card.cardId;
		fileLoader = new FileLoader();
		this.maxFrequency = 3;
		//init();
	}
	
	/*initialize the fileloader and create the base stats*/
	private void init() throws FileNotFoundException {
		usedAction = false;
		fileLoader = new FileLoader();
		fileLoader.setFileName(Game.TEXTS_PATH + Game.CARD_STATS);
		createBaseStats();
		normalizeName();
	}
	/*this is for reading from the file because the Cards name has spaces in it becuase its easier to read but the file will contain _ ie change_of_heart*/
	public void normalizeName() {
		name = name.replace("_", " ");
	}
	/*reads using the file loader and reads the name, capacity, and price into the cards instance variables. this is because
	 * every card has a name, capacity, and a price monsters, traps and spells all do*/
	protected Scanner createBaseStats() throws FileNotFoundException {
		scanner = new Scanner(fileLoader.getFile());//a scanner to go through every line of the file
		Scanner word = null;//a scanner to go through every word (seperated by a space) of each line
		int lineNum = 0;
		while(scanner.hasNextLine()) {//go through every line
			String line = scanner.nextLine();
			if(lineNum == cardId) {	//
				word = new Scanner(line);//create a new scanner for the specific line
				//word.next will give the next word after seperated by a space
				setName(word.next());
				setCapacity(word.nextInt());
				setPrice(word.nextDouble());
				return word;
			}
			lineNum++;
		}
		return null;
	}
	/*return a string of the cards name followed by their type*/
	public String toString() {
		return name + "," + type.getName();
	}
	
	//two cards are equal if their unique id match
	public boolean equals(Card card) {
		if(this == card) {return true;}
		else if(this.cardId == card.cardId) {return true;}
		return false;
	}
	
	@Override
    public Card clone() throws CloneNotSupportedException 
    { 
        return (Card) super.clone(); 
    }
	@Override
	public int compareTo(Card c) {
		if(this.cardId > c.cardId) {return 1;}
		else if(this.cardId < c.cardId) {return -1;}
		else {return 0;}
	}
	
	/*getters and setters*/
	public BufferedImage getImage() {return image;}
	public void setImage(BufferedImage image) {this.image = image;}
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	public int getCapacity() {return capacity;}
	public void setCapacity(int capacity) {this.capacity = capacity;}
	public double getPrice() {return price;}
	public void setPrice(double price) {this.price = price;}
	public CardType getType() {	return type;}
	public void setType(CardType type) {this.type = type;}
	public int getCardId() {return cardId;}
	public void setCardId(int cardId) {this.cardId = cardId;}
	public FileLoader getFileLoader() {return fileLoader;}
	public void setFileLoader(FileLoader loader) {this.fileLoader = loader;}
	public boolean hasUsedAction() {return usedAction;}
	public void setUsedAction(boolean usedAction) {this.usedAction = usedAction;}
	public void setMaxFrequency(int maxFrequency) {this.maxFrequency = maxFrequency;}
	public int getMaxFrequency() {return maxFrequency;}

	
}
