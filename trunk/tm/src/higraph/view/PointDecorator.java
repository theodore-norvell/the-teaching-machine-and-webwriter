package higraph.view;

import higraph.model.interfaces.Edge;
import higraph.model.interfaces.Higraph;
import higraph.model.interfaces.Node;
import higraph.model.interfaces.Payload;
import higraph.model.interfaces.Subgraph;
import higraph.model.interfaces.WholeGraph;
import higraph.view.interfaces.Layable;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Point2D;

import tm.backtrack.BTTimeManager;
import tm.backtrack.BTVar;

/**
 * A Point decorator is a small decorator designed to be placed at a point. Examples might include
 * arrowheads or small circles that appear to be attached to the edges of nodes. Each decorator
 * defines how it is to be drawn in 2D about a reference point (x,y). There are no restrictions on
 * where the point is. However, the decorator is placed by placing (x,y). 
 * 
 *  A horizontal line through the reference point defines an angle of 0 degrees. The
 *  drawing routine should be capable of drawing the decorator at any angle up to +- 180 degrees.
 *  Positive degrees represents clockwise angles (since positive y in graphics is downwards).
 * @author mpbl
 *
 */

public abstract class PointDecorator
	< NP extends Payload<NP>,
	EP extends Payload<EP>,
	HG extends Higraph<NP,EP,HG,WG,SG,N,E>,
	WG extends WholeGraph<NP,EP,HG,WG,SG,N,E>,
	SG extends Subgraph<NP,EP,HG,WG,SG,N,E>,
	N extends Node<NP,EP,HG,WG,SG,N,E>, 
	E extends Edge<NP,EP,HG,WG,SG,N,E>
	>
implements Layable
{
	
	protected final BTVar<Double> xVar;
	protected final BTVar<Double> yVar;
	protected final BTVar<Double> nextXVar;
	protected final BTVar<Double> nextYVar;
	protected final BTVar<Double> thetaVar;
	protected final BTVar<Double> nextThetaVar;
//	protected final BTVar<Color> colorVar;
//	protected final BTVar<Stroke> strokeVar;
//	protected final BTVar<Boolean> visibleVar;
	protected final BTVar<ComponentView<NP,EP,HG,WG,SG,N,E>> ownerVar;
	protected final BTTimeManager timeMan;
	
	protected PointDecorator(HigraphView<NP,EP,HG,WG,SG,N,E> view, BTTimeManager tm){
		xVar = new BTVar<Double> (tm, 0.);
		yVar = new BTVar<Double> (tm, 0.);
		nextXVar = new BTVar<Double> (tm, 0.);
		nextYVar = new BTVar<Double> (tm, 0.);
		thetaVar = new BTVar<Double> (tm, 0.);
		nextThetaVar = new BTVar<Double> (tm, 0.);

		ownerVar = new BTVar<ComponentView<NP,EP,HG,WG,SG,N,E>>(tm);
//		colorVar = new BTVar<Color> (tm, view.getDefaultBranchColor());
//		strokeVar = new BTVar<Stroke> (tm, view.getDefaultBranchStroke());
//		visibleVar = new BTVar<Boolean>(tm, true);
		timeMan = tm;
	}
	
	/**
	 *  Make a decorator of the same subtype as pd
	 * @param pd a concrete decorator of some subtype of PointDecorator
	 * @param view the HigraphView in which the decorator is to be made
	 * @param tm the BTTimeManager
	 * @return
	 */
	public abstract PointDecorator<NP,EP,HG,WG,SG,N,E> makeDecorator(PointDecorator<NP,EP,HG,WG,SG,N,E> pd,
			HigraphView<NP,EP,HG,WG,SG,N,E> view, BTTimeManager tm);
	
	@Override
	public boolean isPinned() {
		return false;
	}

	
	/**
	 * Place the decorator
	 * @param x x co-ordinate of place location
	 * @param y y-co-ordinate of place location
	 */
	public void placeNext (double x, double y){
		nextXVar.set(x);
		nextYVar.set(y);
	}
	
	@Override
	public void translateNext(double dx, double dy) {
		nextXVar.set(getNextX() + dx);
		nextYVar.set(getNextY() + dy);
	}
	
	public void setOwner(ComponentView<NP,EP,HG,WG,SG,N,E> owner){
		ownerVar.set(owner);
	}

	public ComponentView<NP,EP,HG,WG,SG,N,E> getOwner(){
		return ownerVar.get();
	}

	

	
	/**
	 * 
	 * @return the decorator position along the x axis
	 */
	public double getX(){
		return xVar.get();
	}

	/**
	 * 
	 * @return the decorator position along the y axis
	 */

	public double getY(){
		return yVar.get();
	}
	
	/**
	 * Set the drawing angle of the decorator
	 * @param theta drawing angle in radians
	 */
	public void angleNext(double theta){
		nextThetaVar.set(theta);
	}
	
	/**
	 * 
	 * @return the decorator angle in radians
	 */

	public double getAngle(){
		return thetaVar.get();
	}

	/**
	 *  Set the decorator drawing color
	 *  
	 * @param c the color the decorator is to be drawn in
	 
	public void setColor(Color c){
		colorVar.set(c);
	}*/
	
	/**
	 * 
	 * @return the color the decorator is drawn in
	 
	public Color getColor(){
		return 	colorVar.get();
	}*/
	
	/**
	 * Set the stroke used to draw the decorator, if there is one. There is no guarantee
	 * the decorator uses stroke drawing so this may have no effect.
	 * 
	 * @param s characteristics of the Stroke if one is used
	
	public void setStroke(Stroke s){
		strokeVar.set(s);
	} */
	
	/**
	 * 
	 * @return the current Stroke setting
	 
	public Stroke getStroke(){
		return 	strokeVar.get();
	}*/
	
	public double getNextX(){
		return nextXVar.get();
	}
	
	public double getNextY(){
		return nextYVar.get();
	}
	
	protected void startTransition() {
		//TODO
	}
	
	protected void advanceTransition(double degree) {
		// TODO
	}
	
	protected void finishTransition(){
		xVar.set(nextXVar.get());
		yVar.set(nextYVar.get());
		thetaVar.set(nextThetaVar.get());
	}
	

	
	/* convenience method for rotating a single point through theta degrees. Positive theta is clockwise
	 * since positive y is downwards in computer graphics.
	 */
	protected void rotate(Point2D.Double p1, Point2D.Double pc, double theta){
		rotate (p1, pc, Math.cos(theta), Math.sin(theta));
	}
	
	/* Use this method with precomputed trig values to rotate multiple points through the same angle */
	protected void rotate(Point2D.Double p1, Point2D.Double pc, double cosTheta, double sinTheta){
		p1.x -= pc.x;
		p1.y -= pc.y;
		double newX = p1.x * cosTheta - p1.y * sinTheta;
		p1.y = p1.y * cosTheta + p1.x * sinTheta;
		p1.x = newX + pc.x;
		p1.y += pc.y;		
	}
	
	
	/**
	 * Draw the decorator around the reference point (x,y) at the set angle, in the
	 * color specified, using the stroke specified (if stroke drawing is used).
	 * 
	 * @param screen the graphics context for the drawing
	 */
	
	public abstract void draw(Graphics2D screen);
}
