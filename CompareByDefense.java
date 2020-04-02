package cards;

import java.util.Comparator;

public class CompareByDefense implements Comparator<Monster>{
	@Override
	public int compare(Monster m1,Monster m2) {
		if(m1.getDefense() > m2.getDefense()) {return -1;}
		else if(m1.getDefense() < m2.getDefense()) {return 1;}
		else {return 0;}
	}
}
