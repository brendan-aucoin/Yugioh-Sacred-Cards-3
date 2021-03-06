/*
 *Brendan Aucoin
 *06/30/2019
 *An Effect Card that if there is a free space on your field it will summon a new Doron who cannot act that turn
 */
package cards;

import java.io.FileNotFoundException;

import actions.ActionList;
import attributes.Attribute;
import boards.Board;
import dueling.Field;
import dueling.Spot;
import player.Duelist;

public class Doron extends EffectMonster{
	public Doron() throws FileNotFoundException {
		super(CardList.DORON.ordinal(),Attribute.WATER);
		
	}

	@Override
	public void effect(Duelist player,Duelist opponent,Field playerField,Field opponentField,Board board) {
		for(int i =0; i < playerField.getMonsterSpots().size();i++) {
			Spot spot = playerField.getMonsterSpots().get(i);
			if(spot.isOpen()) {
				Monster doron = null;
				try {doron = new Doron();} catch (FileNotFoundException e) {e.printStackTrace();return;}
				getAction(ActionList.SUMMON_CARD).performAction(doron,spot,player,opponent,board,playerField,opponentField);
				break;
			}
		}
	}

	@Override
	public String effectText() {
		return "Clones itself";
	}
	public boolean effectCondition(Duelist player,Duelist opponent,Field playerField,Field opponentField,Board board) {
		return  playerField.getMonsterCards().size() != 5;
	}
	
	
}
