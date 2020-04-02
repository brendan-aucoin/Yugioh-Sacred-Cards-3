/*
 *Brendan Aucoin
 *06/30/2019
 *An effect monster that if you have all 5 pieces of exodia then you win.
 */
package cards;

import java.io.FileNotFoundException;

import attributes.Attribute;
import dueling.Field;
import player.Duelist;

public class RightLegOfTheForbiddenOne extends EffectMonster{
	public RightLegOfTheForbiddenOne() throws FileNotFoundException {
		super(CardList.RIGHT_LEG_OF_THE_FORBIDDEN_ONE.ordinal(),Attribute.DARK);
		
	}

	@Override
	/*loops through the players field and if they have all 5 pieces of exodia then they set the opponents life points to 0*/
	public void effect(Duelist player,Duelist opponent,Field playerField,Field opponentField) {
		
	}

	@Override
	public String effectText() {
		return "If player has all 5 pieces of exodia on the field then you win";
	}
}
