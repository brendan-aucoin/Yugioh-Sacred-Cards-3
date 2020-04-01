/*
 *Brendan Aucoin
 *07/06/2019
 *puts a text popup on screen for a certain amount of time and then sets the phase to be the next player
 * */
package dueling;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import game.Game;
import player.Ai;
import player.Duelist;
import states.DuelingState;

public class StartTurnPhase extends TextPopupPane{
	private Duelist nextPlayer;
	public StartTurnPhase(DuelingState duelingState,Game game) {
		super(duelingState,game);
		init();
	}
	/*sets the default time and text*/
	protected void init() {
		super.init();
		setText("My Turn");
		setTime(75);
	}
	/*update the super class first then if you havnt started update the alpha values going down
	 * once they reach 0 you can start scrolling again and it determines what phase it will be based on the next player*/
	@Override
	public void update() {
		super.update();
		if(!isStart()) {
			setBoxAlpha(getBoxAlpha()-0.09f);
			setTextAlpha(getTextAlpha()- 0.3f);
			if(getBoxAlpha() <=0 || getTextAlpha() <= 0) {
				setBoxAlpha(0);
				setTextAlpha(0);
				DuelingState.phase = nextPlayer instanceof Ai ? Phase.AI_TURN: Phase.PLAYERS_TURN;
				getDuelingState().setCanScroll(true);
			}
		}
	}
	
	/*render the box from the super class*/
	public void render(Graphics2D g) {
		super.render(g);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}
	public Duelist getNextPlayer() {return nextPlayer;}
	public void setNextPlayer(Duelist nextPlayer) {this.nextPlayer = nextPlayer;}	
}
