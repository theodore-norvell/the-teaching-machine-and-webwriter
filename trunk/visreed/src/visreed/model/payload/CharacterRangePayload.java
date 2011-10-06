/**
 * CharacterListPayload.java
 * 
 * @date: Oct 4, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.model.payload;

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
import visreed.view.VisreedNodeView;

/**
 * @author Xiaoyu Guo
 *
 */
public class CharacterRangePayload extends TerminalPayload {

	public CharacterRangePayload(char value) {
		super(value);
		this.minValue = value;
		this.hasMaxValue = false;
	}
	public CharacterRangePayload(char minValue, char maxValue, boolean exclude) {
		super(exclude?"~":"" + minValue + "-" + maxValue);
		this.minValue = minValue;
		this.hasMaxValue = true;
		this.maxValue = maxValue;
		this.exclude = exclude;
	}

	public CharacterRangePayload(String valueStr) {
		super(valueStr);
		char minValue = ' ';
		if(valueStr.length() > 0){
			minValue = valueStr.charAt(0);
		}
		this.minValue = minValue;
		this.hasMaxValue = false;
	}
	public CharacterRangePayload(String minValueStr, String maxValueStr, boolean exclude) {
		super(exclude?"~":"" + minValueStr + "-" + maxValueStr);
		
		char minValue = ' ';
		if(minValueStr.length() > 0){
			minValue = minValueStr.charAt(0);
		}
		char maxValue = ' ';
		if(maxValueStr.length() > 0){
			maxValue = maxValueStr.charAt(0);
		}
		this.minValue = minValue;
		this.hasMaxValue = true;
		this.maxValue = maxValue;
		this.exclude = exclude;
	}
	
	private boolean exclude;
	private char minValue;
	private char maxValue;
	private boolean hasMaxValue;

	/* (non-Javadoc)
	 * @see visreed.model.VisreedPayload#format(visreed.model.VisreedNode)
	 */
	@Override
	public String format(VisreedNode currentNode) {
		String result = "";
		if(this.exclude){
			result = "~";
		}
		result += this.minValue;
		if(this.hasMaxValue){
			result += "-";
			result += this.maxValue;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see visreed.model.VisreedPayload#constructView(higraph.view.HigraphView, visreed.model.VisreedNode, tm.backtrack.BTTimeManager)
	 */
	@Override
	public VisreedNodeView constructView(
			HigraphView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> sgv,
			VisreedNode node, BTTimeManager timeman) {
		return new TerminalNodeView(sgv, node, timeman);
	}

	public boolean getExclude(){
		return this.exclude;
	}
	
	public char getMinValue(){
		return this.minValue;
	}
	
	public boolean hasMaxValue(){
		return this.hasMaxValue;
	}
	
	public char getMaxValue(){
		return this.maxValue;
	}
}
