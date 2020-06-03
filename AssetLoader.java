package game;

import gui.SplashScreen;

public class AssetLoader implements Runnable{
	private Thread thread;
	private boolean running;
	private Game game;
	private SplashScreen splashScreen;
	public AssetLoader(SplashScreen splashScreen) {
		this.splashScreen = splashScreen;
		running = false;
	}
	public void run() {
		game = new Game(splashScreen);
		game.init();
		game.start();
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
}
