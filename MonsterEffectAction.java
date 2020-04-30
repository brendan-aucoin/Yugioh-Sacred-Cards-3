/*
 *Brendan Aucoin
 *03/30/2020
 *performs the action for activating a monsters effect
 * */
package actions;

import boards.Board;
import cards.EffectMonster;
import cards.Monster;
import dueling.Field;
import player.Duelist;

public class MonsterEffectAction extends Action{
	/*checks if the card is an effect monster
	 * and makes sure that card hasnt already used its effect
	 * if it hasnt the call the effect for that card and set the used action for that card*/
	@Override
	public void performAction(Monster card,Duelist player,Duelist opponent,Field playerField,Field opponentField,Board board) {
		if(card instanceof EffectMonster) {
			EffectMonster effectCard = (EffectMonster)card;
			if(!effectCard.hasUsedEffect() && !effectCard.hasUsedAction()) {
				effectCard.effect(player,opponent,playerField,opponentField,board);
				effectCard.setUsedAction(true);
				effectCard.setInDefense(false);
				effectCard.setUsedEffect(true);
				effectCard.setRevealed(true);
			}
		}
	}
}
