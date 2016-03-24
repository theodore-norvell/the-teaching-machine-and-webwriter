package ca.mun.engr;

import java.awt.Graphics;

class Square extends Shape{

	public Square(int x, int y, int width, int height) {
		super(x, y, width, height);
		// TODO Auto-generated constructor stub
	}
	  
    public void draw(Graphics g) {
        g.drawRect(x, y, width, height);
    }
}