package dueling;

import java.util.Comparator;

import cards.Monster;

public class CompareSpotByCardDefense  implements Comparator<Spot>{
	@Override
	public int compare(Spot s1, Spot s2) {
		
		if(s1.getCard() instanceof Monster && s2.getCard() instanceof Monster) {
			Monster m1 = (Monster)s1.getCard();
			Monster m2 = (Monster)s2.getCard();
			if(m1.getDefense() > m2.getDefense()) {return -1;}
			else if(m1.getDefense() < m2.getDefense()) {return 1;}
			else {return 0;}
		}
		return 0;
	}
}