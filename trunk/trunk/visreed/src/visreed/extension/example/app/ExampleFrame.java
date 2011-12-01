/**
 * ExampleFrame.java
 * 
 * @date: Nov 30, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.example.app;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import visreed.app.VisreedSimpleFrame;
import visreed.extension.example.swing.nodebar.ExampleNodeToolBar;
import visreed.extension.example.tag.ExampleTag;
import visreed.swing.MarqueeSelectionSGEventObserver;
import visreed.swing.SwingHelper;
import visreed.swing.VisreedSubgraphEventObserver;

/**
 * @author Xiaoyu Guo
 *
 */
public class ExampleFrame extends VisreedSimpleFrame {
	private static final long serialVersionUID = 4306175134348943714L;

    protected void initializeNodeToolBar(){
        this.nodeListBar = new ExampleNodeToolBar(wholeGraph);
    }

    protected void fillTestToolBar(JToolBar toolBar) {
    	toolBar.add(new AbstractAction("Sequence"){
			private static final long serialVersionUID = -3548912229532819747L;

			@Override
			public void actionPerformed(ActionEvent e) {
				rootSubgraph.clear();
				
				rootSubgraph.addTop(wholeGraph.makeRootNode(ExampleTag.SEQUENCE));
				refreshGraph();
			}});
    }
    
    protected VisreedSubgraphEventObserver getSubgraphObserver(){
    	return new MarqueeSelectionSGEventObserver(this, wholeGraph);
    }
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
        SwingHelper.setSystemLookAndFeel();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	ExampleFrame frame = new ExampleFrame();
                frame.setVisible(true);
            }
        });
	}

}
