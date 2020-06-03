/*
 *Brendan Aucoin
 *03/30/2020
 *sets the card into defense and sets the cards action to true
 * */
package actions;

import cards.Monster;
import player.Ai;
import player.Duelist;
public class DefendAction extends Action{
	/*sets the card into defense and sets the cards action to true*/
	@Override
	public void performAction(Monster card,Duelist duelist) {
		if(!card.hasUsedAction()) {
			/*if the ai is the one setting their card in defense mode then increase the index keeping track of how many monsters were used*/
			if(duelist instanceof Ai) {
				Ai ai = (Ai)duelist;
				ai.setUsedMonsterIndex(ai.getUsedMonsterIndex()+1);
				ai.signalUseMonsters();
			}
			card.setInDefense(true);
			card.setUsedAction(true);
			/*if this is the cards first turn on the field then it stays hidden if its in defense mode*/
			if(card.isFirstTurn()) {
				card.setRevealed(false);
			}
		}
	}
}
