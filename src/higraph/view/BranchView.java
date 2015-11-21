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

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;

import tm.backtrack.*;
import tm.utilities.Assert;

import higraph.model.interfaces.*;
/**
 * The view of a branch between a child 
 * {@link higraph.model.interfaces.Node node}
 * and its parent. Note that there is no equivalent entity in the model
 * so branchViews are always associated with the {@link NodeView} for the
 * child {@link higraph.model.interfaces.Node node}.
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

public class BranchView
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
    public static final String TYPE_STRING = "BranchView" ;
    
    // Invariant: The values of shapeVar and nextShapeVar are local to this object in the
    // sense that they are not shared with other objects.
    // Thus we always clone when setting or getting these.
    // The shapeVar and nextShapeVar may however have values that are the same object.
    private final BTVar<GeneralPath> shapeVar;
	private final BTVar<GeneralPath> nextShapeVar;
	private final BTVar<NodeView<NP,EP,HG,WG,SG,N,E>> nodeViewVar;
	private final BTVar<PointDecorator<NP,EP,HG,WG,SG,N,E>> parentDecoratorVar;
	private final BTVar<PointDecorator<NP,EP,HG,WG,SG,N,E>> childDecoratorVar;



	protected BranchView(NodeView<NP,EP,HG,WG,SG,N,E> nv, BTTimeManager timeMan){
		super(nv.getHigraphView(), timeMan);
		nodeViewVar = new BTVar<NodeView<NP,EP,HG,WG,SG,N,E>>(timeMan, nv);
		shapeVar = new BTVar<GeneralPath>( timeMan ) ; // initially null.
		nextShapeVar = new BTVar<GeneralPath>( timeMan, new GeneralPath());
		HigraphView<NP,EP,HG,WG,SG,N,E> hgv = nv.getHigraphView();
		colorVar.set(hgv.getDefaultBranchColor());
		strokeVar.set(hgv.getDefaultBranchStroke());
		PointDecorator<NP,EP,HG,WG,SG,N,E> pDec = hgv.getViewFactory().makeDecorator(hgv.getDefaultParentDecorator(), hgv);
		if (pDec != null)
			pDec.setOwner(this);
		PointDecorator<NP,EP,HG,WG,SG,N,E> cDec = hgv.getViewFactory().makeDecorator(hgv.getDefaultChildDecorator(), hgv);
		if (cDec != null)
			cDec.setOwner(this);
		parentDecoratorVar = new BTVar<PointDecorator<NP,EP,HG,WG,SG,N,E>>(timeMan, pDec);
		childDecoratorVar = new BTVar<PointDecorator<NP,EP,HG,WG,SG,N,E>>(timeMan, cDec);
	}
    
    @Override public String getViewType() {
        return TYPE_STRING ;
    }
	
	public PathIterator getIterator() {
		return (shapeVar.get() == null) ? null : shapeVar.get().getPathIterator(null);
	}

	@Override
	public GeneralPath getShape() {
		return (shapeVar.get() == null) ? null : (GeneralPath) shapeVar.get().clone();
	}

	@Override
	public void startTransition() {
		// TODO
	    PointDecorator<NP,EP,HG,WG,SG,N,E> parentDec = parentDecoratorVar.get();
	    PointDecorator<NP,EP,HG,WG,SG,N,E> childDec = childDecoratorVar.get();
	    if (parentDec != null)
	    	parentDec.startTransition();
	    if (childDec != null)
	    	childDec.startTransition();
		super.finishTransition();
	}
	
	@Override
	public void advanceTransition(double degree) {
		// TODO
	    PointDecorator<NP,EP,HG,WG,SG,N,E> parentDec = parentDecoratorVar.get();
	    PointDecorator<NP,EP,HG,WG,SG,N,E> childDec = childDecoratorVar.get();
	    if (parentDec != null)
	    	parentDec.advanceTransition( degree);
	    if (childDec != null)
	    	childDec.advanceTransition( degree );
		super.finishTransition();
	}
	
	@Override
	public void finishTransition() {
	    shapeVar.set( nextShapeVar.get() ) ;
	    PointDecorator<NP,EP,HG,WG,SG,N,E> parentDec = parentDecoratorVar.get();
	    PointDecorator<NP,EP,HG,WG,SG,N,E> childDec = childDecoratorVar.get();
	    if (parentDec != null)
	    	parentDec.finishTransition();
	    if (childDec != null)
	    	childDec.finishTransition();
		super.finishTransition();
	}
	
	protected BranchView<NP,EP,HG,WG,SG,N,E> getThis(){return this;}
	


	
	/**
	 * Implementation of Layable Interface
	 */

	public boolean isPinned() {
		return nodeViewVar.get().isPinned() && nodeViewVar.get().getParent().isPinned();
	}


	@Override
	public void setNextShape(Shape n) {
		GeneralPath p = (GeneralPath)n;
		PathIterator pi = p.getPathIterator(null);
		putDecorators(pi, parentDecoratorVar.get(), childDecoratorVar.get());
		nextShapeVar.set ( p );
	}
	
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
	}

	
	public GeneralPath getNextShape() {
		return (GeneralPath)nextShapeVar.get().clone();
	}

	@Override
	public void translateNext(double dx, double dy) {
	    GeneralPath newShape = (GeneralPath) nextShapeVar.get().clone();
		newShape.transform(AffineTransform.getTranslateInstance(dx,dy));
		nextShapeVar.set( newShape );
		PointDecorator<NP,EP,HG,WG,SG,N,E> parentDec = getParentDecorator();
		PointDecorator<NP,EP,HG,WG,SG,N,E> childDec = getChildDecorator();
		if (parentDec != null) parentDec.translateNext(dx, dy);
		if (childDec != null) childDec.translateNext(dx, dy);
		super.translateNext(dx, dy);
	}
	
	public void setParentDecorator(PointDecorator<NP,EP,HG,WG,SG,N,E> parentDec) {
		parentDecoratorVar.set(parentDec);
		if (parentDec != null) parentDec.setOwner(this);
	}
	

	public PointDecorator<NP,EP,HG,WG,SG,N,E> getParentDecorator(){
		return parentDecoratorVar.get();
	}
	
	public void setChildDecorator(PointDecorator<NP,EP,HG,WG,SG,N,E> childDec){
		if (childDec != null) childDec.setOwner(this);
		childDecoratorVar.set(childDec);
	}
	
	public PointDecorator<NP,EP,HG,WG,SG,N,E> getChildDecorator(){
		return childDecoratorVar.get();
	}
	

	
    public String toString(){
    	return "branchView of " + nodeViewVar +
    		(shapeVar.get() != null ? " with shape " + shapeVar.get() : "");
    }
    
}
