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
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import tm.backtrack.*;
import tm.utilities.Assert;
import tm.utilities.Debug;

import higraph.model.interfaces.*;
import higraph.utilities.Geometry;
import higraph.utilities.Pair;

/**
 * The view of an {@link higraph.model.interfaces.Edge Edge}
 * between a pair of {@link higraph.model.interfaces.Node Nodes}.
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

public class EdgeView
    < NP extends Payload<NP>,
      EP extends Payload<EP>,
      HG extends Higraph<NP,EP,HG,WG,SG,N,E>,
      WG extends WholeGraph<NP,EP,HG,WG,SG,N,E>,
      SG extends Subgraph<NP,EP,HG,WG,SG,N,E>,
      N extends Node<NP,EP,HG,WG,SG,N,E>, 
      E extends Edge<NP,EP,HG,WG,SG,N,E>
    >
extends ComponentView<NP,EP,HG,WG,SG,N,E>
{
    public static final String TYPE_STRING = "EdgeView" ;
    
	private final E myEdge ;
	
	// Invariant: The values of shapeVar and nextShapeVar are local to this object in the
    // sense that they are not shared with other objects.
    // Thus we always clone when setting or getting these.
	// The shapeVar and nextShapeVar may however have values that are the same object.
	private final BTVar<GeneralPath> shapeVar;
	private final BTVar<GeneralPath> nextShapeVar;
	private final BTVar<PointDecorator<NP,EP,HG,WG,SG,N,E>> sourceDecoratorVar;
	private final BTVar<PointDecorator<NP,EP,HG,WG,SG,N,E>> targetDecoratorVar;

	protected EdgeView(HigraphView<NP,EP,HG,WG,SG,N,E> hgv, E edge, BTTimeManager timeMan){
	    super(hgv, timeMan);
        shapeVar = new BTVar<GeneralPath>( timeMan ) ; // initially null.
        nextShapeVar = new BTVar<GeneralPath>( timeMan, new GeneralPath()) ;
	    myEdge = edge ;
	    colorVar.set(hgv.getDefaultEdgeColor());
	    strokeVar.set(hgv.getDefaultEdgeStroke());
		PointDecorator<NP,EP,HG,WG,SG,N,E> sDec = hgv.getViewFactory().makeDecorator(hgv.getDefaultSourceDecorator(), hgv);
		if (sDec != null)
			sDec.setOwner(this);
		PointDecorator<NP,EP,HG,WG,SG,N,E> tDec = hgv.getViewFactory().makeDecorator(hgv.getDefaultTargetDecorator(), hgv);
		if (tDec != null)
			tDec.setOwner(this);
	    sourceDecoratorVar = new BTVar<PointDecorator<NP,EP,HG,WG,SG,N,E>>(timeMan, sDec);
	    targetDecoratorVar = new BTVar<PointDecorator<NP,EP,HG,WG,SG,N,E>>(timeMan, tDec);
	}
	
	/**
	 *  Virtual constructor.
	 *  
	 * @param v The subgraphView of which this is a part
	 * @param e The edge in the model which it represents
	 * @param c The color used to display the edge
	 * @param s The stroke used for the edge
	 * @param p The path used to define the edge in the view
	 * 
	 * @return A new EdgeView object
	 

	public EdgeView<NP,EP,WG,SG,N,E> createEdgeView(HigraphView<NP,EP,WG,SG,N,E> v, Edge<NP,EP,WG,SG,N,E> e, Color c, Stroke s, GeneralPath p){
        return new EdgeView<NP,EP,WG,SG,N,E>(v, e, c, s, p);
}*/
    
    @Override public String getViewType() {
        return TYPE_STRING ;
    }

	
	public PathIterator getIterator() {
		return (shapeVar.get() == null) ? null : shapeVar.get().getPathIterator(null);
	}
	

	public Edge<NP,EP,HG,WG,SG,N,E> getEdge(){
		return myEdge ;
	}


	@Override
	public GeneralPath getShape() {
		return (shapeVar.get() == null) ? null : (GeneralPath) shapeVar.get().clone();
	}
	

	@Override
	public void doTransition() {
	    shapeVar.set( nextShapeVar.get() ) ;
	    PointDecorator<NP,EP,HG,WG,SG,N,E> sourceDec = sourceDecoratorVar.get();
	    PointDecorator<NP,EP,HG,WG,SG,N,E> targetDec = targetDecoratorVar.get();
	    if (sourceDec != null)
	    	sourceDec.doTransition();
	    if (targetDec != null)
	    	targetDec.doTransition();
		super.doTransition();
	}
	

	
	/**
	 * Implementation of Layable Interface
	 */

	public boolean isPinned() {    
		return hgViewVar.get().getNodeView( myEdge.getSource() ).isPinned() &&
					hgViewVar.get().getNodeView( myEdge.getTarget() ).isPinned();
	}


	@Override
	public void setNextShape(Shape n) {
		GeneralPath p = (GeneralPath)n;
		PathIterator pi = p.getPathIterator(null);
		putDecorators(pi, sourceDecoratorVar.get(), targetDecoratorVar.get());
		nextShapeVar.set ( p );
		moveLabels() ;
	}
	
	/* Branches and edges are very similar, differing only in the relationship they
	 * model between nodes they go between. Thus this package level routine, which
	 * is quite complex, is available to views other than EdgeView.
	 * 
	 * The routine does not permit any generalPath. It only works for paths that are continuous and open.
	 * That is, they are assumed to start with a SEG_MOVETO, followed by 1 or more actual drawn
	 * segments (SEG_LINETO, SEG_QUADTO and SEG_CUBICTO).
	 * 
	 * HOW DO WE ACTUALLY SHARE THIS METHOD? There is are no instance variables used in the method,
	 * all arguments are passed in. Yet the compiler will not allow it to be static because of the
	 * parameterization of PointDecorator. At the moment the routines are doubled (Grrrrr!)
	 * 
	 * [Put parameters on the static method? TSN]
	 */
	
	void putDecorators(PathIterator pi, PointDecorator<NP,EP,HG,WG,SG,N,E> sourceDec, PointDecorator<NP,EP,HG,WG,SG,N,E> targetDec){
		double coords[] = new double[6];
		int segType = pi.currentSegment(coords);
		Assert.check(segType == PathIterator.SEG_MOVETO, "First segment in a generalPath should be a move");
		/**
		 * Defines the point (P0) where the edge starts which is where the source decorator (if any) should be
		 * placed
		 */
		double xp = coords[0];
		double yp = coords[1];
		pi.next();
		Assert.check(!pi.isDone(), "No second segment in the general Path");
		segType = pi.currentSegment(coords);
		Assert.check(segType == PathIterator.SEG_LINETO || segType == PathIterator.SEG_QUADTO || segType == PathIterator.SEG_CUBICTO,
				"Second segment must either be a straight line or a quad or cubic Bezier curve");
		/**
		 * P0, the source point, has already been defined. Coords[0] and [1] define the first (P1) of up to 3
		 * (P1, P2, & P3) Bezier pts. There are 3 cases, defined by the type of segment (P0 is the start of
		 * the segment for each case):
		 *    SEG_LINTO: P1 is the other end of the straight line.
		 *    SEG_QUADTO: P2 the end of the curve and P1 is the Bezier control point. segment starts from P0
		 *                   in the direction of P1 (which the curve normally doesn't pass through).
		 *    SEG_CUBICTO: P3 is the end of the curve, which starts from P0 in the direction of P1
		 *    
		 *    In each case, P1 defines the direction of the segment vis its starting point P0
		 */
		double xd = coords[0];
		double yd = coords[1];		
		if(sourceDec != null)
			placeDecorator(sourceDec, xp, yp, xd, yd);
		if (targetDec !=null) {
			/** Now it gets tricksy. The target point is the last point of the final segment (assuming
			 * edges may only be defined by LINETO, QUADTO and CUBICTO after the initial move).
			 * But we also need the second last point for the direction
			 */
			do {
				segType = pi.currentSegment(coords);
				switch (segType){
				case PathIterator.SEG_LINETO:
					xd = xp;
					yd = yp;
					xp = coords[0];
					yp = coords[1];
					break;
				case PathIterator.SEG_QUADTO:
					xd = coords[0];
					yd = coords[1];
					xp = coords[2];
					yp = coords[3];
					break;
				case PathIterator.SEG_CUBICTO:
					xd = coords[2];
					yd = coords[3];
					xp = coords[4];
					yp = coords[5];
					break;
				default:
					Assert.check("segment must either be a straight line or a quad or cubic Bezier curve");	
				}
				pi.next();
			} while(!pi.isDone());
			placeDecorator(targetDec,xp, yp, xd, yd);
		}		
	}
	
	private void placeDecorator(PointDecorator<NP,EP,HG,WG,SG,N,E> decorator, double xp, double yp, double xd, double yd){
		decorator.placeNext(xp, yp);
		decorator.angleNext(Math.atan2(yd-yp, xd-xp));
		decorator.setColor( this.getColor() ) ;
		decorator.setStroke( this.getStroke() ) ;
	}
	
	public GeneralPath getNextShape() {
		return (GeneralPath) nextShapeVar.get().clone();
	}

	@Override
	public void translateNext(double dx, double dy) {
	    GeneralPath path = (GeneralPath) nextShapeVar.get().clone() ;
		path.transform(AffineTransform.getTranslateInstance(dx,dy));
		nextShapeVar.set( path ) ;
		PointDecorator<NP,EP,HG,WG,SG,N,E> sourceDec = getSourceDecorator();
		PointDecorator<NP,EP,HG,WG,SG,N,E> targetDec = getTargetDecorator();
		if (sourceDec != null) sourceDec.translateNext(dx, dy);
		if (targetDec != null) targetDec.translateNext(dx, dy);
		super.translateNext(dx, dy);
	}
	
	@Override
	public void moveLabel(Label<NP,EP,HG,WG,SG,N,E>label){
	    //Debug dbg = Debug.getInstance() ;
	    Shape shape = getNextShape() ;
	    Assert.check(shape instanceof Path2D) ;
        Rectangle2D lr = label.getLabelMetrics();
        final double h = lr.getHeight() ;
        final double w = lr.getWidth() ;
        
        
        // Interpretation of the constants is this.
        // Suppose the path goes straight down.
        // Then the label positions are these
        //                  |
        //  STARB. STERN  STERN  PORT STERN
        //                  |
        //        STARB.  CENTER  PORT
        //                  |
        //    STARB. BOW   BOW  PORT BOW
        //                  V
        // If the path goes in any other direction then everything rotates, just like a boat.
        
        // The algorithm is as follows.
        //   Find a point on the edge p and let theta be the direction of the tangent line
        // to the edge at point p.
        //   Think of the label as being centred at (x,y). Initially (x,y) = p.
        //   If the position is to starboard (port), "lift" the label in a direction perpendicular
        // to the tangent until all corners of the label are to the right (left) of the tangent.
        //   If the position is a bow (stern) position, "shove" the label in a direction 
        // parallel to the tangent away from the the target (source) until all corners of the
        // label are on the correct side of a perpendicular to the edge drawn at p.
        //   "Nudge" the label according to its "nudge value".
        // 
        
        int placement = label.getPlacement() ;
        final Pair<Point2D,Double> pair ;
        switch(placement){
        default: 
        case Label.CENTER: case Label.PORT: case Label.STARBOARD: {
            pair = Geometry.pointAlongPath(shape, 0.5 ) ;
        } break;
        case Label.STERN:  case Label.PORTSTERN: case Label.STARBOARDSTERN: {
            pair = Geometry.pointAlongPath(shape, 0.1 ) ;
        } break;
        case Label.STARBOARDBOW: case Label.BOW: case Label.PORTBOW: {
            pair = Geometry.pointAlongPath(shape, 0.9 ) ;
        } break; }
        // p is a point along the edge.
        final Point2D p = pair.first  ;
        double x = p.getX() ;
        double y = p.getY() ;
        // theta is the angle at which the edge is heading at point p.
        final double theta = pair.second;
        
        //dbg.msg(Debug.DISPLAY, "center of label " + label.getId() + " starts at (" + p.getX() + ", " + p.getY() + ")");
        //dbg.msg(Debug.DISPLAY, "angle is " +  theta);
        //dbg.msg(Debug.DISPLAY, "height is " + lr.getHeight() + " width is " + lr.getWidth() );
        
        // For port and starboard, the lift is the amount that the box needs to be moved
        // in a direction of theta + 1/2 pi so that it does not intersect the tangent line.
        // For center, bow, and stern, the lift is 0, as we want the box to intersect the line.
        final double lift ;
        switch(placement){
        default: 
        case Label.CENTER: case Label.STERN: case Label.BOW: {
            lift = 0 ;
        } break;
        case Label.PORT: case Label.PORTSTERN: case Label.PORTBOW: {
            lift = - Geometry.raiseBoxToClearLine(h, w, -theta) ;
        } break;
        case Label.STARBOARD: case Label.STARBOARDSTERN: case Label.STARBOARDBOW:{
            lift = Geometry.raiseBoxToClearLine(h, w, -theta) ;
        } break; }
        //dbg.msg(Debug.DISPLAY, "lift is" + lift);
        // Now we lift the label in a direction of theta + 1/2 pi.
        x += lift * Math.cos( theta + 0.5 * Math.PI ) ;
        y += lift * Math.sin( theta + 0.5 * Math.PI ) ;
        
        // Next we shove the label away from the source or target node view.
        // The shove is in the direction of theta.
        final double shove ;
        switch(placement){
        default: 
        case Label.CENTER: case Label.PORT: case Label.STARBOARD: {
            shove = 0 ;
        } break;
        case Label.STERN:  case Label.PORTSTERN: case Label.STARBOARDSTERN: {
            shove = Geometry.raiseBoxToClearLine(h, w, theta + 0.5 * Math.PI );
        } break;
        case Label.STARBOARDBOW: case Label.BOW: case Label.PORTBOW: {
            shove = - Geometry.raiseBoxToClearLine(h, w, theta + 0.5 * Math.PI );
        } break; }
        x += shove * Math.cos(theta) ;
        y += shove * Math.sin(theta) ;
        
        Point2D.Double nudge = label.getNudge();
        x += nudge.x ;
        y += nudge.y ;
        
        // dbg.msg(Debug.DISPLAY, "center of label " + label.getId() + " at (" + x + ", " + y + ")");
        
        // Now (x,y) is where the centre should be,
        // But we need (x,y) to represent the upper left corner 
        x -= w / 2.0 ;
        y -= h / 2.0 ; 
        //dbg.msg(Debug.DISPLAY, "moved label " + label.getId() + " to (" + x + ", " + y + ")");
        label.placeNext(x, y);
	}
	
	
	public void setSourceDecorator(PointDecorator<NP,EP,HG,WG,SG,N,E> sourceDec){
		sourceDec.setOwner(this);
		sourceDecoratorVar.set(sourceDec);
	}
	

	public PointDecorator<NP,EP,HG,WG,SG,N,E> getSourceDecorator(){
		return sourceDecoratorVar.get();
	}
	
	public void setTargetDecorator(PointDecorator<NP,EP,HG,WG,SG,N,E> targetDec){
		targetDec.setOwner(this);
		targetDecoratorVar.set(targetDec);
	}
	
	public PointDecorator<NP,EP,HG,WG,SG,N,E> getTargetDecorator(){
		return targetDecoratorVar.get();
	}
	
	
	protected EdgeView<NP,EP,HG,WG,SG,N,E> getThis(){return this;}
	
	
	public String toString(){
		return "edgeView from " + myEdge.getSource() + " to " + myEdge.getTarget();
	}
	
	
}
