/*
 * make it so when you scroll up the camera moves to see the rest of the board/hand or opponents field
on all dueling everywhere theres the somewhat transparent black box thing on the bottom of your screen that would go over the camera
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

import boards.Board;
import camera.Camera;
import cards.Card;
import cards.MagicCard;
import cards.Monster;
import dueling.ActivateCardPane;
import dueling.CardInfoPane;
import dueling.Phase;
import dueling.Spot;
import dueling.StartTurnPhase;
import game.Game;
import gui.Display;
import interaction_pane.InteractionPane;
import player.Ai;
import player.Player;

public class DuelingState extends InteractionPane implements State{
	private Board board;
	private Camera cam;
	private Player player;
	private Ai opponent;
	private Rectangle playerHandBounds,opponentHandBounds;
	private BufferedImage backOfCardImage;
	private StartTurnPhase startTurnPhase;
	private ActivateCardPane activateCardPane;
	private CardInfoPane cardInfoPane;
	public static Phase phase;
	private boolean canScroll;
	private boolean playerGoesFirst;
	
	private boolean canDrag;
	private Spot draggedSpot;
	private List<Spot> draggedSpotList;
	private Point draggedPoint;
	private Rectangle draggingBounds;
	public DuelingState(Game game) {
		super(game,new Rectangle(0,0,Display.SCREEN_SIZE.width,Display.SCREEN_SIZE.height));
		cam = new Camera(0,0);
		init();
		
	}
	
	public void setHandBounds() {
		playerHandBounds = new Rectangle(0,
		getBounds().height - getBounds().height/10,
		getBounds().width,getBounds().height/5);
		
		opponentHandBounds = new Rectangle(0,
		(int) (board.getOpponentField().getBounds().y)
		,getBounds().width,getBounds().height/5);
		
	}
	
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
		
	}
	
	public void startDuel() {
		board.setPlayer(player);
		board.setOpponent(opponent);
		
		activateCardPane.setButtonBounds();
		
		player.createHandBounds(playerHandBounds);
		opponent.createHandBounds(opponentHandBounds);
		player.getHand().init();
		opponent.getHand().init();
		
		player.draw(5);
		opponent.draw(5);
		
		decideOrder();
		phase = Phase.START_TURN;
	}
	
	private void decideOrder() {
		playerGoesFirst =  new Random().nextInt(100) <50 ? true: false;
		
		Player firstPlayer = null;
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
		
		startTurnPhase.setStart(true);
	}
	
	public void changeTurn(Player player) {
		startTurnPhase.setNextPlayer(player);
	}
	
	public void update() {
		board.update();
		activateCardPane.update();
		if(phase == Phase.START_TURN) {
			startTurnPhase.update();
		}
		else if(phase == Phase.AI_TURN) {
			opponent.update();
			canScroll = false;
			
		}
	}
	
	
	
	@Override
	public void mouseMoved(MouseEvent e) {
		super.mouseMoved(e);
		setMouseY((int) (getMouseY() + -(cam.getY())));
		cardInfoPane.mouseMoved(e);
		activateCardPane.mouseMoved(e);
	}
	@Override
	public void mousePressed(MouseEvent e) {
		if(!activateCardPane.isDisplaying()) {
			if(mouseOver(player.getHand().getBounds())) {
				clickOnHand(e);
			}
		}
		if(canScroll && mouseOver(board.getPlayerEndTurnBounds()) && phase == Phase.PLAYERS_TURN) {
			phase = Phase.AI_TURN;
		}
		activateCardPane.mousePressed(e);
		
		
	}
	
	private void clickOnHand(MouseEvent e) {
			for(int i =0; i < player.getHand().getSpots().size();i++) {
				Spot spot = player.getHand().getSpots().get(i);
				if(!player.hasPlayedCard() || (spot.getCard() instanceof MagicCard)) {
					if(mouseOver(spot.getBounds())){
						if(spot.hasCard()) {
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
	public void mouseReleased(MouseEvent e) {
		if(canDrag) {
		if(draggedSpotList == null) {return;}
		canDrag = false;
		playCardFromHand();
		}
		
	}
	
	private void playCardFromHand() {
		for(int i =0; i < draggedSpotList.size();i++) {
			Spot boardSpot = draggedSpotList.get(i);
			if(inside(draggingBounds,boardSpot.getBounds())) {	
					boolean playedCard = summonCard(draggedSpot.getCard(),boardSpot,board.getPlayerField().getField(),player);
					if(playedCard) {
						setPlayedCard(player,draggedSpot.getCard());
						player.playCardFromHand(draggedSpot);
					}
					
					
					break;
			}
		}
		draggedSpot = null;
		draggedSpotList = null;
		draggedPoint = new Point();
		draggingBounds = new Rectangle();
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		draggedPoint = e.getPoint();
		draggingBounds = new Rectangle(draggedPoint.x - player.getHand().getSpots().get(0).getBounds().width/2,
				(int) (draggedPoint.y - player.getHand().getSpots().get(0).getBounds().height/2 + -(cam.getY())),
				player.getHand().getSpots().get(0).getBounds().width,player.getHand().getSpots().get(0).getBounds().height
				);
	}
	
	public void setPlayedCard(Player player,Card c) {
		if(!(c instanceof MagicCard)) {
			player.setPlayedCard(true);
		}
	}
	public boolean summonCard(Card card,Spot spot,List<Card> field,Player player) {
			if(spot.isOpen()) {
				if(card instanceof Monster) {
					Monster monster = (Monster)card;
					if(monster.getTributeCost() <= player.getNumTributes()) {
						player.setNumTributes(player.getNumTributes()-monster.getTributeCost());
						if(player.getNumTributes() <=0) {player.setNumTributes(0);}
					}
					else {
						return false;
					}
				}
				spot.setCard(card);
				field.add(card);
				spot.setOpen(false);
				board.buffCard(card);
				return true;
			}
	
		return false;
	}
	
	public void removeCardFromBoard(Spot spot) {
		spot.setCard(null);
		spot.setOpen(true);
	}
	
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
	
	private boolean inside(Rectangle smaller,Rectangle larger) {
		return smaller.x > larger.x &&
		smaller.x + smaller.width < larger.x + larger.width &&
		smaller.y > larger.y &&
		smaller.y + smaller.height < larger.y + larger.height;
		}
	
	
	public void render(Graphics2D g) {
		g.translate(cam.getX(), cam.getY());
		renderDuelState(g);
		renderAllCards(g);
		renderDraggedCards(g);
		activateCardPane.render(g);
		g.translate(-cam.getX(), -cam.getY());
		renderOutsideOfCamera(g);
		
	}
	
	private void renderDuelState(Graphics2D g) {
		renderBoard(g);
		player.getHand().render(g);
		opponent.getHand().render(g);
		renderEffectsOnHand(g);
		renderEffectsOnBoard(g);
	}
	
	private void renderGraphicalEffects(Graphics2D g) {
		if(phase == Phase.START_TURN) {
			startTurnPhase.render(g);
		}
	}
	
	private void renderOutsideOfCamera(Graphics2D g) {
		cardInfoPane.render(g);
		renderGraphicalEffects(g);
		
	}
	private void renderDraggedCards(Graphics2D g) {
		if(canDrag) {
			if(draggedSpot == null) {return;}
			g.drawImage(draggedSpot.getCard().getImage(), draggingBounds.x,draggingBounds.y,
			draggingBounds.width,draggingBounds.height,null);
		}
	}
	
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
	
	private void renderEffectsOnHand(Graphics2D g) {
		if(!canScroll) {return;}
		for(int i =0; i < player.getHand().getSpots().size();i++) {
			Spot spot = player.getHand().getSpots().get(i);
			Rectangle bounds = player.getHand().getSpots().get(i).getBounds();
			if(player.getHand().getSpots().get(i).hasCard()) {
				if(mouseOver(getMouseX(),getMouseY(),bounds)) {
					if(player.hasPlayedCard() && !(spot.getCard() instanceof MagicCard)) {
						g.setColor(Color.BLACK);
						g.setStroke(new BasicStroke(8));
						g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
					}
					else {
						renderCornerHover(g,bounds,new Color(0, 102,204),5);
					}
	
				}
			}
		}
	}

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
	
	/*everything to do with drawing*/
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
	
	private void renderCardsInSpot(Graphics2D g,List<Spot> list) {
		for(int i =0; i < list.size();i++) {
			Spot s = list.get(i);
			s.render(g);
		}
		
	}
	
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
	
	private void renderOpponentCardsInHand(Graphics2D g) {
		for(int i = 0; i < opponent.getHand().getSpots().size();i++) {
			Spot spot = opponent.getHand().getSpots().get(i);
			g.drawImage(backOfCardImage,spot.getBounds().x,spot.getBounds().y,spot.getBounds().width,spot.getBounds().height,null);
		}
	}
	
	private void renderPlayerCardsInHand(Graphics2D g) {
		for(int i =0; i < player.getHand().getSpots().size();i++) {
			Spot s = player.getHand().getSpots().get(i);
			if(s != draggedSpot) {
				s.render(g);
			}
		}
	}
	
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
	
	
	public void setBoard(Board board) {this.board = board;}
	public Board getBoard() {return board;}

	public Player getPlayer() {return player;}
	public void setPlayer(Player player) {this.player = player;}
	public Player getOpponent() {return opponent;}

	public void setOpponent(Ai opponent) {this.opponent = opponent;}

	public Camera getCam() {return cam;}

	public void setCam(Camera cam) {this.cam = cam;}
	
	public void setCanScroll(boolean canScroll) {this.canScroll = canScroll;}
	public boolean getCanScroll() {return canScroll;}
}
