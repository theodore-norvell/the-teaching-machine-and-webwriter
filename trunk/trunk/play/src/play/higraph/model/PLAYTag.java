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

    PLACEHOLDER {

	@Override
	public boolean contentModel(List<PLAYTag> seq) {
	    if (!seq.isEmpty()) {
		if ((seq.size() == 1)
			&& ((seq.indexOf(PLAYTag.EXP) == 0) || (seq
				.indexOf(PLAYTag.PLACEHOLDER) == 0))) {
		    return true;
		}
	    }
	    return false;
	}

	@Override
	public List<PLAYTag> defaultTagSequence() {
	    return new ArrayList<PLAYTag>();
	}

	@Override
	public PLAYPayload defaultPayload() {
	    return new PLAYPayload("PLACEHOLDER", PLAYTag.PLACEHOLDER);
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
	    return "PLACEHOLDER";
	}

    },
    TYPE {

	@Override
	public boolean contentModel(List<PLAYTag> seq) {
	    if (!seq.isEmpty()) {
		if (seq.indexOf(PLAYTag.TYPE) == 0)
		    return true;
	    }
	    return false;
	}

	@Override
	public List<PLAYTag> defaultTagSequence() {
	    return new ArrayList<PLAYTag>();
	}

	@Override
	public PLAYPayload defaultPayload() {
	    return new PLAYPayload("TYPE", PLAYTag.TYPE);
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
	    return "TYPE";
	}

    },
    VARDECL {

	@Override
	public boolean contentModel(List<PLAYTag> seq) {
	    if (seq.isEmpty()) {
		if ((seq.size() == 1)
			&& (seq.contains(PLAYTag.TYPE) || seq
				.contains(PLAYTag.SEQ))) {
		    return true;
		} else if ((seq.size() == 2)
			&& (seq.indexOf(PLAYTag.TYPE) == 0)) {
		    return true;
		}
	    }
	    return false;
	}

	@Override
	public List<PLAYTag> defaultTagSequence() {
	    return new ArrayList<PLAYTag>();
	}

	@Override
	public PLAYPayload defaultPayload() {
	    return new PLAYPayload("VARDECL", PLAYTag.VARDECL);
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
	    return "VARDECL";
	}

    },
    ASSIGN {

	@Override
	public boolean contentModel(List<PLAYTag> seq) {
	    if (!seq.isEmpty()) {
		if ((seq.size() == 1) && (seq.indexOf(PLAYTag.EXP) == 0)) {
		    return true;
		} else if ((seq.size() == 2) && (seq.indexOf(PLAYTag.EXP) == 0)
			&& (seq.lastIndexOf(PLAYTag.EXP) == 1)) {
		    return true;
		}
	    }
	    return false;
	}

	@Override
	public List<PLAYTag> defaultTagSequence() {
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
    IF {

	@Override
	public boolean contentModel(List<PLAYTag> seq) {
	    if (!seq.isEmpty()) {
		if ((seq.size() == 1)
			&& (seq.indexOf(PLAYTag.PLACEHOLDER) == 0)) {
		    return true;
		} else if ((seq.size() == 2)
			&& (seq.indexOf(PLAYTag.PLACEHOLDER) == 0)
			&& (seq.indexOf(PLAYTag.SEQ) == 1)) {
		    return true;
		} else if ((seq.size() == 3)
			&& (seq.indexOf(PLAYTag.PLACEHOLDER) == 0)
			&& (seq.indexOf(PLAYTag.SEQ) == 1)
			&& (seq.lastIndexOf(PLAYTag.SEQ) == 2)) {
		    return true;
		}
	    }
	    return false;
	}

	@Override
	public List<PLAYTag> defaultTagSequence() {
	    return new ArrayList<PLAYTag>();
	}

	@Override
	public PLAYPayload defaultPayload() {
	    return new PLAYPayload("IF", PLAYTag.IF);
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
	    return "IF";
	}

    },
    WHILE {

	@Override
	public boolean contentModel(List<PLAYTag> seq) {
	    if (!seq.isEmpty()) {
		if ((seq.size() == 1)
			&& (seq.indexOf(PLAYTag.PLACEHOLDER) == 0)) {
		    return true;
		} else if ((seq.size() == 2)
			&& (seq.indexOf(PLAYTag.PLACEHOLDER) == 0)
			&& (seq.indexOf(PLAYTag.SEQ) == 1)) {
		    return true;
		} else if ((seq.size() == 3)
			&& (seq.indexOf(PLAYTag.PLACEHOLDER) == 0)
			&& (seq.indexOf(PLAYTag.SEQ) == 1)
			&& (seq.lastIndexOf(PLAYTag.SEQ) == 2)) {
		    return true;
		}
	    }
	    return false;
	}

	@Override
	public List<PLAYTag> defaultTagSequence() {
	    return new ArrayList<PLAYTag>();
	}

	@Override
	public PLAYPayload defaultPayload() {
	    return new PLAYPayload("WHILE", PLAYTag.WHILE);
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
	    return "WHILE";
	}

    },
    EXP {

	@Override
	public boolean contentModel(List<PLAYTag> seq) {
	    return false;
	}

	@Override
	public List<PLAYTag> defaultTagSequence() {
	    return new ArrayList<PLAYTag>();
	}

	@Override
	public PLAYPayload defaultPayload() {
	    return new PLAYPayload("EXP", PLAYTag.EXP);
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
	    return "EXP";
	}

    },
    SEQ {

	@Override
	public boolean contentModel(List<PLAYTag> seq) {
	    return true;
	}

	@Override
	public List<PLAYTag> defaultTagSequence() {
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

    },
    CLASS {

	@Override
	public boolean contentModel(List<PLAYTag> seq) {
	    if (!seq.isEmpty()) {
		if (seq.contains(PLAYTag.VARDECL) || seq.contains(PLAYTag.SEQ)) {
		    return true;
		}
	    }
	    return false;
	}

	@Override
	public List<PLAYTag> defaultTagSequence() {
	    return new ArrayList<PLAYTag>();
	}

	@Override
	public PLAYPayload defaultPayload() {
	    return new PLAYPayload("CLASS", PLAYTag.CLASS);
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
	    return "CLASS";
	}

    },
    FILE {

	@Override
	public boolean contentModel(List<PLAYTag> seq) {
	    if (!seq.isEmpty()) {
		if (seq.contains(PLAYTag.CLASS)) {
		    return true;
		}
	    }
	    return false;
	}

	@Override
	public List<PLAYTag> defaultTagSequence() {
	    return new ArrayList<PLAYTag>();
	}

	@Override
	public PLAYPayload defaultPayload() {
	    return new PLAYPayload("FILE", PLAYTag.FILE);
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
	    return "FILE";
	}

    };

}
