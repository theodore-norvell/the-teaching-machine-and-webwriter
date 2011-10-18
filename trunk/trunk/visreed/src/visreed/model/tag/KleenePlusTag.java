/**
 * KleenePlusTag.java
 * 
 * @date: Aug 28, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.model.tag;

import java.util.ArrayList;
import java.util.List;

import visreed.model.payload.KleenePlusPayload;
import visreed.model.payload.VisreedPayload;

/**
 * @author Xiaoyu Guo
 *
 */
public class KleenePlusTag extends VisreedTag {
    KleenePlusTag(){
        super();
    }
    
    @Override
    public boolean equals(Object o){
        return (o instanceof KleenePlusTag);
    }
    
    @Override
    public String toString(){
        return "KLEENE_PLUS";
    }
    
    @Override
    public boolean contentModel(List<VisreedTag> seq) {
        // Kleene Plus nodes must have exactly 1 sequence as child
        if(seq != null && seq.size() == 1 && seq.get(0).equals(SEQUENCE)){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public VisreedPayload defaultPayload() {
        return new KleenePlusPayload();
    }

    @Override
    public List<VisreedTag> defaultTagSequence() {
        ArrayList<VisreedTag> seq = new ArrayList<VisreedTag>(1);
        seq.add(SEQUENCE);
        return seq;
    }
    
    @Override
    public String getDescription(){
        return "KLN+";
    }

    private static final KleenePlusTag instance = new KleenePlusTag();
    public static VisreedTag getInstance() {
        return instance;
    };
}
