/**
 * IInteractable.java
 * 
 * @date: Aug 7, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.swing;

import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JPopupMenu;

import visreed.model.VisreedNode;

/**
 * IInteractable describes the interactable controls
 * @author Xiaoyu Guo
 */
public interface IInteractable {
    /**
     * Gets whether a drop operation is accepted by this zone.
     * @return <code>true</code> if the view accepts drop, <code>false</code> otherwise.
     */
    boolean isDropAccepted();
    
    /**
     * Handles drop operation
     * @param e The mouse event
     * @param nodes the list containing the transferring data
     */
    void handleDrop(MouseEvent e, List<VisreedNode> nodes);
    
    /**
     * Handles mouse click 
     */
    void handleClick(MouseEvent e);
    
    /**
     * Handles mouse double click
     */
    void handleDoubleClick(MouseEvent e);
    
    /**
     * Gets a pop up menu according to the mouse event
     * @param e
     * @return
     */
    JPopupMenu getPopupMenu(MouseEvent e);

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
