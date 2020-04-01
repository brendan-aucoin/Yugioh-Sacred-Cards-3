/*
 *Brendan Aucoin
 *06/30/2019
 *an enum that has all types of cards ie monster, spell, trap. but you could always add more
 */
package types;

public enum  CardType {
	MONSTER("Monster"),
	SPELL("Spell"),
	TRAP("Trap")
	
	;
	private String name;
	CardType(String name) {
		this.name = name;
		
	}
	
	public void setName(String name) {
		this.name =  name;
	}
	public String getName() {
		return name;
	}
}
