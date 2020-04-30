/*
 *Brendan Aucoin
 *03/30/2020
 *compares two spots by the cards current stat depending if it is in defense or not
 * */
package dueling;

import java.util.Comparator;

import cards.Monster;

public class CompareSpotByCardsCurrentStat implements Comparator<Spot>{
	@Override
	public int compare(Spot s1, Spot s2) {
		
		if(s1.getCard() instanceof Monster && s2.getCard() instanceof Monster) {
			Monster m1 = (Monster)s1.getCard();
			Monster m2 = (Monster)s2.getCard();
			int m1Stat = m1.getAttack();
			int m2Stat = m2.getAttack();
			if(m1.isInDefense()) {m1Stat = m1.getDefense();}
			if(m2.isInDefense()) {m2Stat = m2.getDefense();}
			if(m1Stat > m2Stat) {return -1;}
			else if(m1Stat < m2Stat) {return 1;}
			else {return 0;}
		}
		return 0;
	}
}
