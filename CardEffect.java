/*
 *Brendan Aucoin
 *06/30/2019
 *some cards have an effect so they implement the card effect interface
 */
package card_effects;

import boards.Board;
import dueling.Field;
import player.Duelist;

public interface CardEffect extends Effect{
	public void effect(Duelist player,Duelist opponent,Field playerField,Field opponentField,Board board);
	//public String effectText();
}
