package actions;

import dueling.Phase;
import player.Duelist;
import states.DuelingState;

public class StartTurnAction extends Action{
	@Override
	public void performAction(Duelist player) {
		player.draw();
	}
}
