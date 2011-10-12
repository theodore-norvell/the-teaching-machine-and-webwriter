/**
 * RegexpSpecPayload.java
 * 
 * @date: Oct 4, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.payload;

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
}
