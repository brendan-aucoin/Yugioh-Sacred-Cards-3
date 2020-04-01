/*
 *Brendan Aucoin
 *06/30/2019
 *An effect card that changes the board to a water board
 */
package cards;

import java.io.FileNotFoundException;

import attributes.Attribute;
import boards.Board;
import player.Duelist;

public class TheLegendaryFisherman extends EffectMonster{
	public TheLegendaryFisherman() throws FileNotFoundException {
		super(CardList.THE_LEGENDARY_FISHERMAN.ordinal(),Attribute.WATER);
	}

	@Override
	/*sets the board of the dueling state to a new type of board then loop through all monsters on field and buff them individually*/
	public void effect(Duelist player,Duelist opponent,Board board) {
		
	}
	
	
	@Override
	public String effectText() {
		return "Board becomes a Water element";
	}
}
