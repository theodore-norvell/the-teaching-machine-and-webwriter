package telford.common;

public interface Graphics {
	
	public void setColor( int color ) ;
	

	public void fillRect(int x, int y, int width, int height ) ;
	

	public void drawRect(int x, int y, int width, int height ) ;
	
	public void setFont( Font f) ;
	
	public Font getFont() ;
	
	public void drawString(char[] chars, int i, int count, int x, int y);

	public void drawString( String message, int x, int y ) ;
	
	public FontMetrics getFontMetrics( Font f ) ;

	public int getColor();

	public void fillOval(int x, int y, int weight, int height);

	public void draw(Line line);
}