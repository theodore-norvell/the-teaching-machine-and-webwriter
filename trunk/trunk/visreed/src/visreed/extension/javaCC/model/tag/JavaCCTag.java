/**
 * JavaCCTag.java
 * 
 * @date: Aug 28, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.tag;

import visreed.model.tag.VisreedTag;

/**
 * @author Xiaoyu Guo
 *
 */
public abstract class JavaCCTag extends VisreedTag {
    public static final VisreedTag PRODUCTION = ProductionTag.getInstance();
    public static final VisreedTag ROOT = RootTag.getInstance();
    public static final VisreedTag LINK = JavaCCLinkTag.getInstance();
    
    private static final VisreedTag[] VALUES = new VisreedTag[]{
    	ROOT,
        SEQUENCE,
        ALTERNATION,
        KLEENE_PLUS,
        KLEENE_STAR,
        TERMINAL,
        OPTIONAL,
        PRODUCTION,
        LINK
    };
    
    public static VisreedTag[] values(){
        return VALUES;
    }
}
