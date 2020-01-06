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

	protected void init() {
		super.init();
		start = false;
		ticks = 0;
		text = "";
		textAlpha  =1f;
		boxAlpha = 0.4f;
		time = 0;
	}
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

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
	
}
