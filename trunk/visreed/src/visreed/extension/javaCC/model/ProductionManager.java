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

import visreed.extension.javaCC.model.payload.ProductionPayload;
import visreed.model.VisreedNode;

/**
 * @author Xiaoyu Guo
 *
 */
public class ProductionManager {
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
            String name = ((ProductionPayload)node.getPayload()).getName();
            this.nodeDictionary.remove(name);
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
    
    public void clear(){
        this.nodeList.clear();
        this.nodeDictionary.clear();
    }
}
