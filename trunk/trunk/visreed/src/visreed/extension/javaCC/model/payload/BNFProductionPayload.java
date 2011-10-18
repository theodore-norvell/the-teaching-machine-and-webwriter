/**
 * BNFProductionPayload.java
 * 
 * @date: Oct 2, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.payload;

import visreed.extension.javaCC.parser.JavaCCBuilder;

/**
 * @author Xiaoyu Guo
 *
 */
public class BNFProductionPayload extends ProductionPayload {

	public BNFProductionPayload() {
	}

	public BNFProductionPayload(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see visreed.extension.javaCC.model.payload.ProductionPayload#dump(java.lang.StringBuffer, int)
	 */
	@Override
	public StringBuffer dump(StringBuffer sb, int indentLevel){
		sb = JavaCCBuilder.dumpPrefix(sb, indentLevel);
		
		// modifier
		if(this.getModifier().length() > 0){
			sb.append(this.getModifier());
			sb.append(" ");
		}
		// return type
		if(this.getReturnType().length() > 0){
			sb.append(this.getReturnType());
			sb.append(" ");
		}
		// name
		if(this.getName().length() > 0){
			sb.append(this.getName());
			sb.append(" ");
		}
		// parameters
		sb.append("(");
		if(this.getParameterList().length() > 0){
			sb.append(this.getParameterList());
		}
		sb.append(")");
		
		sb.append(" : ");
		// TODO INIT_BLOCK
		sb.append("{}");
		
		sb.append("{\n");
		for(int i = 0; i < this.getNode().getNumberOfChildren(); i++){
			this.getNode().getChild(i).getPayload().dump(sb, indentLevel + 1);
			sb.append("\n");
		}
		
		JavaCCBuilder.dumpPrefix(sb, indentLevel);
		sb.append("}\n");
		return sb;
	}
}
