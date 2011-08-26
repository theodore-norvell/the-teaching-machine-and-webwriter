/**
 * IHoverable.java
 * 
 * @date: Jul 25, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package regex.model;

/**
 * IHoverable describes the ability to interact with the mouse hovering event.
 * @author Xiaoyu Guo
 */
public interface IHoverable {

    /**
     * Sets the hover on
     */
    public void setHoverOn();

    /**
     * Sets the hover off
     */
    public void setHoverOff();

    /**
     * Gets whether the view is being hovered on.
     * @return
     */
    public boolean isHoverOn();
}
