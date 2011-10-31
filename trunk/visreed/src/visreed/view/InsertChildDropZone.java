/**
 * InsertChildDropZone.java
 * 
 * @date: Aug 7, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.view;

import higraph.view.NodeView;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import tm.backtrack.BTTimeManager;
import visreed.model.VisreedEdge;
import visreed.model.VisreedEdgeLabel;
import visreed.model.VisreedHigraph;
import visreed.model.VisreedNode;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.model.payload.SequencePayload;
import visreed.model.payload.VisreedPayload;
import visreed.model.tag.VisreedTag;

/**
 * @author Xiaoyu Guo
 *
 */
public class InsertChildDropZone extends VisreedDropZone {

    /**
     * @param nv
     * @param timeMan
     */
    public InsertChildDropZone(
            NodeView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> nv,
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
     * @see visreed.view.VisreedDropZone#handleDrop(visreed.model.RegexNode[])
     */
    @Override
    public void handleDrop(MouseEvent e, List<VisreedNode> nodes){
        if(nodes == null){
            return;
        }
        
        boolean isCopy = false;
        /*/
        isCopy = true;
        /*/
        isCopy = (e != null && ((e.getModifiers() & MouseEvent.CTRL_DOWN_MASK) != 0));
        //*/
        
        VisreedWholeGraph wg = this.getHigraphView().getHigraph().getWholeGraph();
        wg.reduceSelection();
        
        VisreedNode thisNode = ((VisreedNodeView)this.getAssociatedComponent()).getNode();

        for(VisreedNode n : nodes){
        	// you can not drag a node into its children
        	if(n == thisNode || thisNode.isChildOf(n)){
        		nodes.remove(n);
        	}
        }
        boolean targetIsSeq = false;
        if(thisNode.getTag().equals(VisreedTag.SEQUENCE)){
            targetIsSeq = true;
        }
        
        // creates a temporary sequence and handle them all
        
        // all nodes in seq form
        ArrayList<VisreedNode> seqList = new ArrayList<VisreedNode>();
        // all nodes which should be deleted
        ArrayList<VisreedNode> deleteList = new ArrayList<VisreedNode>();
        ArrayList<VisreedNode> reLayoutList = new ArrayList<VisreedNode>();
        
        for(int i = 0; i < nodes.size(); i++){
            VisreedNode child = nodes.get(i);
            if(child == null){
                continue;
            }
        	VisreedNode childParent = child.getParent();
            if(child.getTag().equals(VisreedTag.SEQUENCE)){
                if(targetIsSeq){
                    // SEQ -> SEQ
                    for(int j = 0; j < child.getNumberOfChildren(); j++){
                        seqList.add(child.getChild(j).duplicate());
                    }
                    // deletion
                    if(child.getTag().canHoldExactOneChild()){
                    	// dragging the content out of a RepeatRange, preserve the 
                    	// original SEQ inside the RepeatRange
                    	deleteList.add(child.getChild(0));
                    } else if (childParent != null){
	                	if(childParent.getTag().equals(VisreedTag.ALTERNATION)){
	                		 // dragging a direct branch of ALT to elsewhere
	                		if(childParent.getNumberOfChildren() > 2){
	                			// dragging a branch out
	                			deleteList.add(child);
	                		} else {
	                			// dragging all children of a branch out, but the branch remains
	                			for(int j = 0; j < child.getNumberOfChildren(); j++){
	                				deleteList.add(child.getChild(j));
	                			}
	                		}
	                	}
                    } else {
                    	deleteList.add(child);
                    }
                } else {
                    // nonSEQ -> SEQ
                    seqList.add(child.duplicate());
                    deleteList.add(child);
                }
            } else {
                if(targetIsSeq){
                    // SEQ -> nonSeq
                    seqList.add(child.duplicate());
                    deleteList.add(child);
                } else {
                    // nonSeq -> nonSeq
                    VisreedNode seq = wg.makeRootNode(new SequencePayload());
                    seq.appendChild(child.duplicate());
                    seqList.add(seq);
                    deleteList.add(child);
                }
            }
            if(childParent != null){
            	reLayoutList.add(childParent);
            }
        }
        // now inserts all the temporary nodes into the position
        int position = this.getNodeNumber();
        for(int i = 0; i < seqList.size(); i++){
        	// handle the situation of the node is already child of the target
            thisNode.insertChild(position + i, seqList.get(i));
        }
        
        if(!isCopy){
        	// remove all the listed nodes
	        for(int i = 0; i < deleteList.size(); i++){
	        	VisreedNode parent = deleteList.get(i).getParent();
	        	deleteList.get(i).delete();
	        	if(parent != null){
	        		parent.notifyObservers();
	        	}
	        }
        }
        
        // re-layout
        for(VisreedNode n : reLayoutList){
        	n.notifyObservers();
        }
        this.getHigraphView().refresh();
    }
}
