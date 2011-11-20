/**
 * RegexpSpecNodeView.java
 * 
 * @date: Oct 23, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.view;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import higraph.view.HigraphView;
import tm.backtrack.BTTimeManager;
import visreed.awt.GraphicsHelper;
import visreed.extension.javaCC.model.payload.RegexpSpecPayload;
import visreed.extension.javaCC.view.layout.RegexpSpecLayoutManager;
import visreed.model.VisreedEdge;
import visreed.model.VisreedEdgeLabel;
import visreed.model.VisreedHigraph;
import visreed.model.VisreedNode;
import visreed.model.VisreedPayload;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.view.layout.VisreedNodeLayoutManager;

/**
 * @author Xiaoyu Guo
 *
 */
public class RegexpSpecNodeView extends ProductionNodeView {

	/**
	 * @param v
	 * @param node
	 * @param timeMan
	 */
	public RegexpSpecNodeView(
			HigraphView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> v,
			VisreedNode node, BTTimeManager timeMan) {
		super(v, node, timeMan);
	}
	
    /* (non-Javadoc)
     * @see visreed.view.VisreedNodeView#getLayoutHelper()
     */
    @Override
    protected VisreedNodeLayoutManager getLayoutHelper() {
        return RegexpSpecLayoutManager.getInstance();
    }
    
    @Override
    protected void drawString(Graphics2D screen){
		if(this.getColor() != null){
			screen.setColor(this.getColor());
		}
		
		// name of the production
		String nodeName = ((RegexpSpecPayload)this.getNode().getPayload()).getName();
		Rectangle2D extent = this.getNextShapeExtent();
		GraphicsHelper.paintStringOnTopLeft(
			screen, 
			nodeName, 
			extent.getX() + 2, 
			extent.getY() + 1
		);
    }
}
