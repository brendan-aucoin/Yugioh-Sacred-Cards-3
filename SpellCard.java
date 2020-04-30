package cards;

import java.io.FileNotFoundException;

import card_effects.CardEffect;
import types.CardType;

public abstract class SpellCard extends MagicCard implements CardEffect{
	public SpellCard(int cardId) throws FileNotFoundException {
		super(cardId,CardType.SPELL);
	}
	
	@Override
    public SpellCard clone() throws CloneNotSupportedException 
    { 
        return (SpellCard) super.clone(); 
    } 
}
