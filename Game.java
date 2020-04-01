/*
 *Brendan Aucoin
 *06/30/2019
 *is the main thread of the game that updates and renders everything
 */
package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import boards.RegularBoard;
import cards.Card;
import cards.CardHandler;
import cards.CardList;
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
	//static variables for the file paths
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
	
	//cards
	private CardHandler cardHandler;
	//get rid of 
	private RegularBoard board;
	private Player player;
	private Ai opponent;
	public static Deck baseDeck;
	public static ArrayList<Card> allCards;
	
	
	public Game() {
		running = false;
		//initialize all the objects for states and managers
		display = new Display();
		
		stateManager = new StateManager();
		imageLoader = new BufferedImageLoader();
		mouseManager = new MouseManager(stateManager);
		texture = new Texture();
		cardHandler = new CardHandler();
		mainMenu = new MainMenuState(this);
		
		//get rid of
		board = new RegularBoard(this);
		duelingState = new DuelingState(this);
		
		player = new Player("Brendan");
		opponent = new Ai(duelingState,"Opponent");
		
		//get rid of
		duelingState.setPlayer(player);
		duelingState.setBoard(board);
		duelingState.setHandBounds();
		duelingState.setOpponent(opponent);
		
		
		Deck tempDeck = new Deck("Temp deck");
		try {
			tempDeck.createDeck(TEXTS_PATH + "temp base deck.txt");
		} catch (FileNotFoundException e) {
			System.err.println("COULD NOT LOAD FILE");
			System.exit(0);
		}
		player.setDeck(tempDeck);
		opponent.setDeck(tempDeck);
		player.shuffle();
		opponent.shuffle();
	}
	
	/*add all the input listeners and sets starting state of the game*/
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
	}
	
	/*update the current state help in the state manager*/
	public void update() {
		stateManager.getState().update();
	}
	
	/*render the current state in the state manager*/
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
	
	
	/*make the game 60fps */
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
	
	/*start the game*/
	public synchronized void start() {
		if(running) {return;}
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	/*stop the game*/
	public synchronized void stop() {
		if(!running) {return;}
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/*some other classes need access to the image loader which the game has. instead of creating a new image loader*/
	public BufferedImageLoader getImageLoader() {
		return imageLoader;
	}
}
