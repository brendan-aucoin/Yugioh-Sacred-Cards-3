package gui;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import game.Game;
import images.Texture;

public class SplashScreen {
	private JFrame frame;
	public SplashScreen() {
		frame = new JFrame();
		frame.setSize((int) (Display.FULL_SCREEN.width/1.5),(int) (Display.FULL_SCREEN.height/1.5));
		frame.setLocationRelativeTo(null);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setUndecorated(true);
		Dimension loadingScreenDimension = new Dimension((int) (Display.FULL_SCREEN.width/1.5),(int) (Display.FULL_SCREEN.height/1.5));
		BufferedImage loadingScreenImage = null;
		try {
			loadingScreenImage = ImageIO.read(new File(Game.RES_PATH + "dueling_images"  + "/" + "loading_background.png"));
		  }catch(IOException e) {System.err.println("Could not load image");}
		frame.getContentPane().add(new LoadingPanel(loadingScreenDimension,loadingScreenImage));
		frame.setVisible(true);
	}
	
	public void dissapear() {
		frame.removeAll();
		frame.setVisible(false);
	}
	public JFrame getFrame() {return frame;}
	public void setFrame(JFrame frame) {this.frame = frame;}
	
	private class LoadingPanel extends Applet{
		private static final long serialVersionUID = 1L;
		private Dimension dim;
		private AffineTransform affinetransform;
		private FontRenderContext frc;
		private BufferedImage loadingImage;
		public LoadingPanel(Dimension dim,BufferedImage loadingImage) {
			this.dim = dim;
			this.loadingImage = loadingImage;
			affinetransform = new AffineTransform();
			frc = new FontRenderContext(affinetransform,true,true);
			setBackground(Color.WHITE);
		}
		
		public void paint(Graphics g) {
			super.paint(g);
			g.drawImage(loadingImage, 0,0,dim.width,dim.height,null);
			g.setColor(Color.BLACK);
			g.setFont(new Font("Courier",Font.BOLD,40));
			g.drawString("Loading",(int) (dim.width/2 - g.getFont().getStringBounds("Loading", frc).getWidth()/2), dim.height/4);
		}
	}
	
}

