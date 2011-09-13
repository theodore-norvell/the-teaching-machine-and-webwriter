/**
 * ProductionTag.java
 * 
 * @date: Aug 22, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.tag;

import java.util.ArrayList;
import java.util.List;

import visreed.extension.javaCC.model.payload.ProductionPayload;
import visreed.model.VisreedPayload;
import visreed.model.tag.VisreedTag;

/**
 * ProductionTag defines the tag for JavaCC productions.
 * Productions can contain productions.
 * @author Xiaoyu Guo
 */
public class ProductionTag extends JavaCCTag{
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o){
        return (o instanceof ProductionTag);
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString(){
        return "PRODUCTION";
    }
    
    /* (non-Javadoc)
     * @see higraph.model.taggedInterfaces.Tag#contentModel(java.util.List)
     */
    @Override
    public boolean contentModel(List<VisreedTag> seq) {
    	if(seq != null && seq.size() == 1 && seq.get(0).equals(SEQUENCE)){
    		return true;
    	} else {
    		return false;
    	}
    }

    /* (non-Javadoc)
     * @see higraph.model.taggedInterfaces.Tag#defaultTagSequence()
     */
    @Override
    public List<VisreedTag> defaultTagSequence() {
        ArrayList<VisreedTag> seq = new ArrayList<VisreedTag>(1);
        seq.add(SEQUENCE);
        return seq;
    }

    /* (non-Javadoc)
     * @see higraph.model.taggedInterfaces.Tag#defaultPayload()
     */
    @Override
    public VisreedPayload defaultPayload() {
        return new ProductionPayload();
    }

    /* (non-Javadoc)
     * @see visreed.model.IDescribable#getDescription()
     */
    @Override
    public String getDescription() {
        return "Production";
    }

    private static final ProductionTag instance = new ProductionTag();
    
    public static final ProductionTag getInstance(){
        return instance;
    }
}
