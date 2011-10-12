/**
 * TerminalTag.java
 * 
 * @date: Aug 28, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.model.tag;

import java.util.ArrayList;
import java.util.List;

import visreed.model.VisreedPayload;
import visreed.model.payload.TerminalPayload;

/**
 * @author Xiaoyu Guo
 *
 */
public class TerminalTag extends VisreedTag {
    protected TerminalTag (){
        super();
    }
    
    @Override
    public boolean equals(Object o){
        return (o instanceof TerminalTag);
    }
    
    @Override
    public String toString(){
        return "TERMINAL";
    }

    @Override
    public boolean contentModel(List<VisreedTag> seq) {
        // Terminal nodes do not have child
        if(seq == null || seq.size() == 0){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public VisreedPayload defaultPayload() {
        return new TerminalPayload();
    }

    @Override
    public List<VisreedTag> defaultTagSequence() {
        return new ArrayList<VisreedTag>(0);
    }
    
    @Override
    public String getDescription(){
        return "TER";
    };
    
    private static final TerminalTag instance = new TerminalTag();
    
    public static TerminalTag getInstance(){
        return instance;
    }
}
