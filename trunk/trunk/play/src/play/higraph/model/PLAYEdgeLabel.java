/**
 * PLAYEdgeLabel.java - play.higraph.model - PLAY
 * 
 * Created on Feb 13, 2012 by Kai Zhu
 */
package play.higraph.model;

import higraph.model.interfaces.Payload;

/**
 * @author Kai Zhu
 * 
 */
public class PLAYEdgeLabel implements Payload<PLAYEdgeLabel> {

    private final String edgeLabel;

    public PLAYEdgeLabel(String edgeLabel) {
	this.edgeLabel = edgeLabel;
    }

    /**
     * @see higraph.model.interfaces.Payload#copy()
     */
    @Override
    public PLAYEdgeLabel copy() {
	return this;
    }

    @Override
    public String toString() {
	return this.edgeLabel;
    }

}
