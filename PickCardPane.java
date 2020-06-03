/*
 *Brendan Aucoin
 *03/30/2020
 *opens up a pane where you can click on cards in a specific field and then some function will be called once you do
 * */
package dueling;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import game.Game;
import states.DuelingState;

public class PickCardPane extends EffectPane{
	private ArrayList<Spot> spotList;
	private boolean startedPhase;
	private Spot pickedSpot;
	private Timer timer;//used as a cool down timer so that your mouse input isnt counted twice.
	private Consumer<Spot> nextFunction;//once you pick a card you want to call a specific function to handle whichever card you picked
	private Consumer<Spot> failedFunction;//if you click on the screen but you didnt click on one of the possible cards
	private Spot previousSpot;//the original spot you clicked on to get to this phase.  
	public PickCardPane(DuelingState duelingState,Game game) {
		super(game,new Rectangle(),duelingState);
		init();
	}
	public void init() {
		super.init();
		this.spotList = new ArrayList<Spot>();
		this.startedPhase = false;
		this.pickedSpot = null;
		this.timer = new Timer();
		this.nextFunction = null;
		this.failedFunction = null;
		this.previousSpot = null;
	}
	
	public void update() {}
	
	public void render(Graphics2D g) {
		if(spotList == null || !startedPhase) {return;}
		for(int i =0; i < spotList.size();i++) {
			g.setColor(getDuelingState().getBoard().getBoardType().getDraggingSpotCol());
			g.setStroke(new BasicStroke(3));//putting a border around the spots that you can click on so the user knows which cards to pick
			g.draw(spotList.get(i).getBounds());
			//if you hover over a spot that has a card in it then put the corner hover effect over it.
			if(!spotList.get(i).isOpen() && mouseOver(spotList.get(i).getBounds())) {
				getDuelingState().renderCornerHover(g,spotList.get(i).getBounds(),getDuelingState().getBoard().getBoardType().getFourCornerHoverCol(),5);
			}
		}
	}
	@Override
	public void mousePressed(MouseEvent e) {
		if(spotList == null || !startedPhase) {return;}//if you havnt started the phase or the spotlist isnt initialized then quit
		//loop through the spot list
		for(int i=0;i<spotList.size();i++) {
			//if you are over a spot with a card in it
			if(!spotList.get(i).isOpen() && mouseOver(spotList.get(i).getBounds())) {
				pickedSpot = spotList.get(i);//set the picked spot to whatever spot you clicked on
				startedPhase = false;//reset
				if(nextFunction == null) {return;}//if the next function is null then quit
				nextFunction.accept(pickedSpot);//execute the function to deal with the spot you clicked on
				return;
			}
		}
		if(failedFunction != null && previousSpot != null) {//if you have a failed function to run 
			failedFunction.accept(previousSpot);//execute the failed function
		}
		startedPhase = false;//make this picking card phase end
	}
	/*to keep track of where the mouse is*/
	public void mouseMoved(MouseEvent e) {
		super.mouseMoved(e);
	}
	
	/*starts a timer so that two mouse clicks do not immedetly happen.
	 * the timer will wait 50 milliseconds and then sets the started phase boolean to true
	 * this method is for if you want both a next function and a failed function
	 * realistically this is the one that is always gonna be called
	 * you almost always want a next function*/
	public void start(ArrayList<Spot> spotList,Consumer<Spot> nextFunction,Consumer<Spot> failedFunction,Spot previousSpot) {
		//make a delay
		timer.schedule(new TimerTask() {
			public void run() {
				setList(spotList);
				setStartedPhase(true);
				setNextFunction(nextFunction);
				setFailedFunction(failedFunction);
				setPreviousSpot(previousSpot);
			}
		}, 50);
	}
	
	/*starts a timer so that two mouse clicks do not immedetly happen.
	 * the timer will wait 50 milliseconds and then sets the started phase boolean to true
	 * this function is for if you dont want a next or failed function */
	public void start(ArrayList<Spot> spotList) {
		//make a delay
		timer.schedule(new TimerTask() {
			public void run() {
				setList(spotList);
				setStartedPhase(true);
			}
		}, 50);
	}
	/*starts a timer so that two mouse clicks do not immedetly happen.
	 * the timer will wait 50 milliseconds and then sets the started phase boolean to true
	 * this method is for if you only want a next function and not a failed function*/
	public void start(ArrayList<Spot> spotList,Consumer<Spot> nextFunction) {
		//make a delay
		timer.schedule(new TimerTask() {
			public void run() {
				setList(spotList);
				setStartedPhase(true);
				setNextFunction(nextFunction);
			}
		}, 50);
	}
	/*starts a timer so that two mouse clicks do not immedetly happen.
	 * the timer will wait 50 milliseconds and then sets the started phase boolean to true
	 * this method is for if you only want a failed function and not a next function*/
	public void start(ArrayList<Spot> spotList,Consumer<Spot> failedFunction,Spot previousSpot) {
		//make a delay
		timer.schedule(new TimerTask() {
			public void run() {
				setList(spotList);
				setStartedPhase(true);
				setFailedFunction(failedFunction);
				setPreviousSpot(previousSpot);
			}
		}, 50);
	}
	
	private void setList(ArrayList<Spot> spotList) {this.spotList = spotList;}
	public ArrayList<Spot> getSpotList() {return spotList;}
	public void setSpotList(ArrayList<Spot> spotList) {this.spotList = spotList;}
	public boolean isStartedPhase() {return startedPhase;}
	public void setStartedPhase(boolean startedPhase) {this.startedPhase = startedPhase;}
	public void setPickedSpot(Spot pickedSpot) {this.pickedSpot = pickedSpot;}
	public Spot getPickedSpot() {return pickedSpot;}
	public void setNextFunction(Consumer<Spot> nextFunction) {this.nextFunction = nextFunction;}
	public void setPreviousSpot(Spot previousSpot) {this.previousSpot = previousSpot;}
	public void setFailedFunction(Consumer<Spot> failedFunction) {this.failedFunction = failedFunction;}
}
