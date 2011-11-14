/**
 * AlternationTag.java
 * 
 * @date: Aug 28, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.model.tag;

import java.util.ArrayList;
import java.util.List;

import visreed.model.payload.AlternationPayload;
import visreed.model.payload.VisreedPayload;

/**
 * @author Xiaoyu Guo
 *
 */
public class AlternationTag extends VisreedTag {
    protected AlternationTag (){
        super();
    }
    
    @Override
    public boolean equals(Object o){
        return (o instanceof AlternationTag);
    }
    
    @Override
    public String toString(){
        return "ALTERNATION";
    }
    
    @Override
    public boolean canHoldExactOneChild(){
    	return false;
    }
    
    @Override
    protected boolean contentModelHook(final List<VisreedTag> childTags){
    	return childTags.size() >= 2;
    }

    @Override
    public VisreedPayload defaultPayload() {
        return new AlternationPayload();
    }

    @Override
    public List<VisreedTag> defaultTagSequence() {
        ArrayList<VisreedTag> seq = new ArrayList<VisreedTag>(2);
        seq.add(SEQUENCE);
        seq.add(SEQUENCE);
        return seq;
    }
    
    @Override
    public String getDescription(){
        return "ALT";
    };
    
    private static final AlternationTag instance = new AlternationTag();
    
    protected static AlternationTag getInstance(){
        return instance;
    }
}
