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


import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import higraph.model.interfaces.*;
import higraph.view.NodeView;
import higraph.view.HigraphView;

/** <p>An {@link SgLayoutManager} to layout a simple tree consisting of a single
 * {@link higraph.model.interfaces.Node Node} and all its descendants.</p>
 * <p><ol>
 * <li> Each generation is laid out uniformly on a single level.</li>
 * <li> Children of a common parent are spaced uniformly.</li>
 * <li> {@link higraph.view.DropZone DropZones}
 * are placed in front of each child.</li>
 * <li> Their common parent is centred one level above them.</li>
 * <li> Branches are displayed.</li>
 * <li>Special layout managers attached to any
 * {@link higraph.model.interfaces.Node nodes}
 * within the display are ignored.</li></ol></p>
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
public class NestedTreeLayoutManager
    < NP extends Payload<NP>,
      EP extends Payload<EP>,
      HG extends Higraph<NP,EP,HG,WG,SG,N,E>,
      WG extends WholeGraph<NP,EP,HG,WG,SG,N,E>,
      SG extends Subgraph<NP,EP,HG,WG,SG,N,E>,
      N extends Node<NP,EP,HG,WG,SG,N,E>, 
      E extends Edge<NP,EP,HG,WG,SG,N,E>
    >
 extends AbstractLayoutManager<NP,EP,HG,WG,SG,N,E> {
	
    public static enum Axis { X, Y } ;
    
    private static final int GAP = 0;
    
    protected final int xCoef, yCoef ;
    
	public NestedTreeLayoutManager() {
        this(Axis.X);
    }
	
	public NestedTreeLayoutManager(Axis axis) {
        super();
        if( axis == Axis.X ) { xCoef = 1; yCoef = 0 ; } else { xCoef = 0 ; yCoef = 1 ; }
    }
	
	public boolean equals(Object obj){
		return(obj instanceof AbstractLayoutManager);
	}
	


	@Override
	/**
	 * standard method to layout simple tree whose extent will have top
	 * left corner at (0,0)
	 */
	public void layoutLocal(HigraphView<NP,EP,HG,WG,SG,N,E> hgView) {
		Iterator<NodeView<NP,EP,HG,WG,SG,N,E>> iterator = hgView.getTops();
		double p = 0.0 ;
		while(iterator.hasNext()){
			NodeView<NP,EP,HG,WG,SG,N,E> top = iterator.next();
			top.doLayout();
			Rectangle2D r = top.getNextExtent();
			double w = r.getWidth();
			double h = r.getHeight() ;
			top.translateNextHierarchy(p*xCoef, p*yCoef);
			p += w*xCoef + h*yCoef + GAP;
			doDislocations(top);
		}	
		//layoutEdges( hgView, hgView.getHigraph().getGovernedEdges() ) ;
	}
	
	@Override
	public void layoutLocal(NodeView<NP,EP,HG,WG,SG,N,E> nv){
		layoutNodes(nv);  // first layout all the nodes
        //layoutEdges( nv.getHigraphView(), nv.getNode().getGovernedEdges() ) ;
	}
	
	/**
	 * Routine to layout nodes. Proceeds bottom up, laying
	 * out descendants before parents. Updates extent of the
	 * layout as it goes. Only the NextShape of the componenttViews
	 * are affected.
	 * 
	 * @param n the nodeview to be layed out
	 * @param px the x and y co-ordinates where the
	 * @param py  nodeview should be moved, px >=0, py >= 0
	 * @return the width of layout
	 */
	private void layoutNodes(NodeView<NP,EP,HG,WG,SG,N,E> n){
		
		final double OFFSET_X = 5;;
		final double OFFSET_Y = 5;
		
		int kids = n.getNumChildren();
		
		
		Rectangle2D r = new Rectangle2D.Double(0, 0, 30, 30);
		
		
		if (kids > 0) {
			double localX = OFFSET_X*yCoef;
			double localY = OFFSET_Y*xCoef;
			
			// put kids inside stepping over from left
			for (int i = 0; i < kids; i++){
				localX += OFFSET_X*xCoef;
				localY += OFFSET_Y*yCoef;
				NodeView<NP,EP,HG,WG,SG,N,E> kid = n.getChild(i);
				kid.doLayout();
				kid.placeNextHierarchy(localX, localY);
				Rectangle2D kidExtent = kid.getNextExtent();
				localX += xCoef*kidExtent.getWidth();
				localY += yCoef*kidExtent.getHeight() ;
				Rectangle2D.union(r, kidExtent, r);
				kid.getBranch().setVisibility(false);
			}
			// expand myExtent past extent of last child
			r.add(new Point2D.Double(r.getMaxX() + OFFSET_X, r.getMaxY() + OFFSET_Y));
			n.setNextShape(r);
			n.setFillColor(null); // Huh?  TSN
			n.translateNextHierarchy(0,0);
		}
		else n.placeNext(0, 0);
	}

}
