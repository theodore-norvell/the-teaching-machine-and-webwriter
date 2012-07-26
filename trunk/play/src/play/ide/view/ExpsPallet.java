/**
 * ExpsPallet.java - play.ide.view - PLAY
 * 
 * Created on 2012-03-17 by Kai Zhu
 */
package play.ide.view;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import play.higraph.model.PLAYTag;
import play.higraph.swing.SyntaxTransferHandler;

/**
 * @author Kai Zhu
 * 
 */
public class ExpsPallet extends JPanel {

    private static final long serialVersionUID = -7045377348761624238L;

    private JTree expTree;

    private DefaultTreeModel expTreeModel;

    public ExpsPallet() {
	DefaultMutableTreeNode expsRootTreeNode = new DefaultMutableTreeNode(
		"Expressions");
	this.expTreeModel = new DefaultTreeModel(expsRootTreeNode);
	this.expTree = new JTree(this.expTreeModel);

	expsRootTreeNode.add(new DefaultMutableTreeNode(PLAYTag.PLUS));
	expsRootTreeNode.add(new DefaultMutableTreeNode(PLAYTag.MINUS));
	expsRootTreeNode
		.add(new DefaultMutableTreeNode(PLAYTag.MULTIPLICATION));
	expsRootTreeNode.add(new DefaultMutableTreeNode(PLAYTag.DIVISION));
	expsRootTreeNode.add(new DefaultMutableTreeNode(PLAYTag.NUMBER));
	expsRootTreeNode.add(new DefaultMutableTreeNode(PLAYTag.BOOLEAN));
	expsRootTreeNode.add(new DefaultMutableTreeNode(PLAYTag.STRING));
	expsRootTreeNode.add(new DefaultMutableTreeNode(PLAYTag.NULL));
	expsRootTreeNode.add(new DefaultMutableTreeNode(PLAYTag.VAR));
	expsRootTreeNode.add(new DefaultMutableTreeNode(PLAYTag.DOT));

	this.expTree.getSelectionModel().setSelectionMode(
		TreeSelectionModel.SINGLE_TREE_SELECTION);
	this.expTree.setPreferredSize(new Dimension(150, 150));
	this.expTree.setShowsRootHandles(true);
	this.expTree.setAutoscrolls(true);
	this.expTree.setDragEnabled(true);
	this.expTree.setSelectionRow(1);
	this.expTree.expandRow(0);
	this.expTree.setTransferHandler(new SyntaxTransferHandler());

	this.add(new JScrollPane(this.expTree));
    }

}
