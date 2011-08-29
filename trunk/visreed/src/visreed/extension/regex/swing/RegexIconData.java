/**
 * RegexIconData.java
 * 
 * @date: Jul 26, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.regex.swing;

import visreed.model.tag.VisreedTag;

/**
 * RegexIconData represents a button with path to icon image and description.
 * @author Xiaoyu Guo
 */
class RegexIconData {
    private String description;
    private String iconFileName;
    private VisreedTag tag;

    RegexIconData(String desc, String iconFileName, VisreedTag tag){
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
