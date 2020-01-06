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
