/**
 * RegexIconData.java
 * 
 * @date: Jul 26, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package regex.swing;

import regex.model.RegexTag;

/**
 * RegexIconData represents a button with path to icon image and description.
 * @author Xiaoyu Guo
 */
class RegexIconData {
    private String description;
    private String iconFileName;
    private RegexTag tag;

    RegexIconData(String desc, String iconFileName, RegexTag tag){
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
    RegexTag getTag() {
        return tag;
    }

}
