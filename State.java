/*
 *Brendan Aucoin
 *07/06/2019
 *all states will update and have mouse input
 * */
package states;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public interface State {
	public void update();
	public void render(Graphics2D g);
	public void mouseWheelMoved(MouseWheelEvent e);
	public void mouseDragged(MouseEvent e) ;
	public void mouseMoved(MouseEvent e);
	public void mouseClicked(MouseEvent e) ;
	public void mouseEntered(MouseEvent e); 
	public void mouseExited(MouseEvent e); 
	public void mousePressed(MouseEvent e); 
	public void mouseReleased(MouseEvent e);
	public void keyPressed(KeyEvent e);
	public void keyReleased(KeyEvent e);
	public void keyTyped(KeyEvent e);
}
