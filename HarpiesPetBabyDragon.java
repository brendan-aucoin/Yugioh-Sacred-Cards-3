/*
 *Brendan Aucoin
 *06/30/2019
 *An effect monster that increases by 200 atk for each harpie lady or any type of harpie card you have on your field.
 */
package cards;

import java.io.FileNotFoundException;

import attributes.Attribute;
import boards.Board;
import boards.ElementalBoard;
import dueling.Field;
import player.Duelist;

public class HarpiesPetBabyDragon extends EffectMonster {
	public HarpiesPetBabyDragon() throws FileNotFoundException {
		super(CardList.HARPIES_PET_BABY_DRAGON.ordinal(),Attribute.WIND);
		setMaxFrequency(10);
	}

	@Override
	/*this will search your field and checks each cards name except this one you can use basic == for this. but will check if the name contains harpie*/
	public void effect(Duelist player,Duelist opponent,Field playerField,Field opponentField,Board board) {
		//temp changing a board to water
		board.changeType(new ElementalBoard(Attribute.WATER), player, opponent, playerField, opponentField);
	}
	
	@Override
	public String effectText() {
		return "Increase attack power for each harpie card you have on your field. or if you have 5 harpies on your field then they are boosted by 5 times";
	}
	
	public boolean effectCondition(Duelist player,Duelist opponent,Field playerField,Field opponentField,Board board) {
		return !(board.getBoardType() instanceof ElementalBoard && ((ElementalBoard) board.getBoardType()).getAttribute() == Attribute.WATER);
	}
}
