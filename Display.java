/*
 *Brendan Aucoin
 *06/30/2019
 *contains the Frame for the game
 */
package gui;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import game.Game;

public class Display {
	private JFrame frame;
	public static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
	//public static final Dimension SCREEN_SIZE = new Dimension(750,600);
	private Canvas canvas;
	public Display() {
		frame = new JFrame(Game.TITLE);//if i change the title of the game i dont wanna find the string in this class its easier to find in game
		//basic defaults for the jframe 
		frame.setResizable(false);
		frame.setSize(SCREEN_SIZE.width, SCREEN_SIZE.height);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new FlowLayout());
		Image icon = new ImageIcon(this.getClass().getResource(Game.DUELING_IMAGES + "seal of orichalcos.png")).getImage();
		frame.setIconImage(icon);
		
		canvas = new Canvas();
		canvas.setPreferredSize(SCREEN_SIZE);
		canvas.setMaximumSize(SCREEN_SIZE);
		canvas.setMinimumSize(SCREEN_SIZE);
		canvas.setFocusable(true);
		
		frame.add(canvas);
	}
	
	public Canvas getCanvas() {
		return canvas;	
	}
	public JFrame getFrame() {
		return frame;
	}
}
