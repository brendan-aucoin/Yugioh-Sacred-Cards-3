/*
 *Brendan Aucoin
 *06/30/2019
 *runs the game
 */
package runner;

import game.AssetLoader;
import gui.SplashScreen;

public class GameRunner {
	public static void main(String [] args) {
		//Game game = new Game();
		//game.start();
		SplashScreen splashScreen = new SplashScreen();
		AssetLoader loader = new AssetLoader(splashScreen);
		loader.start();
	}
}
