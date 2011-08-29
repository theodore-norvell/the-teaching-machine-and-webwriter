/**
 * PopupMenuHelper.java
 * 
 * @date: Aug 22, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.swing.menu;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

import visreed.model.VisreedNode;
import visreed.model.tag.VisreedTag;
import visreed.view.VisreedNodeView;

/**
 * PopupMenuHelper handles creation and 
 * @author Xiaoyu Guo
 */
public final class PopupMenuHelper {
    public static final JPopupMenu getPopupMenu(VisreedNodeView nv){
        if(nv == null || nv.getNode() == null){
            return null;
        }
        VisreedNode node = nv.getNode();
        JPopupMenu result = new JPopupMenu();
        
        // title
        JMenuItem title = new JMenuItem(nv.toString());
        title.setEnabled(false);
        result.add(title);
       
        result.addSeparator();
        
        // change type sub menu
        JMenu changeTypeMenuEntry = new JMenu("Change Type");
        
        for(VisreedTag t : VisreedTag.values()){
            ChangeTypeMenuItem menuItem = new ChangeTypeMenuItem(node, t);
            changeTypeMenuEntry.add(menuItem);
        }
        result.add(changeTypeMenuEntry);
        
        // surround sub menu
        JMenu surroundWithMenuEntry = new JMenu("Surround With");
        result.add(surroundWithMenuEntry);
        
        result.addSeparator();
                
        // C&P section
        JMenuItem cutMenuItem = new JMenuItem("Cut");
        cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        result.add(cutMenuItem);
        JMenuItem copyMenuItem = new JMenuItem("Copy");
        copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        result.add(copyMenuItem);
        JMenuItem pasteMenuItem = new JMenuItem("Paste");
        pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        result.add(pasteMenuItem);
        
        result.addSeparator();
        return result;
    }
}
