/*
 *Brendan Aucoin
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
	private boolean aiMagicField;
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
		aiMagicField = false;
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
			if(aiMagicField && !displayCard.isRevealed()) {return;}
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
		if(aiMagicField && displayCard instanceof MagicCard ) {return;}
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
			g.setColor(displayMonster.getAttribute().getCol());
			switch(displayMonster.getAttribute()) {
			case LIGHT:
				attributeText = "LGT";
				attributeTextColour = Color.BLACK;
				break;
			case DARK:
				attributeText = "DARK";
				attributeTextColour = Color.WHITE;
				break;
				
			case FIEND:
				attributeText = "FND";
				attributeTextColour = Color.WHITE;
				break;
			case DREAM:
				attributeText = "DRM";
				attributeTextColour = Color.BLACK;
				break;
				
			case FIRE:
				attributeText = "FIRE";
				attributeTextColour = Color.WHITE;
				break;
			
			case GRASS:
				attributeText = "FOR";
				attributeTextColour = Color.WHITE;
				break;
				
			case WATER:
				attributeText = "WAT";
				attributeTextColour = Color.WHITE;
				break;
			case ELECTRIC:
				attributeText = "THR";
				attributeTextColour = Color.WHITE;
				break;
				
			case EARTH:
				attributeText = "ERT";
				attributeTextColour = Color.WHITE;
				break;
				
			case WIND:
				attributeText = "WND";
				attributeTextColour = Color.BLACK;
				break;
				
			case DIVINE:
				attributeText = "DIV";
				attributeTextColour = Color.WHITE;
				break;
			
			default:
				g.setColor(Color.BLACK);
				attributeText = "NON";
				attributeTextColour = Color.WHITE;	
			}
			
		}
		
		
		//if its a magic card then find out if its a spell or trap
		else if (displayCard instanceof MagicCard) {
			g.setColor(new Color(0,102,0));
			attributeText = displayCard.getType() == CardType.SPELL ? "SPELL": "TRAP";
			if(aiMagicField) {attributeText = "MAG";}
		}
		//display the text
		g.fill(attributeBounds);
		attributeTextColour = displayCard instanceof MagicCard ? new Color(199,21,133):attributeTextColour;
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
		aiMagicField = false;
		if(mouseOver(getDuelingState().getPlayer().getHand().getBounds())) {
			spotList =  getDuelingState().getPlayer().getHand().getSpots();
			
		}
		else if(mouseOver(getDuelingState().getBoard().getPlayerField().getBounds())){
			spotList = getDuelingState().getBoard().getPlayerField().allCardSpots();
			
		}
		else if(mouseOver(getDuelingState().getBoard().getOpponentField().getBounds())) {
			spotList =  getDuelingState().getBoard().getOpponentField().allCardSpots();
			aiMagicField = true;
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
