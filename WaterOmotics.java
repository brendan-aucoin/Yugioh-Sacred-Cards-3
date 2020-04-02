/*
 *Brendan Aucoin
 *06/30/2019
 *A Basic water card that is 0 tributes
 */
package cards;

import java.io.FileNotFoundException;

import attributes.Attribute;

public class WaterOmotics extends Monster{
	public WaterOmotics() throws FileNotFoundException {
		super(CardList.WATER_OMOTICS.ordinal(),Attribute.WATER);
	}
}
