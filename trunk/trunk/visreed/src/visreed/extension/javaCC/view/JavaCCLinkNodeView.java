/**
 * JavaCCLinkNodeView.java
 * 
 * @date: Oct 12, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.view;

import java.awt.Color;
import java.awt.event.MouseEvent;

import higraph.view.HigraphView;
import tm.backtrack.BTTimeManager;
import visreed.model.VisreedEdge;
import visreed.model.VisreedEdgeLabel;
import visreed.model.VisreedHigraph;
import visreed.model.VisreedNode;
import visreed.model.VisreedPayload;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.view.TerminalNodeView;

/**
 * @author Xiaoyu Guo
 */
public class JavaCCLinkNodeView extends TerminalNodeView {
    protected static Color FILL_COLOR = Color.green;

	/**
	 * @param v
	 * @param node
	 * @param timeMan
	 */
	public JavaCCLinkNodeView(
			HigraphView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> v,
			VisreedNode node, BTTimeManager timeMan) {
		super(v, node, timeMan);
	}

	/* (non-Javadoc)
	 * @see visreed.view.TerminalNodeView#shouldRefreshDropZone()
	 */
	@Override
    protected boolean shouldRefreshDropZone(){
    	return false;
    }
    
    /* (non-Javadoc)
     * @see visreed.view.VisreedNodeView#reCreateDropZone()
     */
    public void reCreateDropZone(){
    }
    
    /* (non-Javadoc)
     * @see visreed.view.VisreedNodeView#handleDoubleClick(java.awt.event.MouseEvent)
     */
    @Override
    public void handleDoubleClick(MouseEvent e) {
    	// TODO change the subgraph
    }
}
