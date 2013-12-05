/**
 * PLAYNodeView.java - play.higraph.view - PLAY
 * 
 * Created on Feb 14, 2012 by Kai Zhu
 */
package play.higraph.view;

import higraph.view.HigraphView;
import higraph.view.Label;
import higraph.view.NodeView;
import higraph.view.ZoneView;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import play.controller.Controller;
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
public class PLAYNodeView
extends
NodeView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> {

	protected PLAYLabel label;
	private String s;
	private Environment e;
	private PLAYNode n;
	private PLAYSubgraph sg;
	private PLAYHigraphJComponent hj;
	
	public PLAYNodeView(
			HigraphView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> v,
			PLAYNode node, BTTimeManager timeMan) {
		super(v, node, timeMan);
		super.setNodeSize(100, 30);
		super.setNodeShapeType(NodeView.ROUND_RECTANGLE);
		super.setFillColor(null);
		this.label = (PLAYLabel) v.getViewFactory().makeLabel(this, "", 0);
		//System.out.println(this.label);
		this.label.setFillColor(Color.YELLOW);
		this.label.setShow(false);
		this.addLabel(this.label);
		setLabel(node);
		//System.out.println("playnodeview");
	}

	/**
	 * @return the label
	 */
	public PLAYLabel getLabel() {
		return label;
	}
	
	public void setLabel(PLAYNode node) {
		if(isEditable(node.getTag())){
			this.label.setShow(true);
		}
	}

	/**
	 * Get the index of the zone to associated node view
	 * 
	 * @param object
	 * @return
	 */
	public int indexOfZones(Object object) {
		return zones.toList().indexOf(object);
	}
	
	/** get visual position of zones*/
	public int zonePosition(int index) {
		return 0;
	}

	/**
	 * Get the size of the zones in the node view
	 * 
	 * @return
	 */
	public int zonesSize() {
		return super.zones.size();
	}

	/**
	 * Remove all zones from the node view
	 */
	public void removeAllDropZones() {
		for (ZoneView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> zoneView : super.zones) {
			super.removeZone(zoneView);
		}
	}

	@Override
	public void moveLabel(
			Label<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> label) {
		Rectangle2D cr = getNextShapeExtent();
		Rectangle2D lr = label.getLabelMetrics();
		// Into x and y we compute the position for the centre of the label.
		double x;
		double y;
		double halfW = lr.getWidth() / 2.0;
		double halfH = lr.getHeight() / 2.0;

		switch (label.getPlacement()) {
		case Label.EAST:
		case Label.NORTHEAST:
		case Label.SOUTHEAST:
			x = cr.getMaxX() - halfW;
			break;
		case Label.WEST:
		case Label.NORTHWEST:
		case Label.SOUTHWEST:
			x = cr.getMinX() + halfW;
			break;
		default:
			x = cr.getCenterX();
		}
		switch (label.getPlacement()) {
		case Label.NORTH:
		case Label.NORTHEAST:
		case Label.NORTHWEST:
			y = cr.getMinY() + halfH;
			break;
		case Label.SOUTH:
		case Label.SOUTHEAST:
		case Label.SOUTHWEST:
			y = cr.getMaxY() - halfH;
			break;
		default:
			y = cr.getCenterY();
		}
		Point2D.Double p = label.getNudge();
		x += p.x;
		y += p.y;
		label.placeNext(x - halfW, y - halfH);
	}

	@Override
	protected PLAYNodeView getThis() {
		return this;
	}
	
	 //added by ravneet	
	public String execute(Environment env,PLAYNode node,PLAYSubgraph sgraph){
		System.out.println("inside play node view");
		e = env;
		s = null;
		sg = sgraph;
		n = node;
		
		highlight(n);
		
		String value1 = null;
		PLAYTag tag = n.getTag();
		
		if(tag.equals(PLAYTag.NUMBERLITERAL)||tag.equals(PLAYTag.STRINGLITERAL)){
				value1 = n.getPayload().getPayloadValue();
		}
		else if(tag.equals(PLAYTag.THISVAR)|| tag.equals(PLAYTag.LOCALVAR)
				|| tag.equals(PLAYTag.WORLDVAR) ){
			value1 = e.get(n.getPayload().getPayloadValue());
		}
		return value1;
	}
	
	protected boolean isEditable(PLAYTag pt){
		return pt.equals(PLAYTag.NUMBERLITERAL) || pt.equals(PLAYTag.STRINGLITERAL)
				|| pt.equals(PLAYTag.THISVAR) || pt.equals(PLAYTag.LOCALVAR)
				|| pt.equals(PLAYTag.WORLDVAR) || pt.equals(PLAYTag.NUMBERTYPE)
				|| pt.equals(PLAYTag.STRINGTYPE) || pt.equals(PLAYTag.BOOLEANTYPE);
	}
	
	public boolean isCompatible(PLAYNode n1,PLAYNode n2){
		if(n1.getTag().equals(PLAYTag.NUMBERTYPE)){
			if((n2.getTag().equals(PLAYTag.NUMBERLITERAL))||
					(n2.getTag().equals(PLAYTag.NUMBERTYPE))){
				return true;
			}
		}
		else if(n1.getTag().equals(PLAYTag.STRINGTYPE)){
				if((n2.getTag().equals(PLAYTag.STRINGLITERAL))||
					(n2.getTag().equals(PLAYTag.STRINGTYPE)))	{
					return true;
				}
		}
		return false;
	}
	
	public boolean isNumber(String s){
		char[] ch = new char[s.length()];
		ch = s.toCharArray();
		for(int i=0 ; i<ch.length;i++){
			if(!(Character.isDigit(ch[i]))){
				return false;
			}
		}
		return true;
	}
	
	public boolean isVariable(String s){
		char[] ch = new char[s.length()];
		ch = s.toCharArray();
		if(!(Character.isLetter(ch[0]))){
			return false;
		}
		for(int i=1 ; i<ch.length;i++){
			if(!(Character.isLetterOrDigit(ch[i]))){
				return false;
			}
		}
		
		return true;
	}
	
	public void highlight(PLAYNode n){
		this.n = n;
		try {
			Color c = n.getView().getFillColor();
			n.getView().setFillColor(Color.lightGray);
			Controller.getInstance().refreshView(n.getView());
			Thread.sleep(1500);
			n.getView().setFillColor(c);
			Controller.getInstance().refreshView(n.getView());
			
			//Controller.getInstance().
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
