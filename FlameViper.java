/*
 *Brendan Aucoin
 *06/30/2019
 *A Basic Monster Card thats 0 tributes.
 */
package cards;

import java.io.FileNotFoundException;

import attributes.Attribute;

public class FlameViper extends Monster{
	public FlameViper() throws FileNotFoundException{
		super(CardList.FLAME_VIPER.ordinal(),Attribute.FIRE);
	}
}
