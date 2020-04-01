/*
 *Brendan Aucoin
 *03/30/2020
 *sets the card into defense and sets the cards action to true
 * */
package actions;

import cards.Monster;
public class DefendAction extends Action{
	/*sets the card into defense and sets the cards action to true*/
	@Override
	public void performAction(Monster card) {
		card.setInDefense(true);
		card.setUsedAction(true);
	}
}
