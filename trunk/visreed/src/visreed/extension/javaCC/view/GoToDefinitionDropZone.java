/**
 * GoToDefinitionDropZone.java
 * 
 * @date: Oct 18, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.view;

import higraph.view.NodeView;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.SwingUtilities;

import tm.backtrack.BTTimeManager;
import visreed.extension.javaCC.model.JavaCCWholeGraph;
import visreed.extension.javaCC.model.payload.LexicalLinkPayload;
import visreed.model.VisreedEdge;
import visreed.model.VisreedEdgeLabel;
import visreed.model.VisreedHigraph;
import visreed.model.VisreedNode;
import visreed.model.VisreedPayload;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.swing.SwingHelper;
import visreed.view.IGraphContainer;
import visreed.view.VisreedDropZone;

/**
 * @author Xiaoyu Guo
 *
 */
public class GoToDefinitionDropZone extends VisreedDropZone {
	private static final String DROPZONE_IMAGE_URL = "/images/overlay_go_to_definition.png";
	private IGraphContainer gt;
	
	/**
	 * @param nv
	 * @param timeMan
	 */
	public GoToDefinitionDropZone(
		NodeView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> nv,
		BTTimeManager timeMan,
		IGraphContainer gt
	) {
		super(nv, timeMan);
		this.gt = gt;
	}
	
	/* (non-Javadoc)
	 * @see visreed.view.VisreedDropZone#drawSelf(java.awt.Graphics2D)
	 */
	@Override
	protected void drawSelf(Graphics2D screen){
		// show the icon
		Rectangle2D extent = this.getNextShapeExtent();
		screen.drawImage(
			SwingHelper.loadImage(DROPZONE_IMAGE_URL),
			(int)extent.getX(),
			(int)extent.getY(),
			(int)extent.getWidth(),
			(int)extent.getHeight(),
			null
		);
		
		// draw the border
		super.drawSelf(screen);
	}

	@Override
	public void handleClick(MouseEvent e){
		if(e.getButton() == MouseEvent.BUTTON1 && this.gt != null){
	        SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	    			try{
	    				VisreedNode node = getNodeView().getNode();
	    				JavaCCWholeGraph wg = (JavaCCWholeGraph) node.getWholeGraph();
	    				String productionName = ((LexicalLinkPayload)node.getPayload()).getDescription();
	    				// clear all selection since we are going to change a view
	    				wg.deSelectAll();
	    				
	    				gt.setSubgraph(wg.getProductionManager().getSubgraph(productionName));
	    			} catch (Exception ex){
	    				ex.printStackTrace();
	    			}
	            }
	        });
		}
	}
}
