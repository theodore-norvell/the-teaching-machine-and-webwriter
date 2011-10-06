/**
 * VisreedTag.java
 * 
 * @date: 2011-5-25
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.model.tag;

import higraph.model.taggedInterfaces.Tag;
import visreed.model.IDescribable;
import visreed.model.VisreedPayload;

/**
 * @author Xiaoyu Guo
 */
public abstract class VisreedTag 
implements Tag<VisreedTag, VisreedPayload>, IDescribable{
    public static final VisreedTag SEQUENCE = SequenceTag.getInstance();
    public static final VisreedTag ALTERNATION = AlternationTag.getInstance();
    public static final VisreedTag KLEENE_PLUS = KleenePlusTag.getInstance();
    public static final VisreedTag KLEENE_STAR =  KleeneStarTag.getInstance();
    public static final VisreedTag TERMINAL = TerminalTag.getInstance();
    public static final VisreedTag OPTIONAL = OptionalTag.getInstance();
    public static final VisreedTag REPEAT_RANGE = RepeatRangeTag.getInstance();
    
    private static final VisreedTag[] VALUES = new VisreedTag[]{
        SEQUENCE,
        ALTERNATION,
        KLEENE_PLUS,
        KLEENE_STAR,
        TERMINAL,
        OPTIONAL,
        REPEAT_RANGE
    };
    
    public static VisreedTag[] values(){
        return VALUES;
    }
}
