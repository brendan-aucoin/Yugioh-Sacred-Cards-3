/*
 *Brendan Aucoin
 *03/30/2020
 *makes a pop up appear with the spells text
 *then you actiavte the spells effect
 * */
package actions;

import boards.Board;
import cards.MagicCard;
import cards.SpellCard;
import dueling.Field;
import dueling.Spot;
import dueling.TextPopupPane;
import player.Duelist;
import states.DuelingState;

public class ActivateSpellAction extends Action{
	/*
	 * sets the popup pane
	 * and then activates the spells effect and removes it from the board
	 * */
	@Override
	public void performAction(SpellCard magicCard,Duelist player,Duelist opponent,Board board,Field playerField,Field opponentField,Spot spellSpot,TextPopupPane textPopupPane) {		
				textPopupPane.startNew(magicCard.effectText(),MagicCard.TEXT_POPUP_TIME);
				DuelingState.actionHandler.getAction(ActionList.REMOVE_CARD).performAction(spellSpot,board,playerField);
				magicCard.effect(player,opponent,playerField,opponentField,board);
		
	}
}
