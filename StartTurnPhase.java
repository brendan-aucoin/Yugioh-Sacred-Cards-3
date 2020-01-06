package dueling;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import game.Game;
import player.Ai;
import player.Player;
import states.DuelingState;

public class StartTurnPhase extends TextPopupPane{
	private Player nextPlayer;
	public StartTurnPhase(DuelingState duelingState,Game game) {
		super(duelingState,game);
		init();
	}
	
	protected void init() {
		super.init();
		setText("My Turn");
		setTime(75);
	}
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
	
	
	public void render(Graphics2D g) {
		super.render(g);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}
	public Player getNextPlayer() {return nextPlayer;}
	public void setNextPlayer(Player nextPlayer) {this.nextPlayer = nextPlayer;}	
}
