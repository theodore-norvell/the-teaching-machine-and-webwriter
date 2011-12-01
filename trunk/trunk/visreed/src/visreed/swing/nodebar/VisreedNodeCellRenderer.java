/**
 * RegexNodeCellRenderer.java
 * 
 * @date: Jul 26, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.swing.nodebar;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

import visreed.model.VisreedWholeGraph;
import visreed.swing.SwingHelper;

/**
 * @author Xiaoyu Guo
 *
 */
public class VisreedNodeCellRenderer implements ListCellRenderer {
    private VisreedWholeGraph wholeGraph;
    
    public VisreedNodeCellRenderer(VisreedWholeGraph wg){
        this.wholeGraph = wg;
    }
    
    /* (non-Javadoc)
     * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
     */
    @Override
    public Component getListCellRendererComponent(
        JList list, 
        Object value, 
        int index, 
        boolean isSelected,
        boolean cellHasFocus
    ) {
        VisreedNodeButton result = null;
        
        if(value instanceof VisreedNodeToolBarIconData){
            VisreedNodeToolBarIconData data = (VisreedNodeToolBarIconData)value;
            result = new VisreedNodeButton(this.wholeGraph, data.getTag());
            result.setToolTipText(data.getDescription());
            result.setIcon(SwingHelper.loadIcon(data.getIconFileName()));
            
            // check if this cell represents the current DnD drop location
            JList.DropLocation dropLocation = list.getDropLocation();
            if (dropLocation != null
                && !dropLocation.isInsert()
                && dropLocation.getIndex() == index) {
                // you can not drop on this, so do nothing here
            // check if this cell is selected
            } else if (isSelected) {
                result.setSelected(true);
            } else if (cellHasFocus){
                result.setSelected(true);
            }else {
                // unselected, and not the DnD drop location
            };
            
        } else {
            result = new VisreedNodeButton();
        }
        
        result.setPreferredSize(new Dimension(48, 48));
        
        return result;
    }

}
