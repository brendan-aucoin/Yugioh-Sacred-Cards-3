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
	
	public void resetCurrentPlayer(Duelist currentPlayer,Field currentPlayerField) {
		for(int i =0; i< currentPlayerField.getMonsterCards().size();i++) {
			Card c = currentPlayerField.getMonsterCards().get(i);
			if(c instanceof Monster) {
				Monster monster = (Monster)c;
				monster.setUsedAction(false);
				if(monster instanceof EffectMonster) {
					EffectMonster effectMonster = (EffectMonster)monster;
					effectMonster.setUsedEffect(true);
				}
			}
		}
		
		currentPlayer.setPlayedCard(false);
		currentPlayer.setNumTributes(0);
		
	}
}
