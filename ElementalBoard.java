package boards;

import java.awt.Color;

import attributes.Attribute;
import cards.Card;
import cards.Monster;
import cards.TrapCard;
import dueling.Field;
import player.Duelist;

public class ElementalBoard extends BoardType{
	private Attribute attribute;
	public ElementalBoard(Attribute attribute) {
		super(
		attribute.getName().toLowerCase(),
		new Color(255,215,0),new Color(0, 102,204),Color.BLACK,new Color(205,92,92),Color.WHITE);
		this.attribute = attribute;
	}
	
	public void buffCard(Card c,Duelist player,Duelist opponent,Field playerField,Field opponentField,Board board) {
		if(c instanceof Monster) {
			Monster m = (Monster)c;
			if(m.getAttribute() == attribute) {
				m.setAttack(m.getAttack() + Monster.BUFF);
			}
		}
	}
	
	public void updateBoard(Duelist player,Duelist opponent,Field playerField,Field opponentField,Board board) {
		//update player field
		updateField(player,opponent,playerField,opponentField,board);
		//update opponent field
		updateField(opponent,player,opponentField,playerField,board);
	}
	
	private void updateField(Duelist player,Duelist opponent,Field playerField,Field opponentField,Board board) {
		for(int i=0; i < playerField.getMonsterSpots().size();i++) {
			Card c = playerField.getMonsterSpots().get(i).getCard();
			if(c == null) {continue;}
			if(c instanceof Monster) {
				Monster m = (Monster)c;
				buffCard(m,player,opponent,playerField,opponentField,board);
			}
		}
	}
	public boolean equals(ElementalBoard type) {
		return super.equals(type) && (this.attribute.equals(type.attribute));
	}
	public Attribute getAttribute() {return attribute;}
	public void setAttribute(Attribute attribute) {this.attribute = attribute;}
	
	
}
