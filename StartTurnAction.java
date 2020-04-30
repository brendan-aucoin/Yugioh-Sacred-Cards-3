/*
 * Brendan Aucoin
 *03/30/2020
 *starts your turn by drawing
 * */
package actions;

import player.Duelist;

public class StartTurnAction extends Action{
	@Override
	public void performAction(Duelist player) {
		player.draw();
	}
}
