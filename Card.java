/*
 *Brendan Acoin
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

public abstract class Card {
	private BufferedImage image;
	private String name;
	private int capacity;//how much deck capacity they take up
	private double price;//their in game price
	private boolean usedAction;//if the card has done something on a turn
	private CardType type;//ie magic,trap,monster
	private int cardId;//each cards unique id code. this will be the number that it is in the sprite sheet.
	protected FileLoader fileLoader;
	protected Scanner scanner;
	public Card(int cardId,CardType type) throws FileNotFoundException {
		this.cardId = cardId;
		this.type = type;
		
		this.image = Texture.cardSprites[cardId];
		init();
	}
	
	public Card(Card card) throws FileNotFoundException {
		this.image = card.image;
		this.name = card.name;
		this.capacity = card.capacity;
		this.type = card.type;
		this.cardId = card.cardId;
		fileLoader = new FileLoader();
		init();
	}
	
	private void init() throws FileNotFoundException {
		fileLoader = new FileLoader();
		fileLoader.setFileName(Game.TEXTS_PATH + Game.CARD_STATS);
		createBaseStats();
		normalizeName();
	}
	public void normalizeName() {
		name = name.replace("_", " ");
	}
	protected Scanner createBaseStats() throws FileNotFoundException {
		scanner = new Scanner(fileLoader.getFile());
		Scanner word = null;
		int lineNum = 0;
		while(scanner.hasNextLine()) {
			String line = scanner.nextLine();
				if(lineNum == cardId) {	
				word = new Scanner(line);
				setName(word.next());
				setCapacity(word.nextInt());
				setPrice(word.nextDouble());
				return word;
			}
			lineNum++;
		}
		return null;
	}
	
	public String toString() {
		return type.getName() + ": " +  name;
	}
	
	//two cards are equal if their unique id match
	public boolean equals(Card card) {
		if(this == card) {return true;}
		else if(this.cardId == card.cardId) {return true;}
		return false;
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
	
}
