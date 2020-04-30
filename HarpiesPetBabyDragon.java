/*
 *Brendan Aucoin
 *06/30/2019
 *An effect monster that increases by 200 atk for each harpie lady or any type of harpie card you have on your field.
 */
package cards;

import java.io.FileNotFoundException;

import attributes.Attribute;
import boards.Board;
import dueling.Field;
import player.Duelist;

public class HarpiesPetBabyDragon extends EffectMonster {
	public HarpiesPetBabyDragon() throws FileNotFoundException {
		super(CardList.HARPIES_PET_BABY_DRAGON.ordinal(),Attribute.WIND);
	}

	@Override
	/*this will search your field and checks each cards name except this one you can use basic == for this. but will check if the name contains harpie*/
	public void effect(Duelist player,Duelist opponent,Field playerField,Field opponentField,Board board) {
		System.out.println("harpie stuff");
	}
	
	@Override
	public String effectText() {
		return "Increase attack power for each harpie card you have on your field.";
	}
	
	public boolean effectCondition(Duelist player,Duelist opponent,Field playerField,Field opponentField) {
		return false;
	}
}
