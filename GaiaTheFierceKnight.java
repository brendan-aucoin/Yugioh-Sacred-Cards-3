/*
 *Brendan Aucoin
 *06/30/2019
 *A Monster Card that is 2 tribute
 */
package cards;

import java.io.FileNotFoundException;

import attributes.Attribute;

public class GaiaTheFierceKnight extends Monster{
	public GaiaTheFierceKnight() throws FileNotFoundException {
		super(CardList.GAIA_THE_FIERCE_KNIGHT.ordinal(),Attribute.EARTH);
	}
}
