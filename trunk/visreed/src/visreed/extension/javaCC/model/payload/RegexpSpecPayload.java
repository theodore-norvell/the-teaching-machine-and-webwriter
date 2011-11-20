/**
 * RegexpSpecPayload.java
 * 
 * @date: Oct 4, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.payload;

import higraph.view.HigraphView;
import tm.backtrack.BTTimeManager;
import visreed.extension.javaCC.model.tag.JavaCCTag;
import visreed.extension.javaCC.parser.JavaCCBuilder;
import visreed.extension.javaCC.view.RegexpSpecNodeView;
import visreed.model.VisreedEdge;
import visreed.model.VisreedEdgeLabel;
import visreed.model.VisreedHigraph;
import visreed.model.VisreedNode;
import visreed.model.VisreedPayload;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.view.VisreedNodeView;

/**
 * @author Xiaoyu Guo
 *
 */
public class RegexpSpecPayload extends VisreedPayload {
	
	public static RegexpSpecPayload EOF = new RegexpSpecPayload("EOF");
    private String name = "";
    private boolean isPrivate = false;
    
    public String getName(){
        String result = this.name;
        if(result == null){
            result = "null";
        } else if (result.length() == 0){
            result = "\"\"";
        }
        return result;
    }
    
    public void setName(String value){
    	boolean changed = (!this.name.equals(value));
        this.name = value;
        if(changed && this.getNode() != null){
        	this.getNode().notifyObservers();
        }
    }

	public boolean isPrivate() {
		return isPrivate;
	}

	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public RegexpSpecPayload() {
		super(JavaCCTag.REGEXP_SPEC);
	}

	public RegexpSpecPayload(String name) {
		super(JavaCCTag.REGEXP_SPEC);
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		return "RegSpec_" + this.getName();
	}
	
	/* (non-Javadoc)
     * @see visreed.model.VisreedPayload#getDescription()
     */
    @Override
    public String getDescription(){
        return "RegSpec_" + this.getName();
    }
	
	/* (non-Javadoc)
	 * @see visreed.extension.javaCC.model.payload.ProductionPayload#dump(java.lang.StringBuffer, int)
	 */
	@Override
    public StringBuffer dump(StringBuffer sb, int indentLevel) {
    	sb = JavaCCBuilder.dumpPrefix(sb, indentLevel);
    	sb.append("< ");
    	
    	if(this.getName().length() > 0){
    		if(this.isPrivate()){
    			sb.append("#");
    		}
    		sb.append(this.getName());
    		sb.append(" ");
    	
    		sb.append(": ");
    	}
    	
    	for(int i = 0; i < this.getNode().getNumberOfChildren(); i++){
    		VisreedPayload pl = this.getNode().getChild(i).getPayload();
    		pl.dump(sb, 0);
    	}
    	
		sb.append(">");
    	return sb;
    }

	/* (non-Javadoc)
	 * @see visreed.model.payload.VisreedPayload#format(visreed.model.VisreedNode)
	 */
	@Override
	public String format(VisreedNode currentNode) {
		return "RegexpSpec";
	}

	/* (non-Javadoc)
	 * @see visreed.model.payload.VisreedPayload#constructView(higraph.view.HigraphView, visreed.model.VisreedNode, tm.backtrack.BTTimeManager)
	 */
	@Override
	public VisreedNodeView constructView(
			HigraphView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> sgv,
			VisreedNode node, BTTimeManager timeman) {
		return new RegexpSpecNodeView(sgv, node, timeman);
	}
}
