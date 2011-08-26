/**
 * IInteractable.java
 * 
 * @date: Aug 7, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package regex.swing;

import java.awt.event.MouseEvent;
import java.util.List;

import regex.model.RegexNode;

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
     * @param nodes the list containing the transferring data
     */
    void handleDrop(List<RegexNode> nodes);
    
    /**
     * Handles mouse click 
     */
    void handleClick(MouseEvent e);
    
    /**
     * Handles mouse double click
     */
    void handleDoubleClick(MouseEvent e);
}
