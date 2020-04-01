/*
 *Brendan Aucoin
 *07/06/2019
 *holds the current state to be updated and rendered
 * */
package states;

public class StateManager {
	private State state;
	public StateManager() {
		state = null;
	}
	
	public void setState(State state) {
		this.state = state;
	}
	
	public State getState() {
		return state;
	}
}
