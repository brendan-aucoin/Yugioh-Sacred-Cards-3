package cards;

import java.util.Comparator;

public class CompareByCurrentStat implements Comparator<Monster>{
	@Override
	public int compare(Monster m1,Monster m2) {
		int m1Stat = m1.getAttack();
		int m2Stat = m2.getAttack();
		if(m1.isInDefense()) {
			m1Stat = m1.getDefense();
		}
		if(m2.isInDefense()) {
			m2Stat = m2.getDefense();
		}
		if(m1Stat > m2Stat) {return -1;}
		else if(m1Stat < m2Stat) {return 1;}
		else {return 0;}
	}
}