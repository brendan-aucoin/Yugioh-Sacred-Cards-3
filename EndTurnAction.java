/*
 *Brendan Aucoin
 *03/30/2020
 *ends your turn by resetting all the data like the number of tributes you made and all the monsters on your fields back to default
 *sets the phase to whatver phase you pass in
 * */
package actions;

import cards.Card;
import cards.EffectMonster;
import cards.Monster;
import dueling.Field;
import dueling.Phase;
import player.Duelist;
import states.DuelingState;

public class EndTurnAction extends Action {
	public void performAction(Duelist currentPlayer,Field currentPlayerField, Phase nextPhase) {
		resetCurrentPlayer(currentPlayer,currentPlayerField);
		DuelingState.phase = nextPhase;
	}
	/*makes all cards on your field not been used for the next turn and resets the players variables to default*/
	public void resetCurrentPlayer(Duelist currentPlayer,Field currentPlayerField) {
		for(int i =0; i< currentPlayerField.getMonsterCards().size();i++) {
			Card c = currentPlayerField.getMonsterCards().get(i);
			if(c instanceof Monster) {
				Monster monster = (Monster)c;
				monster.setUsedAction(false);
				monster.setFirstTurn(false);
				if(monster instanceof EffectMonster) {
					EffectMonster effectMonster = (EffectMonster)monster;
					effectMonster.setUsedEffect(true);
				}
			}
		}
		
		currentPlayer.setPlayedCard(false);
		currentPlayer.setNumTributes(0);
		if(DuelingState.firstTurn) {DuelingState.firstTurn = false;}
		//maybe immedietly start the next person phase using the start turn action.
	}
}
