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


public class SimpleTreeLayoutManager
    < NP extends Payload<NP>,
      EP extends Payload<EP>,
      HG extends Higraph<NP,EP,HG,WG,SG,N,E>,
      WG extends WholeGraph<NP,EP,HG,WG,SG,N,E>,
      SG extends Subgraph<NP,EP,HG,WG,SG,N,E>,
      N extends Node<NP,EP,HG,WG,SG,N,E>, 
      E extends Edge<NP,EP,HG,WG,SG,N,E>
    >
 extends AbstractLayoutManager<NP,EP,HG,WG,SG,N,E> {
	

	public SimpleTreeLayoutManager() {
	}
	
	@Override
	/**
	 * standard method to layout simple tree whose extent will have top
	 * left corner at (0,0)
	 */
	
	public void layoutLocal(HigraphView<NP,EP,HG,WG,SG,N,E> hgv){
		Iterator<NodeView<NP,EP,HG,WG,SG,N,E>> iterator = hgv.getTops();
		double x = 0.0;
		
		double gap = 30;
		while(iterator.hasNext()){
			NodeView<NP,EP,HG,WG,SG,N,E> top = iterator.next();
			top.doLayout();  // recursive layout using top's layoutManager
			top.translateNextHierarchy(x, 0.);
			x += top.getNextExtent().getWidth() + gap;  // shift default translation over for next top
		}
		layoutEdges( hgv, hgv.getHigraph().getGovernedEdges() );
		
		iterator = hgv.getTops();
		while(iterator.hasNext())
			doDislocations(iterator.next());
	}
	

	@Override
	public void layoutLocal(NodeView<NP,EP,HG,WG,SG,N,E> nv) {
		layoutNodes(nv);  // first layout all the nodes
		layoutBranches(nv);
		layoutEdges(nv);
	}
	

	/**
	 * Recursive routine to layout nodes. Proceeds bottom up, laying
	 * out descendants before parents. Updates extent of the
	 * layout as it goes. Only the NextShape of the componenttViews
	 * are affected.
	 * 
	 * @param n the nodeview to be laid out
	 * @param px the x and y co-ordinates where the
	 * @param py  nodeview should be moved, px >=0, py >= 0
	 * @return the width of layout
	 */
	
	private void layoutNodes(NodeView<NP,EP,HG,WG,SG,N,E> n){
		double shapeWidth = n.getNextShapeExtent().getWidth();  // width of the shape of a single node
		double myHeight = n.getNextHeight();
		int kids = n.getNumChildren();
				
		double localX = 0;
		double offset = shapeWidth/2.0;
		// put kids underneath stepping over from left
		for (int i = 0; i < kids; i++){
			NodeView<NP,EP,HG,WG,SG,N,E> kid = n.getChild(i);
			kid.doLayout();
			kid.placeNextHierarchy(localX, 2 * myHeight);
//			layoutNodes(kid, localX, py + 2 * myHeight);
			localX +=kid.getNextExtent().getWidth() + offset;
		}
		if (kids > 0) {
			double shapeCenterX = n.getChild(kids/2).getNextShapeExtent().getCenterX(); // center of middle kids shape
			if(kids%2 == 0) { // average of center of two middle kids
				shapeCenterX = (shapeCenterX + n.getChild(kids/2-1).getNextShapeExtent().getCenterX())/2.0; 
			}
			double leftToCenterX = n.getNextShapeExtent().getCenterX()-n.getNextX();
			n.placeNext(shapeCenterX - leftToCenterX, 0);
		} else
			n.placeNext(0, 0);
		Rectangle2D r = n.getNextExtent(); // Correct for any excursion out of the positive quadrant
		double dx = r.getMinX();
		double dy = r.getMinY();
		
		dx = (dx < 0) ? -dx : 0;
		dy = (dy < 0) ? -dy : 0;
				
		// Now apply any correction needed
		if (dx!=0 || dy !=0)
			n.translateNextHierarchy(dx, dy);
	
		n.setOutlineOnly(false);
	}
	
}
