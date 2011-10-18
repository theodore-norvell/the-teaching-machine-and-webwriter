/**
 * JavaCCRootNodeView.java
 * 
 * @date: Oct 12, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.view;

import higraph.view.HigraphView;

import java.awt.Graphics2D;

import tm.backtrack.BTTimeManager;
import visreed.model.VisreedEdge;
import visreed.model.VisreedEdgeLabel;
import visreed.model.VisreedHigraph;
import visreed.model.VisreedNode;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.model.payload.VisreedPayload;
import visreed.view.AlternationNodeView;

/**
 * @author Xiaoyu Guo
 *
 */
public class JavaCCRootNodeView extends AlternationNodeView {

	/**
	 * @param v
	 * @param node
	 * @param timeMan
	 */
	public JavaCCRootNodeView(
		HigraphView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> v,
		VisreedNode node, BTTimeManager timeMan) {
		super(v, node, timeMan);
	}

	@Override
	protected void drawNode(Graphics2D screen) {
	}
}
