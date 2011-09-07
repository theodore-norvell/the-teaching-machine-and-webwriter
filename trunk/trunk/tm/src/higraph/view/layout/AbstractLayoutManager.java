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

package higraph.view.layout;


import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;
import java.util.Collection;

import higraph.model.interfaces.*;
import higraph.view.BranchView;
import higraph.view.EdgeView;
import higraph.view.HigraphView;
import higraph.view.NodeView;

/** A common base class for layout managers.
 * 
 * <p>Layout managers are exclusively concerned with computing the 
 * {@link java.awt.Shape NextShape} of {@link higraph.view.ComponentView
 * ComponentView} objects. Often this is simply a new position
 * but could also be a new size or even a new layout (for example
 * in the cases of {@link higraph.view.EdgeView
 * Edges} that may have to route around intervening {@link higraph.view.NodeView
 * Nodes}.</p>
 * 
 * <p>Information about current shapes are not taken from
 * {@link higraph.view.ComponentView ComponentView}
 * Shape objects, which may not even exist when a Layout is called for.
 * When a layout is complete it is put in effect by calling a
 * TransitionManager to handle the transition from the old graph state
 * to the new one. A post condition of the TransitionManager is that
 * the {@link java.awt.Shape Shape} of any affected
 * {@link higraph.view.ComponentView ComponentView}
 * is equal to its {@link java.awt.Shape NextShape}.
 * Thus if a {@link #layoutLocal()} is called after a doTranslation, NextShape
 * objects will carry all information about the current layout.</p>
 * 
 * <p>Because layouts are often local (that is just for a part of a Subgraph)
 * and multiple such layouts may have to be incorporated into a single
 * composite layout, provision is also made to translate a local layout.</p>
 *  
 * 
 * @author mpbl
 *
 * @param <NP> Node Payload
 * @param <EP> Edge Payload
 * @param <WG> Whole Graph
 * @param <SG> Subgraph
 * @param <N>  Node
 * @param <E>  Edge
 */
public abstract class AbstractLayoutManager
    < NP extends Payload<NP>,
      EP extends Payload<EP>,
      HG extends Higraph<NP,EP,HG,WG,SG,N,E>,
      WG extends WholeGraph<NP,EP,HG,WG,SG,N,E>,
      SG extends Subgraph<NP,EP,HG,WG,SG,N,E>,
      N extends Node<NP,EP,HG,WG,SG,N,E>, 
      E extends Edge<NP,EP,HG,WG,SG,N,E>
    > {
		
	public AbstractLayoutManager(){
	}
	

	
	
	/**
	 * Do a local layout.
	 * Results in an extent with top left corner at (0,0)
   */	 
	public abstract void layoutLocal(HigraphView<NP,EP,HG,WG,SG,N,E> view);
	public abstract void layoutLocal(NodeView<NP,EP,HG,WG,SG,N,E> view);
	
	/**
	 * Recursive routine for dislocating nodes. Designed to be called after all
	 * normal layout has been done.
	 * Dislocations move dislocate nodes outside the normal layout flow
	 * @param nv
	 */
	
	public void doDislocations(NodeView<NP,EP,HG,WG,SG,N,E> nv){
		int kids = nv.getNumChildren();
		for (int i = 0; i < kids; i++)
			doDislocations(nv.getChild(i));
		nv.dislocate();
	}
	

	
    /**
     * Layout all governed edges
     */
	
	public void layoutEdges(NodeView<NP,EP,HG,WG,SG,N,E> nv){
		layoutEdges(nv.getHigraphView(), nv.getNode().getGovernedEdges());
	}
	
    public void layoutEdges(HigraphView<NP,EP,HG,WG,SG,N,E> hgv, Collection<E> edges){
        for (E edge : edges)
//          System.out.println("governed edge " + edge );
        	layoutEdge(hgv, edge);
     }
    
    public void layoutEdge(HigraphView<NP,EP,HG,WG,SG,N,E> hgv, E edge){
        EdgeView<NP,EP,HG,WG,SG,N,E> edgeView = hgv.getEdgeView(edge);
        GeneralPath p = edgeView.getNextShape();
        p.reset();
        NodeView<NP,EP,HG,WG,SG,N,E> targetView = hgv.getNodeView( edge.getTarget() );
        NodeView<NP,EP,HG,WG,SG,N,E> sourceView = hgv.getNodeView( edge.getSource() );
        if (targetView != sourceView){
        	boolean doubled = false;
        	/* Moderately inefficient in that the test has to be run both ways. While
        	 * both doubled edges could be laid out at once, the edge would have to be
        	 * marked as laid out and the markings for all edges would have to be
        	 * updated before any edges were laid out.
        	 */
        	for(E outEdge : targetView.getNode().exitingEdges())
        		if(outEdge.getTarget().equals(sourceView.getNode()))
        			doubled = true;
	        if (doubled)
	        	setCurvedPath(p, sourceView, targetView);
	        else
	        	setStraightPath(p, sourceView, targetView);
        }
        else
            sourceView.selfEdge(p, -45., 25, 1.2); 
        edgeView.setNextShape(p);
    }
    
    public void layoutBranches(NodeView<NP,EP,HG,WG,SG,N,E> n){
		int kids = n.getNumChildren();
		for (int i = 0; i < kids; i++) {
			NodeView<NP,EP,HG,WG,SG,N,E> kid = n.getChild(i);
			kid.doBranchLayout();  // recursively handle lower levels
			layoutBranch(n,kid);
		}	
	}

    
    
    /**
     * Layout a single branch. Note that although
     * BranchViews exist there are no corresponding Branches
     * in the model.
     * This routine assumes the parent and kid views are correctly
     * laid out already
     */
    
    public void layoutBranch(NodeView<NP,EP,HG,WG,SG,N,E> kidView){
    	layoutBranch(kidView.getParent(), kidView);
    }

    public void layoutBranch(NodeView<NP,EP,HG,WG,SG,N,E> parentView, NodeView<NP,EP,HG,WG,SG,N,E> kidView){
        BranchView<NP,EP,HG,WG,SG,N,E>  bv = kidView.getBranch();
        if(bv.getVisibility()){
	        GeneralPath p = bv.getNextShape();
	        p.reset();
	        setStraightPath(p, parentView, kidView);
	        bv.setNextShape(p);
        }
    }
    
    /* Set path (branch or edge) between two different nodes */
    
    void setStraightPath(GeneralPath p, NodeView<NP,EP,HG,WG,SG,N,E> sView, NodeView<NP,EP,HG,WG,SG,N,E> tView){
       RectangularShape srcShape = sView.getNextShape();
       RectangularShape trgShape = tView.getNextShape();
       Point2D.Double srcPt = new Point2D.Double(srcShape.getCenterX(), srcShape.getCenterY());
       Point2D.Double trgPt = new Point2D.Double(trgShape.getCenterX(), trgShape.getCenterY());
       Point2D.Double intersection;
       intersection = sView.getIntersection(srcPt, trgPt);
       p.moveTo(intersection.x, intersection.y);
       intersection = tView.getIntersection(trgPt, intersection);
       p.lineTo(intersection.x, intersection.y);   
   }
    
    void setCurvedPath(GeneralPath p, NodeView<NP,EP,HG,WG,SG,N,E> sView, NodeView<NP,EP,HG,WG,SG,N,E> tView){
//    	System.out.println("SetCurvedPath");
        RectangularShape srcShape = sView.getNextShape();
        RectangularShape trgShape = tView.getNextShape();
        Point2D.Double srcPt = new Point2D.Double(srcShape.getCenterX(), srcShape.getCenterY());
        Point2D.Double trgPt = new Point2D.Double(trgShape.getCenterX(), trgShape.getCenterY());
        Double d = Math.hypot(trgPt.x - srcPt.x, trgPt.y - srcPt.y);
        double angle = Math.atan2(trgPt.y - srcPt.y, trgPt.x - srcPt.x);
        /* Normalized control point is d/2 to the right of srcPt with a 
         * heuristic offset in y of d/8 to control curvature. It is then rotated
         * to the correct angle
         */
        Point2D.Double cntrlPt = new Point2D.Double( srcPt.x + d/2.0, srcPt.y+d/8.0);
        rotate(cntrlPt, srcPt, angle);
        Point2D.Double intersection;
        intersection = sView.getIntersection(srcPt, cntrlPt);
        p.moveTo(intersection.x, intersection.y);
        intersection = tView.getIntersection(trgPt, cntrlPt);
        p.quadTo(cntrlPt.x, cntrlPt.y, intersection.x, intersection.y);   
    }
    
	/* convenience method for rotating a single point through theta degrees. Positive theta is clockwise
	 * since positive y is downwards in computer graphics.
	 */
	public static void rotate(Point2D.Double p1, Point2D.Double pc, double theta){
		rotate (p1, pc, Math.cos(theta), Math.sin(theta));
	}
	
	/* Use this method with precomputed trig values to rotate multiple points through the same angle */
	public static void rotate(Point2D.Double p1, Point2D.Double pc, double cosTheta, double sinTheta){
		p1.x -= pc.x;
		p1.y -= pc.y;
		double newX = p1.x * cosTheta - p1.y * sinTheta;
		p1.y = p1.y * cosTheta + p1.x * sinTheta;
		p1.x = newX + pc.x;
		p1.y += pc.y;		
	}

}
