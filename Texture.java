package images;

import java.awt.image.BufferedImage;

public class Texture {
	
	private BufferedImageLoader loader;
	public static BufferedImage [] cardSprites;
	
	public Texture() {
		loader = new BufferedImageLoader();
		loadCardSprites();
	}
	
	
	private void loadCardSprites() {
		cardSprites = new BufferedImage[2000];
		BufferedImage cardSheetImage = loader.loadImage("dueling_images", "card sprite sheet.png");
		SpriteSheet cardSheet = new SpriteSheet(cardSheetImage);
		cardSprites = readFromSheet(cardSprites,cardSheet,2,10,177,254);
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
}
