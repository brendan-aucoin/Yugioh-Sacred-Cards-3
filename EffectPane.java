package dueling;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import game.Game;
import interaction_pane.InteractionPane;
import states.DuelingState;

public abstract class EffectPane extends InteractionPane{
	private DuelingState duelingState;
	public EffectPane(Game game, Rectangle bounds,DuelingState duelingState) {
		super(game,bounds);
		this.duelingState = duelingState;
	}
	public abstract void render(Graphics2D g);
	
	public void update() {
		
	}
	
	protected void init() {
		super.init();
	}
	
	public void mouseMoved(MouseEvent e) {
		super.mouseMoved(e);
		setMouseY((int) (getMouseY() + -(getDuelingState().getCam().getY())));
	}
	public DuelingState getDuelingState() {
		return duelingState;
	}
	public void setDuelingState(DuelingState duelingState) {
		this.duelingState = duelingState;
	}
	
	
}
