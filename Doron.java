package cards;

import java.io.FileNotFoundException;

import attributes.Attribute;
import player.Player;
import states.DuelingState;

public class Doron extends EffectMonster{
	public Doron() throws FileNotFoundException {
		super(CardList.DORON.ordinal(),Attribute.WATER);
		
	}

	@Override
	public void effect(DuelingState duelingState, Player player, Player opponent) {
		
	}

	@Override
	public String effectText() {
		return "Clones itself";
	}
	
}
