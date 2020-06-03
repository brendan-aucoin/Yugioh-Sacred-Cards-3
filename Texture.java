/*
 *Brendan Aucoin
 *06/30/2019
 *holds all the card sprites of the images
 */
package images;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import attributes.Attribute;
import boards.Board;
import cards.CardList;
import game.Game;

public class Texture {
	private BufferedImageLoader loader;
	public static BufferedImage [] cardSprites;
	public static BufferedImage backOfCardImage;
	public static BufferedImage backOfCardImageRotated90;
	public static BufferedImage [] swordSlash = new BufferedImage[10];
	private SpriteSheet swordSlashSheet;
	private BufferedImage swordSlashSheetImage;
	public static String error = "fg ";
	//all the board sprites
	public static ArrayList<NamedImage> boardImages;
	
	public Texture() {
		loader = new BufferedImageLoader();
	}
	public void loadTextures() throws IOException {
		loadCardSprites();
		loadSlashingSpriteSheets();
		loadBoardImages();
	}
	/*load every card sprite from the card sprite sheet*/
	private void loadCardSprites() throws IOException {
		backOfCardImage = loader.loadImage("dueling_images", "backOfCard.png");
		backOfCardImageRotated90 = rotate90(backOfCardImage);
		cardSprites = new BufferedImage[2000];
		//BufferedImage cardSheetImage = loader.loadImage("dueling_images", "card sprite sheet.png");
		//SpriteSheet cardSheet = new SpriteSheet(cardSheetImage);
		//cardSprites = readFromSheet(cardSprites,cardSheet,2,10,177,254);
		String cardPath = Game.RES_PATH + "cards\\";
		for(int i =0; i < CardList.values().length;i++) {
			cardSprites[i] = ImageIO.read(new File(cardPath + CardList.values()[i].name() +".jpg"));
		}
		
	}
	private void loadSlashingSpriteSheets() {
		swordSlashSheetImage = loader.loadImage("sprite_sheets","fire_sword_slash_sprite_sheet.png");
		swordSlashSheet = new SpriteSheet(swordSlashSheetImage);
		swordSlash[0] = swordSlashSheet.crop(81,69,66,64);
		swordSlash[1] = swordSlashSheet.crop(358,92,98,98);
		swordSlash[2] = swordSlashSheet.crop(198,78,104,107);
		swordSlash[3] = swordSlashSheet.crop(215,229,109,102);
		swordSlash[4] = swordSlashSheet.crop(368,243,110,105);
		swordSlash[5] = swordSlashSheet.crop(529,232,111,121);
		swordSlash[6] = swordSlashSheet.crop(705,224,102,122);
		swordSlash[7] = swordSlashSheet.crop(42,386,100,121);
		swordSlash[8] = swordSlashSheet.crop(225,385,84,107);
		swordSlash[9] = swordSlashSheet.crop(393,390,82,101);
	}
	/*returns a bufferedimage of a differnet size*/
	public static BufferedImage scale(BufferedImage image, int newWidth, int newHeight)
	{
	    BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
	    int x, y;
	    int width = image.getWidth();
	    int height = image.getHeight();
	    int[] ys = new int[newHeight];
	    for (y = 0; y < newHeight; y++) {
	        ys[y] = y * height / newHeight;
	    }
	    for (x = 0; x < newWidth; x++) {
	        int newX = x * width / newWidth;
	        for (y = 0; y < newHeight; y++) {
	            int col = image.getRGB(newX, ys[y]);
	            newImage.setRGB(x, y, col);
	        }
	    }
	    return newImage;
	}
	public static BufferedImage rotate90(BufferedImage image) {
		BufferedImage tempImage = Texture.scale(image,(int) (Board.SPOT_WIDTH/1.5),(int) (Board.SPOT_HEIGHT));
		int width = tempImage.getWidth();
		int height = tempImage.getHeight();
		BufferedImage rotatedImage = new BufferedImage( height, width , tempImage.getType());
		for(int x =0; x <width; x++) {
			for(int y =0; y < height; y++) {
				rotatedImage.setRGB(height-y-1, x, tempImage.getRGB(x, y));
			}
		}
		return rotatedImage;
	}
	
	public static BufferedImage rotate180(BufferedImage image) {
		BufferedImage rotatedImage = new BufferedImage(image.getWidth(),image.getHeight(),image.getType());
		for(int x =0; x<image.getWidth(); x++) {
			for(int y =0; y < image.getHeight();y++) {
				rotatedImage.setRGB(image.getWidth()-x-1, image.getHeight()-y-1,image.getRGB(x, y));
			}
		}
		return rotatedImage;
	}
	/*this method crops out images from a spritesheet assuming there is a constant width,height and you know how many rows and columns there are.*/
	  private BufferedImage[] readFromSheet(BufferedImage[] sprites, SpriteSheet sheet,int rows,int cols,int width,int height)
	  {
	    for(int i = 0; i < rows; i++)
	    {
	      for(int x = 0; x < cols; x++)
	      {
	        sprites[x + (i*cols)] = sheet.crop(x*width, i*height, width, height);
	      }
	    }
	    return sprites;
	  }
	  
	  private void loadBoardImages() {
		  boardImages = new ArrayList<NamedImage>();
		  for(int i=0; i < Attribute.values().length;i++) {
			  Attribute attribute = Attribute.values()[i];
			  //this if statement wont be here in the future but for now only a couple of images exist
			  if(attribute.getName().equalsIgnoreCase("regular") || attribute.getName().equalsIgnoreCase("water")) {
				  loadBoardImage(attribute.getName().toLowerCase(),"background");
				  loadBoardImage(attribute.getName().toLowerCase(),"even_tile");
				  loadBoardImage(attribute.getName().toLowerCase(),"odd_tile");
			  }
		  }
	  }
	  
	  private void loadBoardImage(String attribute,String name) {
		  String path = attribute + "_board_" + name;
		  boardImages.add(new NamedImage(loader.loadImage("dueling_images",path + ".png"),path));
	  }
}
