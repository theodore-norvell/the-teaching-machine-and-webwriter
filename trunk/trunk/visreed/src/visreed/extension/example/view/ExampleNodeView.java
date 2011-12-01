/**
 * ExampleNodeView.java
 * 
 * @date: Nov 30, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.example.view;

import higraph.view.HigraphView;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;

import tm.backtrack.BTTimeManager;
import visreed.extension.example.tag.ExampleTag;
import visreed.model.VisreedEdge;
import visreed.model.VisreedEdgeLabel;
import visreed.model.VisreedHigraph;
import visreed.model.VisreedNode;
import visreed.model.VisreedPayload;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedTag;
import visreed.model.VisreedWholeGraph;
import visreed.swing.SwingHelper;
import visreed.view.VisreedNodeView;
import visreed.view.layout.BasicShapeLayoutManager;
import visreed.view.layout.LayoutParameter;
import visreed.view.layout.Offset;
import visreed.view.layout.VisreedNodeLayoutManager;

/**
 * @author Xiaoyu Guo
 *
 */
public class ExampleNodeView extends VisreedNodeView {

	/**
	 * @param v
	 * @param node
	 * @param timeMan
	 */
	public ExampleNodeView(
			HigraphView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> v,
			VisreedNode node, BTTimeManager timeMan) {
		super(v, node, timeMan);
		layoutParameter = new LayoutParameter();
		layoutParameter.setPadding(new Offset(64));
	}
	
    protected VisreedNodeLayoutManager getLayoutHelper(){
    	return BasicShapeLayoutManager.getInstance();
    };

	/* (non-Javadoc)
	 * @see visreed.view.VisreedNodeView#drawNode(java.awt.Graphics2D)
	 */
	@Override
	protected void drawNode(Graphics2D screen) {
		VisreedTag tag = this.getNode().getTag();
		String imageFileName = "";
		if(tag.equals(ExampleTag.APPLE)){
			imageFileName = "/images/node_apple.png";
		} else if (tag.equals(ExampleTag.STRAWBERRY)){
			imageFileName = "/images/node_strawberry.png";
		}
		
		if(imageFileName.length() > 0){
			try {
				Image img = SwingHelper.loadImage(imageFileName);
				
				Rectangle2D extent = this.getNextShapeExtent();
				
				screen.drawImage(img, (int)extent.getX(), (int)extent.getY(), null);
		
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
