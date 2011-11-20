/**
 * VisreedBuilder.java
 * 
 * @date: Sep 12, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.parser;

import java.util.ArrayList;
import java.util.List;

import visreed.model.VisreedNode;
import visreed.model.VisreedPayload;
import visreed.model.VisreedTag;
import visreed.model.VisreedWholeGraph;

/**
 * @author Xiaoyu Guo
 *
 */
public class VisreedBuilder {
	protected List<VisreedNode> stack = new ArrayList<VisreedNode>();
	protected VisreedWholeGraph wholeGraph;
    
    private boolean verbose = true;
    private boolean tracing = true;

    /** Handling Options */
    public boolean getVerbose(){
        return this.verbose;
    }
    public void setVerbose(boolean verbose){
        this.verbose = verbose;
    }
    
    public boolean getTracing(){
        return this.tracing;
    }
    public void setTracing(boolean tracing){
        this.tracing = tracing;
    }
    
    /** Handling stack */
    
    /**
     * Pushes a node at the top of the stack
     * @param node the node
     */
    public void push(VisreedNode node){
        stack.add(node);
    }

    /**
     * Pops the top node from the stack
     * @return the (previous) top node, null if the stack is empty.
     */
    public VisreedNode pop() {
        if(stack.size() == 0){
            return null;
        }
        VisreedNode node = stack.get(stack.size() - 1);
        stack.remove(stack.size() - 1);
        return node;
    }
    
    /**
     * Gets the top node in the stack
     * @return
     */
    public VisreedNode peak() {
        if(stack.size() == 0){
            return null;
        }
        VisreedNode node = stack.get(stack.size() - 1);
        return node;
    }
    
    /**
     * Whether the stack is empty 
     * @return true if the stack is empty, false other wise.
     */
    public boolean isStackEmpty(){
        return stack.size() == 0;
    }
    
    /**
     * Gets the size of the stack.
     * @return the size of the stack.
     */
    public int getStackSize(){
        return stack.size();
    }
    
    public VisreedBuilder(VisreedWholeGraph wg){
        this.wholeGraph = wg;
    }

    
    public void buildNode(VisreedTag tag, int numOfChildren){
    	buildNode(tag.defaultPayload(), numOfChildren);
    }
    
    public void buildNode(VisreedPayload payload, int numOfChildren){
    	if(this.getStackSize() < numOfChildren){
    		return;
    	}
    	VisreedNode node = this.wholeGraph.makeRootNode(payload);
    	for(int i = 0; i < numOfChildren; i++){
    		VisreedNode kid = this.pop();
    		node.insertChild(0, kid);
    	}
    	this.push(node);
    }
    
    
    /**
     * Build a node and push it into the stack.
     * The node is SEQ type, so all its children will be Non-SEQ.
     * @param payload
     * @param numOfChildren
     */
    @Deprecated
    public void buildAndPushNodeWithNoSeq(
        VisreedPayload payload,
        int numOfChildren
    ){
        if(this.getStackSize() < numOfChildren){
            return;
        }
        VisreedNode node = this.wholeGraph.makeRootNode(payload);
        
        for(int i = 0; i < numOfChildren; i++){
        	VisreedNode kid = this.pop();
        	node.insertChild(0, kid);
        }
        
        this.push(node);
    }
    
    /**
     * Build a node and push it into the stack.
     * The node is Non-SEQ type, so all its children will be SEQ.
     * @param payload
     * @param numOfChildren
     */
    @Deprecated
    public void buildAndPushNodeWithSeq(
        VisreedPayload payload,
        int numOfChildren
    ){
        if(this.getStackSize() < numOfChildren){
            return;
        }
        VisreedNode node = this.wholeGraph.makeRootNode(payload);
        
        for(int i = 0; i < numOfChildren; i++){
        	VisreedNode kid = this.pop();
        	node.insertChild(0, kid);
        }
        this.push(node);
    }

	/**
     * Make a SEQ node with the current stack as its children
     */
    public void makeRoot(){}
}
