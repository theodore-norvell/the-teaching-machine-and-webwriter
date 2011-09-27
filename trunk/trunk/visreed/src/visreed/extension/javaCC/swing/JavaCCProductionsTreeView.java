/**
 * JavaCCProductionsTreeView.java
 * 
 * @date: Sep 26, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.swing;

import javax.swing.JTree;
import javax.swing.tree.TreeSelectionModel;

import visreed.extension.javaCC.model.JavaCCWholeGraph;
import visreed.model.VisreedWholeGraph;

/**
 * @author Xiaoyu Guo
 *
 */
public class JavaCCProductionsTreeView extends JTree{

	private static final long serialVersionUID = 1165137759180775119L;
	
	private JavaCCWholeGraph wg;
	
	private void setWholeGraph(VisreedWholeGraph wg){
		if(wg != null && wg instanceof JavaCCWholeGraph){
			this.wg = (JavaCCWholeGraph)wg;
		}
	}
	
	public JavaCCProductionsTreeView(VisreedWholeGraph wg) {
		super(new JavaCCProductionTreeWrapper((JavaCCWholeGraph) wg));
		setWholeGraph(wg);
		
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	}
}