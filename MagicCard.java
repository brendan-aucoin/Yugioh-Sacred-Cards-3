/*
 *Brendan Acoin
 *06/30/2019
 *all magic cards(trap and spells) have only 1 effect
 */
package cards;

import java.io.FileNotFoundException;

import card_effects.CardEffect;
import types.CardType;

public abstract class MagicCard extends Card implements CardEffect{
	public MagicCard(int cardId,CardType type) throws FileNotFoundException {
		super(cardId,type);
	}
	
	public MagicCard(MagicCard c) throws FileNotFoundException {
		super(c);
		
	}
	
	public String toString() {
		return super.toString();
	}

	
}
