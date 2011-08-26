package porthole;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image myImage;
	
	
	public void setImage(Image image){
		//Assumes image already loaded 
		if (image == null){
			myImage = null;
			return;
		}
		int width = image.getWidth(this);
/*		if (width <= 0) {
	         MediaTracker tracker = new MediaTracker(this);
	         tracker.addImage(image, 0);
	 
	         try {
	            tracker.waitForID(0);
	         }
	         catch (InterruptedException ie) {}
	 
	         width = image.getWidth(this);
	      }*/
		int height = image.getHeight(this);
		setPreferredSize(new Dimension(width, height));
		myImage = image;		
	}
	
	public Image getImage(){
		return myImage;
	}
	
	public void paintComponent(Graphics screen){
		screen.drawImage(myImage, 0, 0, this);
	}


}
