package ratRace.view;

import java.util.Set;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;

import ratRace.model.Model;
import telford.common.*;

public class View extends Container implements Observer, ViewI {
	private final Model model ;
	private final int modelWidth ;
	private final int modelHeight ;
	private final Set<ViewListenerI> myListeners 
	    = new HashSet<ViewListenerI>() ;

	@Override
	public void update(Observable arg0, Object arg1) {		
		repaint();
	}
	
	public View( Model model ) {
		this.model = model ;
		modelWidth = model.getMazeWidth();
		modelHeight = model.getMazeHeight();
		model.addObserver( this ) ;
		addMouseListener(new MyMouseListener()) ;
		timer.start() ;
		repaint() ;
	}
	
	public void addListener( ViewListenerI listener) {
		myListeners.add( listener ) ;
	}
	
	private Point worldToView(double i, double j) {
		double myWidth = getWidth();
		double myHeight = getHeight() ;
		double scale = Math.min( myWidth/(modelWidth+4), myHeight/(modelHeight+4) ) ;
		// Multiplying by scale converts model units to view units.
		double x_offset = (myWidth-scale*modelWidth)/2 ;
		double y_offset = (myHeight-scale*modelHeight)/2 ;
		double x =  j * scale + x_offset;
		double y =  i * scale + y_offset;
		return new Point(x, y) ;
	}
	
	private Point viewToWorld(double x, double y) {	
		double myWidth = getWidth() ;
		double myHeight = getHeight() ;
		double scale = Math.min( myWidth/(modelWidth+4), myHeight/(modelHeight+4) ) ;
		// Multiplying by scale converts model units to view units.
		double x_offset = (myWidth-scale*modelWidth)/2 ;
		double y_offset = (myHeight-scale*modelHeight)/2 ;
		double j =  (x-x_offset) / scale;
		double i =  (y-y_offset) / scale ;
		return new Point(i,j) ;
	}
	
	@Override public void paintComponent( Graphics g ) {
		super.paintComponent(g) ;

		int originalColor = g.getColor() ;
		Font originalFont = g.getFont() ;
		
		Point p0 = worldToView( 0, 0 ) ;
		Point p1 = worldToView( modelWidth, modelHeight ) ;
		g.setColor( 0 ) ;
		
		for( int i = 0 ; i <= modelHeight ; ++i ) {
			for( int j = 0 ; j <= modelWidth ; ++j ) {
				if( j < modelWidth && model.hasHWall(i,j) ) {
					p0 = worldToView(i, j) ;
					p1 = worldToView(i, j+1) ;
					Line line =  new Line(p0,p1);
					g.draw(line); 
				}
				if( i < modelHeight && model.hasVWall(i,j) ) {
					p0 = worldToView(i, j) ;
					p1 = worldToView(i+1, j) ;
					Line line = new Line (p0, p1);
					g.draw( line ) ;
				} } }
		
		// Goal
		double goal_i = model.getGoal_i() ;
		double goal_j = model.getGoal_j() ;
		p0 = worldToView(goal_i,goal_j) ;
		p1 = worldToView(goal_i+1, goal_j+1) ;
		double x = p0.getX(), y = p0.getY();
		double w = p1.getX()-x, h = p1.getY()-y ;
		g.setColor( 0xFF) ;
		g.fillRect((int)x, (int)y, (int)w+1, (int)h+1) ;
		
		// Rat
		double rat_i = model.getRat_i() ;
		double rat_j = model.getRat_j() ;
		double radius = model.getRatRadius() ;
		p0 = worldToView(rat_i-radius, rat_j-radius) ;
		p1 = worldToView(rat_i+radius, rat_j+radius) ;
		x = p0.getX(); y = p0.getY();
		w = p1.getX()-x; h = p1.getY()-y ;
		g.setColor( 0xFF0000) ;
		g.fillOval((int)x, (int)y, (int)w+1, (int)h+1) ;
		
		// Draw the elapsed time.
		g.setColor( 0XFF ) ;
		String message = ""+ model.elapsedTime() ;
		int width = (int) getWidth() ;
		int height = (int) getHeight() ;
		if( model.gameOver() ) {
			Font bigFont = Kit.getKit().getFont() ;
			FontMetrics fm = g.getFontMetrics(bigFont) ;
			int wf = fm.stringWidth(message) ;
			int xf = width/2 - wf/2 ;
			int hf = fm.getAscent() ;
			int yf = height/2 + hf/2 ;
			g.setFont( bigFont ) ;
			g.drawString(message, xf, yf) ;
		} else {
			Font smallFont = Kit.getKit().getFont() ;
			FontMetrics fm = g.getFontMetrics(smallFont) ;
			int wf = fm.stringWidth(message) ;
			int xf = (int)( width*0.9) - wf ;
			int hf = fm.getAscent() ;
			int yf = (int)( height*0.1) + hf ;
			g.setFont( smallFont ) ;
			g.drawString(message, xf, yf) ; }
		
		g.setColor(originalColor) ;
		g.setFont(originalFont) ;
	}
	
	public void terminate() {
		timer.stop() ;
	}
	
	public void start(){
		timer.start();
	}
	
	// It is important to use a Swing timer here
	private telford.common.Timer timer = Kit.getKit().getTimer(50, true, new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			for( ViewListenerI listener : myListeners ) {
				listener.pulse() ;
			} }});
	
	class MyMouseListener implements telford.common.MouseListener {
		@Override
		public void mouseMoved(MouseEvent e) {
			double x = e.getX() ;
			double y = e.getY();
			Point p = viewToWorld(x, y) ;
			for( ViewListenerI listener : myListeners ) {
				listener.mouseMovedTo(p.getX(), p.getY()) ;
			}
		}
	}
}
