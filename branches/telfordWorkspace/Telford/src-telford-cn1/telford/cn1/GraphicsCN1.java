package telford.cn1;

import telford.common.Font;
import telford.common.FontMetrics;
import telford.common.Line;

import com.codename1.ui.Graphics;

public class GraphicsCN1 implements telford.common.Graphics {
	Graphics g ;
	
	public GraphicsCN1( Graphics g) {this.g = g ; }
	
	public void setColor( int rgb ) { this.g.setColor( rgb) ; }
	
	public void fillRect(int x, int y, int width, int height ) { g.fillRect( x, y, width, height ) ; }

	public void drawRect(int x, int y, int width, int height) { g.drawRect(x, y, width, height) ; }

	public void setFont(Font f) { g.setFont( ((FontCN1)f).font ) ; }

	public Font getFont() { return new FontCN1( g.getFont() ) ; }

	public void drawString(String message, int x, int y) {
		g.drawString(message, x, y) ;
		
	}
	
	public FontMetrics getFontMetrics( Font f ) {
		return new FontMetricsCN1( ((FontCN1)f).font ) ;
	}

	public int getColor() {
		return g.getColor();
	}

	public void fillOval(int x, int y, int weight, int height) {
		g.
	}

	public void draw(Line line) {
		g.drawLine(line.p0.getX(), line.p0.getY(), line.p1.getX(), line.p1.getY());
	}

}
