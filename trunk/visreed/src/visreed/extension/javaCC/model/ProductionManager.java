/**
 * ProductionManager.java
 * 
 * @date: Aug 29, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model;

import java.util.Hashtable;
import java.util.List;

import visreed.extension.javaCC.model.payload.ProductionPayload;
import visreed.extension.javaCC.model.tag.JavaCCTag;
import visreed.model.VisreedNode;
import visreed.model.VisreedNodeManager;
import visreed.model.VisreedSubgraph;
import visreed.model.payload.VisreedPayload;

/**
 * ProductionManager handles relationship between all names and production nodes.
 * @author Xiaoyu Guo
 */
public class ProductionManager extends VisreedNodeManager {
	private Hashtable<String, VisreedSubgraph> subgraphDirectory;
	private JavaCCWholeGraph wholeGraph;
	
	public ProductionManager(){
		super();
		subgraphDirectory = new Hashtable<String, VisreedSubgraph>();
		this.wholeGraph = null;
	}
	
	/* (non-Javadoc)
	 * @see visreed.model.VisreedNodeManager#registerNode(visreed.model.VisreedNode)
	 */
	@Override
	public void registerNode(VisreedNode node){
		if(node == null || !(node.getPayload() instanceof ProductionPayload)){
			return;
		}
		super.registerNode(node);
		
		String name = this.getString(node);
        if(name == null){
        	return;
        }
		
        if(wholeGraph == null){
			wholeGraph = (JavaCCWholeGraph) node.getWholeGraph();
		}

        // construct a subgraph
		VisreedSubgraph graph = wholeGraph.constructSubgraph();
		// add the node as the only top node in the subgraph
		graph.addTop(node);
		this.subgraphDirectory.put(name, graph);
	}
	
	/* (non-Javadoc)
	 * @see visreed.model.VisreedNodeManager#deRegisterNode(visreed.model.VisreedNode)
	 */
	@Override
	public void deRegisterNode(VisreedNode node){
		super.deRegisterNode(node);
		String name = this.getString(node);
		if(name != null && name.length() > 0 && subgraphDirectory.containsKey(name)){
			subgraphDirectory.remove(name);
		}
	}

	/* (non-Javadoc)
	 * @see visreed.model.VisreedNodeManager#getString(visreed.model.VisreedNode)
	 */
	@Override
	protected String getString(VisreedNode node) {
		if(node == null || node.getPayload() == null){
			return null;
		}
		
		VisreedPayload pl = node.getPayload();
		if(pl.getTag().equals(JavaCCTag.PRODUCTION)){
			return ((ProductionPayload)pl).getName();
		} else {
			return null;
		}
	}

	/**
	 * Gets all the productions registered to this manager
	 * @return
	 */
	public List<VisreedNode> getAllProductions() {
		return this.nodeList;
	}
	
	/**
	 * Gets the corresponding subgraph with the specified production node as 
	 * the only top node
	 * @param productionName
	 * @return {@value null} if such production does not exist
	 */
	public VisreedSubgraph getSubgraph(String productionName){
		if(productionName != null && productionName.length() > 0){
			if(subgraphDirectory.containsKey(productionName)){
				return subgraphDirectory.get(productionName);
			}
		}
		return null;
	}
}
