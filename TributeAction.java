/*
 *Brendan Aucoin
 *03/30/2020
 *performs the action for tributing a card
 * */
package actions;

import boards.Board;
import cards.Monster;
import cards.Token;
import dueling.Field;
import dueling.Spot;
import player.Duelist;
import states.DuelingState;

public class TributeAction extends Action{
	/*you can only tribute if the player hasnt played a card
	 * increment the number of tributes for the player
	 * and remove the card from the board*/
	@Override
	public void performAction(Monster card,Duelist player,Spot tributeSpot,Board board,Field field) {
		if(!player.hasPlayedCard() && !(card instanceof Token)) {
			player.setNumTributes(player.getNumTributes()+1);
			DuelingState.actionHandler.getAction(ActionList.REMOVE_CARD).performAction(tributeSpot, board, field);
		}
	}
}
