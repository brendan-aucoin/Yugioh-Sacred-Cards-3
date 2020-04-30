package actions;

import boards.Board;
import cards.Card;
import cards.Monster;
import dueling.Field;
import dueling.Spot;
import game.Game;
import player.Duelist;
import states.AttackingState;
import states.DuelingState;
import states.StateList;

public class AttackAction extends Action{
	@Override
	public void performAction(Duelist attacker,Duelist reciever,Spot attackersMonsterSpot,Spot recieversMonsterSpot,Game game,Board board,Field attackersField,Field receiversField) {
		//you cannot attack on the first turn
		if(DuelingState.firstTurn) {
			useAttackersCard(attackersMonsterSpot.getCard());
			return;
			}
		
		//do the trap action
		DuelingState.actionHandler.getAction(ActionList.ACTIVATE_TRAP).performAction(
				attackersMonsterSpot,recieversMonsterSpot,attacker,reciever,attackersField,receiversField,board
		);
		
		if(attackersMonsterSpot == null || attackersMonsterSpot.getCard() == null) {return;} //some traps will destory the monters making it null	
		
		//the trap action has to come first because the trap action may make it so that the attacking card has already used its action
		if(attackersMonsterSpot.getCard() instanceof Monster) {
			Monster monster = (Monster) attackersMonsterSpot.getCard();
			//if the attacker has already used an action
			if(monster.hasUsedAction()) {
				return;
			}
		}
		
				
		useAttackersCard(attackersMonsterSpot.getCard());
		useRecieversCard(recieversMonsterSpot);
		
		//setNextState(game,attacker,reciever,attackersMonsterSpot,recieversMonsterSpot,board,attackersField,receiversField);
		AttackingState attackingState = (AttackingState)game.getState(StateList.ATTACKING);
		attackingState.setAttackingInfo(attacker, reciever, attackersMonsterSpot, recieversMonsterSpot,board,attackersField,receiversField);
		game.setState(attackingState);
		attackingState.startState();
	}
	/*makes the card been used as in the card is no longer in defense no matter what, its used its action and its revealed*/
	private void useAttackersCard(Card card) {
		if(card instanceof Monster) {
			Monster monster =(Monster)card;
			monster.setInDefense(false);
			monster.setUsedAction(true);
			monster.setRevealed(true);
		}
	}
	/*when the receiver is attacked then the receivers card is revealed*/
	private void useRecieversCard(Spot spot) {
		if(spot == null) {return;}
		if(spot.getCard() instanceof Monster) {
			Monster monster = (Monster)spot.getCard();
			if(monster != null) {
				monster.setRevealed(true);
			}
		}
	}
}
