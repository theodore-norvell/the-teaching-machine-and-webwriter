/**
 * InsertChildDropZone.java
 * 
 * @date: Aug 7, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.view;

import higraph.view.NodeView;

import java.util.ArrayList;
import java.util.List;

import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;

import tm.backtrack.BTTimeManager;
import visreed.model.VisreedEdge;
import visreed.model.VisreedEdgeLabel;
import visreed.model.VisreedHigraph;
import visreed.model.VisreedNode;
import visreed.model.VisreedPayload;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedTag;
import visreed.model.VisreedTag.TagCategory;
import visreed.model.VisreedWholeGraph;

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
    	boolean result = true;
    	
    	// TODO consult tag for hint
//    	VisreedTag tag = this.getNodeView().getNode().getTag();
    	
        return result;
    }
    
    /* (non-Javadoc)
     * @see visreed.view.VisreedDropZone#handleDrop(visreed.model.RegexNode[])
     */
    @Override
    public void handleDrop(TransferSupport support, List<VisreedNode> nodes){
        if(nodes == null){
            return;
        }
        
        boolean isCopy = false;
        if(support != null){
        	isCopy = ((support.getDropAction() & TransferHandler.COPY) == TransferHandler.COPY);
        }
        
        VisreedWholeGraph wg = this.getHigraphView().getHigraph().getWholeGraph();
        wg.reduceSelection();
        
        VisreedNode thisNode = getNodeView().getNode();

        for(VisreedNode n : nodes){
        	// you can not drag a node into its children
        	if(n == thisNode || thisNode.isChildOf(n)){
        		nodes.remove(n);
        	}
        }
        // creates a temporary sequence and handle them all
        
        // all nodes which should be deleted
        ArrayList<VisreedNode> deleteList = new ArrayList<VisreedNode>();
        ArrayList<VisreedNode> reLayoutList = new ArrayList<VisreedNode>();
        
        for(int i = 0; i < nodes.size(); i++){
            VisreedNode srcNode = nodes.get(i);
            if(srcNode == null){
                continue;
            }
            
            // handle special cases
        	VisreedNode srcParent = srcNode.getParent();
            
        	if(srcParent != null){
	            if(srcParent.getTag().isCategory(TagCategory.SINGLE_SEQ_CHILD)){
	            	// dragging the SEQ outside of a SINGLE_SEQ_CHILD
	            	deleteList.addAll(srcNode.getChildren());
	            } else {
	            	if (srcParent.getTag().isCategory(TagCategory.ALT)){
		        		 // dragging a direct branch of ALT to elsewhere
		        		if(srcParent.getNumberOfChildren() > 2){
		        			// dragging a branch out
		        			deleteList.add(srcNode);
		        		} else {
		        			// dragging all children of a branch out, but the branch remains
		        			for(int j = 0; j < srcNode.getNumberOfChildren(); j++){
		        				deleteList.add(srcNode.getChild(j));
		        			}
		        		}
		            } else {
		            	deleteList.add(srcNode);
		            }
	            }
            	reLayoutList.add(srcParent);
        	}
        }
        // now inserts all the temporary nodes into the position
        int position = this.getNodeNumber();
        for(int i = 0; i < nodes.size(); i++){
        	// handle the situation of the node is already child of the target
            thisNode.insertChild(position + i, nodes.get(i).duplicate());
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
