/**
 * RegexBuilder.java
 * 
 * @date: Jun 27, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.regex.parser;

import visreed.extension.regex.model.tag.RegexTag;
import visreed.model.VisreedNode;
import visreed.model.VisreedPayload;
import visreed.model.VisreedTag;
import visreed.model.VisreedWholeGraph;
import visreed.model.payload.RepeatRangePayload;
import visreed.model.payload.SequencePayload;
import visreed.model.payload.TerminalPayload;
import visreed.parser.VisreedBuilder;

/**
 * @author Xiaoyu Guo
 *
 */
public class RegexBuilder extends VisreedBuilder{
    
	public RegexBuilder(VisreedWholeGraph wg) {
		super(wg);
	}
	
	/* (non-Javadoc)
	 * @see visreed.parser.VisreedBuilder#makeRoot()
	 */
	@Override
    public void makeRoot() {
        VisreedNode root = this.peak();
        if(root.getPayload() instanceof SequencePayload){
            // do nothing
        } else {
            // create a big SEQ that contains every node in the stack
            this.buildSequence(this.getStackSize());
        }
    }
	
	/** Handling nodes building */
    
    /**
     * @param wg
     * @param numOfChildren
     */
    public void buildSequence(int numOfChildren){
    	buildNode(RegexTag.SEQUENCE, numOfChildren);
    }
    
    /**
     * @param seq
     * @return
     */
    public void buildKleeneStar() {
        buildNode(RegexTag.KLEENE_STAR, 1);
    }

    /**
     * @param wg
     * @param seq
     * @return
     */
    public void buildKleenePlus() {
    	buildNode(RegexTag.KLEENE_PLUS, 1);
    }
    
    public void buildOptional() {
    	buildNode(RegexTag.OPTIONAL, 1);
    }
    
    public void buildRepeatRange(int minValue, int maxValue) {
    	buildNode(
			new RepeatRangePayload(RegexTag.REPEAT_RANGE, minValue, maxValue),
			1
		);
    }
    
    /**
     * @param wg
     * @param image
     * @return
     */
    public void buildTerminal(String image) {
    	buildNode(new TerminalPayload(RegexTag.TERMINAL, image), 0);
    }

    /**
     * @param wg
     * @param seq
     * @param numOfChildren
     * @return
     */
    public void buildAlternation(int numOfChildren) {
    	// alternation is special, as it comes with 2 empty sequences.
        if(this.getStackSize() < numOfChildren){
            return;
        }
        VisreedNode node = this.wholeGraph.makeRootNode(RegexTag.ALTERNATION);
        for(int i = 0; i < numOfChildren; i++){
            VisreedNode kid = this.pop();
            
            if(i == numOfChildren - 1 || i == numOfChildren - 2){
            	node.getChild(numOfChildren - i - 1).replace(kid);
            } else {
            	node.insertChild(2, kid);
            }
        }
        this.push(node);
    }
}
