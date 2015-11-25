package ca.mun.engr;

import java.awt.Graphics;

class Triangle extends Shape{

	public Triangle(int x, int y, int width, int height) {
		super(x, y, width, height);
		// TODO Auto-generated constructor stub
	}
	public void draw(Graphics g) {
		g.drawPolygon(new int[]{(x-width/2),x,(x+width/2)},new int[]{y+height,y,y+height},3);
	}
	
}