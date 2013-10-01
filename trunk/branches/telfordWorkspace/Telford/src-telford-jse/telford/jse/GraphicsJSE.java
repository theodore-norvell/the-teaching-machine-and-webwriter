package telford.jse;

import java.awt.*;

public class GraphicsJSE implements telford.common.Graphics {
	Graphics2D g ;
	GraphicsJSE( Graphics2D g ) { this.g = g ; }
	
	@Override
	public void setColor(int color) {
		g.setColor(new Color(color)) ;
	}
	@Override
	public void fillRect(int x, int y, int width, int height) {
		g.fillRect( x, y, width, height) ;
	}
	@Override
	public void drawRect(int x, int y, int width, int height) {
		g.drawRect(x, y, width, height) ;
		
	}
	@Override
	public void setFont(telford.common.Font f) {
		g.setFont( ((FontJSE)f).f) ;
		
	}
	@Override
	public telford.common.Font getFont() {
		return new FontJSE( g.getFont() ) ;
	}
	@Override
	public void drawString(String str, int x, int y) {
		g.drawString( str, x, y) ;
		
	}
	@Override
	public telford.common.FontMetrics getFontMetrics(telford.common.Font f) {
		return new FontMetricsJSE(g.getFontMetrics( ((FontJSE)f).f ) ) ;
	}
}
