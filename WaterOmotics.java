package cards;

import java.io.FileNotFoundException;

import attributes.Attribute;

public class WaterOmotics extends Monster{
	public WaterOmotics() throws FileNotFoundException {
		super(CardList.WATER_OMOTICS.ordinal(),Attribute.WATER);
	}
	
	
}
