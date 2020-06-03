package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import states.StateManager;

public class KeyManager implements KeyListener{
	private StateManager stateManager;
	public KeyManager(StateManager stateManager) {
		this.stateManager = stateManager;
	}
	@Override
	public void keyPressed(KeyEvent e) {
		stateManager.getState().keyPressed(e);
	}
	@Override
	public void keyReleased(KeyEvent e) {
		stateManager.getState().keyReleased(e);
	}
	@Override
	public void keyTyped(KeyEvent e) {
		stateManager.getState().keyTyped(e);
	}
	
	
}
