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
import visreed.model.VisreedTag;
import visreed.model.VisreedWholeGraph;
import visreed.view.TerminalNodeView;
import visreed.view.VisreedNodeView;

/**
 * @author Xiaoyu Guo
 *
 */
public class CharacterListPayload extends TerminalPayload {

	public CharacterListPayload(VisreedTag tag, char value) {
		super(tag, value);
		this.minValue = value;
		this.hasMaxValue = false;
	}
	public CharacterListPayload(VisreedTag tag, char minValue, char maxValue, boolean exclude) {
		super(tag, exclude?"~":"" + minValue + "-" + maxValue);
		this.minValue = minValue;
		this.hasMaxValue = true;
		this.maxValue = maxValue;
		this.exclude = exclude;
	}

	public CharacterListPayload(VisreedTag tag, String valueStr) {
		super(tag, valueStr);
		char minValue = ' ';
		if(valueStr.length() > 0){
			minValue = valueStr.charAt(0);
		}
		this.minValue = minValue;
		this.hasMaxValue = false;
	}
	public CharacterListPayload(VisreedTag tag, String minValueStr, String maxValueStr, boolean exclude) {
		super(tag, exclude?"~":"" + minValueStr + "-" + maxValueStr);
		
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
	
	public StringBuffer dump(StringBuffer sb, int indentLevel){
		if(sb == null){
			sb = new StringBuffer();
		}
		
		sb.append("[");
		if(this.exclude){
			sb.append("~");
		}
		sb.append("\"");
		sb.append(this.minValue);
		sb.append("\"");
		if(this.hasMaxValue){
			sb.append("-");
			sb.append("\"");
			sb.append(this.maxValue);
			sb.append("\"");
		}
		sb.append("]");
		
		return sb;
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
