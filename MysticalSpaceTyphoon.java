/*
 *Brendan Aucoin
 *06/30/2019
 *A Trap Card when the user of this card is attacked this card will destroy all magic cards on the opponents field.
 */
package cards;

import java.io.FileNotFoundException;

import boards.Board;
import dueling.Field;
import dueling.Spot;
import player.Duelist;

public class MysticalSpaceTyphoon extends TrapCard{
	public MysticalSpaceTyphoon() throws FileNotFoundException {
		super(CardList.MYSTICAL_SPACE_TYPHOON.ordinal());
	}

	@Override
	/*loops through the opponents magic spots and gets rid of all cards*/
	public boolean effect(Spot attackingSpot,Spot receivingSpot,Duelist attacker,Duelist receiver,Field attackersField,Field receiversField,Board board) {
		return false;
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
