/*
 *Brendan Aucoin
 *06/30/2019
 *all magic cards(trap and spells) have only 1 effect
 */
package cards;

import java.io.FileNotFoundException;

import card_effects.CardEffect;
import dueling.Field;
import player.Duelist;
import types.CardType;

public abstract class MagicCard extends Card implements CardEffect{
	public static final int TEXT_POPUP_TIME = 95;
	public MagicCard(int cardId,CardType type) throws FileNotFoundException {
		super(cardId,type);
	}
	
	public MagicCard(MagicCard c){
		super(c);
	}
	
	public String toString() {
		return super.toString();
	}
	public abstract boolean playCondition(Duelist player,Duelist opponent,Field playerField,Field opponentField);
	@Override
    public MagicCard clone() throws CloneNotSupportedException 
    { 
        return (MagicCard) super.clone(); 
    } 


	
}
