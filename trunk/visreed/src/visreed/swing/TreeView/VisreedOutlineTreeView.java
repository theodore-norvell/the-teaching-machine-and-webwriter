/**
 * OutlineTreeView.java
 * 
 * @date: Sep 19, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.swing.TreeView;

import javax.swing.JTree;
import javax.swing.tree.TreeSelectionModel;

import visreed.extension.javaCC.model.JavaCCWholeGraph;
import visreed.model.VisreedWholeGraph;

/**
 * @author Xiaoyu Guo
 *
 */
public class VisreedOutlineTreeView extends JTree{

	private static final long serialVersionUID = -1923805499406186616L;

	private JavaCCWholeGraph wg;
	
	private void setWholeGraph(VisreedWholeGraph wg){
		if(wg != null && wg instanceof JavaCCWholeGraph){
			this.wg = (JavaCCWholeGraph)wg;
		}
	}
	
	public VisreedOutlineTreeView(VisreedWholeGraph wg) {
		super(new VisreedOutlineTreeWrapper((JavaCCWholeGraph) wg));
		setWholeGraph(wg);
		
		getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);

        this.setAutoscrolls(true);
        this.setDragEnabled(true);
	}

}
