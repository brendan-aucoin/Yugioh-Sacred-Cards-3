/*
 *Brendan Acoin
 *07/06/2019
 *the box at the bottom displaying the info of the card, the name, the stats, and the type, etc
 * */
package dueling;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;

import cards.Card;
import cards.MagicCard;
import cards.Monster;
import game.Game;
import gui.Display;
import states.DuelingState;
import types.CardType;

public class CardInfoPane extends EffectPane{
	private float backgroundAlpha;
	private Card displayCard;
	private List<Spot> spotList;
	private BufferedImage swordIconImage,shieldIconImage;
	private Rectangle swordIconBounds,shieldIconBounds,attributeBounds;
	private String attributeText;
	private Color attributeTextColour;
	public CardInfoPane(DuelingState duelingState,Game game) {
		super(game,
			new Rectangle(
					0,(int) (Display.SCREEN_SIZE.height/1.12),Display.SCREEN_SIZE.width,Display.SCREEN_SIZE.height
					)
			,duelingState);
		
		init();
	}
	/*sets all the variables to the default*/
	public void init() {
		super.init();
		backgroundAlpha = 0.52f;
		displayCard = null;
		spotList = null;
		swordIconImage = getGame().getImageLoader().loadImage("dueling_images","swordIcon.png");
		shieldIconImage = getGame().getImageLoader().loadImage("dueling_images","shieldIcon.png");
		swordIconBounds = new Rectangle(getBounds().x + getBounds().width - getBounds().width/12,getBounds().y,
				getBounds().width/35,getBounds().height/35);
		
		shieldIconBounds = new Rectangle(getBounds().x + getBounds().width - getBounds().width/12,
		getBounds().y + swordIconBounds.height + getBounds().height/130,
		getBounds().width/35,getBounds().height/35 );
		
		attributeBounds= new Rectangle(swordIconBounds.x - getBounds().width/6,
				swordIconBounds.y,
				getBounds().width/10,shieldIconBounds.y + shieldIconBounds.height);
		attributeText = "";
	}
	
	/*if the display card is not null then render all the cards info and render the bounds*/
	public void render(Graphics2D g) {
		g.setColor(new Color(119,136,153));
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,backgroundAlpha));
		g.fill(getBounds());
		
		if(displayCard != null) {
			renderName(g);
			
			renderIcons(g);
			renderStats(g);
			renderAttribute(g);
			
		}
	}
	
	/*displays the name of the card*/
	private void renderName(Graphics2D g) {
		String displayName = displayCard.getName();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1));
		g.setColor(Color.BLACK);
		g.setFont(new Font("ariel",Font.PLAIN,25));
		//drawing the name
		g.drawString(displayName, getBounds().x + getBounds().width/60
		, getBounds().y + (int)(g.getFont().getStringBounds(displayName, frc).getHeight()));
	}
	/*displays the attack and defense stat*/
	private void renderStats(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.setFont(new Font("ariel",Font.PLAIN,18));
		if(displayCard instanceof Monster) {
			Monster monster = (Monster)displayCard;
			g.drawString(monster.getAttack()+"", (int)(swordIconBounds.getX() + swordIconBounds.getWidth()*1.2),
					(int)(swordIconBounds.getY() + swordIconBounds.getHeight()/1.5)
			);
			
			g.drawString(monster.getDefense()+"", (int)(shieldIconBounds.getX() + shieldIconBounds.getWidth()*1.2),
					(int)(shieldIconBounds.getY() + shieldIconBounds.getHeight()/1.5)
			);
		}
		
	}
	/*displays the attribute of the card*/
	private void renderAttribute(Graphics2D g) {
		//if the card is a monster then get the abbreiation of the monsters attribute
		if(displayCard instanceof Monster) {
			Monster displayMonster = (Monster)displayCard;
			switch(displayMonster.getAttribute()) {
			case LIGHT:
				g.setColor(new Color(224,255,255));
				attributeText = "LGT";
				break;
			case DARK:
				g.setColor(Color.BLACK);
				attributeText = "DARK";
				break;
				
			case FIEND:
				g.setColor(new Color(139,0,139));
				attributeText = "FND";
				break;
			case DREAM:
				g.setColor(new Color(216,191,216));
				attributeText = "DRM";
				break;
				
			case FIRE:
				g.setColor(new Color(204,0,0));
				attributeText = "FIRE";
				break;
			
			case GRASS:
				g.setColor(new Color(0,204,0));
				attributeText = "FOR";
				break;
				
			case WATER:
				g.setColor(new Color(0,0,204));
				attributeText = "WAT";
				break;
			case ELECTRIC:
				g.setColor(new Color(255,255,0));
				attributeText = "THR";
				break;
				
			case EARTH:
				g.setColor(new Color(139,69,19));
				attributeText = "ERT";
				break;
				
			case WIND:
				g.setColor(new Color(127,255,212));
				attributeText = "WND";
				break;
				
			case DIVINE:
				g.setColor(new Color(75,0,130));
				attributeText = "DIV";
				break;
			}
		}
		
		//if its a magic card then find out if its a spell or trap
		else if (displayCard instanceof MagicCard) {
			g.setColor(new Color(0,102,0));
			attributeText = displayCard.getType() == CardType.SPELL ? "SPELL": "TRAP";
		}
		//display the text
		g.fill(attributeBounds);
		attributeTextColour = displayCard instanceof MagicCard ? new Color(199,21,133):Color.WHITE;
		g.setColor(attributeTextColour);
		g.setFont(new Font("ariel",Font.PLAIN,20));
		g.drawString(attributeText,
			(int) (attributeBounds.x + attributeBounds.width/2 - g.getFont().getStringBounds(attributeText, frc).getWidth()/2),
			(int) (attributeBounds.y + attributeBounds.height/30 + g.getFont().getStringBounds(attributeText, frc).getHeight()/2.5)
				);
		
	}
	//display the two icons
	private void renderIcons(Graphics2D g) {
		if(displayCard instanceof Monster) {
		g.drawImage(swordIconImage,swordIconBounds.x,swordIconBounds.y,
				swordIconBounds.width,swordIconBounds.height,null);
		
		g.drawImage(shieldIconImage,shieldIconBounds.x,shieldIconBounds.y,
				shieldIconBounds.width,shieldIconBounds.height,null);
		}
	}
	
	@Override
	/*determine which list to loop through based on if you mouse is over your hand, or which players field*/
	public void mouseMoved(MouseEvent e) {
		super.mouseMoved(e);
		if(mouseOver(getDuelingState().getPlayer().getHand().getBounds())) {
			spotList =  getDuelingState().getPlayer().getHand().getSpots();
			
		}
		else if(mouseOver(getDuelingState().getBoard().getPlayerField().getBounds())){
			spotList = getDuelingState().getBoard().getPlayerField().allCardSpots();
			
		}
		else if(mouseOver(getDuelingState().getBoard().getOpponentField().getBounds())) {
			spotList =  getDuelingState().getBoard().getOpponentField().allCardSpots();
		}
		if(spotList == null) {return;}
		//loop through that spot list and find out which spot you are hovering over and set the display card to whatever card you find
		for(int i=0; i < spotList.size();i++) {
			Spot spot = spotList.get(i);
			if(mouseOver(spot.getBounds()) && !spot.isOpen()) {
				displayCard = spot.getCard();
			}
		}
	}
	
}
