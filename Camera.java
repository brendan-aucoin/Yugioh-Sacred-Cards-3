/*
 Brendan Aucoin
 2017/08/17
 this class holds an x and y position that can be translated in any direction.
 */
package camera;

public class Camera 
{
	private float x,y;//positions
	/*constructors*/
	public Camera(float x,float y)
	{
		this.x = x;
		this.y = y;
	}
	public Camera(Camera cam)
	{
		this.x = cam.x;
		this.y = cam.y;
	}
	public void update(float index)
	{
		y = -index;
	}
	/*getters and setters*/
	public float getX(){return x;}
	public float getY(){return y;}
	public void setX(float x){this.x = x;}
	public void setY(float y){this.y = y;}
}
