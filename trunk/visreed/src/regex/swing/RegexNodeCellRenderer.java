/**
 * RegexNodeCellRenderer.java
 * 
 * @date: Jul 26, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package regex.swing;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

import regex.model.RegexWholeGraph;

/**
 * @author Xiaoyu Guo
 *
 */
public class RegexNodeCellRenderer implements ListCellRenderer {
    private RegexWholeGraph wholeGraph;
    
    public RegexNodeCellRenderer(RegexWholeGraph wg){
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
        RegexNodeButton result = null;
        
        if(value instanceof RegexIconData){
            RegexIconData data = (RegexIconData)value;
            result = new RegexNodeButton(this.wholeGraph, data.getTag());
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
            result = new RegexNodeButton();
        }
        
        result.setPreferredSize(new Dimension(48, 48));
        
        return result;
    }

}
