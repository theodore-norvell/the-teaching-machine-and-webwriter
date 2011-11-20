/**
 * RegexpProductionPayload.java
 * 
 * @date: Sep 26, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.payload;

import visreed.extension.javaCC.model.tag.JavaCCTag;
import visreed.extension.javaCC.parser.JavaCCBuilder;

/**
 * @author Xiaoyu Guo
 */
public class RegexpProductionPayload extends ProductionPayload {

	public enum Kind {
		TOKEN, SPECIAL_TOKEN, SKIP, MORE
	}
	
	public RegexpProductionPayload(String name) {
		super(JavaCCTag.REGULAR_PRODUCTION, name);
	}
	
	/* (non-Javadoc)
	 * @see visreed.extension.javaCC.model.payload.ProductionPayload#getDescription()
	 */
	@Override
	public String getDescription(){
		return this.kind.toString();
	}
	
	/* (non-Javadoc)
	 * @see visreed.extension.javaCC.model.payload.ProductionPayload#getName()
	 */
	@Override
	public String getName(){
		return this.kind.toString();
	}
	
	private boolean ignoreCase = false;
	private Kind kind = Kind.TOKEN;
	public String[] lexStates;
	
	
	public boolean getIgnoreCase(){
		return this.ignoreCase;
	}
	public void setIgnoreCase(boolean value){
		this.ignoreCase = value;
	}
	
	public Kind getKind() {
		return kind;
	}
	public void setKind(Kind kind) {
		this.kind = kind;
	}

	/* (non-Javadoc)
	 * @see visreed.extension.javaCC.model.payload.ProductionPayload#dump(java.lang.StringBuffer, int)
	 */
	@Override
    public StringBuffer dump(StringBuffer sb, int indentLevel) {
    	sb = JavaCCBuilder.dumpPrefix(sb, indentLevel);
    	sb.append(this.kind);
    	sb.append(" : {\n");
    	
    	for(int i = 0; i < this.getNode().getNumberOfChildren(); i++){
    		JavaCCBuilder.dumpPrefix(sb, indentLevel + 1);
    		this.getNode().getChild(i).getPayload().dump(sb, indentLevel + 1);
    	}
    	
		sb.append("\n");
		JavaCCBuilder.dumpPrefix(sb, indentLevel);
		sb.append("}\n");
    	return sb;
    }

}
