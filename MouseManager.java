package input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import states.StateManager;

public class MouseManager implements MouseListener,MouseMotionListener,MouseWheelListener {
	private StateManager stateManager;
	public MouseManager(StateManager stateManager) {
		this.stateManager = stateManager;
	}
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		stateManager.getState().mouseWheelMoved(e);
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		stateManager.getState().mouseDragged(e);
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		stateManager.getState().mouseMoved(e);
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		stateManager.getState().mouseClicked(e);
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		stateManager.getState().mouseEntered(e);
	}
	@Override
	public void mouseExited(MouseEvent e) {
		stateManager.getState().mouseExited(e);
	}
	@Override
	public void mousePressed(MouseEvent e) {
		stateManager.getState().mousePressed(e);
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		stateManager.getState().mouseReleased(e);
	}
	
	
}
