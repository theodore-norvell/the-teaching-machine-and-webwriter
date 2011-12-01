/**
 * VisreedWholegraphNavigator.java
 * 
 * @date: Nov 21, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.swing.navigator;

import java.awt.Component;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import visreed.model.VisreedHigraph;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.pattern.IObserver;
import visreed.view.IGraphContainer;

/**
 * VisreedWholeGraphNavigator is a navigator for all subgraphs in a whole graph.
 * @author Xiaoyu Guo
 */
public class VisreedWholeGraphNavigator
extends JComboBox  {
	private static final long serialVersionUID = -4074996722792557898L;
	
	/**
	 * Constructor
	 * @param wholeGraph2 
	 * @param visreedSimpleFrame
	 */
	public VisreedWholeGraphNavigator(
		IGraphContainer graphContainer, VisreedWholeGraph wholeGraph
	) {
		super();
		this.setModel(new VisreedWholeGraphNavigatorModel(graphContainer, wholeGraph));
		this.setRenderer(new VisreedWholeGraphNavigatorRenderer());
	}

	protected class VisreedWholeGraphNavigatorModel
	extends DefaultComboBoxModel 
	implements IObserver<VisreedHigraph> {
		private static final long serialVersionUID = -6356909645436772735L;

		private IGraphContainer graphContainer;
		private VisreedWholeGraph wholeGraph;
		
		private List<VisreedSubgraph> list;

		/**
		 * @param wholeGraph
		 */
		public VisreedWholeGraphNavigatorModel(
			IGraphContainer graphContainer, VisreedWholeGraph wholeGraph
		) {
			super();
			
			this.wholeGraph = wholeGraph;
			this.graphContainer = graphContainer;
			wholeGraph.registerObserver(this);
		}

		@Override
	    public int getSize() {
			if(list == null){
				return 0;
			}
	        return list.size();
	    }

	    @Override
	    public Object getElementAt(int index) {
	        if (list != null && index >= 0 && index < list.size() ){
	            return list.get(index);
	        }
            return null;
	    }
	    
	    @Override
	    public Object getSelectedItem() {
	        return this.graphContainer.getCurrentSubgraph();
	    }
		
		/* (non-Javadoc)
		 * @see visreed.pattern.IObserver#changed(visreed.pattern.IObservable)
		 */
		@Override
		public void changed(VisreedHigraph object) {
			this.list = this.wholeGraph.getAllSubgraph();
			this.fireContentsChanged(this, 0, list.size());
		}
	}

	protected class VisreedWholeGraphNavigatorRenderer extends BasicComboBoxRenderer{
		private static final long serialVersionUID = 6562208158450625154L;
		
		/* (non-Javadoc)
		 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
		 */
		@Override
		public Component getListCellRendererComponent(
			JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus
		) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			if(value instanceof VisreedSubgraph){
				String text = ((VisreedSubgraph)value).getName();
				setText(text);
			}
			
			return this;
		}
	}
}
