package player;

import dueling.Phase;
import states.DuelingState;

public class Ai extends Player{
	private DuelingState duelingState;
	public Ai(DuelingState duelingState,String name) {
		super(name);
		this.duelingState = duelingState;
	}
	
	public void update() {
		DuelingState.phase = Phase.PLAYERS_TURN;
		//duelingState.getCam().setY(250);
	}
	
	public void setDuelingState(DuelingState duelingState) {
		this.duelingState = duelingState;
	}
	public DuelingState getDuelingState() {
		return duelingState;
	}
	
}
