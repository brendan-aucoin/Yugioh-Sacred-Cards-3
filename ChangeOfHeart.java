package cards;

import java.io.FileNotFoundException;

import player.Player;
import states.DuelingState;
import types.CardType;

public class ChangeOfHeart extends MagicCard{
	public ChangeOfHeart() throws FileNotFoundException {
		super(CardList.CHANGE_OF_HEART.ordinal(),CardType.SPELL);
	}

	@Override
	public void effect(DuelingState duelingState, Player player, Player opponent) {
		
	}

	@Override
	public String effectText() {
		return "Take Opponents monster";
	}

}
