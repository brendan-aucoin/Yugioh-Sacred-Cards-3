package images;

import java.awt.image.BufferedImage;

public class NamedImage{
	private String name;
	private BufferedImage image;
	public NamedImage(BufferedImage image,String name) {
		this.image = image;
		this.name = name;
	}
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	public BufferedImage getImage() {return image;}
	public void setImage(BufferedImage image) {this.image = image;}
	
	
	
}
