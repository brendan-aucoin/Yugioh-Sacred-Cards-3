/*
 *Brendan Aucoin
 *06/30/2019
 *the only difference between this and a regular monster is that these
 *have a boolean telling weather they have used their effect yet.
 */
package cards;

import java.io.FileNotFoundException;

import attributes.Attribute;
import boards.Board;
import card_effects.CardEffect;
import dueling.Field;
import player.Duelist;

public abstract class EffectMonster extends Monster implements CardEffect{//extends the monster class and implements the CardEffect interface
	private boolean usedEffect;
	public EffectMonster(int cardId,Attribute attribute) throws FileNotFoundException {
		super(cardId,attribute);
	}
	
	public EffectMonster(EffectMonster m) {
		super(m);
	}
	
	@Override
    public EffectMonster clone() throws CloneNotSupportedException 
    { 
        return (EffectMonster) super.clone(); 
    } 
	
	public abstract boolean effectCondition(Duelist player,Duelist opponent,Field playerField,Field opponentField,Board board);
	
	public boolean hasUsedEffect() {return usedEffect;}
	public void setUsedEffect(boolean usedEffect) {this.usedEffect = usedEffect;}
	
	
}
