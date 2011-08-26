 package tm.interfaces;
/*
COPYRIGHT (C) 1997 by Michael Bruce-Lockhart.  The associated software is 
released to students for educational purposes only and only for the duration
of the course in which it is handed out. No other use of the software, either
commercial or non-commercial, may be made without the express permission of
the author. 
*/


/**=========================================================================
* Interface: Datum
* @author mpbl
* Overview:
* This interface is defines the methods to be provided by all types of data items. 
* Datum objects, representing different types of identifiers, form the building 
* blocks of a Store object.
* ===========================================================================
* 99.04.23 Two routines added getNumChildren, and getChildAt
*     (by mpbl) for easier access of child datums as
*     part of major Display Engine update.
* 
* 
* 99.08.21 Replaced setVDO and getVDO with setDisplayInfo and getDisplayInfo. TSN
* 99.08.21 Added new reoutine getChildLabelAt. TSN.
* 99.08.21 We will now use Datum to represent memory regions like the
*     stack, heap, etc.  Note that the immediate children of these datums
*     will still have null as their parents.
* 2001.03.20: Added getBirthOrder() method (mpbl)
* 2001.10.31 Changed setDisplayInfo and getDisplayInfo to setProperty and getProperty
* 2005.05.28: Added defaultInitialize() to handle default initialization of 
* 			instantance variables, array datums and statics in Java
* 
*/

public interface Datum {
    
	/**
	 * No highlighting is applied
	 */
    public static final int PLAIN = 0 ;
    /**
     * Highlighting constant
     */
    public static final int HIGHLIGHTED = 1 ;
	//-------------------------------------------------------------------------
    
    /**
     * 
     * @return an int indicating whether and how to highlight this entry
     */
    public int getHighlight() ;
	//-------------------------------------------------------------------------
   
	/**
	 * 
	 * @return the compound Datum to which the Datum object belongs, if there
	 *	is one. If it belongs to no compound Datum, return a null.
	 */
    public Datum getParent();
	//-------------------------------------------------------------------------

    /**
     * 
     * @return a standard, terse string describing the type of the Datum. This
	 * string should be short and should conform to the syntax of the language being
	 * modelled. For example, in C++, return "int" and not "Integer".

	 * 	As long strings may not display well, terseness is particularly important
	 * in the case of compound Datums. For example, again from C++, "double[10]" is
	 * preferred over "An Array of 10 doubles". The former is shorter and conforms
	 * better to standard C++ syntax. 
     */
	public String getTypeString();
	//-------------------------------------------------------------------------

     /**
      * @return the name of the datum: E.g. foo.a[4]
      */
     public String getName() ;
 	//-------------------------------------------------------------------------

     /**
      * @return a standard, terse string describing the value of the Datum. This
	  * string should be short and should conform to the syntax of the language being
	  * modelled.

	  * 	For compound Datums, consider such devices as the useful Microsoft notation
	  * "{...}" to show a non-empty structure (and "null" for an empty one).
	  * The display interfaces support expansion capability to reveal the internals
	  * of compound datums, rendering long strings containing all the internal
	  * information unnecessary.
      */
	public String getValueString();
	/*	Return  */
	//-------------------------------------------------------------------------

	public int getNumBytes();
	/* return the physical size of the Datum, in bytes. This should correspond
	to the range of addresses spanned by the Datum and may be larger than the
	logical size (the sum of all the sizes of child Datums). That is, the Datum
	may include	some holes, for example because of alignment restrictions in a
	structure, in which case the size should include the holes.
	*/
	//-------------------------------------------------------------------------

	public int getAddress();
	// Return the beginning address at which the Datum starts.
	//-------------------------------------------------------------------------

	public int getByte(int i);
	/* Return the byte i of the Datum object. The return value should be on the 
	range [0,255]. */
	//-------------------------------------------------------------------------

	//public void putByte(int i, int val);
	/* Change the byte i of the Datum object. The new value should be in the 
	range [0,255]. 
	TSN 99.08.23.  Why is this here? The only reason I can see for putByte
	to be declared at a language neutral level is that the stack
	uses only datums to fill the memory with garbage.
	If there is a good reason, then it should be documented.
	*/
	//-------------------------------------------------------------------------

    public int getNumChildren() ;
    /* Returns the number of children.
       This does not include any empty datums.
       For top-level regions, like the stack and the heap, this is the
       number of variables directly contained in the region -- e.g. the
       number of variables on the stack.
       For arrays, this is the number of elements of the array.
       For Records, this is the number of fields in the record.
       For noncompound --including pointers and references-- 
       types, this is zero.
    */
	//-------------------------------------------------------------------------
    
    public Datum getChildAt(int i) ;
    /* Precondition:  0 _< i _< getChildrenCount() */
    /* Returns child i (counted from 0 of course.) */
	//-------------------------------------------------------------------------
    
    public int getBirthOrder() ;
    /* Precondition:  getParent() != null */
    /* Returns order among siblings (counted from 0 of course.) */
	//-------------------------------------------------------------------------

    public String getChildLabelAt(int i) ;
    /* Precondition:  0 _< i _< getChildrenCount() */
    /* Returns a label for the child.
       For top-level regions, the name of the variable is returned.
       For if the child is an anonymous variable (e.g. a heap variable
       or a compiler temp) null is returned.
       For arrays, the index is returned, e.g "0", "1", ... (in C++)
       For structures, the field name is returned. */
	//-------------------------------------------------------------------------
    
	public TypeInterface getType();
	/* Return the type of the data.
	*/ 
	//-------------------------------------------------------------------------

    public void setProperty( String name, Object info ) ;
    /* Sets the property named name. */
	//-------------------------------------------------------------------------
	
    public Object getProperty(String name) ;
    /* Retrieves the object last associated with the property named name.
       If there is no such property, then null is returned. */
	//-------------------------------------------------------------------------
}