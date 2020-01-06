package cards;

import java.io.FileNotFoundException;

import attributes.Attribute;

public class RightLegOfTheForbiddenOne extends Monster{
	public RightLegOfTheForbiddenOne() throws FileNotFoundException {
		super(CardList.RIGHT_LEG_OF_FORBIDDEN_ONE.ordinal(),Attribute.DARK);
		
	}
}
