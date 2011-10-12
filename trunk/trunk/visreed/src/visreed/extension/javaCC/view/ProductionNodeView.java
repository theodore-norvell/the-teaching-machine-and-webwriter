/**
 * ProductionNodeView.java
 * 
 * @date: Aug 29, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.view;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import higraph.view.HigraphView;
import tm.backtrack.BTTimeManager;
import visreed.awt.GraphicsHelper;
import visreed.extension.javaCC.model.payload.ProductionPayload;
import visreed.extension.javaCC.view.layout.ProductionLayoutManager;
import visreed.model.VisreedEdge;
import visreed.model.VisreedEdgeLabel;
import visreed.model.VisreedHigraph;
import visreed.model.VisreedNode;
import visreed.model.VisreedPayload;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.view.VisreedNodeView;
import visreed.view.layout.VisreedNodeLayoutManager;

/**
 * @author Xiaoyu Guo
 *
 */
public class ProductionNodeView extends VisreedNodeView {

    /**
     * @param v
     * @param node
     * @param timeMan
     */
    public ProductionNodeView(
            HigraphView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> v,
            VisreedNode node,
            BTTimeManager timeMan) {
        super(v, node, timeMan);
    }
    
    private boolean folded;

    /* (non-Javadoc)
     * @see visreed.view.VisreedNodeView#getLayoutHelper()
     */
    @Override
    protected VisreedNodeLayoutManager getLayoutHelper() {
    	return ProductionLayoutManager.getInstance();
    }

	public boolean isFolded() {
		return folded;
	}

	public void setFolded(boolean folded) {
		if(folded != this.folded){
			this.folded = folded;
		}
	}
	
	/* (non-Javadoc)
	 * @see visreed.view.VisreedNodeView#drawNode(java.awt.Graphics2D)
	 */
	@Override
	protected void drawNode(Graphics2D screen) {
		if(this.getColor() != null){
			screen.setColor(this.getColor());
		}
		
		// name of the production
		String nodeName = ((ProductionPayload)this.getNode().getPayload()).getName();
		Rectangle2D extent = this.getNextShapeExtent();
		GraphicsHelper.paintStringOnTopLeft(
			screen, 
			nodeName, 
			extent.getX() + 2, 
			extent.getY() + 1
		);
		
		// dashed border
	}
}
