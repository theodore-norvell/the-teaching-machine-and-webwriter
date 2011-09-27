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
	public boolean getIgnoreCase(){
		return this.ignoreCase;
	}
	public void setIgnoreCase(boolean value){
		this.ignoreCase = value;
	}
	
	private Kind kind;
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
