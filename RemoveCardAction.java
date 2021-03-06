/*
 * Brendan Aucoin
 *03/30/2020
 *removes a card from the board and field that you pass in
 * */
package actions;

import boards.Board;
import dueling.Field;
import dueling.Spot;

public class RemoveCardAction extends Action{
	
	@Override
	public void performAction(Spot removeSpot,Board board,Field field) {
		field.removeCardFromField(removeSpot.getCard());
		board.removeCardFromBoard(removeSpot);
	}
}
