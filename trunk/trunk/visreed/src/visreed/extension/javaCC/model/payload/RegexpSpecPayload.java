/**
 * RegexpSpecPayload.java
 * 
 * @date: Oct 4, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.payload;

import visreed.extension.javaCC.parser.JavaCCBuilder;
import visreed.model.payload.VisreedPayload;

/**
 * @author Xiaoyu Guo
 *
 */
public class RegexpSpecPayload extends ProductionPayload {
	
	public static RegexpSpecPayload EOF = new RegexpSpecPayload("EOF");

	public RegexpSpecPayload() {
		super();
	}

	public RegexpSpecPayload(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
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
    	}
    	
    	sb.append(": ");
    	
    	for(int i = 0; i < this.getNode().getNumberOfChildren(); i++){
    		VisreedPayload pl = this.getNode().getChild(i).getPayload();
    		pl.dump(sb, indentLevel + 1);
    	}
    	
		sb.append(">\n");
    	return sb;
    }
}
