/**
 * KleenePlusTag.java
 * 
 * @date: Aug 28, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.regex.model.tag;

import visreed.model.VisreedPayload;
import visreed.model.VisreedTag;
import visreed.model.payload.KleenePlusPayload;

/**
 * @author Xiaoyu Guo
 *
 */
public class KleenePlusTag extends RegexTag {
    protected KleenePlusTag(){
        super(TagCategory.SINGLE_SEQ_CHILD);
    }
    
    @Override
    public String toString(){
        return "KLEENE_PLUS";
    }

    @Override
    public VisreedPayload defaultPayload() {
        return new KleenePlusPayload(RegexTag.KLEENE_PLUS);
    }

    @Override
    public String getDescription(){
        return "KLN+";
    }

    private static final KleenePlusTag instance = new KleenePlusTag();
    protected static VisreedTag getInstance() {
        return instance;
    };
}
