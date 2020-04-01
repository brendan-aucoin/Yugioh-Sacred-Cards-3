/*
 *Brendan Aucoin
 *07/06/2019
 *holds all the logic for dueling
 * */
package states;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

import actions.ActionHandler;
import actions.ActionList;
import boards.Board;
import camera.Camera;
import cards.MagicCard;
import cards.Monster;
import dueling.ActivateCardPane;
import dueling.CardInfoPane;
import dueling.Deck;
import dueling.Phase;
import dueling.Spot;
import dueling.StartTurnPhase;
import game.Game;
import gui.Display;
import interaction_pane.InteractionPane;
import player.Ai;
import player.Duelist;
import player.Player;

public class DuelingState extends InteractionPane implements State{
	private Board board;
	private Camera cam;
	private Duelist player;
	private Ai opponent;
	private Rectangle playerHandBounds,opponentHandBounds;
	private BufferedImage backOfCardImage;
	private StartTurnPhase startTurnPhase;
	private ActivateCardPane activateCardPane;
	private CardInfoPane cardInfoPane;
	private Deck originalPlayerDeck,originalOpponentDeck;
	private boolean canScroll;
	private boolean playerGoesFirst;
	
	private boolean canDrag;
	private Spot draggedSpot;
	private List<Spot> draggedSpotList;
	private Point draggedPoint;
	private Rectangle draggingBounds;
	public static int turnCount = 0;
	public static Phase phase;
	public static ActionHandler actionHandler;
	public DuelingState(Game game) {
		super(game,new Rectangle(0,0,Display.SCREEN_SIZE.width,Display.SCREEN_SIZE.height));
		cam = new Camera(0,0);
		init();
	}
	
	/*initializes all the defaults*/
	public void init() {
		super.init();
		opponent = null;
		backOfCardImage = getGame().getImageLoader().loadImage("dueling_images", "backOfCard.png");
		startTurnPhase = new StartTurnPhase(this,getGame());
		cardInfoPane = new CardInfoPane(this,getGame());
		activateCardPane = new ActivateCardPane(this,getGame());
		canScroll = false;
		playerGoesFirst = true;
		canDrag = false;
		draggedSpot = null;
		draggedSpotList = null;
		draggedPoint = new Point();
		draggingBounds = new Rectangle();
		actionHandler = new ActionHandler();
	}
	
	/*needs to do this first after you set the board*/
	public void setHandBounds() {
		playerHandBounds = new Rectangle(0,
		getBounds().height - getBounds().height/10,
		getBounds().width,getBounds().height/5);
		
		opponentHandBounds = new Rectangle(0,
		(int) (board.getOpponentField().getBounds().y)
		,getBounds().width,getBounds().height/5);
		
	}
	/*this is called every time you talk to an enemy */
	public void startDuel() {
		//sets the boards players
		board.setPlayer(player);
		board.setOpponent(opponent);
		//keep track of the original decks during the duel because at the end of the duel you want to reset the players deck
		originalPlayerDeck = new Deck(player.getDeck(),"Original Player Deck");
		originalOpponentDeck = new Deck(opponent.getDeck(),"Original Opponent Deck");
		activateCardPane.setButtonBounds();//the panel for activating cards needs to have the proper button boundaries
		//create the hand bounds of both players
		player.createHandBounds(playerHandBounds);
		opponent.createHandBounds(opponentHandBounds);
		//initialize both players hands which will create the spots
		player.getHand().init();
		opponent.getHand().init();
		
		//always start by drawing 5 cards
		player.draw(5);
		opponent.draw(5);
		//decide which player goes first 
		decideOrder();
		phase = Phase.START_TURN;
	}
	public void endDuel() {
		board.setPlayer(null);
		board.setOpponent(null);
		
		player.setDeck(originalPlayerDeck);
		player.setHealth(Player.STARTING_HEALTH);
		opponent.setDeck(originalOpponentDeck);
		opponent.setHealth(Player.STARTING_HEALTH);
		
		player.createHandBounds(null);
		opponent.createHandBounds(null);
		phase = null;
	}
	/*a 50 50 chance of the player going first and then change the turn*/
	private void decideOrder() {
		playerGoesFirst =  new Random().nextInt(100) <50 ? true: false;
		
		Duelist firstPlayer = null;
		if(playerGoesFirst) {
			firstPlayer = player;
			changeTurn(player);
		}
		else {
		firstPlayer = opponent;
		changeTurn(opponent);
		}
		String nameSuffix = firstPlayer.getName().charAt(0) == 's' || firstPlayer.getName().charAt(0) == 'S' ? "'": "'s";
		startTurnPhase.setText("Its " + firstPlayer.getName() + nameSuffix + " turn");
		
		startTurnPhase.setStart(true);//start the start turn phase pop up
	}
	/*uses the start turn phase pop up to show the next player*/
	public void changeTurn(Duelist player) {
		startTurnPhase.setNextPlayer(player);
	}
	/*you want to update the board and the activate card pop up*/
	public void update() {
		if(checkIfOver()) {
			decideWinner();
		}
		board.update();
		activateCardPane.update();
		if(phase == Phase.START_TURN) {
			startTurnPhase.update();
		}
		//if its the AI's turn then call the update function for the Ai and make it so you cant scroll
		else if(phase == Phase.AI_TURN) {
			opponent.update();
			canScroll = false;
		}
	}
	/*checks if the game is over by seeing the health of both players*/
	private boolean checkIfOver() {
		return player.getHealth() <=0 || opponent.getHealth()<=0;
	}
	/*returns the player who won*/
	private Duelist decideWinner() {
		if(player.getHealth() <=0) {return opponent;}
		else if(opponent.getHealth()<=0) {return player;}
		return null;
	}
	
	@Override
	/*call mouse moved for all sub components*/
	public void mouseMoved(MouseEvent e) {
		super.mouseMoved(e);
		setMouseY((int) (getMouseY() + -(cam.getY())));
		cardInfoPane.mouseMoved(e);
		activateCardPane.mouseMoved(e);
	}
	
	@Override
	/*if you click on your hand*/
	public void mousePressed(MouseEvent e) {
		//when you click on your hand
		if(!activateCardPane.isDisplaying()) {
			if(mouseOver(player.getHand().getBounds())) {
				clickOnHand(e);
			}
		}
		//click on end turn and switch the phase if it is ur turn 
		if(canScroll && mouseOver(board.getPlayerEndTurnBounds()) && phase == Phase.PLAYERS_TURN) {
			//phase = Phase.AI_TURN;
			//perform the action for switching turns here
			actionHandler.getAction(ActionList.END_TURN).performAction(player,board.getPlayerField(),Phase.AI_TURN);
		}
		
		activateCardPane.mousePressed(e);
		
		
	}
	
	/*loop through the players hand 
	 * if the player hasnt played a card
	 * if you click on a full spot set the dragged spot list to be either monster or magic 
	 * */
	private void clickOnHand(MouseEvent e) {
			for(int i =0; i < player.getHand().getSpots().size();i++) {
				Spot spot = player.getHand().getSpots().get(i);
				if(!player.hasPlayedCard() || (spot.getCard() instanceof MagicCard)) {
					if(mouseOver(spot.getBounds())){
						if(spot.hasCard()) {
							if(spot.getCard() instanceof Monster) {
								Monster monster = (Monster)spot.getCard();
								if(player.getNumTributes() < monster.getTributeCost()) {
									continue;
								}
							}
							canDrag = true;
							draggedSpot = spot;
							if(draggedSpot.getCard() instanceof Monster) {
								draggedSpotList = board.getPlayerField().getMonsterSpots();
							}
							else if(draggedSpot.getCard() instanceof MagicCard) {
								draggedSpotList= board.getPlayerField().getMagicSpots();
							}
							
						}
					}
			}
			
		}
	}
	
	@Override
	/*if you are dragging then try and play your card*/
	public void mouseReleased(MouseEvent e) {
		if(canDrag) {
		if(draggedSpotList == null) {return;}
		canDrag = false;
		playCardFromHand();
		}
		
	}
	
	/*loop through whichever dragged spot list is initialized 
	 * if you mouse is inside the bounds then summon that card */
	private void playCardFromHand() {
		for(int i =0; i < draggedSpotList.size();i++) {
			Spot boardSpot = draggedSpotList.get(i);
			if(inside(draggingBounds,boardSpot.getBounds())) {
				actionHandler.getAction(ActionList.PLAY_CARD_FROM_HAND).performAction(player,draggedSpot,boardSpot,board,board.getPlayerField());	
				break;
			}
		}
		//set everything back to default
		draggedSpot = null;
		draggedSpotList = null;
		draggedPoint = new Point();
		draggingBounds = new Rectangle();
	}
	/*creates a point for your dragging when you move the mouse*/
	@Override
	public void mouseDragged(MouseEvent e) {
		draggedPoint = e.getPoint();
		draggingBounds = new Rectangle(draggedPoint.x - player.getHand().getSpots().get(0).getBounds().width/2,
				(int) (draggedPoint.y - player.getHand().getSpots().get(0).getBounds().height/2 + -(cam.getY())),
				player.getHand().getSpots().get(0).getBounds().width,player.getHand().getSpots().get(0).getBounds().height
				);
	}
	
	/*scrolling the mouse*/
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(!canScroll) {return;}
		int notches = e.getWheelRotation();
		//mouse wheel moving up
		 if (notches < 0 && !(cam.getY() > -(opponent.getHand().getBounds().y +  opponent.getHand().getBounds().height/20))) {
			 cam.setY(cam.getY() + 20f);
		 }
		 //mouse wheel moving down
		 else if(notches > 0 && !( cam.getY()  < -(player.getHand().getBounds().y +  player.getHand().getBounds().height*8)/20)) {
			 cam.setY(cam.getY() - 20f);
			
		 }
	}
	
	/*will check if a rectangle is perfectly inside a larger rectangle*/
	private boolean inside(Rectangle smaller,Rectangle larger) {
		return smaller.x > larger.x &&
		smaller.x + smaller.width < larger.x + larger.width &&
		smaller.y > larger.y &&
		smaller.y + smaller.height < larger.y + larger.height;
	}
	
	/*translate the camera and render all components*/
	public void render(Graphics2D g) {
		g.translate(cam.getX(), cam.getY());
		renderDuelState(g);
		renderAllCards(g);///all cards on every field, and every hand
		renderDraggedCards(g);//the current dragged card needs to be rendered
		activateCardPane.render(g);
		g.translate(-cam.getX(), -cam.getY());
		renderOutsideOfCamera(g);//not affected by camera
		
	}
	/*renders the board and the players hands, and all the effects on the hand and board. (colours)*/
	private void renderDuelState(Graphics2D g) {
		renderBoard(g);
		player.getHand().render(g);
		opponent.getHand().render(g);
		renderEffectsOnHand(g);
		renderEffectsOnBoard(g);
	}
	/*render the start turn pop up if it is that phase*/
	private void renderGraphicalEffects(Graphics2D g) {
		if(phase == Phase.START_TURN) {
			startTurnPhase.render(g);
		}
	}
	
	/*you want to render the card info and the other graphical effects outside of the camera*/
	private void renderOutsideOfCamera(Graphics2D g) {
		cardInfoPane.render(g);
		renderGraphicalEffects(g);
		
	}
	/*if you are currently dragging draw the cards image based on the dragged point*/
	private void renderDraggedCards(Graphics2D g) {
		if(canDrag) {
			if(draggedSpot == null) {return;}
			g.drawImage(draggedSpot.getCard().getImage(), draggingBounds.x,draggingBounds.y,
			draggingBounds.width,draggingBounds.height,null);
		}
	}
	
	/*render the black or blue colours that you see*/
	private void renderEffectsOnBoard(Graphics2D g) {
		if(!canScroll) {return;}
		for(int i =0; i < board.getPlayerField().allCardSpots().size();i++) {
			Spot spot = board.getPlayerField().allCardSpots().get(i);
			if(spot.hasCard()) {
				if(mouseOver(spot.getBounds())) {
					if(spot.getCard().hasUsedAction()) {
						g.setColor(Color.BLACK);
						g.setStroke(new BasicStroke(6));
						g.drawRect(spot.getBounds().x, spot.getBounds().y, spot.getBounds().width, spot.getBounds().height);
					}
					else {
						renderCornerHover(g,spot.getBounds(),new Color(0, 102,204),5);
					}
				}
			}
		}
	}
	
	/*same as rendering effects on the board*/
	private void renderEffectsOnHand(Graphics2D g) {
		if(!canScroll) {return;}
		for(int i =0; i < player.getHand().getSpots().size();i++) {
			Spot spot = player.getHand().getSpots().get(i);
			Rectangle bounds = player.getHand().getSpots().get(i).getBounds();
			if(player.getHand().getSpots().get(i).hasCard()) {
				if(mouseOver(getMouseX(),getMouseY(),bounds)) {
					//to make sure you have enough tributes to play this card
					Monster monster = null;
					if(spot.getCard() instanceof Monster) {
						monster = (Monster) spot.getCard();
						if(player.getNumTributes() < monster.getTributeCost()) {
							renderBlackEffectOnHand(g,bounds);
							continue;
						}
					}
					if((player.hasPlayedCard()) && !(spot.getCard() instanceof MagicCard)) {
						renderBlackEffectOnHand(g,bounds);
					}
					
					else {
						renderCornerHover(g,bounds,new Color(0, 102,204),5);
					}
	
				}
			}
		}
	}
	private void renderBlackEffectOnHand(Graphics2D g,Rectangle bounds) {
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(8));
		g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
	}
	
	/*render everything on the board except cards*/
	private void renderBoard(Graphics2D g) {
		board.render(g);
		g.setColor(Color.WHITE);
		if(phase == Phase.AI_TURN) {
			g.setColor(Color.GRAY);
		}
		if(!(phase == Phase.AI_TURN) && canScroll) {
			this.changeColour(g, board.getPlayerEndTurnBounds(), new Color(220,220,220), Color.WHITE);
		}
		g.fill(board.getPlayerEndTurnBounds());
		
		Color aiColour = phase == Phase.AI_TURN ?  Color.WHITE : Color.GRAY;
		g.setColor(aiColour);
		g.fill(board.getOpponentEndTurnBounds());
		
		g.setFont(new Font("ariel",Font.PLAIN,30));
		g.setColor(Color.BLACK);
		g.drawString("END TURN",
		board.getPlayerEndTurnBounds().x + board.getPlayerEndTurnBounds().width/2 -  (int)(g.getFont().getStringBounds("END TURN", frc).getWidth()/2),
		board.getPlayerEndTurnBounds().y +board.getPlayerEndTurnBounds().height -  (int)(g.getFont().getStringBounds("END TURN", frc).getHeight()/3));
	
		
		g.drawString("END TURN",
		board.getOpponentEndTurnBounds().x + board.getOpponentEndTurnBounds().width/2 -  (int)(g.getFont().getStringBounds("END TURN", frc).getWidth()/2),
		board.getOpponentEndTurnBounds().y +board.getOpponentEndTurnBounds().height -  (int)(g.getFont().getStringBounds("END TURN", frc).getHeight()/3));
			
	}
	/*render the blue corner hover effect*/
	private void renderCornerHover(Graphics2D g,Rectangle bounds,Color col,int strokeWidth) {
		g.setColor(col);
		g.setStroke(new BasicStroke(strokeWidth));
		//top left
		//horizontal
		g.drawLine(bounds.x -bounds.width/10, bounds.y - bounds.height/12, bounds.x + bounds.width/5,bounds.y - bounds.height/12);
		//vertical
		g.drawLine(bounds.x -bounds.width/10, bounds.y - bounds.height/12, bounds.x -bounds.width/10,bounds.y + bounds.height/7);
		
		//top right
		//horizontal
		g.drawLine(bounds.x + bounds.width -bounds.width/5, bounds.y - bounds.height/12, bounds.x + bounds.width +  bounds.width/7,bounds.y - bounds.height/12);
		//vertical
		g.drawLine(bounds.x + bounds.width +  bounds.width/7, bounds.y - bounds.height/14, bounds.x + bounds.width +  bounds.width/7,bounds.y + bounds.height/7);
		
		//bottom left
		//vertical
		g.drawLine(bounds.x -bounds.width/10, bounds.y + bounds.height -bounds.height/8,bounds.x -bounds.width/10,bounds.y + bounds.height + bounds.height/12);
		
		//horizontal
		g.drawLine(bounds.x -bounds.width/10, bounds.y + bounds.height + bounds.height/12,bounds.x + bounds.width/5,bounds.y + bounds.height + bounds.height/12);
		
		//bottom right
		//vertical
		g.drawLine(bounds.x + bounds.width +  bounds.width/9, bounds.y + bounds.height -bounds.height/8, bounds.x + bounds.width +  bounds.width/9,bounds.y + bounds.height + bounds.height/12);
		
		//horizontal
		g.drawLine(bounds.x + bounds.width -bounds.width/5,bounds.y + bounds.height + bounds.height/12,  bounds.x + bounds.width +  bounds.width/12,bounds.y + bounds.height + bounds.height/12);
		
	}
	
	/*renders all cards*/
	private void renderAllCards(Graphics2D g) {
		//the player field for monsters
		renderMonstersOnBoard(g,board.getPlayerField().getMonsterSpots());
		//the player field for magic
		renderCardsInSpot(g,board.getPlayerField().getMagicSpots());
		//the player field for hand
		renderPlayerCardsInHand(g);
		
		//the opponent field for monsters
		renderMonstersOnBoard(g,board.getOpponentField().getMonsterSpots());
		//the opponent field for magic
		renderCardsInSpot(g,board.getOpponentField().getMagicSpots());
		
		//the opponent field for hand
		renderCardsInSpot(g, opponent.getHand().getSpots());//you dont want to see the opponents cards
	//	renderOpponentCardsInHand(g);
		
		renderEffectsOnCards(g);
	}
	
	/*render all cards in a list of spots*/
	private void renderCardsInSpot(Graphics2D g,List<Spot> list) {
		for(int i =0; i < list.size();i++) {
			Spot s = list.get(i);
			s.render(g);
		}
	}
	/*render all the monsters on the board and makes sure its in defense and draws the proper image*/
	private void renderMonstersOnBoard(Graphics2D g,List<Spot>spots) {
		for(int i =0; i < spots.size();i++) {
			Spot spot = spots.get(i);
			if(spot.getCard() instanceof Monster) {
				Monster monster = (Monster)spot.getCard();
				if(monster.isInDefense()) {
					// The required drawing location
					int drawLocationX = spot.getBounds().x;
					int drawLocationY = spot.getBounds().y - spot.getBounds().height/6;

					// Rotation information

					double rotationRequired = Math.toRadians (90);
					double locationX = spot.getBounds().getWidth()/1.5;
					double locationY = spot.getBounds().getHeight();
					AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
					AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

					// Drawing the rotated image at the required drawing locations
					g.drawImage(op.filter(spot.getCard().getImage(), null), drawLocationX, drawLocationY, spot.getBounds().width,spot.getBounds().height, null);
				}
				else {
					spot.render(g);
				}
			}
		}
	}
	/*drawing the back of card image for all the opponents cards in their hand*/
	private void renderOpponentCardsInHand(Graphics2D g) {
		for(int i = 0; i < opponent.getHand().getSpots().size();i++) {
			Spot spot = opponent.getHand().getSpots().get(i);
			g.drawImage(backOfCardImage,spot.getBounds().x,spot.getBounds().y,spot.getBounds().width,spot.getBounds().height,null);
		}
	}
	
	/*renders all the cards in your hand*/
	private void renderPlayerCardsInHand(Graphics2D g) {
		for(int i =0; i < player.getHand().getSpots().size();i++) {
			Spot s = player.getHand().getSpots().get(i);
			if(s != draggedSpot) {
				s.render(g);
			}
		}
	}
	
	/*draws the E if the card has been used already*/
	private void renderEffectsOnCards(Graphics2D g) {
		for(int i =0; i < board.allMonsterSpots().size();i++) {
			Spot spot = board.allMonsterSpots().get(i);
			if(spot.hasCard()) {
				if(spot.getCard().hasUsedAction()) {
					g.setFont(new Font("ariel",Font.BOLD,30));
					g.setColor(new Color(30,144,255));
					g.fillRect(spot.getBounds().x, spot.getBounds().y+ spot.getBounds().height - spot.getBounds().height/6
					, spot.getBounds().width/6, spot.getBounds().height/6);
					g.setColor(new Color(0,0,128));
					g.drawString("E", spot.getBounds().x +(int)(g.getFont().getStringBounds("E", frc).getWidth()/10), spot.getBounds().y+ spot.getBounds().height);//- (int)(g.getFont().getStringBounds("e", frc).getHeight()));
				}
			}
		}
	}
	
	/*getters and setters*/
	public void setBoard(Board board) {this.board = board;}
	public Board getBoard() {return board;}
	public Duelist getPlayer() {return player;}
	public void setPlayer(Duelist player) {this.player = player;}
	public Duelist getOpponent() {return opponent;}
	public void setOpponent(Ai opponent) {this.opponent = opponent;}
	public Camera getCam() {return cam;}
	public void setCam(Camera cam) {this.cam = cam;}
	public void setCanScroll(boolean canScroll) {this.canScroll = canScroll;}
	public boolean getCanScroll() {return canScroll;}
}
