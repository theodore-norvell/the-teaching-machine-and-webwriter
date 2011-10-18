/**
 * RegexpProductionPayload.java
 * 
 * @date: Sep 26, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.payload;

import visreed.extension.javaCC.parser.JavaCCBuilder;

/**
 * @author Xiaoyu Guo
 */
public class RegexpProductionPayload extends ProductionPayload {

	public enum Kind {
		TOKEN, SPECIAL_TOKEN, SKIP, MORE
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
	
	public RegexpProductionPayload(String name) {
		super(name);
	}

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
