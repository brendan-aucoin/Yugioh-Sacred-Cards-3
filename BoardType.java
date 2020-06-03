package boards;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.image.BufferedImage;

import cards.Card;
import dueling.Field;
import images.NamedImage;
import images.Texture;
import player.Duelist;

public abstract class BoardType {
	private BufferedImage evenTileImage,oddTileImage,background;
	private Color draggingSpotCol,fourCornerHoverCol,textCol,middleAreaBackCol,middleAreaLineCol;
	public static final BasicStroke MIDDLE_AREA_STROKE = new BasicStroke(6);
	public BoardType(String boardName,Color draggingSpotCol,Color fourCornerHoverCol,Color textCol,Color middleAreaBackCol,Color middleAreaLineCol) {
		this.draggingSpotCol = draggingSpotCol;
		this.fourCornerHoverCol = fourCornerHoverCol;
		this.textCol = textCol;
		this.middleAreaBackCol = middleAreaBackCol;
		this.middleAreaLineCol = middleAreaLineCol;
		loadImages(boardName);
	}
	
	private void loadImages(String boardName) {
		for(int i =0; i < Texture.boardImages.size();i++) {
			NamedImage img = Texture.boardImages.get(i);
			if(img.getName().equals(boardName + "_board_" + "even_tile")) {this.evenTileImage = img.getImage();	}
			else if(img.getName().equals(boardName + "_board_" + "odd_tile")) {this.oddTileImage = img.getImage();	}
			else if(img.getName().equals(boardName + "_board_" + "background")) {this.background = img.getImage();}	
		}
	}
	
	public abstract void buffCard(Card c,Duelist player,Duelist opponent,Field playerField,Field opponentField,Board board);
	
	public abstract void updateBoard(Duelist player,Duelist opponent,Field playerField,Field opponentField,Board board);
	
	public boolean equals(BoardType type) {
		if(this == type) {return true;}
		else if(this.evenTileImage.equals(type.evenTileImage) && this.oddTileImage.equals(type.oddTileImage) && this.background.equals(type.background)) {return true;}
		return false;
	}
	
	public BufferedImage getEvenTileImage() {return evenTileImage;}
	public void setEvenTileImage(BufferedImage evenTileImage) {this.evenTileImage = evenTileImage;}
	public BufferedImage getOddTileImage() {return oddTileImage;}
	public void setOddTileImage(BufferedImage oddTileImage) {this.oddTileImage = oddTileImage;}
	public BufferedImage getBackground() {return background;}
	public void setBackground(BufferedImage background) {this.background = background;}
	public Color getDraggingSpotCol() {return draggingSpotCol;}
	public void setDraggingSpotCol(Color draggingSpotCol) {this.draggingSpotCol = draggingSpotCol;}
	public Color getFourCornerHoverCol() {return fourCornerHoverCol;}
	public void setFourCornerHoverCol(Color fourCornerHoverCol) {this.fourCornerHoverCol = fourCornerHoverCol;}
	public Color getTextCol() {return textCol;}
	public void setTextCol(Color textCol) {this.textCol = textCol;}
	public Color getMiddleAreaBackCol() {return middleAreaBackCol;}
	public void setMiddleAreaBackCol(Color middleAreaBackCol) {this.middleAreaBackCol = middleAreaBackCol;}
	public Color getMiddleAreaLineCol() {return middleAreaLineCol;}
	public void setMiddleAreaLineCol(Color middleAreaLineCol) {this.middleAreaLineCol = middleAreaLineCol;}
}
