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
import player.Duelist;

public class SummonCardAction extends Action{
	@Override
	public void performAction(Card card,Spot boardSpot,Duelist player,Duelist opponent,Board board,Field playerField,Field opponentField) {
		if(boardSpot.isOpen()) {
			boardSpot.setCard(card);
			playerField.addCardToField(card);
			boardSpot.setOpen(false);
			board.buffCard(card,player,opponent,playerField,opponentField);
			card.setFirstTurn(true);
			card.setRevealed(true);
			card.setUsedAction(true);
		}
	}
}
