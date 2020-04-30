package actions;

import boards.Board;
import cards.Card;
import cards.TrapCard;
import dueling.Field;
import dueling.Spot;
import player.Ai;
import player.Duelist;
import states.DuelingState;

public class TrapAction extends Action{
	@Override
	public void performAction(Spot attackingSpot,Spot receivingSpot,Duelist attacker,Duelist receiver,Field attackersField,Field receiversField,Board board) {
		for(int i =0; i< receiversField.getMagicSpots().size();i++) {
			Card currCard = receiversField.getMagicSpots().get(i).getCard();
			Spot currSpot = receiversField.getMagicSpots().get(i);
			if(currCard instanceof TrapCard) {
				TrapCard trap = (TrapCard)currCard;
				trap.setRevealed(true);
				if(trap.effect(attackingSpot, receivingSpot, attacker, receiver, attackersField, receiversField, board)) {
					DuelingState.actionHandler.getAction(ActionList.REMOVE_CARD).performAction(currSpot,board,receiversField);
					if(attacker instanceof Ai) {
						Ai ai = (Ai)attacker;
						ai.setUsedMonsterIndex(ai.getUsedMonsterIndex()+1);
					}
					break;
				}
			}
			
			
		}
	}
}
