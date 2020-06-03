/*
 *Brendan Aucoin
 *05/06/2020
 *a state that lets the user build their deck from cards in their trunk or just view their deck.
 */
package states;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.SwingUtilities;

import attributes.Attribute;
import camera.Camera;
import card_effects.Effect;
import cards.Card;
import cards.EffectMonster;
import cards.Monster;
import cards.SpellCard;
import cards.TrapCard;
import dueling.Deck;
import dueling.Spot;
import game.Game;
import gui.BorderedButton;
import gui.ButtonHandler;
import gui.Display;
import gui.TextField;
import interaction_pane.InteractionPane;
import player.Player;

public class BuildDeckState extends InteractionPane implements State{
	private Player player;
	private Rectangle displayCardBounds;
	private Rectangle currentCardBounds;
	private Rectangle deckBounds;
	private Rectangle trunkBounds;
	private Card currentCard;
	private Rectangle detailsBounds;
	private Rectangle filterBounds;
	private BorderedButton resetFilters,sortByAttack,sortByDefense,sortByCapacity,sortByTribute,filterByMonster,filterBySpell,filterByTrap,filterByEffectMonster;
	private BorderedButton [] attributeButtons;
	private ButtonHandler buttonHandler;
	private Spot [] deckSpots;
	private Spot [] trunkSpots;
	private final int NUM_DECK_SPOTS_HEIGHT = 5;
	private final int NUM_DECK_SPOTS_WIDTH = 8;
	private Deck deck;
	private ArrayList<Card> trunk;
	private ArrayList<Card> originalTrunk;
	private Camera cam;
	private int trunkMouseY;
	private Point draggingPoint;
	private Rectangle draggingBounds;
	private boolean canDrag;
	private Card draggingCard;
	private TextField textField;
	private boolean isFiltering;
	public BuildDeckState(Game game) {
		super(game,Display.FULL_SCREEN);
	}
	
	@Override 
	public void init() {
		super.init();
		displayCardBounds = new Rectangle(
				getBounds().x,getBounds().y,(int) (getBounds().width/3.5),getBounds().height
		);
		currentCardBounds = new Rectangle(
				(displayCardBounds.x + displayCardBounds.width/2) - (int) (displayCardBounds.width/2.6)
				,displayCardBounds.y+displayCardBounds.height/10,
				(int) (displayCardBounds.width/1.3),(int) (displayCardBounds.height/2)
		);
		detailsBounds =	new Rectangle(
				displayCardBounds.x + displayCardBounds.width/40,displayCardBounds.y,displayCardBounds.width - displayCardBounds.width/40,displayCardBounds.height);
		
		deckBounds = new Rectangle(
				displayCardBounds.x + displayCardBounds.width + getBounds().width/30,
				getBounds().y + getBounds().height/8,
				(int) (getBounds().width/2),(int) (getBounds().height - (getBounds().y + getBounds().height/4))
		);
		trunkBounds = new Rectangle(
				deckBounds.x + deckBounds.width + getBounds().width/30,deckBounds.y,
				(getBounds().x + getBounds().width) - (deckBounds.x + deckBounds.width + getBounds().width/40),
				(getBounds().y + getBounds().height) - (deckBounds.y + getBounds().height/25)
		);
		filterBounds = new Rectangle(
				displayCardBounds.x + displayCardBounds.width,
				displayCardBounds.y,(getBounds().x + getBounds().width) - (displayCardBounds.x + displayCardBounds.width),
				deckBounds.y - displayCardBounds.y
		);
		
		
		sortByAttack = new BorderedButton(
				new Rectangle(filterBounds.x + filterBounds.width/40,filterBounds.y + filterBounds.height/20,filterBounds.width/15,filterBounds.height/3),
				new Color(255,222,173),new Color(255,228,225),"ATT",Color.BLACK,Color.BLACK,new Font("ariel",Font.PLAIN,15),
				e -> sortTrunkBy((Card c1,Card c2) -> {
					Monster m1 =(Monster)c1;
					Monster m2 =(Monster)c2;
					return Integer.compare(m1.getAttack(),m2.getAttack());
				}),
				10,10
		);
		sortByDefense = new BorderedButton(
				new Rectangle(sortByAttack.getBounds().x + sortByAttack.getBounds().width + filterBounds.width/40,sortByAttack.getBounds().y,sortByAttack.getBounds().width,sortByAttack.getBounds().height),
				sortByAttack.getBaseCol(),sortByAttack.getHoverCol(),"DEF",sortByAttack.getTextCol(),sortByAttack.getTextHoverCol(),sortByAttack.getFont(),
				e -> sortTrunkBy((Card c1,Card c2) -> {
					Monster m1 =(Monster)c1;
					Monster m2 =(Monster)c2;
					return Integer.compare(m1.getDefense(),m2.getDefense());
				}),
				sortByAttack.getArcWidth(),sortByAttack.getArcHeight()
		);
		sortByCapacity = new BorderedButton(
				new Rectangle(sortByDefense.getBounds().x + sortByDefense.getBounds().width + filterBounds.width/40,sortByDefense.getBounds().y,sortByDefense.getBounds().width,sortByDefense.getBounds().height),
				sortByDefense.getBaseCol(),sortByDefense.getHoverCol(),"CAP",sortByDefense.getTextCol(),sortByDefense.getTextHoverCol(),sortByDefense.getFont(),
				e -> sortTrunkBy((Card c1,Card c2) -> {
					Monster m1 =(Monster)c1;
					Monster m2 =(Monster)c2;
					return Integer.compare(m1.getCapacity(),m2.getCapacity());
				}),
				sortByDefense.getArcWidth(),sortByDefense.getArcHeight()
		);
		sortByTribute = new BorderedButton(
				new Rectangle(sortByCapacity.getBounds().x + sortByCapacity.getBounds().width + filterBounds.width/40,sortByCapacity.getBounds().y,sortByCapacity.getBounds().width,sortByCapacity.getBounds().height),
				sortByCapacity.getBaseCol(),sortByCapacity.getHoverCol(),"TRIB",sortByCapacity.getTextCol(),sortByCapacity.getTextHoverCol(),sortByCapacity.getFont(),
				e -> sortTrunkBy((Card c1,Card c2) -> {
					Monster m1 =(Monster)c1;
					Monster m2 =(Monster)c2;
					return Integer.compare(m1.getTributeCost(),m2.getTributeCost());
				}),
				sortByCapacity.getArcWidth(),sortByCapacity.getArcHeight()
		);
		filterByMonster = new BorderedButton(
				new Rectangle(sortByTribute.getBounds().x + sortByTribute.getBounds().width + filterBounds.width/25,sortByTribute.getBounds().y,filterBounds.width/10,filterBounds.height/3),
				sortByTribute.getBaseCol(),sortByTribute.getHoverCol(),"MONSTER",sortByTribute.getTextCol(),sortByTribute.getTextHoverCol(),new Font("ariel",Font.PLAIN,15),
				e->updateTrunk(originalTrunk.stream().filter(x -> x instanceof Monster).collect(Collectors.toList())),
				sortByTribute.getArcWidth(),sortByTribute.getArcHeight()
		);
		filterBySpell = new BorderedButton(
				new Rectangle(filterByMonster.getBounds().x + filterByMonster.getBounds().width + filterBounds.getBounds().width/35,filterByMonster.getBounds().y,filterByMonster.getBounds().width,filterByMonster.getBounds().height),
				filterByMonster.getBaseCol(),filterByMonster.getHoverCol(),"SPELL",filterByMonster.getTextCol(),filterByMonster.getTextHoverCol(),filterByMonster.getFont(),
				e->updateTrunk(originalTrunk.stream().filter(x -> x instanceof SpellCard).collect(Collectors.toList())),
				filterByMonster.getArcWidth(),filterByMonster.getArcHeight()
		);
		filterByTrap = new BorderedButton(
				new Rectangle(filterBySpell.getBounds().x + filterBySpell.getBounds().width + filterBounds.getBounds().width/35,filterBySpell.getBounds().y,filterBySpell.getBounds().width,filterBySpell.getBounds().height),
				filterBySpell.getBaseCol(),filterBySpell.getHoverCol(),"TRAP",filterBySpell.getTextCol(),filterBySpell.getTextHoverCol(),filterBySpell.getFont(),
				e->updateTrunk(originalTrunk.stream().filter(x -> x instanceof TrapCard).collect(Collectors.toList())),
				filterBySpell.getArcWidth(),filterBySpell.getArcHeight()
		);
		filterByEffectMonster = new BorderedButton(
				new Rectangle(filterByTrap.getBounds().x + filterByTrap.getBounds().width + filterBounds.getBounds().width/35,filterByTrap.getBounds().y,filterByTrap.getBounds().width,filterByTrap.getBounds().height),
				filterByTrap.getBaseCol(),filterByTrap.getHoverCol(),"EFFECT",filterByTrap.getTextCol(),filterByTrap.getTextHoverCol(),filterByTrap.getFont(),
				e->updateTrunk(originalTrunk.stream().filter(x -> x instanceof EffectMonster).collect(Collectors.toList())),
				filterByTrap.getArcWidth(),filterByTrap.getArcHeight()
		);
		resetFilters = new BorderedButton(
				new Rectangle(filterByEffectMonster.getBounds().x + filterByEffectMonster.getBounds().width + filterBounds.getBounds().width/35,filterByEffectMonster.getBounds().y,sortByAttack.getBounds().width,sortByAttack.getBounds().height),
				filterByEffectMonster.getBaseCol(),filterByEffectMonster.getHoverCol(),"RESET",filterByEffectMonster.getTextCol(),filterByEffectMonster.getTextHoverCol(),filterByEffectMonster.getFont(),
				e->updateTrunk(originalTrunk),
				filterByEffectMonster.getArcWidth(),filterByEffectMonster.getArcHeight()
		);
		attributeButtons = new BorderedButton[Attribute.values().length-1];		for(int i =0; i < Attribute.values().length-1;i++) {
			Attribute attribute = Attribute.values()[i];
			Color col = attribute.getCol()==Color.BLACK ? new Color(105,105,105) : attribute.getCol();
			int x = filterBounds.x + filterBounds.width/20 + (i*(filterBounds.width/20 + filterBounds.width/100));
			attributeButtons[i] = new BorderedButton(
				new Rectangle(x,sortByAttack.getBounds().y + sortByAttack.getBounds().height + filterBounds.height/5,filterBounds.width/45,filterBounds.height/5)
				,col,Color.GRAY,"",Color.WHITE,Color.WHITE,new Font("ariel",Font.PLAIN,10),
				e->updateTrunk(originalTrunk.stream().filter(u -> u instanceof Monster && ((Monster)u).getAttribute() == attribute).collect(Collectors.toList())),
				0,0
			);
		}
		//Rectangle bounds,Color borderCol,Color backgroundCol,Color textCol,Color hoverCol,Color cursorCol,Consumer<KeyEvent>keyPress
		textField = new TextField(
				new Rectangle(attributeButtons[attributeButtons.length-1].getBounds().x + attributeButtons[attributeButtons.length-1].getBounds().width + getBounds().width/25,
				trunkBounds.y-getBounds().height/19 ,
				(getBounds().x + getBounds().width) - attributeButtons[attributeButtons.length-1].getBounds().x + attributeButtons[attributeButtons.length-1].getBounds().width + getBounds().width/25,
				getBounds().height/20),
				new Color(95,158,160),Color.GRAY,Color.WHITE,new Color(70,130,180),Color.WHITE,e->searchForCard(e)
				
		);
		buttonHandler = new ButtonHandler(sortByAttack,sortByDefense,sortByCapacity,sortByTribute,filterByMonster,filterBySpell,filterByTrap,filterByEffectMonster,resetFilters);
		buttonHandler.addButtons(attributeButtons);
		deckSpots = new Spot[Deck.MAX_SIZE];
		trunkSpots = new Spot[Deck.MAX_SIZE];
		createDeckSpots();
		deck = new Deck("");
		trunk = new ArrayList<Card>();
		cam = new Camera(0,0);
		trunkMouseY = 0;
		draggingPoint = new Point(0,0);
		draggingBounds = new Rectangle(0,0,0,0);
		canDrag = false;
		draggingCard = null;
		isFiltering = false;
	}
	
	public void makeNewDeck() {
		deck = new Deck("Deck");
	}
	
	public void editDeck(Deck deck) {
		this.deck = deck;
	}
	public void editDeck() {
		this.deck = player.getDeck();
	}
	
	public void startState(Player player) {
		setPlayer(player);
		removeDuplicates();
		this.originalTrunk = new ArrayList<Card>(trunk);
		createTrunk();
	}
	
	@Override
	public void update() {
		if(deckBounds == null || deckSpots == null || trunkBounds == null || cam == null || trunkSpots == null) {return;}
		if(mouseOver(deckBounds)) {
			setCurrentCard(deckSpots,0);
		}
		else if(mouseOver(trunkBounds)) {
			trunkMouseY = (int)(getMouseY() + -(cam.getY()));
			if(trunkSpots!=null) {setCurrentCard(trunkSpots,getMouseY() -trunkMouseY);}
		}
		
		buttonHandler.update(getMouseX(), getMouseY());
		textField.update(getMouseX(), getMouseY());
	}
	
	
	public void createTrunk() {
		this.trunkSpots = new Spot[trunk.size()];
		createTrunkSpots();
		setTrunkCards();
	}
	
	private void createDeckSpots() {
		int counter =0;
		int xOffset = getBounds().width/125;
		int yOffset = getBounds().height/70;
		for(int i=0; i < NUM_DECK_SPOTS_HEIGHT;i++) {
			for(int j = 0; j < NUM_DECK_SPOTS_WIDTH;j++) {
				float width =deckBounds.width/9;
				float height = deckBounds.height/5.5f;
				Rectangle rect = new Rectangle(
						(int)((deckBounds.x+xOffset/2) + (xOffset*j) + (width*j)),
						(int)((deckBounds.y+yOffset/2) + (yOffset*i) + (height*i)),
						(int)width,
						(int)height
				);
				deckSpots[counter] = new Spot(rect);
				counter++;
			}
		}
	}
	
	private void createTrunkSpots() {
		int yOffset = trunkBounds.height/45;
		float width = trunkBounds.width/2.5f;
		float height = trunkBounds.height/6;
		for(int i =0; i < trunkSpots.length;i++) {
			float x = (trunkBounds.x + trunkBounds.width) - (width*1.5f);
			float y = (trunkBounds.y + yOffset/2) + (yOffset*i) + (height*i);
			Rectangle rect = new Rectangle(
					(int)x,
					(int)y,
					(int)width,
					(int)height
			);
			trunkSpots[i] = new Spot(rect);
		}
	}
	
	private void setTrunkCards() {
		for(int i =0; i < trunk.size();i++) {
			trunkSpots[i].setCard(trunk.get(i));
		}
	}
	

	@Override
	public void render(Graphics2D g) {
		renderBackground(g);
		renderCardDisplay(g);
		renderDeck(g);
		renderDeckCapacity(g);
		renderDraggedCard(g);
		renderFilters(g);
		renderButtons(g);
		renderTextField(g);
		renderTrunk(g);
		
	}
	
	private void renderButtons(Graphics2D g) {
		buttonHandler.render(g);
	}
	private void renderTextField(Graphics2D g) {
		textField.render(g);
	}
	private void renderFilters(Graphics2D g) {
		g.setStroke(new BasicStroke(1));
		g.setColor(Color.WHITE);
		g.draw(filterBounds);
	}
	private void renderDraggedCard(Graphics2D g) {
		if(canDrag && draggingCard != null) {
			g.drawImage(draggingCard.getImage(),draggingBounds.x,draggingBounds.y,draggingBounds.width,draggingBounds.height,null);
		}
	}
	private void renderTrunk(Graphics2D g) {
		g.setStroke(new BasicStroke(1));
		g.clipRect(trunkBounds.x,trunkBounds.y,trunkBounds.width,trunkBounds.height);
		g.setColor(Color.RED);
		g.draw(trunkBounds);
		//rendering the spots
		if(trunkSpots==null) {return;}
		for(int i =0; i < trunkSpots.length;i++) {
			if(trunkSpots[i]!=null) {
				int strokeSize = canAddCard(trunkSpots[i]) ? 6 : 1;
				g.setStroke(new BasicStroke(strokeSize));
				g.setColor(Color.WHITE);
				g.translate(cam.getX(),cam.getY());
				g.draw(trunkSpots[i].getBounds());
				if(!isFiltering) {
					try {
					trunkSpots[i].render(g);
					renderTrunkFrequency(g,trunkSpots[i]);
					}catch(ArrayIndexOutOfBoundsException e) {return;}
				}
				g.translate(-cam.getX(),-cam.getY());
			}
		}
	}
	private void renderTrunkFrequency(Graphics2D g,Spot spot){
		if(player!=null && spot.getCard() != null) {
			g.setFont(new Font("ariel",Font.PLAIN,20));
			String frequency = String.valueOf(player.getTrunk().getFrequency(spot.getCard()));
			Color col = (player.getTrunk().getFrequency(spot.getCard()) == 0 || !(deck.canAddCard(spot.getCard()))) || (deck.getCapacity() + spot.getCard().getCapacity() > player.getTotalCapacity())? new Color(255,102,102): Color.WHITE;
			g.setColor(col);
			g.drawString(frequency,spot.getBounds().x - makeRectangleWidth(g.getFont(), frequency)*2,
			(spot.getBounds().y + spot.getBounds().height/2));
		}
	}
	private void renderDeck(Graphics2D g) {
		//drawing the rectangle
		g.setColor(Color.BLUE);
		g.draw(deckBounds);
		//drawing the spots
		renderDeckSpots(g);
		renderDeckCards(g);
	}
	
	private void renderDeckCapacity(Graphics2D g) {
		g.setFont(new Font("ariel",Font.PLAIN,25));
		Color col = deck.getCapacity() > player.getTotalCapacity() ? new Color(255,102,102) : Color.WHITE;
		g.setColor(col);
		g.drawString(deck.getCapacity() + "/" + player.getTotalCapacity(), deckBounds.x + deckBounds.width/2 - makeRectangleWidth(g.getFont(), deck.getCapacity() + "/" + player.getTotalCapacity())/2, deckBounds.y + deckBounds.height +deckBounds.height/15);
	}
	
	private void renderDeckSpots(Graphics2D g) {
		g.setStroke(new BasicStroke(1));
		g.setColor(Color.yellow);
		Spot firstOpenSpot = firstOpenSpot(deckSpots);
		for(int i =0; i < deckSpots.length;i++) {
			if(canDrag && firstOpenSpot !=null && draggingCard !=null) {
				if(deckSpots[i] == firstOpenSpot && mouseOver(deckBounds) && deck.canAddCard(draggingCard)) {
					g.setStroke(new BasicStroke(4));
				}
				else {g.setStroke(new BasicStroke(1));}
			}
			g.draw(deckSpots[i].getBounds());
			deckSpots[i].render(g);
		}
	}
	private void renderDeckCards(Graphics2D g) {
		for(int i =0; i < deck.size();i++) {
			deckSpots[i].setCard(deck.get(i));
		}
	}
	private void renderBackground(Graphics2D g) {
		g.setColor(Color.black);
		g.fill(getBounds());
	}
	private void renderCardDisplay(Graphics2D g) {
		g.setStroke(new BasicStroke(1));
		g.setFont(new Font("ariel",Font.PLAIN,20));
		//drawing the rectangles
		g.setColor(Color.ORANGE);
		g.draw(displayCardBounds);
		//drawing the card and all of its stats
		if(currentCard != null) {
			g.drawImage(currentCard.getImage(), currentCardBounds.x, currentCardBounds.y, currentCardBounds.width,currentCardBounds.height,null);
			g.setColor(Color.WHITE);
			renderCardStat(g,currentCard.getName(),0);
			renderCardStat(g,"Capacity: "+ String.valueOf(currentCard.getCapacity()),(int) (g.getFont().getSize()*1.5));
			if(currentCard instanceof Monster) {
				Monster monster = (Monster)currentCard;
				renderCardStat(g,"Attack: " + monster.getAttack(),(int) (g.getFont().getSize()*3));
				renderCardStat(g,"Defense: " + monster.getDefense(),(int) (g.getFont().getSize()*4.5));
				renderCardStat(g,"Attribute: " + monster.getAttribute().getName(),(int) (g.getFont().getSize()*6));
				renderCardStat(g,"Tributes: " + monster.getTributeCost(),(int) (g.getFont().getSize()*7.5));
				detailsBounds.y = currentCardBounds.y + currentCardBounds.height + makeRectangleHeight(g.getFont(), "Details") + ((int) (g.getFont().getSize()*8));
			}
			
			else {detailsBounds.y = currentCardBounds.y + currentCardBounds.height + makeRectangleHeight(g.getFont(), "Details") + (int)(g.getFont().getSize()*2);}
			g.setColor(Color.RED);
			g.draw(detailsBounds);
			if(currentCard instanceof Effect) {
				g.setFont(new Font("ariel",Font.PLAIN,17));
				g.setColor(Color.WHITE);
				drawStringOverflow(g, ((Effect)currentCard).effectText(), detailsBounds);
			}
		}
	}
	
	private void renderCardStat(Graphics2D g,String str,int yOffset) {
		//Font originalFont = g.getFont();
		while((currentCardBounds.x + currentCardBounds.width/2 - this.makeRectangleWidth(g.getFont(), str)/2) + (this.makeRectangleWidth(g.getFont(), str)) > (this.displayCardBounds.x + this.displayCardBounds.width)) {
			g.setFont(new Font(g.getFont().getFamily(),g.getFont().getStyle(),g.getFont().getSize()-1));
		}
		g.drawString(str, currentCardBounds.x + currentCardBounds.width/2 - this.makeRectangleWidth(g.getFont(), str)/2, currentCardBounds.y + currentCardBounds.height + this.makeRectangleHeight(g.getFont(), str) + (yOffset));
		//g.setFont(originalFont);
	}
	
	
	public void mousePressed(MouseEvent e) {
		//clicking on one of the cards in your trunk
		if(mouseOver(trunkBounds)) {
			if(SwingUtilities.isLeftMouseButton(e)) {
				clickOnTrunk(e);
			}
			else if(SwingUtilities.isRightMouseButton(e)) {
				quickMoveToDeck(e);
			}
		}
		else if(mouseOver(deckBounds)) {
			clickOnDeck(e);
		}
		else if(mouseOver(filterBounds)) {
			clickOnFilters(e);
		}
		textField.mousePressed(e);
	}
	
	private void sortTrunkBy(Comparator<Card> comp) {
		List<Card> tempTrunk = trunk.stream().filter(x -> x instanceof Monster).collect(Collectors.toList());
		Collections.sort(tempTrunk,comp);
		updateTrunk(tempTrunk);
	}
	
	private void updateTrunk(List<Card>list) {
		isFiltering = true;
		trunk.clear();
		trunk = new ArrayList<Card>(list);
		createTrunk();
		cam.setY(0);
		isFiltering = false;
	}
	
	private void clickOnFilters(MouseEvent e) {
		buttonHandler.mousePressed(e);
	}
	
	private void clickOnTrunk(MouseEvent e) {
		for(int i =0; i < trunkSpots.length;i++) {
			if(canAddCard(trunkSpots[i]) && !trunkSpots[i].isOpen()) {
				canDrag = true;
				draggingCard = trunkSpots[i].getCard();
			}
		}
	}
	
	private void quickMoveToDeck(MouseEvent e) {
		for(int i=0; i < trunkSpots.length;i++) {
			if(canAddCard(trunkSpots[i]) && !trunkSpots[i].isOpen()) {
				addToDeck(trunkSpots[i].getCard());
			}
		}
	}
	
	private void clickOnDeck(MouseEvent e) {
		for(int i =0; i < deckSpots.length;i++) {
			if(mouseOver(deckSpots[i].getBounds()) && !(deckSpots[i].isOpen())) {
				if(SwingUtilities.isRightMouseButton(e)) {
					//move all the cards one spot up
					//player.setCurrentCapacity(player.getCurrentCapacity()-deckSpots[i].getCard().getCapacity());
					deck.removeCard(deckSpots[i].getCard());
					player.getTrunk().addCard(deckSpots[i].getCard());
					for(int j = i; j < deckSpots.length-1;j++) {
						if(!deckSpots[j].isOpen()) {
							Spot nextSpot = deckSpots[j+1];
							deckSpots[j].setCard(nextSpot.getCard());
							if(deckSpots[j].getCard() == null) {deckSpots[j].setOpen(true);}
						}
					}
					if(deck.size()==Deck.MAX_SIZE-1) {
						deckSpots[deckSpots.length-1].setCard(null);
						deckSpots[deckSpots.length-1].setOpen(true);
					}
					
				}
			}
		}
	}
	
	public void mouseReleased(MouseEvent e) {
		if(canDrag) {
			if(mouseOver(deckBounds)) {
				addToDeck(draggingCard);
			}
			canDrag = false;
			draggingPoint = new Point(0,0);
			draggingBounds = new Rectangle();
			draggingCard = null;
			
		}
	}
	
	private void addToDeck(Card c) {
		if(player.getTrunk().getFrequency(c) >0) {
			if(deck.size()!=Deck.MAX_SIZE && !(deck.getCapacity() + c.getCapacity() > player.getTotalCapacity())) {
				if(deck.addCard(c)) {
					player.getTrunk().removeCard(c);
					//player.setCurrentCapacity(deck.getCapacity() + c.getCapacity());
				}
			}
		}
	}
	

	
	public void mouseDragged(MouseEvent e) {
		if(canDrag) {
			super.mouseMoved(e);
			draggingPoint = e.getPoint();
			
			draggingBounds = new Rectangle(draggingPoint.x - trunkSpots[0].getBounds().width/2
			,draggingPoint.y - trunkSpots[0].getBounds().height/2
			,trunkSpots[0].getBounds().width,trunkSpots[0].getBounds().height);
		}
	}
	
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(mouseOver(trunkBounds)) {
			int notches = e.getWheelRotation();
			//moving up
			if(notches<0) {
				if(trunkSpots.length<=0) {return;}
				Spot firstSpot = trunkSpots[0];
				if(firstSpot.getBounds().y - cam.getY() < trunkBounds.y+ trunkBounds.height/50) {
					return;
				}
				cam.setY(cam.getY()+30f);
			}
			//moving down
			else if(notches >0) {
				if(trunkSpots.length <=0) {return;}
				Spot lastSpot = trunkSpots[trunkSpots.length-1];
				if((lastSpot.getBounds().y + lastSpot.getBounds().height + cam.getY()) < (trunkBounds.y + trunkBounds.height - trunkBounds.height/200) ) {return;}
				cam.setY(cam.getY()-30f);
				
			}
		}
	}
	
	public void mouseMoved(MouseEvent e) {
		super.mouseMoved(e);
		
	}
	public void keyPressed(KeyEvent e) {
		textField.keyPressed(e);
	}
	
	private void searchForCard(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			textField.backspace();
		}
		else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			textField.addText(" ");
		}
		else if(e.getKeyCode() >=65 && e.getKeyCode()<=90 || (e.getKeyCode() >= 48 && e.getKeyCode()<=57) || (e.getKeyCode() == 222) || (e.getKeyCode() ==59)) {
			textField.addText(e.getKeyChar()+"");
		}
		if(!textField.getText().equals("")) {
			updateTrunk(originalTrunk.stream().filter(x -> x.getName().toLowerCase().contains(textField.getText().toLowerCase())).collect(Collectors.toList()));
		}
		else {
			updateTrunk(originalTrunk);
		}
	}
	private void setCurrentCard(Spot[]spots,int yOffset) {
		for(int i =0; i< spots.length;i++) {
			Spot currentSpot = spots[i];
			if(currentSpot.getCard()!=null) {
				if(mouseOver(getMouseX(),getMouseY() - yOffset,currentSpot.getBounds())) {
					currentCard = currentSpot.getCard();
				}
			}
		}
	}
	
	private void removeDuplicates() {
		ArrayList<String> tempList = new ArrayList<String>();
		if(player == null) {return;}
		for(int i=0;i<player.getTrunk().getList().size();i++) {
			if(!tempList.contains(player.getTrunk().getList().get(i).getName())) {
				tempList.add(player.getTrunk().getList().get(i).getName());
				trunk.add(player.getTrunk().getList().get(i));
			}
		}
		for(int i =0; i < deck.size();i++) {
			if(!tempList.contains(deck.get(i).getName())) {
				System.out.println("name = " + deck.get(i).getName());
				tempList.add(deck.get(i).getName());
				trunk.add(deck.get(i));
			}
		}
	}
	
	private boolean canAddCard(Spot spot) {
		return mouseOver(getMouseX(),(int) (getMouseY() - cam.getY()),spot.getBounds()) && player.getTrunk().getFrequency(spot.getCard()) >0 && deck.canAddCard(spot.getCard()) && !(deck.getCapacity()+ spot.getCard().getCapacity() > player.getTotalCapacity()) ;
	}
	
	private Spot firstOpenSpot(Spot[]spots) {
		for(int i=0;i<spots.length;i++) {
			if(spots[i].isOpen()) {
				return spots[i];
			}
		}
		return null;
	}
	
	
	public void setPlayer(Player player) {this.player = player;}
	public Player getPlayer() {return player;}
}
