/*
 *Brendan Aucoin
 *06/30/2019
 *A Magic Card that takes your opponents strongest monster and puts it on your field if you have room on your field.
 */
package cards;

import java.io.FileNotFoundException;
import java.util.Random;

import boards.Board;
import dueling.Field;
import player.Duelist;

public class ChangeOfHeart extends SpellCard{
	public ChangeOfHeart() throws FileNotFoundException {
		super(CardList.CHANGE_OF_HEART.ordinal());
		setMaxFrequency(1);//you can only have 1 change of heart in your deck.
	}

	@Override
	public void effect(Duelist player,Duelist opponent,Field playerField,Field opponentField,Board board) {
		
	}

	
	/*this will eventually read from a file.*/
	@Override
	public String effectText() {
		//return "Take Opponents monster";
		return (new Random().nextInt(100000) + "");
	}

	public boolean playCondition(Duelist player,Duelist opponent,Field playerField,Field opponentField) {
		//return opponentField.getMonsterCards().size() >= 1;
		return true;
	}
}
