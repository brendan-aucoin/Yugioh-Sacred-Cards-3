/*
 *Brendan Aucoin
 *06/30/2019
 *An Effect Card that if there is a free space on your field it will summon a new Doron who cannot act that turn
 */
package cards;

import java.io.FileNotFoundException;

import attributes.Attribute;
import dueling.Field;
import player.Duelist;

public class Doron extends EffectMonster{
	public Doron() throws FileNotFoundException {
		super(CardList.DORON.ordinal(),Attribute.WATER);
	}

	@Override
	public void effect(Duelist player,Duelist opponent,Field playerField,Field opponentField) {
		
	}

	@Override
	public String effectText() {
		return "Clones itself";
	}
	
}
