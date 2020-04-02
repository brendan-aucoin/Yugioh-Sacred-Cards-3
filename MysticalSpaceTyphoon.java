/*
 *Brendan Aucoin
 *06/30/2019
 *A Trap Card when the user of this card is attacked this card will destroy all magic cards on the opponents field.
 */
package cards;

import java.io.FileNotFoundException;

import dueling.Field;
import player.Duelist;
import types.CardType;

public class MysticalSpaceTyphoon extends MagicCard{
	public MysticalSpaceTyphoon() throws FileNotFoundException {
		super(CardList.MYSTICAL_SPACE_TYPHOON.ordinal(),CardType.TRAP);
	}

	@Override
	/*loops through the opponents magic spots and gets rid of all cards*/
	public void effect(Duelist player,Duelist opponent,Field playerField,Field opponentField) {
		
	}
	@Override
	public String effectText() {
		return "";
	}
	@Override
	public boolean playCondition(Duelist player,Duelist opponent,Field playerField,Field opponentField) {
		return true;
	}
	
}
