/**
 * RegexBuilder.java
 * 
 * @date: Jun 27, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package regex.parser;

import java.util.ArrayList;
import java.util.List;

import regex.model.RegexNode;
import regex.model.RegexPayload;
import regex.model.RegexWholeGraph;
import regex.model.payload.AlternationPayload;
import regex.model.payload.KleenePlusPayload;
import regex.model.payload.KleeneStarPayload;
import regex.model.payload.OptionalPayload;
import regex.model.payload.SequencePayload;
import regex.model.payload.TerminalPayload;

/**
 * @author Xiaoyu Guo
 *
 */
public class RegexBuilder {
    private List<RegexNode> stack = new ArrayList<RegexNode>();
    private RegexWholeGraph wholeGraph;
    
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
    public void push(RegexNode node){
        stack.add(node);
    }

    /**
     * Pops the top node from the stack
     * @return the (previous) top node, null if the stack is empty.
     */
    public RegexNode pop() {
        if(stack.size() == 0){
            return null;
        }
        RegexNode node = stack.get(stack.size() - 1);
        stack.remove(stack.size() - 1);
        return node;
    }
    
    /**
     * Gets the top node in the stack
     * @return
     */
    public RegexNode peak() {
        if(stack.size() == 0){
            return null;
        }
        RegexNode node = stack.get(stack.size() - 1);
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
    
    public RegexBuilder(RegexWholeGraph wg){
        this.wholeGraph = wg;
    }
    
    /** Handling nodes building */
    
    /**
     * @param seq
     * @return
     */
    public void buildKleeneStar() {
        buildAndPushNodeWithSeq(new KleeneStarPayload(), 1);
    }
    
    public void buildOptional() {
        buildAndPushNodeWithSeq(new OptionalPayload(), 1);
    }
    
    /**
     * @param wg
     * @param image
     * @return
     */
    public void buildTerminal(String image) {
        buildAndPushNodeWithNoSeq(new TerminalPayload(image), 0);
    }

    /**
     * @param wg
     * @param seq
     * @param numOfChildren
     * @return
     */
    public void buildAlternation(int numOfChildren) {
        buildAndPushNodeWithSeq(new AlternationPayload(), numOfChildren);
    }

    /**
     * @param wg
     * @param seq
     * @return
     */
    public void buildKleenePlus() {
        buildAndPushNodeWithSeq(new KleenePlusPayload(), 1);
    }
    
    /**
     * @param wg
     * @param numOfChildren
     */
    public void buildSequence(int numOfChildren){
        buildAndPushNodeWithNoSeq(new SequencePayload(), numOfChildren);
    }
    
    private void buildAndPushNodeWithNoSeq(
        RegexPayload payload,
        int numOfChildren
    ){
        if(this.getStackSize() < numOfChildren){
            return;
        }
        RegexNode node = this.wholeGraph.makeRootNode(payload);
        if(numOfChildren == 1){
            RegexNode kid = this.pop();
            if(kid.getPayload() instanceof SequencePayload){
                // do nothing
            }
            else{
                node.insertChild(0, kid);
            }
        } else { 
            for(int i = 0; i < numOfChildren; i++){
                RegexNode kid = this.pop();
                if(kid.getPayload() instanceof SequencePayload){
                    int kidChildren = kid.getNumberOfChildren();
                    for(int j = 0; j < kidChildren; j++){
                        node.insertChild(0, kid.getChild(kidChildren - j - 1));
                    }
                } else {
                    node.insertChild(0, kid);
                }
            }
        }
        this.push(node);
    }
    
    private void buildAndPushNodeWithSeq(
        RegexPayload payload,
        int numOfChildren
    ){
        if(this.getStackSize() < numOfChildren){
            return;
        }
        RegexNode node = this.wholeGraph.makeRootNode(payload);
        for(int i = 0; i < numOfChildren; i++){
            RegexNode kid = this.pop();
            if(kid.getPayload() instanceof SequencePayload){
                node.insertChild(0, kid);
            } else {
                RegexNode seq = this.wholeGraph.makeRootNode(new SequencePayload());
                seq.insertChild(0, kid);
                node.insertChild(0, seq);
            }
        }
        this.push(node);
    }
    
    /**
     * Make a SEQ node with the current stack as its children
     */
    public void makeRoot() {
        RegexNode root = this.peak();
        if(root.getPayload() instanceof SequencePayload){
            // do nothing
        } else {
            // create a big SEQ that contains every node in the stack
            this.buildSequence(this.getStackSize());
        }
    }
}
