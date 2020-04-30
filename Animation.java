package images;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
public class Animation
{
  private int speed;
  private int frames;//how many images.
  private int index = 0;
  private int count = 0;
  
  private BufferedImage [] images;
  private BufferedImage currentImg;
  
  public Animation(int speed, BufferedImage... args)//lets you have as many bufferedimages as you want
  {
    this.speed = speed;
    images = new BufferedImage[args.length];
    for(int i = 0; i< args.length; i++)
    {
      images[i] = args[i];
    }
    frames = args.length;
  }
  public Animation(Animation animation)
  {
	  this.images = animation.images;
  }
  public void setAnimation(int speed, BufferedImage... args)
  {
    this.speed = speed;
    images = new BufferedImage[args.length];
    for(int i = 0; i< args.length; i++)
    {
      images[i] = args[i];
    }
  }
  public void runAnimation()
  {
   index++;
   if(index > speed)
   {
     index = 0;
     nextFrame();
   }
   
  }
  private void nextFrame()
  {
    for(int i = 0; i < frames; i++)
    {
      if(count == i)
      {
        currentImg = images[i];
      }
    }
    count++;
    if(count > frames)
    {
      count = 0;
    }
  }
  
  public boolean isOver()
  {
	if(count >= frames)
	{
		return true;
	}
	else {return false;}
  }
  public void reset() {
	  index =0;
	  count = 0;
  }
  public BufferedImage getCurrentImage()
  {
    return currentImg;
  }
  public void setSpeed(int speed){this.speed = speed;}
  public int getSpeed(){return speed;}
  public int getCount(){ return count;}
  public void setCount(int count) {this.count = count;}
  public void drawAnimation(Graphics g, int x, int y)
  {
    g.drawImage(currentImg,x,y,null);
  }
  public void drawAnimation(Graphics g, int x, int y,int scaleX, int scaleY)
  {
	  if(currentImg != null) {
		  g.drawImage(currentImg,x,y,currentImg.getWidth()*scaleX,currentImg.getHeight()*scaleY,null);
	  }	
  }
}
