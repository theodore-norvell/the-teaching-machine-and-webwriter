/**
 * NodeInsertionParameter.java
 * 
 * @date: Nov 14, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.model;

import java.util.List;

/**
 * @author Xiaoyu Guo
 */
public class NodeInsertionParameter {
	public NodeInsertionParameter(
		VisreedNode insertionTarget, 
		List<VisreedNode> children
	){
		this.insertionTarget = insertionTarget;
		this.children = children;
	}
	
	public VisreedNode insertionTarget;
	public List<VisreedNode> children;
}
