/*
 *Brendan Aucoin
 *06/30/2019
 *The ai that extends from player 
 */
package player;

import dueling.Phase;
import states.DuelingState;

public class Ai extends Duelist{
	private DuelingState duelingState;
	public Ai(DuelingState duelingState,String name) {
		super(name);
		this.duelingState = duelingState;
	}
	
	/*the logic of the AI*/
	public void update() {
		DuelingState.phase = Phase.PLAYERS_TURN;
		//duelingState.getCam().setY(250);
	}
	
	
	
	public void setDuelingState(DuelingState duelingState) {this.duelingState = duelingState;}
	public DuelingState getDuelingState() {return duelingState;}
	
}
