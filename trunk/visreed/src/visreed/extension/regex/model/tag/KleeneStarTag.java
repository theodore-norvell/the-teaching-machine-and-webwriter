/**
 * KleeneStarTag.java
 * 
 * @date: Aug 28, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.regex.model.tag;

import visreed.model.VisreedPayload;
import visreed.model.payload.KleeneStarPayload;

/**
 * @author Xiaoyu Guo
 *
 */
public class KleeneStarTag extends RegexTag {
    protected KleeneStarTag(){
        super(TagCategory.SINGLE_SEQ_CHILD);
    }
    
    @Override
    public String toString(){
        return "KLEENE_STAR";
    }
    
    @Override
    public VisreedPayload defaultPayload() {
        return new KleeneStarPayload(RegexTag.KLEENE_STAR);
    }
    
    @Override
    public String getDescription(){
        return "KLN*";
    };

    private static final KleeneStarTag instance = new KleeneStarTag();
    
    protected static KleeneStarTag getInstance(){
        return instance;
    }
}
