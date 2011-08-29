/**
 * ChangeTypeMenuItem.java
 * 
 * @date: Aug 22, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.swing.menu;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import visreed.model.VisreedNode;
import visreed.model.tag.VisreedTag;

/**
 * @author Xiaoyu Guo
 *
 */
public class ChangeTypeMenuItem extends VisreedNodeMenuItem {
    private static final long serialVersionUID = 5182463195106861499L;
    protected VisreedTag newType;
    
    private class ChangeTypeAction extends AbstractAction{
        private static final long serialVersionUID = -7611366621788120824L;
        VisreedNode node;
        VisreedTag newType;
        ChangeTypeAction(VisreedNode node, VisreedTag newType){
            this.node = node;
            this.newType = newType;
        }
        
        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if(this.node != null && this.node.getPayload() != null && this.node.getPayload().getTag() != this.newType){
                // TODO change the node type
            }
        }
        
    }
    
    /**
     * @param text
     * @param icon
     * @param node
     */
    public ChangeTypeMenuItem(
        VisreedNode node,
        VisreedTag newType
    ) {
        super(newType.getDescription(), node);
        this.newType = newType;
        this.setAction(new ChangeTypeAction(this.node, this.newType));
    }
    
    
}
