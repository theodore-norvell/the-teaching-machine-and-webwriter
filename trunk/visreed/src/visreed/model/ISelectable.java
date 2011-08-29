/**
 * ISelectable.java
 * 
 * @date: Jul 11, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.model;

/**
 * Describes a selectable item
 * @author Xiaoyu Guo
 */
public interface ISelectable {
    /**
     * Gets whether the item is currently selected
     */
    boolean isSelected();
    
    /**
     * Selects the item (=setSelected(true))
     */
    void select();
    
    /**
     * Deselects the item (=setSelected(false))
     */
    void deSelect();
    
    /**
     * Sets the selected status of the item
     * @param selected
     */
    void setSelected(boolean selected);
}
