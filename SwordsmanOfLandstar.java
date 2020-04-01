/*
 *Brendan Aucoin
 *06/30/2019
 *A basic monster card thats 0 tributes
 */
package cards;

import java.io.FileNotFoundException;

import attributes.Attribute;

public class SwordsmanOfLandstar extends Monster{
	public SwordsmanOfLandstar() throws FileNotFoundException {
		super(CardList.SWORDSMAN_OF_LANDSTAR.ordinal(),Attribute.LIGHT);
	}
}
