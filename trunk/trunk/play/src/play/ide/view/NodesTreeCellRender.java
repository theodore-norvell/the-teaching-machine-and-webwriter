/**
 * NodesTreeCellRender.java - play.ide.view - PLAY
 * 
 * Created on 2012-03-28 by Kai Zhu
 */
package play.ide.view;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * @author Kai Zhu
 * 
 */
public class NodesTreeCellRender extends DefaultTreeCellRenderer {

    private static final long serialVersionUID = 7193592669188711160L;

    /**
     * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree,
     *      java.lang.Object, boolean, boolean, boolean, int, boolean)
     */
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
	    boolean sel, boolean expanded, boolean leaf, int row,
	    boolean hasFocus) {
	// TODO
	return super.getTreeCellRendererComponent(tree, value, sel, expanded,
		leaf, row, hasFocus);
    }

}
