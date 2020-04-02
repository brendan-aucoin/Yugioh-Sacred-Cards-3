package cards;

import java.util.Comparator;

public class CompareByAttack implements Comparator<Monster>{

	@Override
	public int compare(Monster m1, Monster m2) {
		if(m1.getAttack() > m2.getAttack()) {return -1;}
		else if(m1.getAttack() < m2.getAttack()) {return 1;}
		else {return 0;}
	}



}
