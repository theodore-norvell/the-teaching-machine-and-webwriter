/**
 * PLAYTag.java - play.higraph.model - PLAY
 * 
 * Created on Feb 13, 2012 by Kai Zhu
 */
package play.higraph.model;

import higraph.model.taggedInterfaces.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kai Zhu
 * 
 */
public enum PLAYTag implements Tag<PLAYTag, PLAYPayload> {

    ASSIGN {

	@Override
	public boolean contentModel(List<PLAYTag> seq) {
	    // TODO Auto-generated method stub
	    return false;
	}

	@Override
	public List<PLAYTag> defaultTagSequence() {
	    // TODO Auto-generated method stub
	    return new ArrayList<PLAYTag>();
	}

	@Override
	public PLAYPayload defaultPayload() {
	    return new PLAYPayload("ASSIGN", PLAYTag.ASSIGN);
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
	    return "ASSIGN";
	}

    },
    SEQ {

	@Override
	public boolean contentModel(List<PLAYTag> seq) {
	    // TODO Auto-generated method stub
	    return true;
	}

	@Override
	public List<PLAYTag> defaultTagSequence() {
	    // TODO Auto-generated method stub
	    return new ArrayList<PLAYTag>();
	}

	@Override
	public PLAYPayload defaultPayload() {
	    return new PLAYPayload("SEQ", PLAYTag.SEQ);
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
	    return "SEQ";
	}

    }

}
