/*
 *Brendan Aucoin
 *06/30/2019
 *A God Card that makes every monster on the opponents field have 0 attack and 0 defense
 */
package cards;

import java.io.FileNotFoundException;

import attributes.Attribute;
import boards.Board;
import player.Duelist;

public class ObeliskTheTormentor extends EffectMonster{
	public ObeliskTheTormentor() throws FileNotFoundException {
		super(CardList.OBELISK_THE_TOREMENTOR.ordinal(),Attribute.DIVINE);
	}

	@Override
	/*loop through opponents monsters and set all their attack and defense stats to 0*/
	public void effect(Duelist player,Duelist opponent,Board board) {
		
	}
	
	@Override
	public String effectText() {
		return "Sets atk and def of all opponents monsters to 0.";
	}
	
}
