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
			&& ((seq.indexOf(PLAYTag.EXP) == 0)
				|| (seq.indexOf(PLAYTag.NUMBER) == 0)
				|| (seq.indexOf(PLAYTag.BOOLEAN) == 0)
				|| (seq.indexOf(PLAYTag.STRING) == 0)
				|| (seq.indexOf(PLAYTag.NULL) == 0)
				|| (seq.indexOf(PLAYTag.VAR) == 0)
				|| (seq.indexOf(PLAYTag.DOT) == 0) || (seq
				.indexOf(PLAYTag.PLACEHOLDER) == 0))) {
		    return true;
		}
	    }
	    return false;
	}

	@Override
	public List<PLAYTag> defaultTagSequence() {
	    List<PLAYTag> defaultTagSequence = new ArrayList<PLAYTag>();
	    return defaultTagSequence;
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
		if ((seq.indexOf(PLAYTag.PLACEHOLDER) == 0)
			|| (seq.indexOf(PLAYTag.EXP) == 0)
			|| (seq.indexOf(PLAYTag.NUMBER) == 0)
			|| (seq.indexOf(PLAYTag.BOOLEAN) == 0)
			|| (seq.indexOf(PLAYTag.STRING) == 0)
			|| (seq.indexOf(PLAYTag.NULL) == 0)
			|| (seq.indexOf(PLAYTag.VAR) == 0)
			|| (seq.indexOf(PLAYTag.DOT) == 0)) {
		    if (seq.size() == 1) {
			return true;
		    } else if ((seq.size() == 2)
			    && ((seq.lastIndexOf(PLAYTag.PLACEHOLDER) == 1)
				    || (seq.lastIndexOf(PLAYTag.EXP) == 1)
				    || (seq.lastIndexOf(PLAYTag.NUMBER) == 1)
				    || (seq.lastIndexOf(PLAYTag.BOOLEAN) == 1)
				    || (seq.lastIndexOf(PLAYTag.STRING) == 1)
				    || (seq.lastIndexOf(PLAYTag.NULL) == 1)
				    || (seq.lastIndexOf(PLAYTag.VAR) == 1) || (seq
				    .lastIndexOf(PLAYTag.DOT) == 1))) {
			return true;
		    }
		}
	    }
	    return false;
	}

	@Override
	public List<PLAYTag> defaultTagSequence() {
	    List<PLAYTag> defaultTagSequence = new ArrayList<PLAYTag>();
	    defaultTagSequence.add(PLAYTag.PLACEHOLDER);
	    defaultTagSequence.add(PLAYTag.PLACEHOLDER);
	    return defaultTagSequence;
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
		if ((seq.indexOf(PLAYTag.EXP) == 0)
			|| (seq.indexOf(PLAYTag.NUMBER) == 0)
			|| (seq.indexOf(PLAYTag.BOOLEAN) == 0)
			|| (seq.indexOf(PLAYTag.STRING) == 0)
			|| (seq.indexOf(PLAYTag.NULL) == 0)
			|| (seq.indexOf(PLAYTag.VAR) == 0)
			|| (seq.indexOf(PLAYTag.DOT) == 0)
			|| (seq.indexOf(PLAYTag.PLACEHOLDER) == 0)) {
		    if (seq.size() == 1) {
			return true;
		    } else if ((seq.size() == 2)
			    && (seq.indexOf(PLAYTag.SEQ) == 1)) {
			return true;
		    } else if ((seq.size() == 3)
			    && (seq.indexOf(PLAYTag.SEQ) == 1)
			    && (seq.lastIndexOf(PLAYTag.SEQ) == 2)) {
			return true;
		    }
		}
	    }
	    return false;
	}

	@Override
	public List<PLAYTag> defaultTagSequence() {
	    ArrayList<PLAYTag> defaultTagSequence = new ArrayList<PLAYTag>();
	    defaultTagSequence.add(PLAYTag.PLACEHOLDER);
	    defaultTagSequence.add(PLAYTag.SEQ);
	    defaultTagSequence.add(PLAYTag.SEQ);
	    return defaultTagSequence;
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
		if ((seq.indexOf(PLAYTag.EXP) == 0)
			|| (seq.indexOf(PLAYTag.NUMBER) == 0)
			|| (seq.indexOf(PLAYTag.BOOLEAN) == 0)
			|| (seq.indexOf(PLAYTag.STRING) == 0)
			|| (seq.indexOf(PLAYTag.NULL) == 0)
			|| (seq.indexOf(PLAYTag.VAR) == 0)
			|| (seq.indexOf(PLAYTag.DOT) == 0)
			|| (seq.indexOf(PLAYTag.PLACEHOLDER) == 0)) {
		    if (seq.size() == 1) {
			return true;
		    } else if ((seq.size() == 2)
			    && (seq.indexOf(PLAYTag.SEQ) == 1)) {
			return true;
		    } else if ((seq.size() == 3)
			    && (seq.indexOf(PLAYTag.SEQ) == 1)
			    && (seq.lastIndexOf(PLAYTag.SEQ) == 2)) {
			return true;
		    }
		}
	    }
	    return false;
	}

	@Override
	public List<PLAYTag> defaultTagSequence() {
	    List<PLAYTag> defaultTagSequence = new ArrayList<PLAYTag>();
	    defaultTagSequence.add(PLAYTag.PLACEHOLDER);
	    defaultTagSequence.add(PLAYTag.SEQ);
	    defaultTagSequence.add(PLAYTag.SEQ);
	    return defaultTagSequence;
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
	    if (!seq.isEmpty()) {
		if ((seq.size() == 1)
			&& (seq.contains(PLAYTag.PLACEHOLDER) || seq
				.contains(PLAYTag.NUMBER))) {
		    return true;
		}
	    }
	    return false;
	}

	@Override
	public List<PLAYTag> defaultTagSequence() {
	    List<PLAYTag> defaultTagSequence = new ArrayList<PLAYTag>();
	    defaultTagSequence.add(PLAYTag.PLACEHOLDER);
	    return defaultTagSequence;
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
	    if (!seq.contains(PLAYTag.SEQ)) {
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

    },
    NUMBER {

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
	    return new PLAYPayload("NUM", PLAYTag.NUMBER);
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
	    return "NUM";
	}

    },
    BOOLEAN {

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
	    return new PLAYPayload("BOOLEAN", PLAYTag.BOOLEAN);
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
	    return "BOOLEAN";
	}

    },
    STRING {

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
	    return new PLAYPayload("STRING", PLAYTag.STRING);
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
	    return "STRING";
	}

    },
    NULL {

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
	    return new PLAYPayload("NULL", PLAYTag.NULL);
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
	    return "NULL";
	}

    },
    VAR {

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
	    return new PLAYPayload("VAR", PLAYTag.VAR);
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
	    return "VAR";
	}

    },
    DOT {

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
	    return new PLAYPayload("DOT", PLAYTag.DOT);
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
	    return "DOT";
	}

    };

}
