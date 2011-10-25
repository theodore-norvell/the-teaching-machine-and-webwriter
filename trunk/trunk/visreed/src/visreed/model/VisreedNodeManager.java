/**
 * VisreedNodeManager.java
 * 
 * @date: Oct 21, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.model;

import java.util.ArrayList;
import java.util.Hashtable;

import visreed.pattern.IObserver;

/**
 * VisreedNodeManager associates a node with an unique String.
 * @author Xiaoyu Guo
 */
public abstract class VisreedNodeManager implements IObserver<VisreedNode> {
    protected ArrayList<VisreedNode> nodeList;
    protected Hashtable<String, VisreedNode> nodeDictionary;
    
    public VisreedNodeManager(){
        this.nodeList = new ArrayList<VisreedNode>();
        this.nodeDictionary = new Hashtable<String, VisreedNode>();
    }
    
    /**
     * Adds a new production to the manager
     * @param node
     */
    public void registerNode(VisreedNode node){
    	String name = this.getString(node);
        if(name != null){
            this.nodeList.add(node);
            if(name != null && name.length() > 0){
            	this.nodeDictionary.put(name, node);
            }
            node.registerObserver(this);
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
     * Updates the name for each production node in its list. 
     */
    public void refresh(){
        this.nodeDictionary.clear();
        for(VisreedNode node : this.nodeList){
            String name = this.getString(node);
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

	/* (non-Javadoc)
	 * @see visreed.pattern.IObserver#changed(visreed.pattern.IObservable)
	 */
	@Override
	public void changed(VisreedNode node) {
		if(this.nodeDictionary.containsValue(node)){
			// possibly the object has changed its name, so delete it and re-add it.
			this.nodeDictionary.values().remove(node);
			
			String key = this.getString(node);
			this.nodeDictionary.put(key, node);
		}
	}
	
	/**
	 * Decides how a node is associated to a String form
	 * @param node
	 * @return the string representation of the node
	 */
	protected abstract String getString(VisreedNode node);
}
