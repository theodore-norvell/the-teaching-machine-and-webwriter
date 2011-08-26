/**
 * RegexEdgeLabel.java
 * 
 * @date: 2011-5-25
 * @author: Xiaoyu Guo (MUN# 200982155)
 * This file was prepared by Xiaoyu Guo. It was completed by me alone.
 */
package regex.model;

import higraph.model.interfaces.Payload;

/**
 * RegexEdgeLabels are generally not used in the project.
 * @author Xiaoyu Guo
 */
public class RegexEdgeLabel implements Payload<RegexEdgeLabel> {

    String str;
    
    public RegexEdgeLabel (String str){
        this.str = str;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see higraph.model.interfaces.Payload#copy()
     */
    @Override
    public RegexEdgeLabel copy() {
        return this;
    }

}
