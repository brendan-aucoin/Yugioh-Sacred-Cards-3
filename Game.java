package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

import boards.RegularBoard;
import cards.Card;
import cards.ChangeOfHeart;
import cards.Doron;
import cards.FlameViper;
import cards.GaiaTheFierceKnight;
import cards.HarpiesPetBabyDragon;
import cards.MysticalSpaceTyphoon;
import cards.ObeliskTheTormentor;
import cards.RightLegOfTheForbiddenOne;
import cards.SwordsmanOfLandstar;
import cards.TheLegendaryFisherman;
import cards.TrapHole;
import cards.WaterOmotics;
import dueling.Deck;
import gui.Display;
import images.BufferedImageLoader;
import images.Texture;
import input.MouseManager;
import player.Ai;
import player.Player;
import states.DuelingState;
import states.MainMenuState;
import states.StateManager;

public class Game  implements Runnable{
	//static variables
	public static final String DUELING_IMAGES = "/dueling_images/";
	public static final String TITLE = "Sacred Cards 3";
	public static final String CARD_STATS = "cardStats.txt";
	public static final String TEXTS_PATH = System.getProperty("user.dir")+ System.getProperty("file.separator")+"texts"+System.getProperty("file.separator");// + "decks" +System.getProperty("file.separator");
	//runner variables
	private boolean running;
	private Thread thread;
	//gui
	private Display display;
	//images
	private BufferedImageLoader imageLoader;
	public static Texture texture;
	//input
	private MouseManager mouseManager;
	//state
	private StateManager stateManager;
	private MainMenuState mainMenu;
	private DuelingState duelingState;
	
	//get rid of 
	private RegularBoard board;
	private Player player;
	private Ai opponent;
	public static Deck baseDeck;
	public static ArrayList<Card> allCards;
	
	
	public Game() {
		running = false;
		display = new Display();
		stateManager = new StateManager();
		imageLoader = new BufferedImageLoader();
		mainMenu = new MainMenuState(this);
		//get rid of
		board = new RegularBoard(this);
		duelingState = new DuelingState(this);
		
		player = new Player("Brendan");
		opponent = new Ai(duelingState,"Opponent");
		

		duelingState.setPlayer(player);
		duelingState.setBoard(board);
		duelingState.setHandBounds();
		
		//get rid of
		duelingState.setOpponent(opponent);
		
		mouseManager = new MouseManager(stateManager);
		texture = new Texture();
		
		//get rid of
		
		baseDeck = new Deck("Base Deck");
		allCards = new ArrayList<Card>();
		try {
		allCards.add(new Doron());
		allCards.add(new WaterOmotics());
		allCards.add(new FlameViper());
		allCards.add(new SwordsmanOfLandstar());
		allCards.add(new TheLegendaryFisherman());
		allCards.add(new ChangeOfHeart());
		allCards.add(new TrapHole());
		allCards.add(new MysticalSpaceTyphoon());
		allCards.add(new GaiaTheFierceKnight());
		allCards.add(new HarpiesPetBabyDragon());
		allCards.add(new RightLegOfTheForbiddenOne());
		allCards.add(new ObeliskTheTormentor());
		
		}catch(FileNotFoundException e) {
			System.out.println("file not found for creating card stats");
		}
		
		Deck tempDeck = new Deck("Temp deck");
		File tempDeckFile = new File(TEXTS_PATH + "temp base deck.txt");
		LinkedList<Card> baseDeck = new LinkedList<Card>();
		try {
			Scanner tempDeckScanner = new Scanner(tempDeckFile);
			while(tempDeckScanner.hasNextLine()) {
				String line  = tempDeckScanner.nextLine();
				for(int i =0; i < allCards.size();i++) {
					String cardName = allCards.get(i).getName().replace(" ", "_");
					if(line.equalsIgnoreCase(cardName)) {
						baseDeck.add(allCards.get(i));
					}
				}
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("couldnt load base deck");
		}
		tempDeck.setDeck(baseDeck);
		player.setDeck(tempDeck);
		opponent.setDeck(tempDeck);
		for(Card c : opponent.getDeck().getDeck()) {
			//System.out.println(c);
		}
		
		player.shuffle();
		opponent.shuffle();
		
	
	}
	
	private void init() {
		/*set up all the mouse stuff and keyboard stuff*/
		display.getCanvas().addMouseListener(mouseManager);
		display.getCanvas().addMouseMotionListener(mouseManager);
		display.getCanvas().addMouseWheelListener(mouseManager);
		/*set up all managers like state manager*/
		stateManager.setState(duelingState);
		
		//get rid of 
		//duelingState.setBoard(board);
		
		duelingState.startDuel();
		/*end of method*/
	
		
		display.getFrame().setVisible(true);
		createBaseDeck();
	}
	
	private void createBaseDeck() {
		
	}
	
	public void update() {
		stateManager.getState().update();
	}
	
	public void render() {
		BufferStrategy bs = display.getCanvas().getBufferStrategy();
		if(bs == null){display.getCanvas().createBufferStrategy(3);return;}
		Graphics g = bs.getDrawGraphics();
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		display.getCanvas().paint(g);
		g.setColor(Color.WHITE);
		g.fillRect(0,0,Display.SCREEN_SIZE.width,Display.SCREEN_SIZE.height);
		
		stateManager.getState().render(g2d);
		g.dispose();
		bs.show();
		
	}
	
	
	
	public void run() {
		init();
		int fps = 60;
		double timePerTick = 1000000000/fps;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		long timer =0;
		int ticks = 0;
		while(running) {
			now = System.nanoTime();
			delta+=(now-lastTime)/timePerTick;
			timer+= now - lastTime;
			lastTime = now;
			if(delta>=1) {
				update();
				render();
				ticks++;
				delta--;
			}
			
			if(timer >1000000000) {
				//System.out.println("Updates: and frames "  +ticks);
				ticks =0;
				timer = 0;
			}
		}
		
		stop();
	}
	
	
	public synchronized void start() {
		if(running) {return;}
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop() {
		if(!running) {return;}
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public BufferedImageLoader getImageLoader() {
		return imageLoader;
	}
}
