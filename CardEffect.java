/*
 *Brendan Aucoin
 *06/30/2019
 *some cards have an effect so they implement the card effect interface
 */
package card_effects;

import dueling.Field;
import player.Duelist;

public interface CardEffect {
	public void effect(Duelist player,Duelist opponent,Field playerField,Field opponentField);
	public String effectText();
}
