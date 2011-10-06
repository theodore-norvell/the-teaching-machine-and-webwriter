/**
 * RegexpProductionPayload.java
 * 
 * @date: Sep 26, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.payload;

/**
 * @author Xiaoyu Guo
 */
public class RegexpProductionPayload extends ProductionPayload {

	public enum Kind {
		TOKEN, SPECIAL_TOKEN, SKIP, MORE
	}
	
	private boolean ignoreCase = false;
	private Kind kind;
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


}
