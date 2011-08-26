//     Copyright 1998--2010 Michael Bruce-Lockhart and Theodore S. Norvell
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License. 
// You may obtain a copy of the License at 
//
//     http://www.apache.org/licenses/LICENSE-2.0 
//
// Unless required by applicable law or agreed to in writing, 
// software distributed under the License is distributed on an 
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
// either express or implied. See the License for the specific language 
// governing permissions and limitations under the License.

package tm.clc.analysis;

import java.util.*;

import tm.utilities.Assert;

/**
 * Generally, a representation of an <em>id</em> with possible qualification.
 * <ul><code>ScopedName</code>s are used in several contexts:
 * <li> to represent an id or operator with any qualifications as encountered
 * in the source code </li>
 * <li> to represent the <em>fully qualified name</em> (fqn) corresponding to 
 * a declared entity </li>
 * <li> as keys in the runtime symbol table </li>
 * </ul>
 * <p>The "encountered representations" if ids are used by the lookup logic 
 * (compile-time symbol table) to find the matching <code>Declaration</code>(s).
 * The fqn of a <code>Declaration</code> is then used to uniquely identify 
 * objects and other entities at runtime (important exceptions in C++ are 
 * non-static class attributes, which necessarily use a different naming scheme 
 * in order to support multiple inheritance).
 * <p>A <code>ScopedName</code> has two distinct and enforced states. The 
 * first is <em>modifiable</em>, wherein the name can be built, extended, and
 * revised. The second is <em>readonly</em>, in which the components of the name
 * may be traversed, but not modified.  State transition is one-way  
 * (irreversable), from modifiable to readonly, via the <code>completed</code>
 * method. 
 * <p>An internal traversal index is provided for each <code>ScopedName</code>.
 * This index is shared among all objects with references to the name, 
 * and must be treated as such. 
 */
public abstract class ScopedName {

    /**
     * Error message provided when modification is attempted on a 
     * <code>ScopedName</code in <em>readonly</em> state.
     */
    protected static final String READONLY = 
        "Attempt to modify readonly ScopedName {0}";

    /**
     * Error message provided when traversal (using internal iterator) is 
     * attempted on a <code>ScopedName</code in <em>modifiable</em> state.
     */
    protected static final String MODIFIABLE = 
        "Attempt to traverse modifiable ScopedName {0}";
    protected static final String INVALID = 
        "Attempt to traverse modified ScopedName {0}";

    /** Constructor flag for unique id */
    public static final int UNIQUE   = 0;
    /** Constructor flag for anonymous id */
    public static final int ANON     = 1;
    /** Constructor flag for builtin copy ref argument */
    public static final int COPY_REF = 2;
    private static final String UNIQUE_PREFIX = "_TMUniqueId";
    private static final String ANON_ID = "_TMAnonId";
    public  static final String COPY_REF_ID = "_TMCopyRef";

    private static int unique_id = 0; // used to assign unique names

    private boolean absolute = false; // starting from global scope?
    
    // Invariant: qualifiedName.getElement(i) instanceof String, for all i
    private Vector qualifiedName = new Vector ();

    /**
     * An internal index/iterator over the elements of this 
     * <code>ScopedName</code>. This is shared among all entities with 
     * references to this object, so care must be taken when using it to 
     * traverse over the elements of the name.
     */
    public ScopedNameIndex index = new ScopedNameIndex ();

    // are we readonly or modifiable ?
    private boolean readonly = false;

    /**
     * Creates a new <code>ScopedName</code> instance.
     */
    public ScopedName () { }

    /**
     * Creates a new <code>ScopedName</code> instance.
     * 
     * @param rawName either an initial terminal id, a simple name, or
     *  a string representation of a qualified name.
     */
    protected ScopedName (String rawName) { 
        // is the purpose of this constructor to break down a 
        // String representation into parts ?
    	if (rawName == null) return;
    	String scopeOp = getScopeOp();
    	int from = 0;
    	int to = rawName.indexOf(scopeOp);
     	while (to != -1){
    		qualifiedName.addElement(rawName.substring(from,to));
    		from = to + scopeOp.length();
    		to = rawName.indexOf(scopeOp, from);
    	}
    	qualifiedName.addElement(rawName.substring(from,rawName.length()));
    }
    
    /**
     * Creates a new <code>ScopedName</code> instance.
     *
     * @param elements the components of the name.
     */
    protected ScopedName (String elements[]){
    	for (int e = 0; e < elements.length; e++)
    		qualifiedName.addElement(elements[e]);
    }

    /**
     * Copy constructor. 
     * @param orig a <code>ScopedName</code> value
     */
    public ScopedName (ScopedName orig) { 
        absolute = orig.absolute;

        // deep copy Vector
        qualifiedName = (Vector) orig.qualifiedName.clone ();
    }
    
    public abstract Object clone () ;

    /**
     * Creates a new <code>ScopedName</code> initialized according to
     * the flag passed in.
     * @param flag controls initialization. Valid values defined in class.
     */
    public ScopedName (int flag) {
        String id = null;
        switch (flag) {
        case UNIQUE: 
            id = UNIQUE_PREFIX + Integer.toString (unique_id++);
            break;
        case ANON:
            id = ANON_ID; // would this ever need to be a unique instance ?
            break;
        case COPY_REF:
            id = COPY_REF_ID;
            break;
        }
        qualifiedName.addElement (id);
    }
        
    /**
     * Gives the number of parts in this name
     * @return the number of parts
     */
    public int length () { return qualifiedName.size (); }
    
    /** Return the language specific scope operator. C++'s "::" is the default.
     * Over-ride for other languages
     * @return the default C++ scope operator
    */        
    public abstract String getScopeOp() ;

    /**
     * Indicate that this id is <em>absolutely qualified</em>, i.e. identifies 
     * an entity by its placement in relation to the outermost scope.
     */
    public void set_absolute () { absolute = true; }

    /**
     * Is this id <em>absolutely qualified</em> or not ?
     * @return a <code>boolean</code> indication of this
     */
    public boolean is_absolute () { return absolute; }

    /**
     * Is the name prefixed by any scope qualifiers (global scope included)?
     * @return a <code>boolean</code> indication of this
     */
    public boolean is_qualified () { return (absolute || qualifiedName.size () > 1); }

    /**
     * Implementation of <code>equals (Object)</code>. Tries a few short cuts 
     * before resorting to element-by-element comparison of this 
     * <code>ScopedName</code> with that provided as an argument.<br>
     * <b>Note:</b> states (modifiable or readonly) are not compared.
     * @param other an <code>Object</code> value to compare
     * @return <code>true</code> if the <code>ScopedName</code>s consist of 
     * <code>String</code> elements for which <code>equals</code> returns true
     * when comparing each in sequence. Otherwise <code>false</code>. 
     */
    public boolean equals( Object other ) {
        if( other == null ) return false ;
        if( ! (other instanceof ScopedName) ) return false ;
        ScopedName other_scoped_name = (ScopedName) other ;
        if( this.absolute != other_scoped_name.absolute )
            return false ;
        int sz = this.qualifiedName.size() ;
        if( sz != other_scoped_name.qualifiedName.size() )
            return false ;
        Enumeration e0 = this.qualifiedName.elements();
        Enumeration e1 = other_scoped_name.qualifiedName.elements();
        while (e0.hasMoreElements ()) {
            if( ! e0.nextElement().equals( e1.nextElement() ) ) {
                return false ; } }
        return true ;
    }
    
    
    /**
     * Is other name contained at end of this name?
     * Added by mpbl April, 2003
     * <b>Note:</b> states (modifiable or readonly) are not compared.
     * @param other a <code>ScopedName</code> value to compare
     * @return <code>true</code> if this <code>ScopedName</code>s ends
     * in other ScopedName. Otherwise <code>false</code>. 
     */
    public boolean contains( ScopedName other ) {
        if( this.qualifiedName.size() < other.qualifiedName.size() )
            return false ;
        Enumeration e0 = this.qualifiedName.elements();
        Enumeration e1 = other.qualifiedName.elements();
        while (e1.hasMoreElements ()) {
            if( ! e0.nextElement().equals( e1.nextElement() ) ) {
                return false ; } }
        return true ;
    }

    
    /**
     * Provides a <code>String</code> representation of this 
     * <code>ScopedName</code> suitable for presentation, including 
     * <em>separators</em>. Default separator is <em>scope operator</em>.
     * <p>For example, in C++, for a name marked as <code>absolute</code> with 
     * elements "nsA", "clB", "memC", this method would evaluate to 
     * "::nsA::clB::memC".  
     *
     * @return a presentable <code>String</code> representation
     */
    public String getName (String separator) { 
        StringBuffer name = new StringBuffer ();

        if (absolute) name.append (separator);
        
        for (Enumeration parts = qualifiedName.elements ();
             parts.hasMoreElements (); ) {

            name.append ((String) parts.nextElement ());
            if (parts.hasMoreElements ()) name.append (separator);
        }

        return name.toString ();
    }
    
    public String getName () {
        return getName(getScopeOp());
    }
    
    
    /** put here for debugging purposes, may be safely removed */
    public String toString () { return this.getName (); }

    /**
     * Provides an <code>Enumeration</code> over the elements of this 
     * <code>ScopedName</code>. Useful when the internal shared iterator 
     * cannot be relied upon.
     *
     * @return an <code>Enumeration</code> over the parts of this name
     */
    public Enumeration getParts () { return qualifiedName.elements (); }

    /**
     * Returns a <code>String</code> representation of this name without
     * its qualifiers. 
     * @return the name stripped of any qualifiers
     */
    public String getUnqualifiedName () { 
        return (String) qualifiedName.lastElement ();
    }

    /**
     * Returns the <code>String</code> representing the terminal id of this
     * name. Identical behaviour to <code>getUnqualifiedName</code>.
     * @return the terminal id
     */
    public String getTerminalId () { 
        return getUnqualifiedName ();
    }

    /**
     * Replaces the current terminal id with the supplied value.
     * @param tid the new terminal id of this <code>ScopedName</code>
     */
    public void setTerminalId (String tid) { 
        if (readonly) Assert.apology (READONLY, getName ());
        qualifiedName.removeElementAt (qualifiedName.size () - 1);
        qualifiedName.addElement (tid);
        index.invalidate();
    }

    /**
     * Removes the current terminal id. The qualifier preceding the 
     * terminal id (if any) becomes the new terminal id. Does nothing if
     * the size of this <code>ScopedName</code> is 0.
     */
    public void removeTerminalId () { 
        if (readonly) Assert.apology (READONLY, getName ());
        int qsize = qualifiedName.size ();
        if (qsize > 0) {
            qualifiedName.removeElementAt (qsize - 1);
            index.invalidate();
        }
    }

    /**
     * Appends the elements in the provided <code>ScopedName</code> to this
     * <code>ScopedName</code>. Elements are appended in the order encountered.
     * @param toAppend a <code>ScopedName</code> containing elements to 
     * append.
     */
    public void append (ScopedName toAppend) { 
        if (readonly) Assert.apology (READONLY, getName ());
        Enumeration e = toAppend.qualifiedName.elements();
        while (e.hasMoreElements ()) 
            qualifiedName.addElement (e.nextElement ());
        index.invalidate();
    }

    /**
     * Appends the <code>String</code> provided to this <code>ScopedName</code>.
     * The provided element becomes the new terminal id for the name.
     * @param toAppend the <code>String</code> to append
     */
    public void append (String toAppend) { 
        if (readonly) Assert.apology (READONLY, getName ());
        qualifiedName.addElement (toAppend); 
        index.invalidate();
    }

    /**
     * Inserts the elements in the provided <code>ScopedName</code> at the front of this
     * <code>ScopedName</code>. Order of the inserted elements is preserved.
     * added April, 2003 by mpbl
     * @param toAppend a <code>ScopedName</code> containing elements to 
     * append.
     */
    public void insert (ScopedName toInsert) { 
        if (readonly) Assert.apology (READONLY, getName ());
        Enumeration e = toInsert.qualifiedName.elements();
        int where = 0;
        while (e.hasMoreElements ()) 
            qualifiedName.insertElementAt(e.nextElement (),where++);
        index.invalidate();
    }

    /**
     * Inserts the <code>String</code> provided in front of this <code>ScopedName</code>.
     * The provided element becomes the new element 0 for the name.
     * added April, 2003 by mpbl
     * @param toAppend the <code>String</code> to append
     */
    public void insert (String toInsert) { 
        if (readonly) Assert.apology (READONLY, getName ());
        qualifiedName.insertElementAt (toInsert, 0); 
        index.invalidate();
    }

    /**
     * Moves this <code>ScopedName</code>'s state from <em>modifiable</em>
     * to <em>readonly</em>, allowing traversal via the internal iterator.
     */
    public void completed () { readonly = true; index.reset(); }

    /**
     * Provides access to the part of this <code>ScopedName</code> currently
     * indexed by the internal iterator.
     * @return the currently selected part
     */
    public String selectedPart () { 
        if (!index.isValid()) 
            Assert.check (INVALID, this.getName ());
        return (String) qualifiedName.elementAt (index.value ());
    } 

    /**
     * Provides a reference to the elements of a <code>ScopedName</code>. 
     * Intended for use when a <code>ScopedName</code> has been built and set 
     * to <em>readonly</em> via the <code>completed</code> method. 
     * Operation undefined otherwise.
     * <p>As this provides a single shared internal traversal, an 
     * <code>Enumeration</code> ( via <code>ScopedName.getParts ()</code> ) 
     * is preferred where exclusive traversal is required.
     */
    public class ScopedNameIndex {
        private int index = 0;
        private boolean valid = false;
        
        public void invalidate(){valid = false;}
        
        public boolean isValid(){return valid;}

        /**
         * Advances the index by one.
         *
         * @return <code>true</code> if the advance was successful, 
         * <code>false</code> if it wasn't possible 
         */
        public boolean advance () { 
            if (index < qualifiedName.size () - 1) {
                index++;
                return true;
            }
            return false;
        }

        /**
         * Resets the index to the first element in the <code>ScopedName</code>.
         */
        public void reset () { index = 0; valid = true; }

        /**
         * Reverses the index by one.
         *
         * @return <code>true</code> if the reverse was successful, 
         * <code>false</code> if it wasn't possible 
         */
        public boolean reverse () {
            if (index > 0) {
                index--;
                return true;
            }
            return false;
        }

        /**
         * Provides the position of the currently selected element in the 
         * <code>ScopedName</code>
         *
         * @return the position
         */
        public int value () { return index; }

        /**
         * Indicates whether or not the index currently references the 
         * <code>ScopedName</code>'s terminal id.
         *
         * @return <code>true</code> if the terminal id is the currently 
         * selected element, <code>false</code> otherwise
         */
        public boolean terminal () { 
            return (index + 1 == qualifiedName.size ()); 
        }
        
    }
    

    
}
