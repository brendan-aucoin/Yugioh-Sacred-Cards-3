package card_effects;

import boards.Board;
import dueling.Field;
import dueling.Spot;
import player.Duelist;

public interface TrapEffect extends Effect {
	public boolean effect(Spot attackingSpot,Spot receivingSpot,Duelist attacker,Duelist receiver,Field attackersField,Field receiversField,Board board);
}
