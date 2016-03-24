package ca.mun.engr;

import java.awt.Graphics;

class Circle extends Shape{

	public Circle(int x, int y, int width, int height) {
		super(x, y, width, height);
		// TODO Auto-generated constructor stub
		if(width!=height)
			System.out.println("The width and height are not equal!");
	}
	  
    public void draw(Graphics g) {
        g.drawOval(x, y, width, height);
    }
	
}