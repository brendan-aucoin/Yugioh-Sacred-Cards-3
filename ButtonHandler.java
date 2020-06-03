package gui;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

public class ButtonHandler {
	private Button[] buttons;
	public ButtonHandler(Button ... buttons) {
		this.buttons = buttons;
	}
	
	public void update(int mouseX,int mouseY) {
		for(int i =0; i < buttons.length; i++) {
			if(buttons[i]!= null) {
				buttons[i].update(mouseX, mouseY);
			}
		}
	}
	
	public void render(Graphics2D g) {
		for(int i =0; i< buttons.length;i++) {
			if(buttons[i]!= null) {
				buttons[i].render(g);
			}
		}
	}
	
	public void mousePressed(MouseEvent e) {
		for(int i =0; i < buttons.length;i++) {
			if(buttons[i] != null) {
				buttons[i].mousePressed(e);
			}
		}
	}
	
	public void addButtons(Button [] buttons) {
		Button [] tempButtons = new Button[this.buttons.length + buttons.length];
		for(int i =0; i < this.buttons.length;i++) {
			tempButtons[i] = this.buttons[i];
		}
		int tempCount = this.buttons.length;
		for(int i = 0;i < buttons.length;i++) {
			tempButtons[tempCount] = buttons[i];
			tempCount++;
		}
		this.buttons = tempButtons;
	}
}
