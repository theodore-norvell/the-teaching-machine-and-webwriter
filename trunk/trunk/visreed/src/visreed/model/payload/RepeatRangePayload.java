/**
 * RepeatRangePayload.java
 * 
 * @date: Oct 3, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.model.payload;

import higraph.view.HigraphView;
import tm.backtrack.BTTimeManager;
import tm.utilities.Assert;
import visreed.model.VisreedEdge;
import visreed.model.VisreedEdgeLabel;
import visreed.model.VisreedHigraph;
import visreed.model.VisreedNode;
import visreed.model.VisreedPayload;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.model.tag.VisreedTag;
import visreed.view.RepeatRangeNodeView;
import visreed.view.VisreedNodeView;

/**
 * RepeatRangePayload describes a set of repeating patterns
 * Example: a{3,5}
 * @author Xiaoyu Guo
 */
public class RepeatRangePayload extends VisreedPayload {

	public RepeatRangePayload() {
		super(VisreedTag.REPEAT_RANGE);
	}
	
	public RepeatRangePayload(int value) {
		super(VisreedTag.REPEAT_RANGE);
		this.minValue = value;
		this.hasMaxValue = false;
	}
	
	public RepeatRangePayload(int minValue, int maxValue) {
		super(VisreedTag.REPEAT_RANGE);
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.hasMaxValue = true;
	}
	
	private int minValue;
	private int maxValue;
	private boolean hasMaxValue;

	/* (non-Javadoc)
	 * @see visreed.model.VisreedPayload#format(visreed.model.VisreedNode)
	 */
	@Override
	public String format(VisreedNode currentNode) {
		StringBuffer result = new StringBuffer();
		int numOfChildren = currentNode.getNumberOfChildren();
		// RepeatRange -> Sequence
		Assert.check(numOfChildren == 1);
			
		VisreedNode currentChildN = currentNode.getChild(0);
		VisreedPayload currentChildPl = currentChildN.getPayload();
		
		result.append(currentChildPl.format(currentChildN));
		
		result.append(this.getDescription());
		return result.toString();
	}
	
	public String getDescription(){
		StringBuffer result = new StringBuffer();

        if(result.length() > 1){
            result.insert(0, "(");
            result.append(")");
        }
		
        result.append("{");
        result.append(this.minValue);
        
		if(this.hasMaxValue){
			result.append("," + this.maxValue);
		}
		result.append("}");
		return result.toString();
	}

	/* (non-Javadoc)
	 * @see visreed.model.VisreedPayload#constructView(higraph.view.HigraphView, visreed.model.VisreedNode, tm.backtrack.BTTimeManager)
	 */
	@Override
	public VisreedNodeView constructView(
			HigraphView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> sgv,
			VisreedNode node, BTTimeManager timeman) {
		return new RepeatRangeNodeView(sgv, node, timeman);
	}

	public int getMinValue(){
		return this.minValue;
	}
	
	public int getMaxValue(){
		return this.maxValue;
	}
	
	public boolean hasMaxValue(){
		return this.hasMaxValue;
	}
}
