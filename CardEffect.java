/*
 *Brendan Acoin
 *06/30/2019
 *some cards have an effect so they implement the card effect interface
 */
package card_effects;

import player.Player;
import states.DuelingState;

public interface CardEffect {
	public void effect(DuelingState duelingState,Player player,Player opponent);
	public String effectText();
}
