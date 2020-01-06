package cards;

import java.io.FileNotFoundException;

import player.Player;
import states.DuelingState;
import types.CardType;

public class TrapHole extends MagicCard{
	public TrapHole() throws FileNotFoundException {
		super(CardList.TRAP_HOLE.ordinal(),CardType.TRAP);
		
	}
	
	public void effect(DuelingState duelingState, Player player, Player opponent) {
		
	}
	

	@Override
	public String effectText() {
		return "";
	}
}
