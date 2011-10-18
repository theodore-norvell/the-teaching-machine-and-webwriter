/**
 * SequenceTag.java
 * 
 * @date: Aug 28, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.model.tag;

import java.util.ArrayList;
import java.util.List;

import visreed.model.payload.SequencePayload;
import visreed.model.payload.VisreedPayload;

/**
 * @author Xiaoyu Guo
 * 
 */
public class SequenceTag extends VisreedTag {
    SequenceTag(){
        super();
    }
    
    @Override
    public boolean equals(Object obj){
        return (obj instanceof SequenceTag);
    }
    
    @Override
    public String toString(){
        return "SEQUENCE";
    }
    
    @Override
    public boolean contentModel(List<VisreedTag> seq) {
        // Sequence may have 0 or more children, but none can be sequence.
        boolean validated = true;
        if (seq == null || seq.size() == 0) {
            validated = true;
        }
        if (seq.contains(SEQUENCE) == true) {
            validated = false;
        }
        return validated;
    }

    @Override
    public VisreedPayload defaultPayload() {
        return new SequencePayload();
    }

    @Override
    public List<VisreedTag> defaultTagSequence() {
        return new ArrayList<VisreedTag>(0);
    }

    @Override
    public String getDescription() {
        return "SEQ";
    }
    
    private static final SequenceTag instance = new SequenceTag();

    public static SequenceTag getInstance() {
        return instance;
    };

}
