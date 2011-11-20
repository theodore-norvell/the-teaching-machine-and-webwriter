/**
 * RegexpSpecManager.java
 * 
 * @date: Oct 21, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model;

import visreed.extension.javaCC.model.payload.RegexpSpecPayload;
import visreed.extension.javaCC.model.tag.JavaCCTag;
import visreed.model.VisreedNode;
import visreed.model.VisreedNodeManager;
import visreed.model.VisreedPayload;

/**
 * @author Xiaoyu Guo
 *
 */
public class RegexpSpecManager extends VisreedNodeManager{

	/* (non-Javadoc)
	 * @see visreed.model.VisreedNodeManager#getString(visreed.model.VisreedNode)
	 */
	@Override
	protected String getString(VisreedNode node) {
		if(node == null || node.getPayload() == null){
			return null;
		}
		
		VisreedPayload pl = node.getPayload();
		if(pl.getTag().equals(JavaCCTag.REGEXP_SPEC)){
			return ((RegexpSpecPayload)pl).getName();
		} else {
			return null;
		}
	}

}
