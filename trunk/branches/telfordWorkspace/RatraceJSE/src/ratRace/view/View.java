package ratRace.view;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Set;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import ratRace.model.Model;
import telford.common.Component;


public class View implements Observer, ViewI {
	private final Model model ;
	private final int modelWidth ;
	private final int modelHeight ;
	private Component component;
	private final Set<ViewListenerI> myListeners 
	    = new HashSet<ViewListenerI>() ;

	@Override
	public void update(Observable arg0, Object arg1) {
		repaint() ;
	}
	
	public View( Model model ) {
		this.model = model ;
		modelWidth = model.getMazeWidth();
		modelHeight = model.getMazeHeight();
		model.addObserver( this ) ;
		addMouseMotionListener(new MyMouseListener()) ;
		timer.setRepeats(true) ;
		timer.start() ;
		repaint() ;
	}
	
	public void addListener( ViewListenerI listener) {
		myListeners.add( listener ) ;
	}
	
	private Point2D worldToView(double i, double j) {
		double myWidth = getWidth() ;
		double myHeight = getHeight() ;
		double scale = Math.min( myWidth/(modelWidth+4), myHeight/(modelHeight+4) ) ;
		// Multiplying by scale converts model units to view units.
		double x_offset = (myWidth-scale*modelWidth)/2 ;
		double y_offset = (myHeight-scale*modelHeight)/2 ;
		double x = j * scale + x_offset;
		double y = i * scale + y_offset;
		return new Point2D.Double(x,y) ;
	}
	
	private Point2D viewToWorld(double x, double y) {
		double myWidth = getWidth() ;
		double myHeight = getHeight() ;
		double scale = Math.min( myWidth/(modelWidth+4), myHeight/(modelHeight+4) ) ;
		// Multiplying by scale converts model units to view units.
		double x_offset = (myWidth-scale*modelWidth)/2 ;
		double y_offset = (myHeight-scale*modelHeight)/2 ;
		double j = (x-x_offset) / scale;
		double i = (y-y_offset) / scale ;
		return new Point2D.Double(i,j) ;
	}
	
	@Override public void paintComponent( Graphics g ) {
		super.paintComponent(g) ;
		if( !(g instanceof Graphics2D) )
			throw new AssertionError("Needs Graphics2D support." ) ;
		Graphics2D g2d = (Graphics2D) g ;
		Color originalColor = g2d.getColor() ;
		Font originalFont = g2d.getFont() ;
		
		Point2D p0 = worldToView( 0, 0 ) ;
		Point2D p1 = worldToView( modelWidth, modelHeight ) ;
		g2d.setColor( Color.BLACK ) ;
		
		for( int i = 0 ; i <= modelHeight ; ++i ) {
			for( int j = 0 ; j <= modelWidth ; ++j ) {
				if( j < modelWidth && model.hasHWall(i,j) ) {
					p0 = worldToView(i, j) ;
					p1 = worldToView(i, j+1) ;
					Line2D line = new Line2D.Double(p0, p1) ;
					g2d.draw( line ) ;
				}
				if( i < modelHeight && model.hasVWall(i,j) ) {
					p0 = worldToView(i, j) ;
					p1 = worldToView(i+1, j) ;
					Line2D line = new Line2D.Double(p0, p1) ;
					g2d.draw( line ) ;
				} } }
		
		// Goal
		double goal_i = model.getGoal_i() ;
		double goal_j = model.getGoal_j() ;
		p0 = worldToView(goal_i,goal_j) ;
		p1 = worldToView(goal_i+1, goal_j+1) ;
		double x = p0.getX(), y = p0.getY();
		double w = p1.getX()-x, h = p1.getY()-y ;
		g2d.setColor( Color.BLUE ) ;
		g2d.fillRect((int)x, (int)y, (int)w+1, (int)h+1) ;
		
		// Rat
		double rat_i = model.getRat_i() ;
		double rat_j = model.getRat_j() ;
		double radius = model.getRatRadius() ;
		p0 = worldToView(rat_i-radius, rat_j-radius) ;
		p1 = worldToView(rat_i+radius, rat_j+radius) ;
		x = p0.getX(); y = p0.getY();
		w = p1.getX()-x; h = p1.getY()-y ;
		g2d.setColor( Color.RED) ;
		g2d.fillOval((int)x, (int)y, (int)w+1, (int)h+1) ;
		
		// Draw the elapsed time.
		g2d.setColor( new Color(0, 0, 255, 100) ) ;
		String message = ""+ model.elapsedTime() ;
		int width = getWidth() ;
		int height = getHeight() ;
		if( model.gameOver() ) {
			Font bigFont = new Font(Font.MONOSPACED, Font.PLAIN, 100 ) ;
			FontMetrics fm = g2d.getFontMetrics(bigFont) ;
			int wf = fm.stringWidth(message) ;
			int xf = width/2 - wf/2 ;
			int hf = fm.getAscent() ;
			int yf = height/2 + hf/2 ;
			g2d.setFont( bigFont ) ;
			g2d.drawString(message, xf, yf) ;
		} else {
			Font smallFont = new Font(Font.MONOSPACED, Font.PLAIN, 20 ) ;
			FontMetrics fm = g2d.getFontMetrics(smallFont) ;
			int wf = fm.stringWidth(message) ;
			int xf = (int)( width*0.9) - wf ;
			int hf = fm.getAscent() ;
			int yf = (int)( height*0.1) + hf ;
			g2d.setFont( smallFont ) ;
			g2d.drawString(message, xf, yf) ; }
		
		g2d.setColor(originalColor) ;
		g2d.setFont(originalFont) ;
	}
	
	public void terminate() {
		timer.stop() ;
	}
	
	// It is important to use a Swing timer here
	private javax.swing.Timer timer = new javax.swing.Timer(50,
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					for( ViewListenerI listener : myListeners ) {
						listener.pulse() ;
					} }});
	
	class MyMouseListener extends MouseMotionAdapter {
		@Override
		public void mouseMoved(MouseEvent e) {
			double x = e.getX() ;
			double y = e.getY();
			Point2D p = viewToWorld(x, y) ;
			for( ViewListenerI listener : myListeners ) {
				listener.mouseMovedTo(p.getX(), p.getY()) ;
			}
		}
	}
}
