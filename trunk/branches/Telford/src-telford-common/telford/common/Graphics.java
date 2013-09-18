package telford.common;

public interface Graphics {
	
	public void setColor( int color ) ;
	

	public void fillRect(int x, int y, int width, int height ) ;
	

	public void drawRect(int x, int y, int width, int height ) ;
	
	public void setFont( Font f) ;
	
	public Font getFont() ;

	public void drawString( String message, int x, int y ) ;
	
	public FontMetrics getFontMetrics( Font f ) ;
}