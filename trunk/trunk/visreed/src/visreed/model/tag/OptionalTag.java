/**
 * OptionalTag.java
 * 
 * @date: Aug 28, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.model.tag;

import java.util.ArrayList;
import java.util.List;

import visreed.model.VisreedPayload;
import visreed.model.payload.OptionalPayload;

/**
 * @author Xiaoyu Guo
 *
 */
public class OptionalTag extends VisreedTag {
    OptionalTag(){
        super();
    }
    
    @Override
    public boolean equals(Object o){
        return (o instanceof OptionalTag);
    }
    

    @Override
    public boolean contentModel(List<VisreedTag> seq) {
        // Optional nodes must have exactly 1 sequence as child
        if(seq != null && seq.size() == 1 && seq.get(0).equals(SEQUENCE)){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public VisreedPayload defaultPayload() {
        return new OptionalPayload();
    }

    @Override
    public List<VisreedTag> defaultTagSequence() {
        ArrayList<VisreedTag> seq = new ArrayList<VisreedTag>(1);
        seq.add(SEQUENCE);
        return seq;
    }
    
    @Override
    public String getDescription(){
        return "OPT";
    };
    
    private static final OptionalTag instance = new OptionalTag();
    
    public static final OptionalTag getInstance(){
        return instance;
    }
}
