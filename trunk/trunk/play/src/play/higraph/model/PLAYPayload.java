/**
 * PLAYPayload.java - play.higraph.model - PLAY
 * 
 * Created on Feb 13, 2012 by Kai Zhu
 */
package play.higraph.model;

import higraph.model.taggedInterfaces.TaggedPayload;

/**
 * @author Kai Zhu
 * 
 */
public class PLAYPayload implements TaggedPayload<PLAYTag, PLAYPayload> {

    private String name;

    private final PLAYTag tag;

    public PLAYPayload(String name, PLAYTag tag) {
	this.name = name;
	this.tag = tag;
    }

    /**
     * @see higraph.model.interfaces.Payload#copy()
     */
    @Override
    public PLAYPayload copy() {
	return this;
    }

    /**
     * @see higraph.model.taggedInterfaces.TaggedPayload#getTag()
     */
    @Override
    public PLAYTag getTag() {
	return this.tag;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return this.name;
    }

}
