/**
 * ProductionManager.java
 * 
 * @date: Aug 29, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import visreed.extension.javaCC.model.payload.ProductionPayload;
import visreed.model.VisreedNode;
import visreed.pattern.IObserver;

/**
 * ProductionManager handles relationship between all names and production nodes.
 * @author Xiaoyu Guo
 */
public class ProductionManager implements IObserver<VisreedNode> {
    private ArrayList<VisreedNode> nodeList;
    private Hashtable<String, VisreedNode> nodeDictionary;
    
    public ProductionManager(){
        this.nodeList = new ArrayList<VisreedNode>();
        this.nodeDictionary = new Hashtable<String, VisreedNode>();
    }
    
    /**
     * Adds a new production to the manager
     * @param node
     */
    public void registerNode(VisreedNode node){
        if(node != null && node.getPayload() != null){
            if(node.getPayload() instanceof ProductionPayload){
                this.nodeList.add(node);
                String key = ((ProductionPayload)node.getPayload()).getName();
                if(key != null && key.length() > 0){
                	this.nodeDictionary.put(key, node);
                }
                node.registerObserver(this);
            }
        }
    }
    
    /**
     * Removes a production from the manager
     * @param node
     */
    public void deRegisterNode(VisreedNode node){
        if(node != null && this.nodeDictionary.containsValue(node)){
            this.nodeList.remove(node);
            this.nodeDictionary.values().remove(node);
        }
    }
    
    /**
     * Gets the specified production node by name
     * @param name
     * @return
     */
    public VisreedNode getNode(String name){
        if(name != null && name.length() > 0){
            return this.nodeDictionary.get(name);
        }
        return null;
    }
    
    public List<VisreedNode> getAllProductions(){
    	return this.nodeList;
    }

	/* (non-Javadoc)
	 * @see visreed.pattern.IObserver#changed(visreed.pattern.IObservable)
	 */
	@Override
	public void changed(VisreedNode object) {
		if(this.nodeDictionary.containsValue(object)){
			// possibly the object has changed its name, so delete it and re-add it.
			this.nodeDictionary.values().remove(object);
			
			String key = ((ProductionPayload)object.getPayload()).getName();
			this.nodeDictionary.put(key, object);
		}
	}
    
    /**
     * Updates the name for each production node in its list. 
     */
    public void refresh(){
        this.nodeDictionary.clear();
        for(VisreedNode node : this.nodeList){
            String name = ((ProductionPayload)node.getPayload()).getName();
            this.nodeDictionary.put(name, node);
        }
    }
    
    /**
     * Clear all the registered node
     */
    public void clear(){
    	if(this.nodeList != null && this.nodeList.size() > 0){
    		for(VisreedNode n : this.nodeList){
    			n.deRegisterObserver(this);
    		}
    	}
        this.nodeList.clear();
        this.nodeDictionary.clear();
    }
}
