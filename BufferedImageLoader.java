/*
 Brendan Aucoin
 2017-08-11
 this class is so that you can load buffered images from a directory.
 */
package images;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import game.Game;
public class BufferedImageLoader
{
  private BufferedImage image = null;
  /*loads and image from a specified directory and the name of the image.*/
  public BufferedImage loadImage(String dirName,String path)
  {
	//try{image = ImageIO.read(this.getClass().getResource("/" + "" + dirName + "/" + "" +  path));}catch(IOException e) {System.err.println("there was a problem loading the level");}
	  try {
		  image = ImageIO.read(new File(Game.RES_PATH  + dirName + "\\"  + path));
	  }catch(IOException e) {System.err.println("Could not load image");}
	  return image;
  }
  /*loads an image if they are all in the base resource folder*/
  public BufferedImage loadImage(String path)
  {
   try {image = ImageIO.read(new File(path));}catch(IOException e) {System.err.println("there was a problem loading the level");}
   return image;
  }
}