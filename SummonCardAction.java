/*
 * Brendan Aucoin
 *04/27/2020
 *summons a card to the board. it doesnt come from your hand necessarily it is 
 *just some card that you want to summon through an effect most likely
 * */
package actions;

import boards.Board;
import cards.Card;
import dueling.Field;
import dueling.Spot;

public class SummonCardAction extends Action{
	@Override
	public void performAction(Card card,Spot boardSpot,Board board,Field field) {
		if(boardSpot.isOpen()) {
			boardSpot.setCard(card);
			field.addCardToField(card);
			boardSpot.setOpen(false);
			board.buffCard(card);
			card.setFirstTurn(true);
			card.setRevealed(true);
			card.setUsedAction(true);
		}
	}
}
