/*
 *Brendan Aucoin
 *06/30/2019
 *A Magic Card that takes your opponents strongest monster and puts it on your field if you have room on your field.
 */
package cards;

import java.io.FileNotFoundException;

import boards.Board;
import player.Duelist;
import types.CardType;

public class ChangeOfHeart extends MagicCard{
	public ChangeOfHeart() throws FileNotFoundException {
		super(CardList.CHANGE_OF_HEART.ordinal(),CardType.SPELL);
		setMaxFrequency(1);//you can only have 1 change of heart in your deck.
	}

	@Override
	public void effect(Duelist player,Duelist opponent,Board board) {
		
	}

	
	/*this will eventually read from a file.*/
	@Override
	public String effectText() {
		return "Take Opponents monster";
	}

}
