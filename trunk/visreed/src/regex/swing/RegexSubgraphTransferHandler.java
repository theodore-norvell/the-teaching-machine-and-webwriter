/**
 * RegexSubgraphTransferHandler.java
 * 
 * @date: Jul 26, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package regex.swing;

import higraph.swing.SubgraphTransferHandler;

import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import regex.awt.RegexSubgraphMouseAdapter;
import regex.model.RegexEdge;
import regex.model.RegexEdgeLabel;
import regex.model.RegexHigraph;
import regex.model.RegexNode;
import regex.model.RegexPayload;
import regex.model.RegexSubgraph;
import regex.model.RegexWholeGraph;

/**
 * The transfer handler is associated with {@link HigraphView}s.
 * @author Xiaoyu Guo
 */
public class RegexSubgraphTransferHandler
extends SubgraphTransferHandler<RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge> 
{

    private static final long serialVersionUID = 6051659623878408438L;

    /**
     * 
     */
    public RegexSubgraphTransferHandler(
        RegexSubgraphMouseAdapter adapter
    ) {
        super(adapter);
    }
    
    @Override
    public boolean canImport(TransferHandler.TransferSupport support){
        return super.canImport(support);
    }

    @Override
    public int getSourceActions(JComponent comp) {
        if(comp instanceof RegexJList){
            return COPY;
        } else {
            return super.getSourceActions(comp);
        }
    }
    
    @Override
    protected Transferable createTransferable(JComponent container){
        if(container instanceof RegexJComponent){
            // TODO
            return new RegexNodeTransferObject(null);
        } else {
            return super.createTransferable(container);
        }
    }
}
