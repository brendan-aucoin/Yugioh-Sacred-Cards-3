package cards;

import java.io.FileNotFoundException;

import card_effects.TrapEffect;
import types.CardType;

public abstract class TrapCard extends MagicCard implements TrapEffect{
	public TrapCard(int cardId) throws FileNotFoundException {
		super(cardId,CardType.TRAP);
	}
	
	@Override
    public TrapCard clone() throws CloneNotSupportedException 
    { 
        return (TrapCard) super.clone(); 
    } 
}
