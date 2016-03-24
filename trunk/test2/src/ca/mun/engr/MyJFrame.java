package ca.mun.engr;

import java.awt.Graphics;

import javax.swing.JFrame;

public class MyJFrame extends JFrame{
	public MyJFrame(String name){
		super(name);
	}
	
	@Override
	public void paint(Graphics g){
		System.out.println("paint()");
		super.paint(g);
	}
	
	@Override 
	public void repaint(){
		System.out.println("repaint()");
		super.repaint();
	}
	
}
