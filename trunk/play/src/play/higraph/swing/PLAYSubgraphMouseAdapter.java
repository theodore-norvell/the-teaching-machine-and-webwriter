/**
 * PLAYSubgraphMouseAdapter.java - play.higraph.swing - PLAY
 * 
 * Created on Feb 16, 2012 by Kai Zhu
 */
package play.higraph.swing;

import higraph.swing.SubgraphMouseAdapter;

import javax.swing.JComponent;

import play.higraph.model.PLAYEdge;
import play.higraph.model.PLAYEdgeLabel;
import play.higraph.model.PLAYHigraph;
import play.higraph.model.PLAYNode;
import play.higraph.model.PLAYPayload;
import play.higraph.model.PLAYSubgraph;
import play.higraph.model.PLAYWholeGraph;
import play.higraph.view.PLAYHigraphView;
import play.higraph.view.PLAYSubgraphEventObserver;

/**
 * @author Kai Zhu
 * 
 */
public class PLAYSubgraphMouseAdapter
	extends
	SubgraphMouseAdapter<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> {

    /**
     * @param view
     * @param observer
     */
    public PLAYSubgraphMouseAdapter(PLAYHigraphView higraphView,
	    PLAYSubgraphEventObserver subgraphEventObserver) {
	super(higraphView, subgraphEventObserver);
    }

    /**
     * @see higraph.swing.SubgraphMouseAdapter#installIn(javax.swing.JComponent)
     */
    @Override
    public void installIn(JComponent jComponent) {
	super.installIn(jComponent);
    }

}
