/*
 *Brendan Aucoin
 *06/30/2019
 *A Trap card that whatever monster attacked from the opponent if it has over 1000 attack then it gets destroyed
 */
package cards;

import java.io.FileNotFoundException;

import boards.Board;
import player.Duelist;
import types.CardType;

public class TrapHole extends MagicCard{
	public TrapHole() throws FileNotFoundException {
		super(CardList.TRAP_HOLE.ordinal(),CardType.TRAP);
		
	}
	
	public void effect(Duelist player,Duelist opponent,Board board) {
		
	}
	

	@Override
	public String effectText() {
		return "";
	}
}
