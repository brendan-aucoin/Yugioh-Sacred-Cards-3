package cards;

import java.io.FileNotFoundException;

import attributes.Attribute;
import player.Player;
import states.DuelingState;

public class ObeliskTheTormentor extends EffectMonster{
	public ObeliskTheTormentor() throws FileNotFoundException {
		super(CardList.OBELISK_THE_TOREMENTOR.ordinal(),Attribute.DIVINE);
	}

	@Override
	public void effect(DuelingState duelingState, Player player, Player opponent) {
		
	}
	
	@Override
	public String effectText() {
		return "";
	}
	
}
