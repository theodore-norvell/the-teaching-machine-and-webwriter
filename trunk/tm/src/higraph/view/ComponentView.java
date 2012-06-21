//     Copyright 1998--2010 Michael Bruce-Lockhart and Theodore S. Norvell
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License. 
// You may obtain a copy of the License at 
//
//     http://www.apache.org/licenses/LICENSE-2.0 
//
// Unless required by applicable law or agreed to in writing, 
// software distributed under the License is distributed on an 
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
// either express or implied. See the License for the specific language 
// governing permissions and limitations under the License.

package higraph.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.Iterator;
import java.util.Stack;
import higraph.model.interfaces.*;
import higraph.view.interfaces.Layable;
import tm.backtrack.*;
import tm.utilities.Assert;
/**
 * <p>Base class for view components. Each view component represents a
 * low level component of a {@link HigraphView}. All such components have
 * both a {@link java.awt.Shape shape} and a {@link java.awt.Shape nextShape}.
 * Their shape represents the component as currently viewed on the screen.</p>
 *  
 * <p>All view components implement {@link Layable} and thus can be manipulated
 * by an {@link SgLayoutManager}. Its layout manager always manipulates the
 * {@link java.awt.Shape nextShape}, never the current {@link java.awt.Shape shape}.</p>
 *  
 * <p>Transitions from {@link java.awt.Shape shape} to {@link java.awt.Shape nextShape}
 * will be delegated to transitionManager.</p>
 * 
 * <p>If the {@link java.awt.Shape shape} is null and the nextShape exists, a construction transition
 * will be invoked. If the {@link java.awt.Shape shape} exists and the nextShape is null a destruction
 *  transition will result. Otherwise, if nextShape is different from {@link java.awt.Shape shape}, a
 *  move transition is deemed to occur.</p>
 *  
 *  <p>In general, view components can also have {@link ZoneView zones} (mouse
 *  sensitive areas) and labels attached to them (although it should be noted that
 *  zones and labels are themselves subclasses of ComponentView). Thus,
 *  while each view component has a {@link java.awt.Shape shape} with
 *  an associated boundary, it also has an extent.</p>
 *  
 *  <p> The extent of a simple view component with no attached zones or labels
 *  is the enclosing rectangle of the boundary of its current shape. The extent
 *  of a simple view component with attached zones or labels is the union of
 *  all their extents. Note that in the awt a rectangle is nothing more
 *  than a pair of opposing corners, so that a union of rectangles is still
 *  a rectangle - the smallest one to enclose all all the rectangles that make
 *  up the union. Note that the extent of {@link NodeView node views}
 *  includes the node views of the children of the associated node.</p> 
 *  
 *  <p><strong>Known Subclasses: </strong> {@link BranchView}, 
 *  {@link EdgeView}, {@link NodeView}, {@link ZoneView}</p>
 *  
 * @author mpbl
 *
 * @param NP The Node Payload type
 * @param EP The Edge Payload type
 * @param HG The Higraph type 
 * @param WG The Whole Graph type
 * @param SG The Subgraph type
 * @param N  The Node type
 * @param E  The Edge type
 */
public abstract class ComponentView
    < NP extends Payload<NP>,
      EP extends Payload<EP>,
      HG extends Higraph<NP,EP,HG,WG,SG,N,E>,
      WG extends WholeGraph<NP,EP,HG,WG,SG,N,E>,
      SG extends Subgraph<NP,EP,HG,WG,SG,N,E>,
      N extends Node<NP,EP,HG,WG,SG,N,E>, 
      E extends Edge<NP,EP,HG,WG,SG,N,E>
    >
implements Layable {
	
	static final double  BOX = 2.0;  // size of box around mouse for boundary detection
	protected final BTVar<Color> colorVar;
	protected final BTVar<Color> fillColorVar;
	
	/* The initial color for all labels of this component. A label's color may be
	set individually once it is created */
	protected final BTVar<Color> labelColorVar;
	protected final BTVar<Stroke> strokeVar;
	protected final BTVar<HigraphView<NP,EP,HG,WG,SG,N,E>> hgViewVar;
//	protected boolean isOver = false; // Mouse is currently over this component
	// Mouse zones and labels associated with this component
	protected final BTVector<ZoneView<NP,EP,HG,WG,SG,N,E>> zones;
	protected final BTVector<Label<NP,EP,HG,WG,SG,N,E>> labels;
    protected final BTVar<String> idVar;
    protected final BTVar<Boolean> visibleVar;

	
	/**
	 * Constructor. Note that {@link java.awt.Shape NextShape} is set to the specified shape
	 *              rather than the current {@link java.awt.Shape shape}.
	 * 
	 * @param hgv The {@link HigraphView} to which this view component belongs
	 * @param c the color of the {@link java.awt.Stroke stroke} which defines the outline of the
	 *           component's {@link java.awt.Shape shape}. May be null.
	 * @param f normally the fill {@link java.awt.Color color} for filling
	 * 		the component's {@link java.awt.Shape shape}. May be null.
	 * @param s the {@link java.awt.Stroke stroke} used for drawing the
	 * 			component's {@link java.awt.Shape shape}. May be null.
	 */

	protected ComponentView(HigraphView<NP,EP,HG,WG,SG,N,E> hgv, BTTimeManager timeMan){
        hgViewVar = new BTVar<HigraphView<NP,EP,HG,WG,SG,N,E>>(timeMan, hgv);
        colorVar = new BTVar<Color>(timeMan);
		fillColorVar = new BTVar<Color>(timeMan);
		labelColorVar = new BTVar<Color>(timeMan, hgv.getDefaultLabelColor());
		strokeVar = new BTVar<Stroke>( timeMan);
		zones = new BTVector<ZoneView<NP,EP,HG,WG,SG,N,E>>(timeMan);
		labels = new BTVector<Label<NP,EP,HG,WG,SG,N,E>>(timeMan);
		idVar = new BTVar<String>(timeMan, null);
		visibleVar = new BTVar<Boolean>(timeMan, true);
	}
	
    public HigraphView<NP,EP,HG,WG,SG,N,E> getHigraphView(){
    	return hgViewVar.get();
    }
    
    /** Returns a string indicating the class of the 
     * ComponentView. For example a NodeView might return
     * "NodeView".
     * @return
     */
    public abstract String getViewType() ;
    
	/**
	 * 	
	 * @return the color of the component's shape outline
	 */
	public Color getColor() {
		return colorVar.get();
	}
	
	/**
	 * 
	 * @param color of the component's outline.
	 */
	public void setColor(Color color) {
		this.colorVar.set( color );
	}
	
	/**
	 * 	
	 * @return the color of the component's fill.
	 */
	public Color getFillColor() {
		return fillColorVar.get() ;
	}
	
	/**
	 * 
	 * @param color used to fill the component.
	 */
	public void setFillColor(Color color) {
		fillColorVar.set( color ) ;
	}

	/**
	 * 
	 * @return stroke used for the component's outline.
	 */
	public Stroke getStroke() {
		return strokeVar.get() ;
	}

	/**
	 * 
	 * @param stroke used for the component's outline.
	 */
	public void setStroke(Stroke stroke) {
		strokeVar.set( stroke );
	}
	
	public void setId(String id){
		idVar.set(id);
	}
	
	public boolean hasId(String id){
		return id == null ? idVar.get() == null : idVar.get().compareTo(id) == 0;
	}

	public String getId(){
		return idVar.get();
	}
	
	public void setVisibility(boolean visible){
		visibleVar.set(visible);
		for(ZoneView<NP,EP,HG,WG,SG,N,E> zone : zones)
			zone.setVisibility(visible);
		for(Label<NP,EP,HG,WG,SG,N,E> label : labels)
			label.setVisibility(visible);
	}

	public boolean getVisibility(){
		return visibleVar.get();
	}

	
/**
 * Note that the actual shape depends upon the subclass
 * 	
 * @return the shape used to draw the component
 */
	protected abstract Shape getShape();
	
	

/**
 * 	
 * @param p {@link java.awt.Point point} on the screen
 * @return <ul><li>the particular {@link ZoneView zone}, belonging to this
 * 		   component which contains the {@link java.awt.Point point}, or</li>
 *         <li><b>this</b> if the {@link java.awt.Point point} is inside this
 *         component but not in any of its associated components or</li>
 *         <li><b>null</b> if it's not contained in any part
 *         of this component.</li></ul>
 */
	public void getComponentsUnder(Stack<ComponentView<NP,EP,HG,WG,SG,N,E>> stack, Point p){
		if (getShape() != null && getShape().contains(p))
			stack.push(this);		// I am the deepest backup
		for(ZoneView<NP,EP,HG,WG,SG,N,E> zone : zones)  // My zones are considered before me
			zone.getComponentsUnder(stack, p);
	}
	
	/**
	 * Shape extent is the bounding rectangle of the component's
	 * raw shape.
	 * 
	 * @return a rectangular bound which wholly contains the extent
	 * of the shape
	 */
	public Rectangle2D getNextShapeExtent(){
		Shape nextShape = getNextShape();
		return nextShape == null ? new Rectangle2D.Double() : nextShape.getBounds2D();
	}
	/**
	 * Base extent is the bounding rectangle of the component's
	 * shape "unioned", with bounding rectangles
	 * of all its zones and labels.
	 * Note that inasmuch as a rectangle is defined as the two
	 * endpoints of one of its two diagonals, this definition can
	 * then be considered a true union of multiple rectangles. 
	 * @return a rectangular bound which wholly contains the extent
	 * of the shape plus all zones and labels
	 */
	
	public Rectangle2D getNextBaseExtent(){
		Rectangle2D r = getNextShapeExtent();
		if(getHigraphView().getCountZones())
			for(ZoneView<NP,EP,HG,WG,SG,N,E> zone : zones)
				Rectangle2D.union(r, zone.getNextBaseExtent(), r);
		if(getHigraphView().getCountLabels())
			for(Label<NP,EP,HG,WG,SG,N,E> label : labels)
				Rectangle2D.union(r, label.getNextShapeExtent(), r);
//System.out.println("Base extent of " + this + " is (" + r.getMinX() + ", " + r.getMinY() + ", " + r.getWidth() + ", " + r.getHeight() + ")");
		return r;
	}
	
	/**
	 * Extent is the component's baseExtent "unioned", with extents
	 * of all its children.
	 * At the moment, for anything other than nodes and possibly zones
	 * this is the same as the baseExtent
	 * @return a rectangular bound which wholly contains the extent
	 * of the shape plus all zones and labels
	 */
	
	public Rectangle2D getNextExtent(){
		return getNextBaseExtent();
	}
	


	/**
	 * Boundary check. Return a true if the point is close to the boundary.
	 * That is if a small box surrounding the point intersects the path
	 * defining the boundary of the component.
	 * 
	 * For GeneralPaths (such as branches or edges) this should be used
	 * in place of contains(p) since a line can never contain a point.
	 * 
	 * For closed shapes, it can be used to tell if the point is close
	 * to the boundary.
	 * 
	 * NOTE: this is an expensive routine. It is perfectly possible
	 * for a point to be outside the component but close to its
	 * boundary. 
	 * For conventional shapes, it is recommended the routine only be
	 * called if you know a point IS inside the component.
	 * 
	 * It is not clear how well the routine will work for severely curved
	 * paths since the Java.awt.geom package takes intersection of a shape
	 * and a rectangle to be true if any point in the rectangle is contained
	 * by the Shape. In order to do that, open shapes are deemed to be closed.
	 * It is for this reason that this algorithm examines paths segment by
	 * segment but the problem is likely to exist for seriously curved segments.
	 * 
	 * @param p the point to be checked
	 * @return true if the point is close to the boundary
	 */
	public boolean nearBoundary(Point p){
		if (getShape() == null) return false;
		PathIterator iterator = getShape().getPathIterator(null);
		double points[] = new double[6];
		Assert.check(iterator.currentSegment(points)==PathIterator.SEG_MOVETO);
		double x = points[0];
		double y = points[1];
		double startX = x;
		double startY = y;
		
		Rectangle2D r =
			new Rectangle2D.Double(p.getX() - BOX/2, p.getY() - BOX/2, BOX, BOX);
		CubicCurve2D c = new CubicCurve2D.Double();
		QuadCurve2D q = new QuadCurve2D.Double();	
		Line2D l = new Line2D.Double();
		Shape s = l;  // initialize to shut the compiler up
		
		while(!iterator.isDone()){
			int segType = iterator.currentSegment(points);
			double nextX = points[0];
			double nextY = points[1];
			switch (segType){
			case PathIterator.SEG_LINETO:
				l.setLine(x, y, nextX, nextY);
				s = l;
				break;
			case PathIterator.SEG_QUADTO:
				q.setCurve(x, y, points[2], points[3], nextX, nextY);
				s = q;
				break;
			case PathIterator.SEG_CUBICTO:
				c.setCurve(x, y, points[2], points[3], points[4], points[5],nextX, nextY);
				s = q;
				break;
			case PathIterator.SEG_CLOSE:
				l.setLine(x, y, startX, startY);
				s = l;
				break;
			}
			if (s.intersects(r)) return s.contains(p);
			x = nextX; y = nextY;
		}
		return false;
	}
	
	/**
	 * 
	 * @return true if Mouse is over the component
	 
	public boolean isMouseOver(){
		return isOver;
	}*/
	
	/**
	 * This flag should only be set by the mousing routines
	 * 
	 * @param flag true if mouse is over component
	 
			
	public void setMouseOver(boolean flag){
		isOver = flag;
		view.repaintLocalComponent(this);
	}*/
	
	/**
	 * Default is true, override for componentViews for which it is false
	 * 
	 * @return true if zones may be added to this component, false otherwise
	 */
	public boolean mayAddZones(){
		return true;
	}

	
	/**
	 * Default is true, override for componentViews for which it is false
	 * 
	 * @return true if labels may be added to this component, false otherwise
	 */
	public boolean mayAddLabels(){
		return true;
	}
	
	/**
	 *  add {@link ZoneView zone} to the list of zones associated
	 *  with this component.
	 *  @throws tm.utilities.AssertException.AssertException if
	 *  {@link ZoneView zone} already in the list.
	 * @param {@link ZoneView zone} to be added to the list
	 */
	
	public void addZone(ZoneView<NP,EP,HG,WG,SG,N,E> zone){
		Assert.check(mayAddZones(), "Zones may not be added to " + getViewType());
		Assert.check(!zones.contains(zone), "Zone already added to " + this);
		zone.setVisibility(getVisibility());		
		zones.add(zone);
	}
	
	/**
	 *  remove {@link ZoneView zone} from the list of zones associated
	 *  with this component.
	 *  @throws tm.utilities.AssertException.AssertException if
	 *  {@link ZoneView zone} is not in the list.
	 * @param {@link ZoneView zone} to be removed from the list
	 */
	public void removeZone(ZoneView<NP,EP,HG,WG,SG,N,E> zone){
		Assert.check(mayAddZones(), "Zones are not available for " + getViewType());
		Assert.check(zones.contains(zone), "Can't remove nonexistent zone from " + this);
		zones.remove(zone);
	}
	/**
	 * 
	 * @return an {@link java.util.Iterator Iterator} over the
	 *              {@link ZoneView zone} list.
	 */
	public Iterator<ZoneView<NP,EP,HG,WG,SG,N,E>> getZoneIterator(){
		Assert.check(mayAddZones(), "Zones are not available for " + getViewType());
		return zones.iterator();
	}
	
	/**
	 * find the first zoneView in the zones list with the id. It is intended
	 * that each zone in the zones list have a unique id but that is not
	 * checked for.
	 * @param id the id in the list being sought
	 * @return the first zoneView in the zones list having the specified id
	 */
	
	public ZoneView<NP,EP,HG,WG,SG,N,E> findZone(String id){
		Assert.check(id != null, "Can only search for a non-null id");
		Iterator<ZoneView<NP,EP,HG,WG,SG,N,E>> iter = getZoneIterator();
		ZoneView<NP,EP,HG,WG,SG,N,E> found;
		while (iter.hasNext()){
			found = iter.next();
			if (found.hasId(id))
				return found;
		}
		return null;
	}
	
	
	/**
	 *  add {@link Label label} to the list of labels associated
	 *  with this component.
	 *  @throws tm.utilities.AssertException.AssertException if
	 *  {@link Label label} already in the list.
	 * @param {@link Label label} to be added to the list
	 */
	
	public void addLabel(Label<NP,EP,HG,WG,SG,N,E> label){
		Assert.check(mayAddLabels(), "Labels may not be added to " + getViewType());
		Assert.check(!labels.contains(label), "Label already added to " + this);
		label.setVisibility(getVisibility());
		labels.add(label);
//		label.moveYourself();
	}
	
	/**
	 *  remove {@link Label label} from the list of labels associated
	 *  with this component.
	 *  @throws tm.utilities.AssertException.AssertException if
	 *  {@link Label label} is not in the list.
	 * @param {@link Label label} to be removed from the list
	 */
	public void removeLabel(Label<NP,EP,HG,WG,SG,N,E> label){
		Assert.check(mayAddLabels(), "Labels are not available for " + getViewType());
		Assert.check(labels.contains(label), "Can't remove nonexistent label from " + this);
		labels.remove(label);
	}
	/**
	 * 
	 * @return an {@link java.util.Iterator Iterator} over the
	 *              {@link LabelView label} list.
	 */
	public Iterator<Label<NP,EP,HG,WG,SG,N,E>> getLabelIterator(){
		Assert.check(mayAddLabels(), "Labels are not available for " + getViewType());
		return labels.iterator();
	}
	
	/**
	 * find the first label in the labels list with the id. It is intended
	 * that each label in the labels list have a unique id but that is not
	 * checked for.
	 * @param id the id in the list being sought
	 * @return the first label in the labels list having the specified id
	 */
	
	public Label<NP,EP,HG,WG,SG,N,E> findLabel(String id){
		Assert.check(id != null, "Can only search for a non-null id");
		Iterator<Label<NP,EP,HG,WG,SG,N,E>> iter = getLabelIterator();
		Label<NP,EP,HG,WG,SG,N,E> found;
		while (iter.hasNext()){
			found = iter.next();
			if (found.hasId(id))
				return found;
		}
		return null;
	}
		
	/**
	 * Prepare this view to receive tick messages
	 */
	protected void startTransition(){
		for(ZoneView<NP,EP,HG,WG,SG,N,E>zone : zones)
			zone.startTransition();		
		for(Label<NP,EP,HG,WG,SG,N,E>label : labels)
			label.startTransition();		
	}
	
	/** Advance the position according to the degree.
	 * @param degree in [0.0, 1.0] */
	protected void advanceTransition( double degree ) {
		for(ZoneView<NP,EP,HG,WG,SG,N,E>zone : zones)
			zone.advanceTransition( degree );		
		for(Label<NP,EP,HG,WG,SG,N,E>label : labels)
			label.advanceTransition( degree );
	}
	
	/**
	 * Finalize the transition by moving to the final position.
	 */
	protected void finishTransition(){
		for(ZoneView<NP,EP,HG,WG,SG,N,E>zone : zones)
			zone.finishTransition();		
		for(Label<NP,EP,HG,WG,SG,N,E>label : labels)
			label.finishTransition();		
	}
	
	/**
	 * Sets the next {@link java.awt.Shape shape} to n in a
	 * way that depends upon the kind of shape. See subclasses for details.
	 * @param n a reference to a {@link java.awt.Shape shape}
	 */
	public abstract void setNextShape(Shape n);
	
	/** 
	 * @return a clone of {@link java.awt.Shape nextShape}. 
	 */
	public abstract Shape getNextShape();
	
	public final double getNextX() {
		Rectangle2D r =  getNextBaseExtent();
		return r.getMinX();
	}
	
	public final double getNextY() {
		Rectangle2D r =  getNextBaseExtent();
		return r.getMinY();
	}
	
	public final double getNextCenterX() {
		Rectangle2D r =  getNextBaseExtent();
		return r.getCenterX();
	}
	
	public final double getNextCenterY() {
		Rectangle2D r =  getNextBaseExtent();
		return r.getCenterY();
	}
	
	public final double getNextWidth() {
		Rectangle2D r =  getNextBaseExtent();
		return r.getWidth();
	}
	
	public final double getNextHeight() {
		Rectangle2D r =  getNextBaseExtent();
		return r.getHeight();
	}
	
	
	
	/**
	 * translate {@link java.awt.Shape nextShape} Note that the shape itself plus any attached zones and
	 * labels are translated but not any other shapes that depend upon them (for example, and quite
	 * specifically, children of nodes).
	 * 
	 * This method must be overridden because the translation depends upon the type of shape used by
	 * the subclass. Translation of General Paths is different from translation of Rectangular shapes.
	 * 
	 * The override MUST call this method after it has done the component shape translation since self
	 * placement of zones and labels is relative to this component.
	 * 
	 * @param dx change in the x direction
	 * @param dy change in the y direction
	 */
	
			
	public void translateNext(double dx, double dy){
		moveZones();
		moveLabels();
	}
	
	/**
	 * This is always a convenience method and should not be over-ridden so it's meaning is consistent
	 * 
	 * @param x the new x location of the top left corner
	 * @param y the new y location of the top left corner
	 */
	final public void placeNext(double x, double y){
		translateNext(x - getNextX(), y - getNextY());
	}
	
	/**
	 * move the components labels relative to the component's current next location
	 */
	
	public final void moveLabels(){	
		for(Label<NP,EP,HG,WG,SG,N,E>label : labels)
			moveLabel(label);
	}

	/**
	 * move the components zones relative to the component's current next location
	 */
	
	public final void moveZones(){	
		for(ZoneView<NP,EP,HG,WG,SG,N,E>zone : zones)
			moveZone(zone);
	}
	
	/**
	 * The way in which a label is moved relative to its component is dependent upon the type of component.
	 * COmponents which actually have labels have to override this method.
	 * @param label the label to be moved
	 */
	public void moveLabel(Label<NP,EP,HG,WG,SG,N,E>label){
	}
	
	public void moveZone(ZoneView<NP,EP,HG,WG,SG,N,E>zone){
	}


	/**
	 * Default drawing routine for component views.
	 * Override at your peril.  draw(Graphics2D) calls drawSelf and then drawChildren.
	 * These two methods are intended for overriding.
	 * @param screen
	 * 
	 * NOTE: TBD We probably need a Label class and it is likely a component
	 * could have multiple labels so we need a routine drawLabels.
	 * --Yes labels should be zones and as such should be drawn by drawChildren. TSN
	 */
	public void draw(Graphics2D screen) {
		// If shape is null, no doTransition has yet been called on this component
	    if( screen == null  || getShape() == null ) return;
	    drawSelf(screen);
	    if (this.mayAddZones()) drawZones(screen);
	    if (this.mayAddLabels()) drawLabels(screen);
	}
	
	/**  By default, draws the
     * {@link java.awt.Shape shape} in the outline {@link java.awt.Color color}
     * using the outline {@link java.awt.Stroke stroke} and fills
     * it with the {@link java.awt.Color fillColor} */
	protected void drawSelf(Graphics2D screen) {
		if (!getVisibility()) return;
		Color currentColor = screen.getColor();
		Stroke currentStroke = screen.getStroke();
		Shape s = getShape();		
		if (fillColorVar.get() != null){
			screen.setColor( fillColorVar.get() );
			screen.fill(s);
		}
		if (colorVar.get() != null && strokeVar.get() != null) {
//				System.out.println("Drawing " + toString() );
			screen.setColor( colorVar.get() );
			screen.setStroke( strokeVar.get() );
			screen.draw(s);
		}
		screen.setColor(currentColor);	
		screen.setStroke(currentStroke);
	}
	
	/** By default, calls draw on all
     * {@link ZoneView zones} in the zone list. */
	protected void drawZones(Graphics2D screen) {
		for(ZoneView<NP,EP,HG,WG,SG,N,E>zone : zones)
			zone.draw(screen);
	}
	
	/** By default, calls draw on all
     * {@link Label labels} in the label list. */
	protected void drawLabels(Graphics2D screen) {
		for(Label<NP,EP,HG,WG,SG,N,E>label : labels)
			label.draw(screen);
	}

	public void refresh(){
		hgViewVar.get().repaintLocalComponent(this);
	}
	
	protected abstract ComponentView<NP,EP,HG,WG,SG,N,E> getThis();

}
