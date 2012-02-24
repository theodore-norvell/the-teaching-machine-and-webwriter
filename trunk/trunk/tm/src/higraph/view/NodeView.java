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

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.awt.geom.RoundRectangle2D;
import java.awt.geom.Point2D.Double;
import java.util.Collection;

import tm.backtrack.* ;
import tm.utilities.Assert;

import higraph.model.interfaces.*;
import higraph.utilities.Geometry;
import higraph.view.layout.AbstractLayoutManager;

/**
 * <p>A view of a {@link higraph.model.interfaces.Node Node}.
 * If the NodeView represents a {@link higraph.model.interfaces.Node child node}
 * and branches between parents and children are to be displayed in this
 * {@link HigraphView}, then this node view will have an associated
 * {@link BranchView}.</p>
 *
 * <p>Note that the extent for NodeViews includes the extents of all the NodeViews
 * (taken from the same {@link HigraphView} as this NodeView)
 * of all the descendants of this NodeView's associated
 * {@link higraph.model.interfaces.Node Node}.</p>
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


public class NodeView 	
    < NP extends Payload<NP>,
      EP extends Payload<EP>,
      HG extends Higraph<NP,EP,HG,WG,SG,N,E>,
      WG extends WholeGraph<NP,EP,HG,WG,SG,N,E>,
      SG extends Subgraph<NP,EP,HG,WG,SG,N,E>,
      N extends Node<NP,EP,HG,WG,SG,N,E>, 
      E extends Edge<NP,EP,HG,WG,SG,N,E> >
extends ComponentView<NP,EP,HG,WG,SG,N,E> 
{
    public static final String TYPE_STRING = "NodeView" ;

	private final N myNode ;
	private final BTVar<Boolean> pinnedVar ;		// is node location fixed
	private final BTVar<AbstractLayoutManager<NP,EP,HG,WG,SG,N,E>> layoutManagerVar;
	private final BTVar<BranchView<NP,EP,HG,WG,SG,N,E>> branchViewVar;
	
	// Invariant: The values of shapeVar and nextShapeVar are local to this object in the
    // sense that they are not shared with other objects.
    // Thus we always clone when setting or getting these.
	// The shapeVar and nextShapeVar may however have values that are the same object.
    private final BTVar<RectangularShape> shapeVar;
	private final BTVar<RectangularShape> nextShapeVar;
	private final BTVar<Rectangle2D> extentVar;
//	private final BTVar<Rectangle2D> nextExtentVar;
	private final BTVar<Point2D.Double> dislocationVar ;
	private final BTVar<Point2D.Double> placementVar ;
	/* draw outline (a box defining the extent) instead of the shape defined for this component*/
    protected final BTVar<Boolean> drawOutlineOnlyVar;
    
// Allowable shapes for nodes
    public final static int ELLIPSE = 0;
    public final static int RECTANGLE = 1;
    public final static int ROUND_RECTANGLE = 2;

	
	/**
	 * Virtual constructor. Note that {@link java.awt.Shape NextShape} is set to the specified shape
	 *              rather than the current {@link java.awt.Shape shape}.
	 *
	 * @param vf the ViewFactory used to invoke this constructor
	 * @param v The {@link HigraphView} to which this view component belongs
	 * @param node the model {@link higraph.model.interfaces.Node Node} which this view represents
	 * @param c the color of the {@link java.awt.Stroke stroke} which defines the outline of the
	 *           component's {@link java.awt.Shape shape}. May be null.
	 * @param f normally the fill {@link java.awt.Color color} for filling
	 * 		the component's {@link java.awt.Shape shape}. May be null.
	 * @param s the {@link java.awt.Stroke stroke} used for drawing the
	 * 			component's {@link java.awt.Shape shape}. May be null.
	 * @param r the rectangularShape used for the shape 
	 */ 
	
	public NodeView(HigraphView<NP,EP,HG,WG,SG,N,E> v, N node, BTTimeManager timeMan ) {
		super(v, timeMan);
		myNode = node ;
		pinnedVar = new BTVar<Boolean>( timeMan, false ) ;
		layoutManagerVar = new BTVar<AbstractLayoutManager<NP,EP,HG,WG,SG,N,E>>( timeMan ) ; // initially null.
		branchViewVar = new BTVar<BranchView<NP,EP,HG,WG,SG,N,E>>( timeMan ) ; // initially null. 
		shapeVar = new BTVar<RectangularShape>( timeMan ) ; // there is no existing shape
		nextShapeVar = new BTVar<RectangularShape>( timeMan, v.getDefaultNodeShape()) ;
		extentVar = new BTVar<Rectangle2D>( timeMan ) ; // initially next
//		nextExtentVar = new BTVar<Rectangle2D>( timeMan, nextShapeVar.get().getBounds() ) ; 
		dislocationVar = new BTVar<Point2D.Double>( timeMan) ;
		placementVar = new BTVar<Point2D.Double>( timeMan) ;
		drawOutlineOnlyVar = new BTVar<Boolean>(timeMan, false);
//		showNodeNameVar = new BTVar<Boolean>(timeMan, v.getDefaultShowNodeName());
		colorVar.set(v.getDefaultNodeColor());
		fillColorVar.set(v.getDefaultNodeFillColor());
		strokeVar.set(v.getDefaultNodeStroke());
		
//		System.out.println("Creating view of node " + node + " at " + r.getBounds2D());
	}
	

	public NodeView<NP,EP,HG,WG,SG,N,E> getParent(){
		return hgViewVar.get().getNodeView(myNode.getParent());		
	}
    
    @Override public String getViewType() {
        return TYPE_STRING ;
    }
	
	public int getNumChildren(){
		return myNode.getNumberOfChildren();
	}
	
	public NodeView<NP,EP,HG,WG,SG,N,E> getChild(int i){
		return hgViewVar.get().getNodeView(myNode.getChild(i));
	}
	
	public BranchView<NP,EP,HG,WG,SG,N,E> getBranch(){
		BranchView<NP,EP,HG,WG,SG,N,E> brView = branchViewVar.get();
		if (brView == null && getParent() != null) {
			HigraphView<NP,EP,HG,WG,SG,N,E> hgView = hgViewVar.get();
			ViewFactory<NP,EP,HG,WG,SG,N,E> viewFactory = hgView.getViewFactory();
			brView = viewFactory.makeBranchView(this) ;
            brView.setParentDecorator(viewFactory.makeDecorator(hgView.getDefaultParentDecorator(), hgView));
            brView.setChildDecorator(viewFactory.makeDecorator(hgView.getDefaultChildDecorator(), hgView));
            branchViewVar.set(brView);
		}
		return brView;
	}
	
	public void setBranch(BranchView<NP,EP,HG,WG,SG,N,E> branchView ){
		branchViewVar.set( branchView );
	}
	
	public void setPinned(boolean p){
		pinnedVar.set( p ) ;
	}
	
	/**
	 * Note that it will be assumed that extents includes the branches
	 * between generations but this is not formally computed (for reasons
	 * of efficiency). It is possible to construct pathological cases
	 * where a branch extends outside the extent of its parent as
	 * defined here.
	 * 
	 * 
	 * @return the smallest rectangle that encloses the bounding rectangles
	 *         of this node plus all its descendants plus all the
	 *         edges it governs
	 */
	
	public Rectangle2D getNextExtent(){
	    Rectangle2D newExtent = getNextBaseExtent();
		if (newExtent != null)
		    for (int i = 0; i < getNumChildren(); i++)
					Rectangle2D.union(newExtent, getChild(i).getNextExtent(), newExtent);
		return newExtent;
	}
	
	public Rectangle2D getExtent(){
		return extentVar.get();
	}
	
	@Override
	protected Shape getShape() {
		RectangularShape shape = shapeVar.get();
		if (shape == null) return null;
		return (Shape)shape.clone();
	}	

	
	/**
	 * Translates the node, all its children, its governed edges and
	 * its extent.
	 * 
	 * @param dx translation in the +x direction
	 * @param dy translation in the +y direction
	 */
	
	public void translateNextHierarchy(double dx, double dy){
		/* Private note. The lack of a do is not inconsistent here.
		 * Since transition is a noun and layout can be either a noun
		 * or a verb, the do is indicated. translate is unequivocally
		 * a verb and doTranslate would be an abomination! :))
		 */
		// Note, the existing extent is translated rather than recalculated
		for(int i = 0; i < getNumChildren(); i++) {
			getChild(i).translateNextHierarchy(dx, dy);
			getChild(i).getBranch().translateNext(dx, dy);
		}
		for(E edge : getNode().getGovernedEdges()) {
//			System.out.println("translating edge " + edge);
			EdgeView<NP,EP,HG,WG,SG,N,E> edgeView = getHigraphView().getEdgeView(edge);
			edgeView.translateNext(dx, dy);			
		}
			
//		Rectangle2D ne = getNextExtent();
/*		if (ne == null) 
			ne = getNextBaseExtent();*/
//		double newX = ne.getMinX()+dx;
//		double newY = ne.getMinY()+dy;
		/*Assert.check(newX >= 0.0 && newY >= 0.0,
				"trying to translate " + this + " to (" + newX + ", " + newY + ") which is outside the 1st quadrant");*/
//		ne.setFrame(newX, newY, ne.getWidth(), ne.getHeight());
//		nextExtentVar.set(ne);		
		translateNext(dx,dy);
	}
	
	/**
	 * This is always a convenience method and should not be over-ridden so it's meaning is consistent
	 * 
	 * @param x the new x location of the top left corner
	 * @param y the new y location of the top left corner
	 */
	final public void placeNextHierarchy(double x, double y){
		translateNextHierarchy(x - getNextX(), y - getNextY());
	}

	
	/**
	 * Standard translateNext routine translates only the nextShape of
	 * this node.
	 * @param dx translation in the +x direction
	 * @param dy translation in the +y direction
	 */
	@Override	
	public void translateNext(double dx, double dy) {
        RectangularShape newShape = (RectangularShape) nextShapeVar.get().clone() ;
        Rectangle2D r = newShape.getFrame(); 
        newShape.setFrame(r.getMinX() + dx, r.getMinY() + dy, r.getWidth(), r.getHeight()) ;
        nextShapeVar.set(newShape) ;     
		super.translateNext(dx, dy);
	}

    @Override
    public void moveLabel(Label<NP,EP,HG,WG,SG,N,E>label){
        Rectangle2D cr = getNextShapeExtent();
        Rectangle2D lr = label.getLabelMetrics();
        // Into x and y we compute the position for the centre of the label.
        double x  ;
        double y  ; 
        double halfW = lr.getWidth() / 2.0 ;
        double halfH = lr.getHeight() / 2.0 ;
        
        switch(label.getPlacement()){
        case Label.EAST: case Label.NORTHEAST: case Label.SOUTHEAST:
            x = cr.getMaxX() + halfW ;
        break;
        case Label.WEST: case Label.NORTHWEST: case Label.SOUTHWEST:
            x = cr.getMinX() - halfW ;
        break;
        default: x = cr.getCenterX() ;
        }
        switch(label.getPlacement()){
        case Label.NORTH: case Label.NORTHEAST: case Label.NORTHWEST :
            y = cr.getMinY() - halfH ;
        break;
        case Label.SOUTH: case Label.SOUTHEAST: case Label.SOUTHWEST:
            y = cr.getMaxY() + halfH;
        break;
        default:
            y = cr.getCenterY() ;
        }
        Point2D.Double p = label.getNudge();
        //	System.out.println("moved label " + getId() + " to (" + (x+p.x) + ", " + (y+p.y) + ")");
        x += p.x ; y += p.y ;
        // So much for the centre, but we want the upper left corner
        label.placeNext(x - halfW, y - halfH);
     }



	/**
	 * Move the top left corner of the node to (x, y)
	 * @param x new co-ordinate of top left corner on the x axis
	 * @param y new co-ordinate of top left corner on the y axis
	 

	public void moveTo(double x, double y){
		Assert.check(x >= 0. && y >= 0., "Can't move nodes out of the 1st quadrant");
		Rectangle2D ne = nextExtentVar.get();
		if (ne == null)
			translate(x, y);
		else
			translate(x-ne.getMinX(), y - ne.getMinY());
	}*/


	
	@Override
	public void doTransition() {
		if ( pinnedVar.get() ) return ;
		
		for(int i = 0; i < getNumChildren(); i++)
			getChild(i).doTransition();
		
		Assert.check( nextShapeVar.get() != null, "trying to update to a null nextShape" );
		
		shapeVar.set( (RectangularShape)(nextShapeVar.get().clone()) ) ;
		
		if (branchViewVar.get() !=null)
			branchViewVar.get().doTransition();
		
/*		for(E edge : myNodeVar.get().exitingEdges())
		    hgViewVar.get().getEdgeView(edge).doTransition();*/
		
		extentVar.set(getNextExtent()) ;
//		nextExtentVar.set(null); // nextExtent is no longer valid
		super.doTransition();
	}
		

	public void setDislocation(int dx, int dy){	
		dislocationVar.set( dx ==0 && dy == 0 ? null : new Point2D.Double(dx, dy));
	}
	
	public Point2D.Double getDislocation(){
		return dislocationVar.get();
	}
	
	/**
	 * Apply the dislocation specified to this node.
	 */
	
	public void dislocate(){
		Point2D.Double p = dislocationVar.get();
		if (p != null){
			translateNext(p.x, p.y);
			AbstractLayoutManager<NP,EP,HG,WG,SG,N,E> myLM = getLayoutManager();
			NodeView<NP, EP, HG, WG, SG, N, E> parent = getParent();
			if (parent != null)
				parent.getLayoutManager().layoutBranch(parent,this);
			for (int i = 0; i < getNumChildren(); i++){
				myLM.layoutBranch(this, getChild(i));
			}
			Collection<E> edges = this.getNode().getIncidentEdges();
			for(E edge : edges){
				HigraphView<NP, EP, HG, WG, SG, N, E> hgv = getHigraphView();
				if (hgv.getHigraph().contains(edge)){
					N gn = edge.getGoverningNode(hgv.getHigraph());
					AbstractLayoutManager<NP, EP, HG, WG, SG, N, E>
						lm = (gn == null) ? hgv.getLayoutManager() : hgv.getNodeView(gn).getLayoutManager();
					lm.layoutEdge(hgv,edge);	
				}
			}
		}
	}
	
	public void setPlacement(double dx, double dy){
		placementVar.set( dx ==0 && dy == 0 ? null : new Point2D.Double(dx, dy));
	}
	
	public Point2D.Double getPlacement(){
		return placementVar.get() ;
	}
	
	public void doLayout(){
		getLayoutManager().layoutLocal(this);
	}
	
	public void doBranchLayout(){
		getLayoutManager().layoutBranches(this);
	}
	
	public void doEdgeLayout(){
		getLayoutManager().layoutEdges(getHigraphView(), getNode().getGovernedEdges());
	}
		
	public AbstractLayoutManager<NP,EP,HG,WG,SG,N,E> getLayoutManager(){		
		if(layoutManagerVar.get() == null){
			NodeView<NP,EP,HG,WG,SG,N,E> p = getParent();
			return (p == null) ? getHigraphView().getLayoutManager() : p.getLayoutManager();
		}
		return layoutManagerVar.get();
	}

	public void setLayoutManager(AbstractLayoutManager<NP,EP,HG,WG,SG,N,E> manager){
		layoutManagerVar.set( manager ) ;
	}

	
	
	public N getNode() {
		return myNode ;
	}
	
	/**
	 * get - set methods for drawOutlineOnly  field
	 */
		
		public boolean getOutlineOnly(){
			return drawOutlineOnlyVar.get();
		}
		
		public void setOutlineOnly(boolean outlineOnly){
			drawOutlineOnlyVar.set(new Boolean(outlineOnly));
		}

	
	/*****************************************
	 *  implementation of LayableNode interface
	 *  */
	public boolean isPinned(){
		return pinnedVar.get();
	}
	
/*	public double getNextX(){
		return nextShapeVar.get().getMinX();
	}
	
	public double getNextY(){
		return nextShapeVar.get().getMinY();
	} 
	
	public double getNextCenterX(){
		return nextShapeVar.get().getCenterX();
	}
	
	public double getNextCenterY(){
		return nextShapeVar.get().getCenterY();
	} 
	
	public double getNextWidth(){
		return nextShapeVar.get().getWidth();
	}
	
	public double getNextHeight(){
		return nextShapeVar.get().getHeight();
	}*/
	
	
	
	public RectangularShape getNextShape() {
		return (RectangularShape) nextShapeVar.get().clone() ;
	}
	
	/** 
     * @param shape must be a RectangularShape
     */
	@Override
	public void setNextShape(Shape shape) {
        Assert.check( shape instanceof RectangularShape ) ;
        RectangularShape newShape = (RectangularShape) ((RectangularShape) shape).clone() ;
        // Should we move all labels and zones?  TSN 2011 July 4
        nextShapeVar.set( newShape ) ; 
	}

	public void setNodeShapeType(int s){
		RectangularShape oldShape = nextShapeVar.get();
		RectangularShape newShape = oldShape;
        double width = oldShape != null ? oldShape.getWidth() : HigraphView.DEFAULT_SHAPE_WIDTH;
        double height = oldShape != null ? oldShape.getHeight() : HigraphView.DEFAULT_SHAPE_HEIGHT;
        double x = oldShape != null ? oldShape.getX() : 0.0;
        double y = oldShape != null ? oldShape.getY() : 0.0; 
        
		switch (s){
		case ELLIPSE:
			newShape = new Ellipse2D.Double(x, y, width, height);
			break;
		case NodeView.RECTANGLE:
			newShape = new Rectangle2D.Double(x, y, width, height);
			break;
		case NodeView.ROUND_RECTANGLE:
			newShape = new RoundRectangle2D.Double(x, y, width, height,
					HigraphView.DEFAULT_ROUND_X, HigraphView.DEFAULT_ROUND_Y);
			break;
			default: Assert.scriptingError("Only shapes defined for nodes are ELLIPSE, RECTANGLE and ROUND_RECTANGLE.");
		}
        nextShapeVar.set(newShape) ;
		
	}
	
	public void setNodeSize(int w, int h){
		RectangularShape newShape = nextShapeVar.get();
		Assert.check(newShape != null, "Can't set size of a null shape");
		newShape.setFrame(newShape.getX(), newShape.getY(), w, h);
		nextShapeVar.set(newShape);
	}
	
	/* note that vertical incrementing regions are ABOVE and BELOW so numbering
	 * scheme ensures region <= BELOW is vertical incrementing 
	 */
	private final static int ABOVE = 0;
	private final static int RIGHT = 2;
	private final static int BELOW = 1;
	private final static int LEFT = 3;
	

	
	/**
	 * get the intersection point of a line with this nodeView. 
	 * 
	 * The routine works for nodes which use closed rectangular shapes (i.e presently any rectangular shape except
	 * arc.) It first finds the intersection of the edge with the node's bounding rectangle, then walks down the
	 * edge towards the node, pixel by pixel, until it hits the node's nextShape boundary
	 * @param inPt - a point inside the nextShape of the nodeView
	 * @param outPt - a point outside nextShape's bounding box
	 * @return the point of intersection of the line between inPt and outPt and the nextShape border
	 */


	public Point2D.Double getIntersection(Point2D.Double inPt, Point2D.Double outPt){
	    return Geometry.getIntersection( nextShapeVar.get(), inPt, outPt ) ;
	}

	public void selfEdge(GeneralPath p, double axisAngle, double spreadAngle, double extent){
		RectangularShape nodeShape = getNextShape();
		double h = nodeShape.getHeight();
		double w = nodeShape.getWidth();
		double mag = Math.sqrt(h*h + w*w); // a line from the center, mag long cannot be inside the node boundary
		Point2D.Double center = new Point2D.Double(nodeShape.getCenterX(), nodeShape.getCenterY());
		
		// Control Points, for an angle of 0 degrees
		Point2D.Double P1 = new Point2D.Double(center.x + mag * (1 + extent/3), center.y); 
		Point2D.Double P2 = new Point2D.Double(center.x + mag * (1 + extent/3), center.y );

		// rotate control points to the desired angles

		Geometry.rotate(P1, center, Math.PI*(axisAngle+spreadAngle/2.0)/180.);
		Geometry.rotate(P2, center, Math.PI*(axisAngle-spreadAngle/2.0)/180.);
		
		/* Project back to the centre to find intersections which are start and end pts
		 * Now starting tangents of self curve will be along spokes from centre to
		 * control points. Self curve will be inside the spokes
		 */
		Point2D.Double P0 = getIntersection(center, P1);
		Point2D.Double P3 = getIntersection(center, P2);
				
		p.moveTo(P0.x, P0.y);
		p.curveTo(P1.x, P1.y, P2.x, P2.y, P3.x,P3.y);
	}
	



	/***** Transition interface ****/
	
	
	
/*	public Transition disappearTransition(final int time) {
		Rectangle2D bounds = shape.getBounds2D() ;
		Rectangle2D finalBounds = new Rectangle2D.Double(
				bounds.getX(),
				bounds.getY(),
				0.0,
				0.0 ) ;
		return new NodeViewTransition( time, bounds, finalBounds ) ;
	}

	public Transition moveTransition(int time, NodeView nv1) {
		Rectangle2D bounds = shape.getBounds2D() ;
		Rectangle2D finalBounds = nv1.getFrame() ;
		return new NodeViewTransition( time, bounds, finalBounds ) ;
	}
	
	public NodeView makeInitialVersion() {
		Rectangle2D bounds = getFrame() ;
		Rectangle2D initialBounds = new Rectangle2D.Double(
				bounds.getX(), bounds.getY(), 0, 0 ) ;
		return new NodeView(node, color, initialBounds) ;
	}
	
	public Transition nestTranstion(int time, NodeView nv1){
		Rectangle2D bounds = nv1.getFrame();
		Rectangle2D finalBounds = makeInitialVersion() .getFrame() ;
		return new NodeViewTransition( time, bounds, finalBounds ) ;
      	   
		
		
	}
	private class NodeViewTransition implements Transition {
		double xVel ;
		double yVel ;
		double wVel ;
		double hVel ;
		int time ;

		public NodeViewTransition(int time, Rectangle2D bounds, Rectangle2D finalBounds) {
			this.time = time ;
			xVel = (finalBounds.getX()- bounds.getX()  ) / time ;
			yVel = (finalBounds.getY() - bounds.getY() ) / time ; 
			wVel = (finalBounds.getWidth() - bounds.getWidth() ) / time ;
			hVel = (finalBounds.getHeight() - bounds.getHeight() ) / time ;
		}

		public void advanceBy(int m) {
			Rectangle2D r = getFrame() ;
			double x = r.getX() + xVel* m  ;
			double y = r.getY() + yVel * m ;

			double w = r.getWidth() + wVel * m ;
			double h = r.getHeight() + hVel * m ;
			r = new Rectangle2D.Double( x, y, w, h ) ;
			setFrame( r )  ;
			time -= m ;
			
		}

		public boolean done() {
			return time <= 0 ;
		}
	}*/
	
	protected NodeView<NP,EP,HG,WG,SG,N,E> getThis(){return this;}
	
	@Override protected void drawSelf(Graphics2D screen){
		if(!getVisibility()) return;
		if (drawOutlineOnlyVar.get()) {
			if (colorVar.get() != null && strokeVar.get() != null) {
				screen.setColor( colorVar.get() );
				screen.setStroke( strokeVar.get() );
				screen.draw(getNextExtent());
			}
		} else super.drawSelf(screen);
       if (branchViewVar.get() != null) branchViewVar.get().draw(screen);
        
        /* In order for this to work, the final location of edges
         * must already be defined. This rules out using the
         * AffineTransformation in the Graphics2D context as edges
         * can in general cross contexts. ????
         *  
         */
        // Draw the descendants of this node
        for(int i = 0; i < getNumChildren(); i++ )
            getChild(i).draw(screen);
	}
	
	
   public String toString(){
    	return "nodeView " + hashCode() + (shapeVar.get() != null ? " @(" + shapeVar.get().getCenterX() + ", " + shapeVar.get().getCenterY() + ")" : "")
    				+ " of " + myNode + " " + myNode.hashCode();
    }
}
