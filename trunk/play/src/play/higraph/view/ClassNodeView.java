/**
 * IFNodeView.java - play.higraph.view - PLAY
 * 
 * Created on 2012-03-04 by Kai Zhu
 */
package play.higraph.view;

import higraph.view.HigraphView;
import higraph.view.ZoneView;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import play.executor.Environment;
import play.higraph.model.PLAYEdge;
import play.higraph.model.PLAYEdgeLabel;
import play.higraph.model.PLAYHigraph;
import play.higraph.model.PLAYNode;
import play.higraph.model.PLAYPayload;
import play.higraph.model.PLAYSubgraph;
import play.higraph.model.PLAYTag;
import play.higraph.model.PLAYWholeGraph;
import play.higraph.swing.PLAYHigraphJComponent;
import tm.backtrack.BTTimeManager;

/**
 * @author Kai Zhu
 * 
 */
public class ClassNodeView extends PLAYNodeView {

	private String s;
	private Environment e;
	private PLAYNode n;
	private PLAYSubgraph sg;
	private PLAYHigraphJComponent hj;
	
	/**
	 * @param v
	 * @param node
	 * @param timeMan
	 */
	protected ClassNodeView(
			HigraphView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> v,
			PLAYNode node, BTTimeManager timeMan) {
		super(v, node, timeMan);
		//super.setNodeSize(200, 200);
		super.setColor(new Color(50,0,255));
		super.setStroke(new BasicStroke(3));
		super.setFillColor(null);
		//System.out.println("classview");
	}

	/**
	 * @see higraph.view.NodeView#drawSelf(java.awt.Graphics2D)
	 */
	@Override
	protected void drawSelf(Graphics2D screen) {
		//System.out.println("drawself class");
		double x = super.getNextX();
		double y = super.getNextY();
		double width = super.getNextWidth();
		// double height = super.getNextHeight();
		int number = this.getNumChildren();
		//System.out.println("children = " + number);

		super.drawSelf(screen);
		
		screen.setColor(Color.BLUE);
		screen.setFont(new Font("Serif",Font.ITALIC,15));
		screen.drawString("Fields", (float) (x + 5), (float) (y + 15));

		for(int i = 0; i<number-1; i++){
			PLAYTag child1 = this.getChild(i).getNode().getTag();
			PLAYTag child2 = this.getChild(i+1).getNode().getTag();

			if((child1==PLAYTag.VARDECL)&&(child2 == PLAYTag.METHOD)){
				Rectangle2D rect1 = ((PLAYNodeView) this
						.getChild(i)).getNextExtent();
				Rectangle2D rect2 = ((PLAYNodeView) this
						.getChild(i+1)).getNextExtent();
				screen.setColor(Color.BLUE);
				screen.drawLine(
						(int) x,
						(int) (rect1.getMaxY() + (rect2
								.getMinY() - rect1.getMaxY()) / 10),
								(int) (x + width),
								(int) (rect1.getMaxY() + (rect2
										.getMinY() - rect1.getMaxY()) / 10));
				
				screen.setColor(Color.BLUE);
				screen.setFont(new Font("Serif",Font.ITALIC,15));
				screen.drawString("Methods", (float) (x + 5), (float) ((rect1.getMaxY() + (rect2
						.getMinY() - rect1.getMaxY()) / 10)+15));
			}
		}
	}

	/**
	 * @see higraph.view.ComponentView#moveZone(higraph.view.ZoneView)
	 */
	@Override
	public void moveZone(
			ZoneView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> zone) {
		PLAYNodeView nodeView = (PLAYNodeView) zone.getAssociatedComponent();
		int number = super.getNumChildren();
		double x = 0;
		double y = 0;
		double width = 0;
		double height = 0;
		int index = super.indexOfZones(zone);
		//System.out.println("index = "+index);

		if (number > 1) {
			PLAYNodeView leftChildNodeView = (PLAYNodeView) nodeView
					.getChild(index);
			PLAYNodeView rightChildNodeView = (PLAYNodeView) nodeView
					.getChild(index+1);
			x = leftChildNodeView.getNextShapeExtent().getMinX();
			y = leftChildNodeView.getNextShapeExtent().getMaxY();
			width = rightChildNodeView.getNextShapeExtent().getWidth();
			height = rightChildNodeView.getNextShapeExtent().getMinY() - y;

		}
		else {
			// no children
			x = nodeView.getNextShapeExtent().getMinX()
					+ nodeView.getNextShapeExtent().getWidth() / 10;
			y = nodeView.getNextShapeExtent().getMinY()
					+ nodeView.getNextShapeExtent().getHeight() / 10;
			width = nodeView.getNextShapeExtent().getWidth() * 4 / 5;
			height = nodeView.getNextShapeExtent().getHeight() * 4 / 5;
		}
		zone.setNextShape(new Rectangle2D.Double(x, y, width, height));
		zone.placeNext(x, y);
		super.moveZone(zone);
	}

	public int zonePosition(int index){
		return index+1;
	}
	
	public String execute(Environment env,PLAYNode node,PLAYSubgraph sgraph){
		e = env;
		s = null;
		sg = sgraph;
		n = node;
		
		//Graphics2D g = (Graphics2D)c.getGraphics();
		//g.drawLine(0, 10, 25, 10);
		
		
		System.out.println("class children = "+n.getNumberOfChildren());
		highlight(n);
				
		for(int i = 0;i < n.getNumberOfChildren();i++){
			System.out.println(n.getChild(i).getTag());
			PLAYTag tag = n.getChild(i).getTag();
			
			if(tag == PLAYTag.VARDECL){
				/*
				 * get the variables and their values
				 */
				
				n.getChild(i).getView().execute(e,n.getChild(i),sg);
			}
								
			else if(tag == PLAYTag.METHOD){
								
				PLAYNodeView mn = n.getChild(i).getView();
				s = mn.execute(e,n.getChild(i),sg);
				
			}
			else{
				System.out.println("error");
			}
				
		}
		return s;
	}
}
