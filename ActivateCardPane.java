package dueling;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

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
	
	public void init() {
		super.init();
		displaying = false;
		card = null;
		spot = null;
		textFont = new Font("ariel",Font.PLAIN,55);
		milleniumEyeImage = getGame().getImageLoader().loadImage("dueling_images","milleniumEye.png");
		textPopupPane = new TextPopupPane(getDuelingState(),getGame());
	}

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
	public void render(Graphics2D g) {
		if(displaying && getBounds()!=null) {
			renderBox(g);
			renderButtons(g);
		}
		if(textPopupPane.isStart()) {
			textPopupPane.render(g);
		}
	}
	
	private void renderBox(Graphics2D g) {
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.78f));
		g.setColor(Color.BLACK);
		g.fill(getBounds());
		
	}
	
	private void renderButtons(Graphics2D g) {
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
		g.setFont(textFont);
		renderButton(g,attackBounds,"Attack",!card.hasUsedAction());
		renderButton(g,tributeBounds,"Tribute",!getDuelingState().getPlayer().hasPlayedCard());
		renderButton(g,defenseBounds,"Defense",!card.isInDefense());
		if(card instanceof EffectMonster) {
			EffectMonster effectCard = (EffectMonster)card;
			renderButton(g,effectBounds,"Effect",!effectCard.hasUsedEffect());
		}
		else {
			renderButton(g,effectBounds,"Effect",false);
		}
	}
	
	private void renderButton(Graphics2D g, Rectangle bounds,String text,boolean condition) {
		g.setColor(Color.WHITE);
		if(mouseOver(bounds)) {
			g.setColor(Color.RED);
		}
		if(!condition && mouseOver(bounds)) {
			g.setColor(Color.GRAY);
		}
		g.drawString(text,bounds.x,bounds.y + bounds.height);

		if(mouseOver(bounds)) {
			g.drawImage(milleniumEyeImage, bounds.x - getBounds().width/40,bounds.y + bounds.height/2 - milleniumEyeImage.getHeight()/2 ,milleniumEyeImage.getWidth(),milleniumEyeImage.getHeight(),null);
		}
	}
	
	public void mousePressed(MouseEvent e) {
		if(!displaying) {
			activateMonster();
			
			activateSpell();
		}
		
		else {
			clickOnButtons();
		}
	}
	
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
	
	private void activateSpell() {
		for(int i =0; i < getDuelingState().getBoard().getPlayerField().getMagicSpots().size();i++) {
			Spot spot = getDuelingState().getBoard().getPlayerField().getMagicSpots().get(i);
			if(mouseOver(spot.getBounds())) {
				Card c = spot.getCard();
				if(c instanceof MagicCard && c.getType() == CardType.SPELL) {
					MagicCard spell = (MagicCard)c;
					textPopupPane.setText(spell.effectText());
					textPopupPane.setTime(95);
					textPopupPane.setStart(true);
					getDuelingState().removeCardFromBoard(spot);
				}
			}
		}
	}
	
	private void clickOnButtons() {
		if(mouseOver(attackBounds)) {
			
			card.setInDefense(false);
			
			card.setUsedAction(true);
		}
		
		else if(mouseOver(defenseBounds)) {
			if(!card.isInDefense()) {
				card.setInDefense(true);
				card.setUsedAction(true);
			}
		}
		
		else if(mouseOver(tributeBounds)) {
			if(!getDuelingState().getPlayer().hasPlayedCard()) {
			
			getDuelingState().getPlayer().setNumTributes(getDuelingState().getPlayer().getNumTributes()+1);
			getDuelingState().removeCardFromBoard(spot);
			}
		}
		
		else if(mouseOver(effectBounds)) {
			if(card instanceof EffectMonster) {
				EffectMonster effectCard = (EffectMonster)card;
				if(!effectCard.hasUsedEffect()) {
					card.setUsedAction(true);
				}
			}
		}
			displaying = false;
		
		
	}
	
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
	
	public void mouseMoved(MouseEvent e) {
		super.mouseMoved(e);
	}

	public boolean isDisplaying() {
		return displaying;
	}

	public void setDisplaying(boolean displaying) {
		this.displaying = displaying;
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Monster card) {
		this.card = card;
	}
	
	
}
