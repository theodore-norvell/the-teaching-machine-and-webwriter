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
    AlternationTag (){
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
    public boolean contentModel(List<VisreedTag> seq) {
        // Alternation must have 2 or more children, all must be sequences.
        if(seq == null || seq.size() < 2){
            return false;
        }

        for(int i = 0; i < seq.size(); i++){
            if(!(seq.get(i).equals(SequenceTag.getInstance()))){
                return false;
            }
        }
        return true;
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
    
    public static final AlternationTag getInstance(){
        return instance;
    }
}
