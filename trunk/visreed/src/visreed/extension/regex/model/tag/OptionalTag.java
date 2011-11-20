/**
 * OptionalTag.java
 * 
 * @date: Aug 28, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.regex.model.tag;

import visreed.model.VisreedPayload;
import visreed.model.payload.OptionalPayload;

/**
 * @author Xiaoyu Guo
 *
 */
public class OptionalTag extends RegexTag {
    protected OptionalTag(){
        super(TagCategory.SINGLE_SEQ_CHILD);
    }
    
    @Override
    public VisreedPayload defaultPayload() {
        return new OptionalPayload(RegexTag.OPTIONAL);
    }
    
    @Override
    public String getDescription(){
        return "OPT";
    };
    
    private static final OptionalTag instance = new OptionalTag();
    
    protected static OptionalTag getInstance(){
        return instance;
    }
}
