package cards;

import java.io.FileNotFoundException;

import attributes.Attribute;

public class SwordsmanOfLandstar extends Monster{
	public SwordsmanOfLandstar() throws FileNotFoundException {
		super(CardList.SWORDSMAN_OF_LANDSTAR.ordinal(),Attribute.LIGHT);
	}
}
