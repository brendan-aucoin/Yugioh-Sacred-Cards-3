/*
 Brendan Aucoin
 2017/08/11
 this class is so that you can crop out parts of a buffered image.
 */
package images;

import java.awt.image.BufferedImage;
public class SpriteSheet
{
  public BufferedImage sheet;
  public SpriteSheet(BufferedImage sheet)
  {
    this.sheet = sheet;
  }
  public BufferedImage crop(int x, int y, int width, int height)
  {
    return sheet.getSubimage(x,y,width,height);//returns the cropped image.
  }
}