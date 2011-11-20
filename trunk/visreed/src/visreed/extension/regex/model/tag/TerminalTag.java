/**
 * TerminalTag.java
 * 
 * @date: Aug 28, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.regex.model.tag;

import java.util.ArrayList;
import java.util.List;

import visreed.model.VisreedPayload;
import visreed.model.VisreedTag;
import visreed.model.payload.TerminalPayload;

/**
 * @author Xiaoyu Guo
 *
 */
public class TerminalTag extends RegexTag {
    protected TerminalTag (){
        super(TagCategory.TERMINAL);
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
    public VisreedPayload defaultPayload() {
        return new TerminalPayload(RegexTag.TERMINAL);
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
    
    protected static TerminalTag getInstance(){
        return instance;
    }
}
