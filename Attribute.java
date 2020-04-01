/*
 *Brendan Aucoin
 *06/30/2019
 *all attributes(types) for cards
 * */
package attributes;

public enum Attribute {
	/*used for determining which types beat which types*/
	LIGHT("LIGHT"),
	DARK("DARK"),
	FIEND("FIEND"),
	DREAM("DREAM"),
	FIRE("FIRE"),
	GRASS("GRASS"),
	WATER("WATER"),
	ELECTRIC("ELECTRIC"),
	EARTH("EARTH"),
	WIND("WIND"),
	DIVINE("DIVINE");
	
	private String name;
	
	Attribute(String name){
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
