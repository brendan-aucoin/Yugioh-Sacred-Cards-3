/*
 *Brendan Aucoin
 *06/30/2019
 *all attributes(types) for cards
 * */
package attributes;

import java.awt.Color;

public enum Attribute {
	/*used for determining which types beat which types*/
	LIGHT("LIGHT",new Color(224,255,255)),
	DARK("DARK",Color.BLACK),
	FIEND("FIEND",new Color(139,0,139)),
	DREAM("DREAM",new Color(216,191,216)),
	FIRE("FIRE",new Color(204,0,0)),
	GRASS("GRASS",new Color(0,204,0)),
	WATER("WATER",new Color(0,0,204)),
	ELECTRIC("ELECTRIC",new Color(255,255,0)),
	EARTH("EARTH",new Color(139,69,19)),
	WIND("WIND",new Color(127,255,212)),
	DIVINE("DIVINE",new Color(75,0,130)),
	NOTHING("REGULAR",Color.WHITE);
	
	private String name;
	private Color col;
	Attribute(String name,Color col){
		this.setName(name);
		this.setCol(col);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Color getCol() {
		return col;
	}
	
	public void setCol(Color col) {
		this.col =col;
	}
}
