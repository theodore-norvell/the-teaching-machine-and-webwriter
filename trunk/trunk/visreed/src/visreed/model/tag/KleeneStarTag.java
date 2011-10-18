/**
 * KleeneStarTag.java
 * 
 * @date: Aug 28, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.model.tag;

import java.util.ArrayList;
import java.util.List;

import visreed.model.payload.KleeneStarPayload;
import visreed.model.payload.VisreedPayload;

/**
 * @author Xiaoyu Guo
 *
 */
public class KleeneStarTag extends VisreedTag {
    KleeneStarTag(){
        super();
    }
    
    @Override
    public boolean equals(Object o){
        return (o instanceof KleeneStarTag);
    }
    
    @Override
    public String toString(){
        return "KLEENE_STAR";
    }
    
    @Override
    public boolean contentModel(List<VisreedTag> seq) {
        // Kleene Star nodes must have exactly 1 sequence as child
        if(seq != null && seq.size() == 1 && seq.get(0).equals(SEQUENCE)){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public VisreedPayload defaultPayload() {
        return new KleeneStarPayload();
    }

    @Override
    public List<VisreedTag> defaultTagSequence() {
        ArrayList<VisreedTag> seq = new ArrayList<VisreedTag>(1);
        seq.add(SEQUENCE);
        return seq;
    }
    
    @Override
    public String getDescription(){
        return "KLN*";
    };

    private static final KleeneStarTag instance = new KleeneStarTag();
    
    public static final KleeneStarTag getInstance(){
        return instance;
    }
}
