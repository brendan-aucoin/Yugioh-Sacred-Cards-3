/*
 *Brendan Aucoin
 *07/06/2019
 *puts a pop up on screen for a certain amount of time
 * */
package dueling;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import game.Game;
import gui.Display;
import states.DuelingState;

public class TextPopupPane extends EffectPane{
	private boolean start;
	private int ticks;
	private String text;
	private float textAlpha,boxAlpha;
	private int time;
	public TextPopupPane(DuelingState duelingState,Game game) {
		super(game,new Rectangle(
		0, Display.SCREEN_SIZE.height/2,Display.SCREEN_SIZE.width,Display.SCREEN_SIZE.height/2 + Display.SCREEN_SIZE.height/5),duelingState);
		init();
	}
	/*sets the default values*/
	protected void init() {
		super.init();
		start = false;
		ticks = 0;
		text = "";
		textAlpha  =1f;
		boxAlpha = 0.4f;
		time = 0;
	}
	/*every tick you want to update the effect pane(super class) if you have started then make if so you cant scroll
	 * if you reach the time limit set start to false and make it so you can scroll*/
	public void update() {
		super.update();
		if(isStart()) {
			getDuelingState().setCanScroll(false);
			ticks++;
			if(ticks >= time) {
				setStart(false);
				getDuelingState().setCanScroll(true);
			}
		}
		/*else {
			getDuelingState().setCanScroll(true);
		}*/
	}
	public void startNew(String text,int time) {
		reset();
		setText(text);
		setTime(time);
		setStart(true);
	}
	/*reset everything back to the defaults*/
	public void reset() {
		ticks= 0;
		start = false;
		textAlpha = 1f;
		boxAlpha = 0.4f;
		time = 0;
	}
	/*render the box and the text */
	@Override
	public void render(Graphics2D g) {
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,boxAlpha));
		g.setColor(new Color(110,94,94));
		g.fillRect(getBounds().x,getBounds().y,getBounds().width,getBounds().height);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,textAlpha));
		g.setFont(new Font("ariel",Font.PLAIN,35));
		g.setColor(Color.WHITE);
		g.drawString(text,(int) (getBounds().x + g.getFont().getStringBounds(text, frc).getWidth()/40),(int) (getBounds().y + g.getFont().getStringBounds(text, frc).getHeight()));
	}
	/*getters and setters*/
	public void setStart(boolean start) {this.start = start;}
	public boolean isStart() {return start;}
	public int getTicks() {return ticks;}
	public void setTicks(int ticks) {this.ticks = ticks;}
	public String getText() {return text;}
	public void setText(String text) {this.text = text;}
	public float getTextAlpha() {return textAlpha;}
	public void setTextAlpha(float textAlpha) {this.textAlpha = textAlpha;}
	public float getBoxAlpha() {return boxAlpha;}
	public void setBoxAlpha(float boxAlpha) {this.boxAlpha = boxAlpha;}
	public int getTime() {return time;}
	public void setTime(int time) {	this.time = time;}
}
