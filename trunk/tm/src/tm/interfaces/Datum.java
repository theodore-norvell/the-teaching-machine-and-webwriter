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

package tm.interfaces;


/**
* <h2> Overview:</h2>
* <p>This interface defines the methods to be provided by all types of data items. 
* Datum objects, representing different types of identifiers, form the building 
* blocks of a Store object.</p>
* 
* @author mpbl
*/
 
/* Revision history
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
	 * Highlighting is <b>not</b> applied
	 */
    public static final int PLAIN = 0 ;
    /**
     * Highlighting is applied
     */
    public static final int HIGHLIGHTED = 1 ;
	//-------------------------------------------------------------------------
    
    /**
     * gets a highlighting constant indicating whether and how to highlight this datum
     * 
     * @return a valid highlighting constant 
     */
    public int getHighlight() ;
	//-------------------------------------------------------------------------
    
    /**
     * return a unique serial number for this datum
     * 
     * @return a valid highlighting constant 
     */
    public int getSerialNumber() ;
    //-------------------------------------------------------------------------
   
	/**
	 * gets the compound Datum to which this Datum object belongs
	 * 
	 * @return the parent Datum, if there is one. null if it belongs to no compound Datum.
	 */
    public Datum getParent();
	//-------------------------------------------------------------------------

    /**
     * Get a standard, terse string describing the type of the Datum. This
	 * string should be short and should conform to the syntax of the language being
	 * modelled. For example, in C++, return "int" and not "Integer".
	 * <p>
	 * 	As long strings may not display well, terseness is particularly important
	 * in the case of compound Datums. For example, again from C++, "double[10]" is
	 * preferred over "An Array of 10 doubles". The former is shorter and conforms
	 * better to standard C++ syntax.
	 * 
	 * @return a terse string describing the type of this Datum
     */
	public String getTypeString();
	//-------------------------------------------------------------------------

     /**
      * Get the name of this Datum (which may be fully qualified). For example: foo.a[4]
      * 
      * @return the name of this Datum
      */
     public String getName() ;
 	//-------------------------------------------------------------------------

     /**
      * Gets a standard, terse string describing the value of this Datum. The
	  * string should be short and should conform to the syntax of the language being
	  * modelled.
	  * <p>
	  * 	For compound Datums, consider notation such as "{...}".
      * <p>
	  * The display interfaces support expansion capability to reveal the internals
	  * of compound datums, rendering long strings containing all the internal
	  * information unnecessary.
	  * 
	  * @return the value string for this Datum
      */
	public String getValueString();

	/**
	* Get the physical size of the Datum, in bytes. This should correspond
	* to the range of addresses spanned by the Datum and may be larger than the
	* logical size (the sum of all the sizes of child Datums). That is, the Datum
	* may include some holes, for example because of alignment restrictions in a
	* structure, in which case the size should include the holes.
	* 
	* @return the number of bytes in the Datum
	*/
	public int getNumBytes();
	//-------------------------------------------------------------------------

	/**  Get the byte address at which this Datum starts.
	 * 
	 * @return the beginning address
	*/
	public int getAddress();
	//-------------------------------------------------------------------------
	
	/**
	 * Get byte i of this Datum (starting at 0).
	 * 
	 * @param i the number of the desired byte 
	 * @return the byte i of the Datum object. The return value will be in the 
	 * range [0,255].
	 * 
	 */

	public int getByte(int i);

    /**
     * @return the number of datum children.
     * <p>Here are some examples:
     * <ul><li> For regions, such as the stack and the heap, this is the
     *          number of variables directly contained in the region -- e.g. the
     *          number of variables on the stack.
     *    </li>
     *    <li> For C++ arrays, this is the number of elements of the array.
     *    </li>
     *    <li> For objects and structs, this is the number of fields in the record plus 
     *         the number of subobjects. 
     *    </li>
     *    <li>For scalars such as ints, doubles, including pointers and references,
     *         this is zero.
     *    </li>
     * </ul>
     */
    public int getNumChildren() ;
	//-------------------------------------------------------------------------
    
    /**
     * @param i the number of the child desired
     * Precondition:  0 _< i _< getNumChildren()
     * @return child i (counted from 0 of course.)
     */
    public Datum getChildAt(int i) ;
	//-------------------------------------------------------------------------
    
    /**
     *  Precondition:  getParent() != null
     *  @return order among siblings (counted from 0 of course.)
     */
    public int getBirthOrder() ;
	//-------------------------------------------------------------------------

    /** @param i the number of the child
     * Precondition:  0 _< i _< getNumChildren() 
     * @return a label for the child.
     * For top-level regions, the name of the variable is returned.
     * If the child is an anonymous variable (e.g. a heap variable
     * or a compiler temp) an empty string is returned.
     * For C++ arrays, the index is returned, e.g "0", "1", ...
     * For structures, the name of the field or, in the case of sub-objects, the name
     * of the superclass is returned.
     */
    public String getChildLabelAt(int i) ;
	//-------------------------------------------------------------------------

     /**
     * Attaches an arbitrary name-value pair to the Datum object
     * @param name the name for the property
     * @param info the value for the property
     **/
    public void setProperty( String name, Object info ) ;
	//-------------------------------------------------------------------------
	
    /**
     * @param name the name of the property being sought
     * @return the object last associated with the property named <code>name</code>.
     * If there is no such property, then <code>null</code> is returned.
     */
    public Object getProperty(String name) ;
	//-------------------------------------------------------------------------
}