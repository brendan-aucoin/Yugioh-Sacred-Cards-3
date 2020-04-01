/*
 *Brendan Aucoin
 *07/06/2019
 *an interaction pane that has access to a dueling state object and updates the camera
 */
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
	//all effect panes render and update
	public abstract void render(Graphics2D g);
	public void update() {}
	
	protected void init() {
		super.init();
	}
	
	/*updates with the camera from the dueling state*/
	public void mouseMoved(MouseEvent e) {
		super.mouseMoved(e);
		setMouseY((int) (getMouseY() + -(getDuelingState().getCam().getY())));
	}
	/*getters and setters*/
	public DuelingState getDuelingState() {return duelingState;}
	public void setDuelingState(DuelingState duelingState) {this.duelingState = duelingState;}
	
	
}
