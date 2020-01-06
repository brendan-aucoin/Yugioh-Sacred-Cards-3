package cards;

import java.io.FileNotFoundException;

import attributes.Attribute;
import player.Player;
import states.DuelingState;

public class TheLegendaryFisherman extends EffectMonster{
	public TheLegendaryFisherman() throws FileNotFoundException {
		super(CardList.THE_LEGENDARY_FISHERMAN.ordinal(),Attribute.WATER);
	}

	@Override
	public void effect(DuelingState duelingState, Player player, Player opponent) {
		
	}
	
	
	@Override
	public String effectText() {
		return "";
	}
}
