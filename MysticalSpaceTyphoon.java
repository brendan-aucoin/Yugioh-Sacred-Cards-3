package cards;

import java.io.FileNotFoundException;

import player.Player;
import states.DuelingState;
import types.CardType;

public class MysticalSpaceTyphoon extends MagicCard{
	public MysticalSpaceTyphoon() throws FileNotFoundException {
		super(CardList.MYSTICAL_SPACE_TYPHOON.ordinal(),CardType.TRAP);
	}

	@Override
	public void effect(DuelingState duelingState, Player player, Player opponent) {
		
	}
	@Override
	public String effectText() {
		return "";
	}
	
}
