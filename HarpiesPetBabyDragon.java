package cards;

import java.io.FileNotFoundException;

import attributes.Attribute;
import player.Player;
import states.DuelingState;

public class HarpiesPetBabyDragon extends EffectMonster {
	public HarpiesPetBabyDragon() throws FileNotFoundException {
		super(CardList.HARPIES_PET_BABY_DRAGON.ordinal(),Attribute.WIND);
	}

	@Override
	public void effect(DuelingState duelingState, Player player, Player opponent) {
		
	}
	
	@Override
	public String effectText() {
		return "";
	}
}
