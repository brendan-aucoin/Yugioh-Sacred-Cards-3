/*
 *Brendan Aucoin
 *07/06/2019
 *a board for the dueling state has it is the parent to any board that has special attribute effects for the board
 *renders everything that all boards will have
 * */
package boards;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import cards.Card;
import dueling.Field;
import dueling.Spot;
import game.Game;
import gui.Display;
import player.Duelist;

public abstract class Board {
	private BoardType type;//grass,normal,ghoul, etc.
	//you have two sides of the board the player field(monsters and magic cards included) and the opponent field.
	private Field playerField;
	private Field opponentField;
	private Rectangle bounds;//the entire bounds of the board (the entire screen but a bit more becuase you can scroll)
	protected final Rectangle middleLineBounds;
	private Game game;
	//used only for graphics.
	protected AffineTransform affinetransform;
	protected FontRenderContext frc;
	private Duelist player,opponent;//you need access to the two players
	private Rectangle playerEndTurnBounds,opponentEndTurnBounds;//the two buttons.
	public Board(Game game,BoardType type) {
		this.game = game;
		this.type = type;
		//always the screen size no matter what
		bounds = new Rectangle(0,0,Display.SCREEN_SIZE.width,Display.SCREEN_SIZE.height);
		
		middleLineBounds = new Rectangle(0,(int)(getBounds().height/2 - getBounds().height/7),getBounds().width,getBounds().height/18); 	
		//the two buttons
		playerEndTurnBounds = new Rectangle(middleLineBounds.x + middleLineBounds.width/2 - middleLineBounds.width/6- middleLineBounds.width/20 ,
		middleLineBounds.y,middleLineBounds.width/6,middleLineBounds.height);
		opponentEndTurnBounds = new Rectangle((int) (middleLineBounds.width/2 + middleLineBounds.width/25),
		middleLineBounds.y,middleLineBounds.width/6,middleLineBounds.height);
		
		init();
	}
	/*creates the fields for the player and opponent that are always the same*/
	protected void init() {
		playerField = new Field(new Rectangle(0,middleLineBounds.y + middleLineBounds.height,
		Display.SCREEN_SIZE.width,Display.SCREEN_SIZE.height - middleLineBounds.y + middleLineBounds.height));
		
		opponentField = new Field(new Rectangle(0,middleLineBounds.y - playerField.getBounds().height
		,Display.SCREEN_SIZE.width,playerField.getBounds().height
		));
		affinetransform = new AffineTransform();   
		frc = new FontRenderContext(affinetransform,true,true);
		
		createAllSpots();
	}
	
	/*removes a card from a spot*/
	public void removeCardFromBoard(Spot spot) {
		spot.setCard(null);
		spot.setOpen(true);
	}
	
	/*creates all the bounds for all the spots on the fields*/
	protected void createAllSpots() {
		//monster spots for player
		createRowForSpots(playerField.getMonsterSpots(),middleLineBounds.y + middleLineBounds.height + getBounds().height/50);
		//spell spots for player
		createRowForSpots(playerField.getMagicSpots(),
		middleLineBounds.y + middleLineBounds.height + getBounds().height/50 + (int)(getBounds().height/5) + getBounds().height/40);
		
		//monster spots for opponent
		createRowForSpots(opponentField.getMonsterSpots(),middleLineBounds.y - getBounds().height/50 -(int)(getBounds().height/5));
		//spell spots for opponent
		createRowForSpots(opponentField.getMagicSpots(),
		middleLineBounds.y -  (int)(getBounds().height/5) - getBounds().height/50 -(int)(getBounds().height/5) -  getBounds().height/40);
		
	}
	/*creates the bounds for one list of spots*/
	protected void createRowForSpots(ArrayList<Spot> list, int y) {
		//cant be the list size this method is only used for 1 row (specifically for monter spots or magic spots not both)
		for(int i =0; i < 5; i++ ) {
			Rectangle spotBound = new Rectangle(
			getBounds().width/25 + (int)(getBounds().width/10)*(i*2) ,y,
			(int)(getBounds().width/9),(int)(getBounds().height/5)
			);
			list.add(new Spot(spotBound));
		}
	}
	//all boards update
	public abstract void update();
	//all boards will have some buff to a certain type of card
	public abstract void buffCard(Card c);
	
	//all boards render similar things like the field
	public void render(Graphics2D g) {
		//players monster and magic spots
		renderSpots(g,playerField.getMonsterSpots());
		renderSpots(g,playerField.getMagicSpots());
		//the opponents monster and magic cards
		renderSpots(g,opponentField.getMonsterSpots());
		renderSpots(g,opponentField.getMagicSpots());
	}
	/*draws the outline of a spot to the screen*/
	private void renderSpots(Graphics2D g,ArrayList<Spot> spots) {
		g.setColor(Color.YELLOW);
		for(int i = 0; i < spots.size();i++) {
			g.draw(spots.get(i).getBounds());
		}
	}
	/*drawing text for life points some boards may want to add stuff in though*/
	protected void renderText(Graphics2D g,Color col) {
		//text that goes in the middle border line to show LP and anything else you may need
		g.setColor(col);
		g.setFont(new Font("ariel",Font.BOLD,20));
		//drawing the players LP
		g.drawString("Player LP: " + player.getHealth(),(int) (middleLineBounds.x +  g.getFont().getStringBounds("Player LP: " + player.getHealth(), frc).getWidth()/10),
		(int) (middleLineBounds.y + middleLineBounds.height/2 + g.getFont().getStringBounds("Player LP: " + player.getHealth(), frc).getHeight()/4));
		
		//drawing opponents LP
		g.drawString("Opponent LP: " + opponent.getHealth(),
		(int) (middleLineBounds.x + middleLineBounds.width -  g.getFont().getStringBounds("Opponent LP: " + opponent.getHealth(), frc).getWidth() -
		g.getFont().getStringBounds("Opponent LP: 8000", frc).getWidth()/10),
		(int) (middleLineBounds.y + middleLineBounds.height/2 + g.getFont().getStringBounds("Opponent LP: " + opponent.getHealth(), frc).getHeight()/4));
	}
	
	/*returns a list of all the cards on the board*/
	public ArrayList<Card> allCardsOnBoard(){
		ArrayList<Card> allCards = new ArrayList<Card>();
		for(int i =0; i < playerField.getField().size(); i++) {
			Card c = playerField.getField().get(i);
			allCards.add(c);
		}
		for(int i =0; i < opponentField.getField().size();i++) {
			Card c = opponentField.getField().get(i);
			allCards.add(c);
		}
		return allCards;
	}
	/*returns a list of all the spots on the board*/
	public ArrayList<Spot> allSpots(){
		ArrayList<Spot> allSpots = new ArrayList<Spot>();
		for(int i =0; i < playerField.allCardSpots().size();i++) {
			allSpots.add(playerField.allCardSpots().get(i));
		}
		
		for(int i =0; i < opponentField.allCardSpots().size();i++) {
			allSpots.add(opponentField.allCardSpots().get(i));
		}
		
		return allSpots;
	}
	/*this will be used by all boards but can be added on to but sub classes*/
	protected void renderMiddleArea(Graphics2D g,BasicStroke stroke,Color back,Color line) {
		renderMiddleBar(g,stroke,back,line);
	}
	/*get a list of all the monter spots*/
	public ArrayList<Spot> allMonsterSpots(){
		ArrayList<Spot> allSpots = new ArrayList<Spot>();
		for(int i =0; i < playerField.getMonsterSpots().size();i++) {
			allSpots.add(playerField.getMonsterSpots().get(i));
		}
		
		for(int i =0; i < opponentField.getMonsterSpots().size();i++) {
			allSpots.add(opponentField.getMonsterSpots().get(i));
		}
		return allSpots;
	}
	
	/*renders the middle bar splitting the two fields*/
	protected void renderMiddleBar(Graphics2D g, BasicStroke stroke,Color middleLineCol,Color strokeCol ) {
		g.setColor(middleLineCol);
		g.fill(middleLineBounds);
		g.setColor(strokeCol);
		g.setStroke(stroke);
		g.drawLine((int) (middleLineBounds.x + middleLineBounds.width/2 - stroke.getLineWidth()), middleLineBounds.y,
		(int) (middleLineBounds.x + middleLineBounds.width/2 - stroke.getLineWidth()), middleLineBounds.y + middleLineBounds.height);
	}
	
	/*getters and setters*/
	public BoardType getType() {return type;}
	public void setType(BoardType type) {this.type = type;}
	public Field getPlayerField() {return playerField;}
	public void setPlayerField(Field playerField) {this.playerField = playerField;}
	public Field getOpponentField() {return opponentField;}
	public void setOpponentField(Field opponentField) {	this.opponentField = opponentField;}
	public Rectangle getBounds() {return bounds;}
	public void setBounds(Rectangle bounds) {this.bounds = bounds;}
	public Game getGame() {return game;}
	public Rectangle getPlayerEndTurnBounds() {return playerEndTurnBounds;}
	public void setPlayerEndTurnBounds(Rectangle playerEndTurnBounds) {this.playerEndTurnBounds = playerEndTurnBounds;}
	public Rectangle getOpponentEndTurnBounds() {return opponentEndTurnBounds;}
	public void setOpponentEndTurnBounds(Rectangle opponentEndTurnBounds) {this.opponentEndTurnBounds = opponentEndTurnBounds;}
	public Duelist getPlayer() {return player;}
	public void setPlayer(Duelist player) {this.player = player;}
	public Duelist getOpponent() {return opponent;}
	public void setOpponent(Duelist opponent) {this.opponent = opponent;}
	public Rectangle getMiddleLineBounds() {return middleLineBounds;}
	public AffineTransform getAffinetransform() {return affinetransform;}
	public void setAffinetransform(AffineTransform affinetransform) {this.affinetransform = affinetransform;}
	public FontRenderContext getFrc() {return frc;}
	public void setFrc(FontRenderContext frc) {this.frc = frc;}
}
