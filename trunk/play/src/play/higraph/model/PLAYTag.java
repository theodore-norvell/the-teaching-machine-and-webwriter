/**
 * java - play.higraph.model - PLAY
 * 
 * Created on Feb 13, 2012 by Kai Zhu
 */
package play.higraph.model;

import higraph.model.taggedInterfaces.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kai Zhu, Shiwei Han
 * 
 */
public enum PLAYTag implements Tag<PLAYTag, PLAYPayload> {

    CLASS {

	@Override
	public boolean contentModel(List<PLAYTag> seq) {
	    if (seq.isEmpty())
		return false;

	    for (int i = 0; i < seq.size() - 1; i++) {
		if (seq.get(i).equals(VARDECL))
		    continue;
		else
		    return false;
	    }

	    return seq.get(seq.size() - 1).equals(VARDECL)
		    || seq.get(seq.size() - 1).equals(METHOD);
	    // METHOD is optional but must be at last
	}

	@Override
	public List<PLAYTag> defaultTagSequence() {
	    return new ArrayList<PLAYTag>();
	}

	@Override
	public PLAYPayload defaultPayload() {
	    return new PLAYPayload("CLASS", CLASS);
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
	    return "CLASS";
	}

    },

    VARDECL {

	@Override
	public boolean contentModel(List<PLAYTag> seq) {
	    return seq.size() == 2
		    && (seq.get(0).equals(NOTYPE) || isType(seq.get(0)))
		    && seq.get(1).equals(SEQ);

	}

	@Override
	public List<PLAYTag> defaultTagSequence() {
	    List<PLAYTag> defaultTagSequence = new ArrayList<PLAYTag>();
	    defaultTagSequence.add(NOTYPE);
	    defaultTagSequence.add(SEQ);
	    return defaultTagSequence;
	}

	@Override
	public PLAYPayload defaultPayload() {
	    return new PLAYPayload("VARDECL", VARDECL);
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
	    return "VARDECL";
	}

    },

    /*CONDECL {

	@Override
	public boolean contentModel(List<PLAYTag> seq) {
	    return seq.size() == 2
		    && (seq.get(0).equals(NOTYPE) || isType(seq.get(0)))
		    && seq.get(1).equals(SEQ);
	}

	@Override
	public List<PLAYTag> defaultTagSequence() {
	    List<PLAYTag> defaultTagSequence = new ArrayList<PLAYTag>();
	    defaultTagSequence.add(NOTYPE);
	    defaultTagSequence.add(SEQ);
	    return defaultTagSequence;
	}

	@Override
	public PLAYPayload defaultPayload() {
	    return new PLAYPayload("CONDECL", CONDECL);
	}

	*//**
	 * @see java.lang.Enum#toString()
	 *//*
	@Override
	public String toString() {
	    return "CONDECL";
	}

    },*/

    ALTTYPE {

	@Override
	public boolean contentModel(List<PLAYTag> seq) {
	    if (seq.isEmpty())
		return false;

	    for (PLAYTag pt : seq) {
			if (isType(pt))
			    continue;
			else
			    return false;
	    }
	    return true;

	}

	@Override
	public List<PLAYTag> defaultTagSequence() {
	    return new ArrayList<PLAYTag>();
	}

	@Override
	public PLAYPayload defaultPayload() {
	    return new PLAYPayload("ALTTYPE", ALTTYPE);
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
	    return "ALTTYPE";
	}

    },

    NOTYPE {

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
	    return new PLAYPayload("NOTYPE", NOTYPE);
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
	    return "NOTYPE";
	}

    },

    BOOLEANTYPE {

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
	    return new PLAYPayload("BOOLEANTYPE", BOOLEANTYPE);
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
	    return "BOOLEANTYPE";
	}

    },

    STRINGTYPE {

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
	    return new PLAYPayload("STRINGTYPE", STRINGTYPE);
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
	    return "STRINGTYPE";
	}

    },

    NUMBERTYPE {

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
	    return new PLAYPayload("NUMBERTYPE", NUMBERTYPE);
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
	    return "NUMBERTYPE";
	}

    },

    ANYTYPE {

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
	    return new PLAYPayload("ANYTYPE", ANYTYPE);
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
	    return "ANYTYPE";
	}

    },

    NULLTYPE {

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
	    return new PLAYPayload("NULLTYPE", NULLTYPE);
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
	    return "NULLTYPE";
	}

    },

    COMMTYPE {

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
	    return new PLAYPayload("COMMTYPE", COMMTYPE);
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
	    return "COMMTYPE";
	}

    },

    CLASSTYPE {

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
	    return new PLAYPayload("CLASSTYPE", CLASSTYPE);
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
	    return "CLASSTYPE";
	}

    },

    SEQ {

	@Override
	public boolean contentModel(List<PLAYTag> seq) {
	    if (seq.isEmpty())
		return false;

	    for (PLAYTag pt : seq) {
		if (isExp(pt) || pt.equals(VARDECL) )
		    continue;
		else
		    return false;
	    }
	    return true;

	}

	@Override
	public List<PLAYTag> defaultTagSequence() {
	    return new ArrayList<PLAYTag>();
	}

	@Override
	public PLAYPayload defaultPayload() {
	    return new PLAYPayload("SEQ", SEQ);
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
	    return "SEQ";
	}

    },

    NUMBERLITERAL {

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
	    return new PLAYPayload("NUMBERLITERAL", NUMBERLITERAL, "0.0");
	}

	@Override
	public String toString() {
	    return "NUMBERLITERAL";
	}

    },

    STRINGLITERAL {

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
	    return new PLAYPayload("STRINGLITERAL", STRINGLITERAL, "");
	}

	@Override
	public String toString() {
	    return "STRINGLITERAL";
	}

    },

    TRUE {

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
	    return new PLAYPayload("TRUE", TRUE);
	}

	@Override
	public String toString() {
	    return "TRUE";
	}

    },

    FALSE {

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
	    return new PLAYPayload("FALSE", FALSE);
	}

	@Override
	public String toString() {
	    return "FALSE";
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
	    return new PLAYPayload("NULL", NULL);
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
	    return "NULL";
	}

    },

    THISVAR {

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
	    return new PLAYPayload("THISVAR", THISVAR);
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
	    return "THISVAR";
	}

    },

    LOCALVAR {

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
	    return new PLAYPayload("LOCALVAR", LOCALVAR);
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
	    return "LOCALVAR";
	}

    },

    WORLDVAR {

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
	    return new PLAYPayload("WORLDVAR", WORLDVAR);
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
	    return "WORLDVAR";
	}

    },

    DOT {

	@Override
	public boolean contentModel(List<PLAYTag> seq) {

	    return seq.size() == 1 && isExp(seq.get(0));

	}

	@Override
	public List<PLAYTag> defaultTagSequence() {
	    return new ArrayList<PLAYTag>();
	}

	@Override
	public PLAYPayload defaultPayload() {
	    return new PLAYPayload("DOT", DOT);
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
	    return "DOT";
	}

    },

    THIS {

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
	    return new PLAYPayload("THIS", THIS);
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
	    return "THIS";
	}

    },

    CALLCLOSURE {

	@Override
	public boolean contentModel(List<PLAYTag> seq) {
	    if (seq.isEmpty())
		return false;

	    for (PLAYTag pt : seq) {
		if (isExp(pt))
		    continue;
		else
		    return false;
	    }
	    return true;

	}

	@Override
	public List<PLAYTag> defaultTagSequence() {
	    return new ArrayList<PLAYTag>();
	}

	@Override
	public PLAYPayload defaultPayload() {
	    return new PLAYPayload("CALLCLOSURE", CALLCLOSURE);
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
	    return "CALLCLOSURE";
	}

    },

    CALLWORLD {

	@Override
	public boolean contentModel(List<PLAYTag> seq) {
	    if (seq.isEmpty())
		return false;

	    for (PLAYTag pt : seq) {
		if (isExp(pt))
		    continue;
		else
		    return false;
	    }
	    return true;
	}

	@Override
	public List<PLAYTag> defaultTagSequence() {
	    return new ArrayList<PLAYTag>();
	}

	@Override
	public PLAYPayload defaultPayload() {
	    return new PLAYPayload("CALLWORLD", CALLWORLD);
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
	    return "CALLWORLD";
	}

    },

    ASSIGN {

	@Override
	public boolean contentModel(List<PLAYTag> seq) {

	    return seq.size() == 2 && isExp(seq.get(0)) && isExp(seq.get(1));

	}

	@Override
	public List<PLAYTag> defaultTagSequence() {
	    List<PLAYTag> defaultTagSequence = new ArrayList<PLAYTag>();
	    defaultTagSequence.add(EXPPLACEHOLDER);
	    defaultTagSequence.add(EXPPLACEHOLDER);
	    return defaultTagSequence;
	}

	@Override
	public PLAYPayload defaultPayload() {
	    return new PLAYPayload("ASSIGN", ASSIGN);
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

	    return seq.size() == 3 && isExp(seq.get(0))
		    && seq.get(1).equals(SEQ) && seq.get(2).equals(SEQ);
	}

	@Override
	public List<PLAYTag> defaultTagSequence() {
	    ArrayList<PLAYTag> defaultTagSequence = new ArrayList<PLAYTag>();
	    defaultTagSequence.add(EXPPLACEHOLDER);
	    defaultTagSequence.add(SEQ);
	    defaultTagSequence.add(SEQ);
	    return defaultTagSequence;
	}

	@Override
	public PLAYPayload defaultPayload() {
	    return new PLAYPayload("IF", IF);
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
	    return seq.size() == 2 && isExp(seq.get(0))
		    && seq.get(1).equals(SEQ);
	}

	@Override
	public List<PLAYTag> defaultTagSequence() {
	    List<PLAYTag> defaultTagSequence = new ArrayList<PLAYTag>();
	    defaultTagSequence.add(EXPPLACEHOLDER);
	    defaultTagSequence.add(SEQ);
	    return defaultTagSequence;
	}

	@Override
	public PLAYPayload defaultPayload() {
	    return new PLAYPayload("WHILE", WHILE);
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
	    return "WHILE";
	}

    },

    EXPPLACEHOLDER {

	@Override
	public boolean contentModel(List<PLAYTag> seq) {
	    return seq.size() == 1 && isExp(seq.get(0));
	}

	@Override
	public List<PLAYTag> defaultTagSequence() {
	    List<PLAYTag> defaultTagSequence = new ArrayList<PLAYTag>();
	    return defaultTagSequence;
	}

	@Override
	public PLAYPayload defaultPayload() {
	    return new PLAYPayload("EXPPLACEHOLDER", EXPPLACEHOLDER);
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
	    return "EXPPLACEHOLDER";
	}

    },

    NEW {

	@Override
	public boolean contentModel(List<PLAYTag> seq) {
	    if (seq.size() < 2)
		return false;

	    if (!seq.get(0).equals(CLASSTYPE))
		return false;

	    for (int i = 1; i < seq.size(); i++) {
		if (isExp(seq.get(i)))
		    continue;
		else
		    return false;
	    }
	    return true;

	}

	@Override
	public List<PLAYTag> defaultTagSequence() {
	    List<PLAYTag> defaultTagSequence = new ArrayList<PLAYTag>();
	    return defaultTagSequence;
	}

	@Override
	public PLAYPayload defaultPayload() {
	    return new PLAYPayload("NEW", NEW);
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
	    return "NEW";
	}

    },

    METHOD {

	@Override
	public boolean contentModel(List<PLAYTag> seq) {
	    if (seq.size() < 3||!seq.get(0).equals(PARAMS))
	    	return false;

	    return ( isType(seq.get(seq.size() - 2)) 
	    		 || seq.get(seq.size() - 2) .equals(NOTYPE) 
	    		) //OptType
	    		&& seq.get(seq.size() - 1).equals(SEQ);//Seq

	}

	@Override
	public List<PLAYTag> defaultTagSequence() {
		List<PLAYTag> l = new ArrayList<PLAYTag>();
		l.add(PARAMS);
		l.add(NOTYPE);
		l.add(SEQ);
	    return l;
	}

	@Override
	public PLAYPayload defaultPayload() {
	    return new PLAYPayload("METHOD", METHOD);
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
	    return "METHOD";
	}

    },
    
    PARAMS {
	@Override
	public boolean contentModel(List<PLAYTag> seq) {
		
		if(seq.size()>0){
			for(PLAYTag t:seq){
				if(!t.equals(VARDECL))
					return false;
			}
		}
		return true;

	}

	@Override
	public List<PLAYTag> defaultTagSequence() {
	    return new ArrayList<PLAYTag>();
	}

	@Override
	public PLAYPayload defaultPayload() {
	    return new PLAYPayload("PARAMS", PARAMS);
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
	    return "PARAMS";
	}
    };

    public boolean isExp(PLAYTag pt) {
	return pt.equals(NUMBERLITERAL) || pt.equals(STRINGLITERAL)
		|| pt.equals(TRUE) || pt.equals(FALSE) || pt.equals(NULL)
		|| pt.equals(THISVAR) || pt.equals(LOCALVAR)
		|| pt.equals(WORLDVAR) || pt.equals(DOT) || pt.equals(THIS)
		|| pt.equals(CALLCLOSURE) || pt.equals(CALLWORLD)
		|| pt.equals(NEW) || pt.equals(METHOD) || pt.equals(IF)
		|| pt.equals(WHILE) || pt.equals(ASSIGN)
		|| pt.equals(EXPPLACEHOLDER);
    }

    public boolean isType(PLAYTag pt) {
	return pt.equals(BOOLEANTYPE) || pt.equals(STRINGTYPE)
		|| pt.equals(NUMBERTYPE) || pt.equals(ANYTYPE)
		|| pt.equals(NULLTYPE) || pt.equals(COMMTYPE)
		|| pt.equals(ALTTYPE) || pt.equals(CLASSTYPE);
    }

}