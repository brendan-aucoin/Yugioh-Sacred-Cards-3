/*
 *Brendan Aucoin
 *07/06/2019
 *The main menu of the game
 * */
package states;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import game.Game;
import gui.ButtonHandler;
import gui.Display;
import gui.TextButton;
import interaction_pane.InteractionPane;

public class MainMenuState extends InteractionPane implements State {
	private final String NEW_GAME = "New Game", CONTINUE = "Continue";
	private BufferedImage backgroundImage;
	private BufferedImage sealImage;
	private boolean firstTime,resetAlpha;
	private Rectangle continueBounds,newGameBounds;
	private TextButton continueButton,newGameButton;
	private ButtonHandler buttonHandler;
	private float sealAlpha;
	private Font buttonFont;
	public MainMenuState(Game game) {
		super(game,new Rectangle(0,0,Display.SCREEN_SIZE.width,Display.SCREEN_SIZE.height));//sets the bounds
	}
	/*initializes all the defaults*/
	protected void init() {
		super.init();
		buttonFont = new Font("Lucida Handwriting",Font.PLAIN,60);
		backgroundImage = getGame().getImageLoader().loadImage("dueling_images","main menu background.png");
		sealImage = getGame().getImageLoader().loadImage("dueling_images","seal of orichalcos.png");
		sealAlpha = 0;
		firstTime = true;
		resetAlpha = false;
		//the two buttons
		newGameBounds = new Rectangle(
		(int)(getBounds().width/3 - getBounds().width/7),
		getBounds().height - getBounds().height/6,
		makeRectangleWidth(buttonFont,NEW_GAME),
		(int)(makeRectangleHeight(buttonFont,NEW_GAME)/1.78)
		);
		
		continueBounds = new Rectangle(
		getBounds().width/2 + getBounds().width/10,
		getBounds().height - getBounds().height/6
		, makeRectangleWidth(buttonFont,CONTINUE),
		(int)(makeRectangleHeight(buttonFont,CONTINUE)/1.78)
		);
		continueButton = new TextButton(continueBounds,new Color(255,215,0),new Color(244,164,96),CONTINUE,buttonFont,e -> pressContinue(e));
		newGameButton = new TextButton(newGameBounds,new Color(255,215,0),new Color(244,164,96),NEW_GAME,buttonFont,e -> pressNewGame(e));
		buttonHandler = new ButtonHandler(continueButton,newGameButton);
	}
	/*all you update is the background image fading in and out*/
	public void update() {
		updateSeal();
		buttonHandler.update(getMouseX(),getMouseY());
	}

	/*make the seal background image fade in and out*/
	private void updateSeal() {
		if(firstTime) {
			sealAlpha += 0.004f;
			if(sealAlpha >=1) {
				sealAlpha = 1;
				firstTime = false;
			} 
		}
		
		if(!firstTime) {
			if(!resetAlpha) {
				sealAlpha -= 0.0048f;
				if(sealAlpha <=0.3) {
					resetAlpha = true;
				}
			}
			else {
				sealAlpha += 0.006f;
				if(sealAlpha >=0.99) {
					resetAlpha =false;
				}
			}
			
		}
	}
	/*draw the background images, and the buttons*/
	public void render(Graphics2D g) {
		g.drawImage(backgroundImage,0,0,Display.SCREEN_SIZE.width,Display.SCREEN_SIZE.height,null);
		drawButtons(g);
		drawSealBackground(g);
	}
	/*events for clicking on the new game and the continue buttons*/
	public void mousePressed(MouseEvent e) {
		buttonHandler.mousePressed(e);
	}
	
	private void pressContinue(MouseEvent e) {
		System.out.println("CONTINUE");
	}
	private void pressNewGame(MouseEvent e) {
		System.out.println("new game");
	}
	
	/*draw the continue and new game buttons*/
	private void drawButtons(Graphics2D g) {
		buttonHandler.render(g);
	}
	/*draw the seal background image with the proper alpha*/
	private void drawSealBackground(Graphics2D g) {
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,sealAlpha));
		g.drawImage(sealImage,(int)(Display.SCREEN_SIZE.width/2 - sealImage.getWidth()/2)
		, (int)(Display.SCREEN_SIZE.height/2 - sealImage.getHeight()/1.7),
		(int)(sealImage.getWidth()),(int)(sealImage.getHeight()),null);
	}
	
	
	
	
}
