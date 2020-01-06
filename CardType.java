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
