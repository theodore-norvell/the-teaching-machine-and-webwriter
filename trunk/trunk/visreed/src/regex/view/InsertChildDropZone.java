/**
 * InsertChildDropZone.java
 * 
 * @date: Aug 7, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package regex.view;

import java.util.ArrayList;
import java.util.List;

import higraph.view.NodeView;
import regex.model.RegexEdge;
import regex.model.RegexEdgeLabel;
import regex.model.RegexHigraph;
import regex.model.RegexNode;
import regex.model.RegexPayload;
import regex.model.RegexSubgraph;
import regex.model.RegexTag;
import regex.model.RegexWholeGraph;
import regex.model.payload.SequencePayload;
import tm.backtrack.BTTimeManager;

/**
 * @author Xiaoyu Guo
 *
 */
public class InsertChildDropZone extends RegexDropZone {

    /**
     * @param nv
     * @param timeMan
     */
    public InsertChildDropZone(
            NodeView<RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge> nv,
            BTTimeManager timeMan) {
        super(nv, timeMan);
    }

    /**
     * @return
     */
    @Override
    public boolean isDropAccepted(){
        return true;
    }
    
    /* (non-Javadoc)
     * @see regex.view.RegexDropZone#handleDrop(regex.model.RegexNode[])
     */
    @Override
    public void handleDrop(List<RegexNode> nodes){
        if(nodes == null){
            return;
        }
        
        RegexWholeGraph wg = this.getHigraphView().getHigraph().getWholeGraph();
        
        RegexNode thisNode = ((RegexNodeView)this.getAssociatedComponent()).getNode();
        
        boolean parentIsSeq = false;
        if(thisNode.getPayload().getTag() == RegexTag.SEQUENCE){
            parentIsSeq = true;
        }
        
        int numChildren = nodes.size();
        // creates a temporary sequence and handle them all
        
        ArrayList<RegexNode> seqList = new ArrayList<RegexNode>();
        for(int i = 0; i < numChildren; i++){
            RegexNode child = nodes.get(i);
            if(child.getPayload().getTag() == RegexTag.SEQUENCE){
                if(parentIsSeq){
                    // SEQ -> SEQ
                    for(int j = 0; j < child.getNumberOfChildren(); j++){
                        seqList.add(child.getChild(j));
                    }
                } else {
                    // nonSEQ -> SEQ
                    seqList.add(child);
                }
            } else {
                if(parentIsSeq){
                    // SEQ -> nonSeq
                    seqList.add(child);
                } else {
                    // nonSeq -> nonSeq
                    RegexNode seq = wg.makeRootNode(new SequencePayload());
                    seq.appendChild(child);
                    seqList.add(seq);
                }
            }
        }
        // now inserts all the temporary nodes into the position
        int position = this.getNodeNumber();
        for(int i = 0; i < seqList.size(); i++){
            thisNode.insertChild(position + i, seqList.get(i));
        }
    }
}
