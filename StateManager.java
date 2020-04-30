/*
 *Brendan Aucoin
 *07/06/2019
 *holds the current state to be updated and rendered
 * */
package states;

import java.util.HashMap;

import game.Game;

public class StateManager {
	private State state;
	private Game game;
	private HashMap<StateList,State> allStates;
	public StateManager(Game game) {
		this.game = game;
		state = null;
		allStates = new HashMap<StateList,State>();
		fillStates();
	}
	private void fillStates() {
		allStates.put(StateList.MAIN_MENU,new MainMenuState(game));
		allStates.put(StateList.DUELING,new DuelingState(game));
		allStates.put(StateList.ATTACKING,new AttackingState(game));
	}
	
	public void setState(StateList state) {
		this.state = allStates.get(state);
	}
	
	public void setState(State state) {
		this.state = state;
	}
	public State getState(StateList state) {
		return allStates.get(state);
	}
	
	public State getState() {
		return state;
	}
	
	
}
