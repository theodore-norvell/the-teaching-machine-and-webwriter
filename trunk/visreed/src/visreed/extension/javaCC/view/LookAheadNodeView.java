/**
 * LookAheadNodeView.java
 * 
 * @date: Nov 2, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.view;

import higraph.view.HigraphView;

import java.awt.Color;
import java.awt.Graphics2D;

import tm.backtrack.BTTimeManager;
import visreed.awt.GraphicsHelper;
import visreed.model.VisreedEdge;
import visreed.model.VisreedEdgeLabel;
import visreed.model.VisreedHigraph;
import visreed.model.VisreedNode;
import visreed.model.VisreedPayload;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.view.SingleChildNodeView;
import visreed.view.layout.LayoutParameter;
import visreed.view.layout.Offset;

/**
 * @author Xiaoyu Guo
 *
 */
public class LookAheadNodeView extends SingleChildNodeView {
	private static final double VSPACE_TOP = 20;
	private static final Color FILL_COLOR = new Color(0, 0, 0);

	/**
	 * @param v
	 * @param node
	 * @param timeMan
	 */
	public LookAheadNodeView(
		HigraphView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> v,
		VisreedNode node, BTTimeManager timeMan
	) {
		super(v, node, timeMan);
		
		layoutParameter = new LayoutParameter();
		layoutParameter.setPadding(new Offset(VSPACE_TOP, 0, 0, 0));
		
		this.fillColorVar.set(FILL_COLOR);
		this.setEntryOffsetY(VSPACE_TOP / 2.0);
	}

	/* (non-Javadoc)
	 * @see visreed.view.VisreedNodeView#drawNode(java.awt.Graphics2D)
	 */
	@Override
	protected void drawNode(Graphics2D screen) {
		String text = "LOOKAHEAD";
		
		GraphicsHelper.paintStringOnTopLeft(
			screen, text, getNextX() + 1, getNextY() + 1
		);
	}
	
	
	
}
