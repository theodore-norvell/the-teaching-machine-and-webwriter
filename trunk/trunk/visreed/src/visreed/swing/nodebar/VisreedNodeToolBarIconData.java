/**
 * RegexIconData.java
 * 
 * @date: Jul 26, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.swing.nodebar;

import visreed.model.VisreedTag;

/**
 * RegexIconData represents a button with path to icon image and description.
 * @author Xiaoyu Guo
 */
public class VisreedNodeToolBarIconData {
    private String description;
    private String iconFileName;
    private VisreedTag tag;

    public VisreedNodeToolBarIconData(String desc, String iconFileName, VisreedTag tag){
        this.description = desc;
        this.iconFileName = iconFileName;
        this.tag = tag;
    }
    
    /**
     * @return the description
     */
    String getDescription() {
        return description;
    }
    
    /**
     * @return the iconFileName
     */
    String getIconFileName() {
        return iconFileName;
    }

    /**
     * @return the tag
     */
    VisreedTag getTag() {
        return tag;
    }

}
