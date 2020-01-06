/*
 *Brendan Acoin
 *06/30/2019
 *the only difference between this and a regular monster is that these
 *have a boolean telling weather they have used their effect yet.
 */
package cards;

import java.io.FileNotFoundException;

import attributes.Attribute;
import card_effects.CardEffect;

public abstract class EffectMonster extends Monster implements CardEffect{
	private boolean usedEffect;
	public EffectMonster(int cardId,Attribute attribute) throws FileNotFoundException {
		super(cardId,attribute);
	}
	
	public EffectMonster(EffectMonster m) throws FileNotFoundException {
		super(m);
		this.usedEffect = m.usedEffect;
	}
	
	public String toString() {
		return super.toString();
	}
	
	public boolean hasUsedEffect() {return usedEffect;}
	public void setUsedEffect(boolean usedEffect) {this.usedEffect = usedEffect;}
	
	
}
