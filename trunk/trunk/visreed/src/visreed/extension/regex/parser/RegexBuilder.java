/**
 * RegexBuilder.java
 * 
 * @date: Jun 27, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.regex.parser;

import visreed.model.VisreedNode;
import visreed.model.VisreedWholeGraph;
import visreed.model.payload.SequencePayload;
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
}
