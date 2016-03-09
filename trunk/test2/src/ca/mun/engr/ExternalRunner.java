package ca.mun.engr;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;

public class ExternalRunner {
	private static JP_1 jp = null;

	public static void create(){
		jp = JP_1.go(null);
	}
		
    public static BufferedImage ShotRequest() {
    	BufferedImage ShotResult = getScreenshot(jp.frame);
		return ShotResult;
	}

	static private BufferedImage getScreenshot(JFrame frame) {
		BufferedImage bi = new BufferedImage(frame.getHeight(), frame.getHeight(), BufferedImage.TYPE_INT_ARGB);  
		Graphics g = bi.createGraphics();
	    frame.paint(g);  //this == JComponent
	        g.dispose();
	        return bi;
	}
	
	public static Component getComponentRoot(){
		return jp.frame;
	}
}
