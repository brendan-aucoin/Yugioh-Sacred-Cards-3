/*
 *Brendan Aucoin
 *07/06/2019
 *the basic board without any special effects
 * */
package boards;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import cards.Card;
import dueling.Spot;
import game.Game;
/*a sub class of board*/
public class RegularBoard extends Board{
	//the images that go over the spots
	private BufferedImage brownTileImage;
	private BufferedImage whiteTileImage;
	private BufferedImage background;
	private BasicStroke middleLineStroke;
	public RegularBoard(Game game) {
		super(game,BoardType.REGULAR);
	}
	
	//the regular board doesnt need to update anything
	public void update() {
		
	}
	/*makes all the images this board needs*/
	@Override
	public void init() {
		super.init();
		brownTileImage = getGame().getImageLoader().loadImage("dueling_images","brownTileBoard.png");
		whiteTileImage = getGame().getImageLoader().loadImage("dueling_images","whiteTileBoard.png");
		background = getGame().getImageLoader().loadImage("dueling_images","regular board background.png");
		middleLineStroke = new BasicStroke(6);
	}
	//the abstract method but the regular board does nothing for buffs.
	public void buffCard(Card c) {}
	/*draws everything the super class draws and the images specific to this board*/
	public void render(Graphics2D g) {
		super.render(g);//you want to render everything from the super class
		//draw the background
		g.drawImage(background, getBounds().x, getBounds().y - getBounds().height, getBounds().width, getBounds().height*2,null);
		renderMiddleArea(g,middleLineStroke,new Color(205,92,92),Color.WHITE);//method from the super class
		renderText(g,Color.BLACK);//method from the super class
		//render the spots with the proper image
		renderColouredSpots(g,getPlayerField().getMonsterSpots(),0,1);
		renderColouredSpots(g,getPlayerField().getMagicSpots(),1,0);
		renderColouredSpots(g,getOpponentField().getMonsterSpots(),1,0);
		renderColouredSpots(g,getOpponentField().getMagicSpots(),0,1);
	}
	

	/*draws the images for the tiles*/
	private void renderColouredSpots(Graphics2D g,ArrayList<Spot> spots,int even,int odd) {
		//half of the spots should be lighter image and the other half should be the darker image
		for(int i =even; i < spots.size();i+=2) {
			Spot s = spots.get(i);
			g.drawImage(brownTileImage,s.getBounds().x,s.getBounds().y,s.getBounds().width,s.getBounds().height,null);
		}
		
		for(int i=odd; i < spots.size();i+=2) {
			Spot s = spots.get(i);
			g.drawImage(whiteTileImage,s.getBounds().x,s.getBounds().y,s.getBounds().width,s.getBounds().height,null);
		}
	}
}
