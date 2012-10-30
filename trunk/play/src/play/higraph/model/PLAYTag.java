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
		    if (!seq.isEmpty()) {
		    	for(int i=0;i<seq.size();i++){
		    		if( seq.get(i).equals(VARDECL)||
		    			seq.get(i).equals(CONDECL)||
		    			(seq.get(i).equals(SEQ)&&i==seq.size()-1)   )
		    			continue;
		    		else
		    			return false;	// containing some tag illegal
		    	}
		    	return true;
		    }
		    return false;// empty
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
    	    if (!seq.isEmpty()) {
	    		if ((seq.size() == 2)
	    			&& (seq.indexOf(TYPE) == 0 || seq.indexOf(NOTYPE) == 0)
	    			&& (seq.indexOf(SEQ) == 1) ) {
	    		    return true;
	    		}
    	    }
    	    return false;
    	}

    	@Override
    	public List<PLAYTag> defaultTagSequence() {
    		 List<PLAYTag> defaultTagSequence = new ArrayList<PLAYTag>();
    		 defaultTagSequence.add(TYPE);
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
	        
    CONDECL {

    	@Override
    	public boolean contentModel(List<PLAYTag> seq) {
    		if (!seq.isEmpty()) {
	    		if ((seq.size() == 2)
	    			&& (seq.indexOf(TYPE) == 0 || seq.indexOf(NOTYPE) == 0)
	    			&& (seq.indexOf(SEQ) == 1) ) {
	    		    return true;
	    		}
    	    }
    	    return false;
    	}

    	@Override
    	public List<PLAYTag> defaultTagSequence() {
    		 List<PLAYTag> defaultTagSequence = new ArrayList<PLAYTag>();
    		 defaultTagSequence.add(TYPE);
    		 defaultTagSequence.add(SEQ);
    		 return defaultTagSequence;
    	}

    	@Override
    	public PLAYPayload defaultPayload() {
    	    return new PLAYPayload("CONDECL", CONDECL);
    	}

    	/**
    	 * @see java.lang.Enum#toString()
    	 */
    	@Override
    	public String toString() {
    	    return "CONDECL";
    	}

    },
    
    TYPE {

    	@Override
    	public boolean contentModel(List<PLAYTag> seq) {	    
    	    if (!seq.isEmpty()) {
		    	for(PLAYTag pt : seq){
		    		if(pt.equals(BOOLEAN)
		    			||pt.equals(STRING)
		    			||pt.equals(NUMBER)
		    			||pt.equals(ANY)
		    			||pt.equals(NULL)
		    			||pt.equals(COMM)
		    			||pt.equals(CLASS)
		    			)
		    			continue;
		    		else
		    			return false;
		    	}
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
    	    return new PLAYPayload("TYPE", TYPE);
    	}

    	/**
    	 * @see java.lang.Enum#toString()
    	 */
    	@Override
    	public String toString() {
    	    return "TYPE";
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
    	    return new PLAYPayload("BOOLEAN", BOOLEAN);
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
    	    return new PLAYPayload("STRING", STRING);
    	}
    	
    	/**
    	 * @see java.lang.Enum#toString()
    	 */
    	@Override
    	public String toString() {
    	    return "STRING";
    	}

    },
    
    NUMBER{

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
    	    return new PLAYPayload("NUMBER", NUMBER);
    	}

    	/**
    	 * @see java.lang.Enum#toString()
    	 */
    	@Override
    	public String toString() {
    	    return "NUMBER";
    	}

    },
    
    ANY {

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
    	    return new PLAYPayload("ANY", ANY);
    	}

    	/**
    	 * @see java.lang.Enum#toString()
    	 */
    	@Override
    	public String toString() {
    	    return "ANY";
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
    
    COMM {

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
    	    return new PLAYPayload("COMM", COMM);
    	}

    	/**
    	 * @see java.lang.Enum#toString()
    	 */
    	@Override
    	public String toString() {
    	    return "COMM";
    	}

    },
        
    SEQ {

    	@Override
    	public boolean contentModel(List<PLAYTag> seq) {
    		if (!seq.isEmpty()) {
		    	for(PLAYTag pt : seq){
		    		if(pt.equals(EXP)
		    			||pt.equals(VARDECL)
		    			||pt.equals(CONDECL)
		    			)
		    			continue;
		    		else
		    			return false;
		    	}
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
    
    EXP {

    	@Override
    	public boolean contentModel(List<PLAYTag> seq) {
    	    if (!seq.isEmpty()) {
	    		if ( (seq.size() == 1)
	    			 &&( seq.contains(NUMBERLITERAL)
	    				 || seq.contains(STRINGLITERAL)  
	    				 || seq.contains(TRUE)
	    				 || seq.contains(FALSE)
	    				 || seq.contains(NULL)
	    				 || seq.contains(VAR)
	    				 || seq.contains(DOT)
	    				 || seq.contains(THIS)
	    				 || seq.contains(CALLCLOSURE)
	    				 || seq.contains(CALL)
	    				 || seq.contains(CALLWORLD)
	    				 || seq.contains(IF)
	    				 || seq.contains(WHILE)
	    				 || seq.contains(NEW)
	    				 || seq.contains(METHOD)
	    				 || seq.contains(ASSIGN)
	    				 || seq.contains(EXPPLACEHOLDER) ) 
	    			) {
	    		    return true;    
	    		}
    	    }
    	    return false;
    	}

    	@Override
    	public List<PLAYTag> defaultTagSequence() {
    	    List<PLAYTag> defaultTagSequence = new ArrayList<PLAYTag>();
    	    defaultTagSequence.add(EXPPLACEHOLDER);
    	    return defaultTagSequence;
    	}

    	@Override
    	public PLAYPayload defaultPayload() {
    	    return new PLAYPayload("EXP", EXP);
    	}

    	/**
    	 * @see java.lang.Enum#toString()
    	 */
    	@Override
    	public String toString() {
    	    return "EXP";
    	}

    },
    
    NUMBERLITERAL{

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
        
    STRINGLITERAL{

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
    
    TRUE{

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

    FALSE{

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
    
    VAR {

    	@Override
    	public boolean contentModel(List<PLAYTag> seq) {
    		if (!seq.isEmpty()) {
	    		if ( (seq.size() == 1)
	    			 &&( seq.contains(THIS)
	    				 || seq.contains(LOCAL)  
	    				 || seq.contains(WORLD)
	    				) 
	    			) {
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
    	    return new PLAYPayload("VAR", VAR);
    	}

    	/**
    	 * @see java.lang.Enum#toString()
    	 */
    	@Override
    	public String toString() {
    	    return "VAR";
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
    
    LOCAL {

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
		    return new PLAYPayload("LOCAL", LOCAL);
		}
	
		/**
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
		    return "LOCAL";
		}

    },
    
    WORLD {

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
		    return new PLAYPayload("WORLD", WORLD);
		}
	
		/**
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
		    return "WORLD";
		}

    },
      
    DOT {

		@Override
		public boolean contentModel(List<PLAYTag> seq) {
			if (!seq.isEmpty()) {
	    		if ( seq.size() == 1 && seq.contains(EXP) ) {
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
    
    CALLCLOSURE {

		@Override
		public boolean contentModel(List<PLAYTag> seq) {
			if (!seq.isEmpty()) {
		    	for(PLAYTag pt : seq){
		    		if(pt.equals(EXP) )
		    			continue;
		    		else
		    			return false;
		    	}
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
    
    CALL {

		@Override
		public boolean contentModel(List<PLAYTag> seq) {
			if (!seq.isEmpty()) {
		    	for(PLAYTag pt : seq){
		    		if(pt.equals(EXP) )
		    			continue;
		    		else
		    			return false;
		    	}
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
		    return new PLAYPayload("CALL", CALL);
		}
	
		/**
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
		    return "CALL";
		}

    },
    
    CALLWORLD {

		@Override
		public boolean contentModel(List<PLAYTag> seq) {
			if (!seq.isEmpty()) {
		    	for(PLAYTag pt : seq){
		    		if(pt.equals(EXP) )
		    			continue;
		    		else
		    			return false;
		    	}
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
    		if (!seq.isEmpty()) {
	    		if (seq.size() == 2
	    			&& seq.get(0).equals(EXP)
	    			&& seq.get(1).equals(EXP) ) {
	    		    return true;
	    		}
    	    }
    	    return false;
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
		    if (!seq.isEmpty()) {
		    	if (seq.size() == 3 &&
		    		seq.get(0).equals(EXP) && 
		    		seq.get(1).equals(SEQ) &&
		    		seq.get(2).equals(SEQ)) {
				return true;
			    }
		    }
		    return false;
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
			if (!seq.isEmpty()) {
		    	if (seq.size() == 2 &&
		    		seq.get(0).equals(EXP) && 
		    		seq.get(2).equals(SEQ)) {
				return true;
			    }
		    }
		    return false;
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
		    if (!seq.isEmpty()) {
				if (seq.size() == 1 && seq.indexOf(EXP) == 0) {
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
		    if (!seq.isEmpty()) {
				if (seq.size() >=2) {
					if(seq.get(0).equals(TYPE)){
						for(int i=1;i<seq.size();i++){
							if(seq.get(i).equals(EXP))
								continue;
							else
								return false;
						}
						return true;
					}
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
    
    METHOD {

		@Override
		public boolean contentModel(List<PLAYTag> seq) {
		    if (!seq.isEmpty()) {
				if (seq.size() >=2) {
					for(int i=0;i<seq.size()-2;i++){
						if(seq.get(i).equals(VARDECL)||seq.get(i).equals(CONDECL)){
							continue;
						}else
							return false;
					}
					if((seq.get(seq.size()-2).equals(TYPE)||
						seq.get(seq.size()-2).equals(NOTYPE)  )
						&&
						(seq.get(seq.size()-1).equals(SEQ))
																){
						return true;
					}
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
    
    
    
//////////////////Following is to be revised///////////////////////////////////////////////////////////    
   
        
    
	PLUS {
		@Override
		public boolean contentModel(List<PLAYTag> seq) {
			if (!seq.isEmpty()) {
				if ((seq.size() == 1)
						&& ((seq.indexOf(EXPPLACEHOLDER) == 1)
						|| (seq.lastIndexOf(EXP) == 1)
						|| (seq.lastIndexOf(NUMBER) == 1)
						|| (seq.lastIndexOf(BOOLEAN) == 1)
						|| (seq.lastIndexOf(STRING) == 1)
						|| (seq.lastIndexOf(NULL) == 1)
						|| (seq.lastIndexOf(VAR) == 1)
						|| (seq.indexOf(PLUS) == 1)
						|| (seq.indexOf(MINUS) == 1)
						|| (seq.indexOf(MULTIPLICATION) == 1)
						|| (seq.indexOf(DIVISION) == 1) || (seq
						.lastIndexOf(DOT) == 1))) {
		    		return true;
				} else if ((seq.size() == 2)
					&& ((seq.lastIndexOf(EXPPLACEHOLDER) == 1)
						|| (seq.lastIndexOf(EXP) == 1)
						|| (seq.lastIndexOf(NUMBER) == 1)
						|| (seq.lastIndexOf(BOOLEAN) == 1)
						|| (seq.lastIndexOf(STRING) == 1)
						|| (seq.lastIndexOf(NULL) == 1)
						|| (seq.lastIndexOf(VAR) == 1)
						|| (seq.indexOf(PLUS) == 1)
						|| (seq.indexOf(MINUS) == 1)
						|| (seq.indexOf(MULTIPLICATION) == 1)
						|| (seq.indexOf(DIVISION) == 1) || (seq
						.lastIndexOf(DOT) == 1))) {
				    return true;
				}
		    }
		    return false;
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
		    return new PLAYPayload("\uFF0B", PLUS);
		}
	
		/**
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
		    return "\uFF0B";
		}
    },
    
    MINUS {
		@Override
		public boolean contentModel(List<PLAYTag> seq) {
			if (!seq.isEmpty()) {
				if ((seq.size() == 1)
					&& ((seq.indexOf(EXPPLACEHOLDER) == 1)
						|| (seq.lastIndexOf(EXP) == 1)
						|| (seq.lastIndexOf(NUMBER) == 1)
						|| (seq.lastIndexOf(BOOLEAN) == 1)
						|| (seq.lastIndexOf(STRING) == 1)
						|| (seq.lastIndexOf(NULL) == 1)
						|| (seq.lastIndexOf(VAR) == 1)
						|| (seq.indexOf(PLUS) == 1)
						|| (seq.indexOf(MINUS) == 1)
						|| (seq.indexOf(MULTIPLICATION) == 1)
						|| (seq.indexOf(DIVISION) == 1) || (seq
						.lastIndexOf(DOT) == 1))) {
				    return true;
				} else if ((seq.size() == 2)
					&& ((seq.lastIndexOf(EXPPLACEHOLDER) == 1)
						|| (seq.lastIndexOf(EXP) == 1)
						|| (seq.lastIndexOf(NUMBER) == 1)
						|| (seq.lastIndexOf(BOOLEAN) == 1)
						|| (seq.lastIndexOf(STRING) == 1)
						|| (seq.lastIndexOf(NULL) == 1)
						|| (seq.lastIndexOf(VAR) == 1)
						|| (seq.indexOf(PLUS) == 1)
						|| (seq.indexOf(MINUS) == 1)
						|| (seq.indexOf(MULTIPLICATION) == 1)
						|| (seq.indexOf(DIVISION) == 1) || (seq
						.lastIndexOf(DOT) == 1))) {
				    return true;
				}
		    }
		    return false;
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
		    return new PLAYPayload("\uFF0D", MINUS);
		}
	
		/**
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
		    return "\uFF0D";
		}
    },
    
    MULTIPLICATION {
		@Override
		public boolean contentModel(List<PLAYTag> seq) {
		    if (!seq.isEmpty()) {
				if ((seq.size() == 1)
					&& ((seq.indexOf(EXPPLACEHOLDER) == 1)
						|| (seq.lastIndexOf(EXP) == 1)
						|| (seq.lastIndexOf(NUMBER) == 1)
						|| (seq.lastIndexOf(BOOLEAN) == 1)
						|| (seq.lastIndexOf(STRING) == 1)
						|| (seq.lastIndexOf(NULL) == 1)
						|| (seq.lastIndexOf(VAR) == 1)
						|| (seq.indexOf(PLUS) == 1)
						|| (seq.indexOf(MINUS) == 1)
						|| (seq.indexOf(MULTIPLICATION) == 1)
						|| (seq.indexOf(DIVISION) == 1) || (seq
						.lastIndexOf(DOT) == 1))) {
				    return true;
				} else if ((seq.size() == 2)
					&& ((seq.lastIndexOf(EXPPLACEHOLDER) == 1)
						|| (seq.lastIndexOf(EXP) == 1)
						|| (seq.lastIndexOf(NUMBER) == 1)
						|| (seq.lastIndexOf(BOOLEAN) == 1)
						|| (seq.lastIndexOf(STRING) == 1)
						|| (seq.lastIndexOf(NULL) == 1)
						|| (seq.lastIndexOf(VAR) == 1)
						|| (seq.indexOf(PLUS) == 1)
						|| (seq.indexOf(MINUS) == 1)
						|| (seq.indexOf(MULTIPLICATION) == 1)
						|| (seq.indexOf(DIVISION) == 1) || (seq
						.lastIndexOf(DOT) == 1))) {
				    return true;
				}
		    }
		    return false;
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
		    return new PLAYPayload("\u2715", MULTIPLICATION);
		}
	
		/**
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
		    return "\u2715";
		}
    },
    
    DIVISION{
		@Override
		public boolean contentModel(List<PLAYTag> seq) {
		    if (!seq.isEmpty()) {
				if ((seq.size() == 1)
					&& ((seq.indexOf(EXPPLACEHOLDER) == 1)
						|| (seq.lastIndexOf(EXP) == 1)
						|| (seq.lastIndexOf(NUMBER) == 1)
						|| (seq.lastIndexOf(BOOLEAN) == 1)
						|| (seq.lastIndexOf(STRING) == 1)
						|| (seq.lastIndexOf(NULL) == 1)
						|| (seq.lastIndexOf(VAR) == 1)
						|| (seq.indexOf(PLUS) == 1)
						|| (seq.indexOf(MINUS) == 1)
						|| (seq.indexOf(MULTIPLICATION) == 1)
						|| (seq.indexOf(DIVISION) == 1) || (seq
						.lastIndexOf(DOT) == 1))) {
				    return true;
				} else if ((seq.size() == 2)
					&& ((seq.lastIndexOf(EXPPLACEHOLDER) == 1)
						|| (seq.lastIndexOf(EXP) == 1)
						|| (seq.lastIndexOf(NUMBER) == 1)
						|| (seq.lastIndexOf(BOOLEAN) == 1)
						|| (seq.lastIndexOf(STRING) == 1)
						|| (seq.lastIndexOf(NULL) == 1)
						|| (seq.lastIndexOf(VAR) == 1)
						|| (seq.indexOf(PLUS) == 1)
						|| (seq.indexOf(MINUS) == 1)
						|| (seq.indexOf(MULTIPLICATION) == 1)
						|| (seq.indexOf(DIVISION) == 1) || (seq
						.lastIndexOf(DOT) == 1))) {
				    return true;
				}
		    }
		    return false;
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
		    return new PLAYPayload("\u00F7", DIVISION);
		}
		
		/**
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
		    return "\u00F7";
		}
	};

}