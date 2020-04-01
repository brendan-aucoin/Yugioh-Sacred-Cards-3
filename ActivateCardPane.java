/*
 *Brendan Acoin
 *07/06/2019
 *the box that pops up when you click on your card to attack, defend, tribute or activate the effect.
 */
package dueling;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import actions.ActionList;
import cards.Card;
import cards.EffectMonster;
import cards.MagicCard;
import cards.Monster;
import game.Game;
import states.DuelingState;
import types.CardType;

public class ActivateCardPane extends EffectPane{
	private boolean displaying;
	private Monster card;
	private Spot spot;
	private Rectangle attackBounds,defenseBounds,tributeBounds,effectBounds;
	private Font textFont;
	private BufferedImage milleniumEyeImage;
	private TextPopupPane textPopupPane;
	public ActivateCardPane(DuelingState duelingState,Game game) {
		super(game,new Rectangle(),duelingState);
		init();
	}
	/*initializes all the variables to null or the default*/
	public void init() {
		super.init();
		displaying = false;
		card = null;
		spot = null;
		textFont = new Font("ariel",Font.PLAIN,55);
		milleniumEyeImage = getGame().getImageLoader().loadImage("dueling_images","milleniumEye.png");
		textPopupPane = new TextPopupPane(getDuelingState(),getGame());
	}
	/*you cannot scroll if the pop up is displaying*/
	public void update() {
		if(displaying) {
			getDuelingState().setCanScroll(false);
		}
		else {
			getDuelingState().setCanScroll(true);
			
		}
		textPopupPane.update();
	}
	
	@Override
	/*render the black box if it should be displaying */
	public void render(Graphics2D g) {
		if(displaying && getBounds()!=null) {
			renderBox(g);
			renderButtons(g);
		}
		if(textPopupPane.isStart()) {
			textPopupPane.render(g);
		}
	}
	/*the black box */
	private void renderBox(Graphics2D g) {
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.78f));
		g.setColor(Color.BLACK);
		g.fill(getBounds());
		
	}
	/*the words attack, defense, etc*/
	private void renderButtons(Graphics2D g) {
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
		g.setFont(textFont);
		renderButton(g,attackBounds,"Attack",!card.hasUsedAction());//you cant attack if the card has used its action
		renderButton(g,tributeBounds,"Tribute",!getDuelingState().getPlayer().hasPlayedCard());//you cant tribute if you have already played a card
		renderButton(g,defenseBounds,"Defense",!card.hasUsedAction());//you cant put your card in defense if you have already done an action
		if(card instanceof EffectMonster) {
			EffectMonster effectCard = (EffectMonster)card;
			renderButton(g,effectBounds,"Effect",!effectCard.hasUsedEffect());//if the card has not used its effect than you can activate it
		}
		else {
			renderButton(g,effectBounds,"Effect",false);//if the card is not an effect monster then you cant use its effect
		}
	}
	/*render the hovering colour of the button based on some condition passed in*/
	private void renderButton(Graphics2D g, Rectangle bounds,String text,boolean condition) {
		g.setColor(Color.WHITE);
		if(mouseOver(bounds)) {
			g.setColor(Color.RED);
		}
		//if the condition is not satisfied then gray out the word
		if(!condition && mouseOver(bounds)) {
			g.setColor(Color.GRAY);
		}
		//draw the string
		g.drawString(text,bounds.x,bounds.y + bounds.height);

		//draw the hover image on the side if you are hovering over it
		if(mouseOver(bounds)) {
			g.drawImage(milleniumEyeImage, bounds.x - getBounds().width/40,bounds.y + bounds.height/2 - milleniumEyeImage.getHeight()/2 ,milleniumEyeImage.getWidth(),milleniumEyeImage.getHeight(),null);
		}
	}
	/*if the box is displaying then you can activate your monster or spell*/
	public void mousePressed(MouseEvent e) {
		if(!displaying) {
			activateMonster();
			
			activateSpell();
		}
		else {
			clickOnButtons();
		}
	}
	
	/*loops through players monsters
	 * if your mouse is over a spot
	 * and the spot is not open and the card in that spot has not done an action
	 * then set displaying to true*/
	private void activateMonster() {
		for(int i =0; i < getDuelingState().getBoard().getPlayerField().getMonsterSpots().size();i++) {
			Spot spot = getDuelingState().getBoard().getPlayerField().getMonsterSpots().get(i);
			if(mouseOver(spot.getBounds()) && !spot.isOpen() && !spot.getCard().hasUsedAction()) {
				if(spot.getCard() instanceof Monster) {
					card = (Monster) spot.getCard();
				}
				displaying = true;
				this.spot = spot;
			}
		}
	}
	/*
	 * loop through the players spell cards
	 * if you are hovering over a spot check if the card is a spell
	 * activates the spell action
	 * */
	private void activateSpell() {
		for(int i =0; i < getDuelingState().getBoard().getPlayerField().getMagicSpots().size();i++) {
			Spot spot = getDuelingState().getBoard().getPlayerField().getMagicSpots().get(i);
			if(mouseOver(spot.getBounds())) {
				if(spot.getCard() instanceof MagicCard && spot.getCard().getType() == CardType.SPELL) {
					MagicCard spell = (MagicCard)spot.getCard();
					DuelingState.actionHandler.getAction(ActionList.ACTIVATE_SPELL).performAction(spell, getDuelingState().getPlayer(), getDuelingState().getOpponent(), getDuelingState().getBoard(),spot,getDuelingState().getBoard().getPlayerField(),textPopupPane);
				}
			}
		}
	}
	
	/*
	 * performs the appropriate action for which button you click on
	 * */
	private void clickOnButtons() {
		if(mouseOver(attackBounds)) {
			
			card.setInDefense(false);
			
			card.setUsedAction(true);
		}
		
		else if(mouseOver(defenseBounds)) {
			DuelingState.actionHandler.getAction(ActionList.DEFEND).performAction(card);
		}
		
		else if(mouseOver(tributeBounds)) {
			DuelingState.actionHandler.getAction(ActionList.TRIBUTE).performAction(card,getDuelingState().getPlayer(),spot,getDuelingState().getBoard(),getDuelingState().getBoard().getPlayerField());
		}
		
		else if(mouseOver(effectBounds)) {
			DuelingState.actionHandler.getAction(ActionList.ACTIVATE_MONSTER_EFFECT).performAction(card,getDuelingState().getPlayer(),getDuelingState().getOpponent(),getDuelingState().getBoard());
		}
		
		displaying = false;
		
		
	}
	
	/*sets where the buttons are going to be*/
	public void setButtonBounds() {
		Spot baseSpot = getDuelingState().getBoard().getPlayerField().getMonsterSpots().get(0);
		//the bounds
		setBounds(new Rectangle(
				(int) (getDuelingState().getBoard().getBounds().x + getDuelingState().getBoard().getBounds().width/5),
				(int) (baseSpot.getBounds().y - baseSpot.getBounds().height/2 - baseSpot.getBounds().height/2),
				(int) (getDuelingState().getBoard().getBounds().width/1.8),
				(int) (baseSpot.getBounds().height/1.3)
						));
		//the buttons
		attackBounds = new Rectangle(
				getBounds().x + getBounds().width/25,
				getBounds().y + getBounds().height/8,
				(int)(textFont.getStringBounds("Attack", frc).getWidth()),
				(int)(textFont.getStringBounds("Attack", frc).getHeight()/1.7));
		
		tributeBounds = new Rectangle(
			(getBounds().x + getBounds().width) -( attackBounds.x + attackBounds.width) + (int)(textFont.getStringBounds("Tribute", frc).getWidth()),
			attackBounds.y,
			(int)(textFont.getStringBounds("Tribute", frc).getWidth()),
			(int)(textFont.getStringBounds("Tribute", frc).getHeight()/1.7));
		
		
		defenseBounds = new Rectangle(
			attackBounds.x,attackBounds.y + attackBounds.height + getBounds().height/8  ,
			(int)(textFont.getStringBounds("Defense", frc).getWidth()),
			(int)(textFont.getStringBounds("Defense", frc).getHeight()/1.7)
				);
		
		effectBounds = new Rectangle(
			tributeBounds.x,defenseBounds.y,
			(int)(textFont.getStringBounds("Effect", frc).getWidth()),
			(int)(textFont.getStringBounds("Effect", frc).getHeight()/1.7)
			);
	}
	
	/*update mouse position*/
	public void mouseMoved(MouseEvent e) {
		super.mouseMoved(e);
	}
	/*getters and setters*/
	public boolean isDisplaying() {return displaying;}
	public void setDisplaying(boolean displaying) {this.displaying = displaying;}
	public Card getCard() {return card;}
	public void setCard(Monster card) {this.card = card;}
	
	
}
