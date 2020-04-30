/*
 *Brendan Aucoin
 *06/30/2019
 *A Trap card that whatever monster attacked from the opponent if it has over 1000 attack then it gets destroyed
 */
package cards;

import java.io.FileNotFoundException;

import boards.Board;
import dueling.Field;
import dueling.Spot;
import player.Duelist;

public class TrapHole extends TrapCard{
	public TrapHole() throws FileNotFoundException {
		super(CardList.TRAP_HOLE.ordinal());
		setMaxFrequency(100);
	}
	
	public boolean effect(Spot attackingSpot,Spot receivingSpot,Duelist attacker,Duelist receiver,Field attackersField,Field receiversField,Board board) {
		System.out.println("TRAP HOLE");
		Card c = attackingSpot.getCard();
		if(c==null) {return false;}
		if(c instanceof Monster) {
			Monster monster = (Monster)c;
			if(monster.getAttack() >= 1000) {
				monster.setUsedAction(true);
				monster.setRevealed(true);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean playCondition(Duelist player,Duelist opponent,Field playerField,Field opponentField) {
		return true;
	}
	@Override
	public String effectText() {
		return "TRAP HOLE";
	}
}
