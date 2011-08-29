/**
 * VisreedNodeMenuItem.java
 * 
 * @date: Aug 22, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.swing.menu;

import javax.swing.Icon;
import javax.swing.JMenuItem;

import visreed.model.VisreedNode;

/**
 * @author Xiaoyu Guo
 *
 */
public abstract class VisreedNodeMenuItem extends JMenuItem {
    private static final long serialVersionUID = -3666150562158299838L;
    
    protected VisreedNode node;

    /**
     * @param text
     * @param icon
     */
    public VisreedNodeMenuItem(
        String text,
        Icon icon,
        VisreedNode node
    ) {
        super(text, icon);
        this.node = node;
    }
    
    /**
     * @param text
     */
    public VisreedNodeMenuItem(
        String text,
        VisreedNode node
    ) {
        super(text);
        this.node = node;
    }
}
