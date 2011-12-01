/**
 * VisreedNodeToolBar.java
 * 
 * @date: Nov 30, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.swing.nodebar;

import java.awt.SystemColor;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

import visreed.model.VisreedNode;
import visreed.model.VisreedWholeGraph;

/**
 * The base class for draggable tool bar
 * @author Xiaoyu Guo
 */
public abstract class VisreedNodeToolBar extends JList {
	private static final long serialVersionUID = 5925912462308877966L;
	
	protected VisreedWholeGraph wholeGraph;

	private static final DefaultListModel getDefaultDataModel(VisreedNodeToolBarIconData[] data) {
	    DefaultListModel model = new DefaultListModel();
	
	    for(int i = 0; i < data.length; i++){
	        model.add(0, data[i]);
	    }
	    return model;
	}

	/**
	 * Constructor
	 */
	public VisreedNodeToolBar(VisreedWholeGraph wg, VisreedNodeToolBarIconData[] data) {
		super(getDefaultDataModel(data));
		this.wholeGraph = wg;
        
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.setAutoscrolls(true);
        this.setDragEnabled(true);
        this.setLayoutOrientation(JList.VERTICAL);
        
        this.setBackground(SystemColor.control);
        this.setBorder(BorderFactory.createEmptyBorder());
        
        this.setCellRenderer(new VisreedNodeCellRenderer(wg));
        
        this.setTransferHandler(new VisreedNodeToolBarTransferHandler());
	}

	/**
	 * Creates a new {@link VisreedNode} object, using the selected tag.
	 * @return a new {@link VisreedNode} or <code>null</code> if nothing is selected.
	 */
	public VisreedNode createSelectedNode() {
        VisreedNodeToolBarIconData selectedData = (VisreedNodeToolBarIconData) this.getSelectedValue();
        if(selectedData == null){
            return null;
        }
        VisreedNode node = this.wholeGraph.makeRootNode(selectedData.getTag());

        return node;
    }
}