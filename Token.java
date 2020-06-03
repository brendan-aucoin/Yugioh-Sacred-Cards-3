package cards;

import java.io.FileNotFoundException;

import attributes.Attribute;

public abstract class Token extends Monster{
	public Token(int cardId,Attribute attribute) throws FileNotFoundException {
		super(cardId,attribute);
		setUsedAction(true);
		setRevealed(true);
	}
	
	public Token(Token t) {
		super(t);
		setUsedAction(true);
		setRevealed(true);
	}
	
	@Override
    public Token clone() throws CloneNotSupportedException 
    { 
        return (Token) super.clone(); 
    } 
	
	@Override
	public void setUsedAction(boolean usedAction) {super.setUsedAction(true);}
	//@Override
	//public void setAttack(int attack) {super.setAttack(getAttack());}
	//@Override
	//public void setDefense(int attack) {super.setDefense(getDefense());}
	@Override
	public void setRevealed(boolean revealed) {super.setRevealed(true);}
	//@Override 
	//public void setInDefense(boolean isInDefense) {super.setInDefense(isInDefense());}
}
