package Viewer;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JInternalFrame;

public interface ImageViewer {
	public 	void outputImage(int [] [] outArray); 
	public BufferedImage getImage();
	public int getNumb();
}
