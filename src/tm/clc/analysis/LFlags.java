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



import java.util.Hashtable;

import tm.utilities.Assert;




/**
 * "lookup flags"
 * <ol>LFlags serve two related functions:
 * <li>they are passed to id resolution methods in the compile
 * time symbol table to direct and constrain the lookup logic.</li>
 * <li>they are used to help identify the "category" of 
 * <code>Declaration</code> a particular instance falls under.</li></ol> 
 * <p>In this way, lookup flags can be passed to the symbol table such that
 *  particular type(s) of <code>Declaration</code> can be searched for or 
 * ruled out during lookup. In addition, the category provides information
 * useful when determining what to do once an id is resolved.
 * <p> <strong>format</strong>
 * <br>digits 0-2 (0 least significant) : language entity identifiers
 * <br>digits 3-5 : additional flags, meaning dependent on use of LFlag and 
 * value of first three digits:
 * <ul>
 * <li>when used as a lookup parameter, they contain lookup context information
 * <li>when a Declaration category, contain properties specific to the 
 *     language element identified in 0-2. For example, a variable will provide
 *     basic type information here (scalar, compound, POD, etc.).
 * </ul>
 * <br>digit 6 : modifier for additional, language-dependent uses of flags. 
 */

public class LFlags {

	/** 
	 * Contains a mapping from flag constants to <code>Strings</code>.
	 * Elements in this table are to be added by extending, language
	 * specific classes. 
	 */

    public static Hashtable flagStrings = new Hashtable ();

	/**
	 * Error message provided when the <code>flagStrings</code> table
	 * is accessed before it is built.
	 */

    protected static final String NO_FLAG_STRING_MAP = 
            "Mapping from flag values to Strings has not been built";


	/** flag indicating nothing set/specified */
    public static final int EMPTY = 0;
    public static final LFConst EMPTY_LF = new LFConst (EMPTY);

    static { flagStrings.put (new Integer (EMPTY), "nothing set/specified"); }

    // language entities
    /** language entity flag range */

    public static final int LELEMENT  = 0xFFF;

	/** a variable declaration */
    public static final int VARIABLE  = 0x1;

	// see Cpp implementation for reason of value assignments
	/** a function declaration */

    public static final int FUNCTION  = 0x6; 

	/** a class declaration */
    public static final int CLASS     = 0x20;

    // variable types
    /** variable type flag range */
    public static final int VTYPE     = 0xFFF000;
    public static final int VNONE     = 0x0;

    // lookup context / directive
    /** lookup context flag range */
    public static final int LCONTEXT = 0xFFF000;
    public static final int LDEFAULT = 0x0;


    /**
	 * This is the actual value underlying the flag set. Should be read as 
	 * a bitfield (32 bit), with sections and meanings as defined in the class
	 * documentation.
	 */
	protected int val;

    /**
	 * Creates a new <code>LFlags</code> instance with no flags set.
	 */
	public LFlags () { val = 0; }

    /**
     * Creates a new <code>LFlags</code> instance with flags set according
     * to those set in <code>rawVal</code>
     * @param rawVal an <code>int</code> value, interpreted as a bitfield.
     */

	public LFlags (int rawVal) { val = rawVal; }

    /**
     * Copy constructor.
     * @param flags instance will have same flags set 
     */
	public LFlags (LFlags flags) { val = flags.val; }

    /**
     * Return the flags as an <code>int</code>
     *
     * @return an <code>int</code> value representing the flags set.
     */
    public int getRawVal () { return val; }

    /**
     * Replace flags with a new set represented as an <code>int</code>
     *
     * @param rawVal <code>int</code> representation of a flag set.
     */
    public void classify (int rawVal) { val = rawVal; }
    
    /**
     * Replace flags with a new set represented in <code>flag</code>
     *
     * @param flag an <code>LFlags</code> value.
     */
    public void classify(LFlags flag){ classify(flag.val); }   
    

    /**
     * Sets any flags present in <code>flag</code> that are not currently set.
     *
     * @param flag an <code>LFlags</code> value
     */
    public void set (LFlags flag) { set (flag.val); }

    /**
     * Sets any flags in <code>rv</code> (interpreted as a bit field) that 
     * are not currently set.
     *
     * @param rv an <code>int</code> value taken as a bit field.
     */
    public void set (int rv) { val += (rv & ~val); }

    /**
     * Unsets any flags present in <code>flag</code> that are currently set. 
     *
     * @param flag a <code>LFlags</code> value
     */
    public void unset (LFlags flag) { unset (flag.val); }

    /**
     * Unsets any flags in <code>rv</code> (interpreted as a bit field) that 
     * are currently set.
     *
     * @param rv an <code>int</code> value taken as a bit field.
     */
    public void unset (int rv) { val -= (rv & val); }

    /**
     * Indicates whether all flags set in <code>flag</code> are set in this 
     * instance.
     *
     * @param flag an <code>LFlags</code> value
     * @return <code>true</code> if all flags set in <code>flag</code> are set
     * in this instance, <code>false</code> otherwise
     */
    public boolean contains (LFlags flag) { return contains (flag.val); }

    /**
     * Indicates whether all flags present in <code>rv</code> (interpreted
     * as a bit field) are set in this instance.
     *
     * @param rv an <code>int</code> value taken as a bit field
     * @return <code>true</code> if all flags set in <code>flag</code> are set
     * in this instance, <code>false</code> otherwise
     */
    public boolean contains (int rv) { return ((rv & val) == rv); }

    /**
     * Indicates whether all flags set in this instance are set in 
     * <code>flag</code>
     *
     * @param flag a <code>LFlags</code> value
     * @return <code>true</code> if all flags set in this instance are 
     * set in <code>flag</code>, <code>false</code> otherwise
     */
    public boolean in (LFlags flag) { return in (flag.val); }

    /**
     * Indicates whether all flags set in this instance are present in 
     * <code>rv</code> (interpreted as a bit field)
     *
     * @param rv an <code>int</code> value (taken as a bit field) 
     * @return <code>true</code> if all flags set in this instance are 
     * present in <code>rv</code>, <code>false</code> otherwise
     */
    public boolean in (int rv) { return ((rv & val) == val); }

    /**
     * Do the flags intersect or are they disjoint?
     *
     * @param flag a <code>LFlags</code> value
     * @return <code>true</code> if they intersect, <code>false</code> if 
     * disjoint.
     */
    public boolean intersects (LFlags flag) { return intersects (flag.val); }

    /**
     * Do the flags intersect or are they disjoint? (<code>rv</code> 
     * interpreted as a bit field)
     *
     * @param rv an <code>int</code> value taken as a bit field
     * @return <code>true</code> if they intersect, <code>false</code> if 
     * disjoint.
     */
    public boolean intersects (int rv) { return ((rv & val) != 0); }

    /**
     * Are the same flags set in <code>flag</code> as in this instance?
     *
     * @param flag a <code>LFlags</code> value
     * @return <code>true</code> if the same flags are set, <code>false</code>
     * otherwise
     */
    public boolean equals (LFlags flag) { return equals (flag.val); }

    /**
     * Are the same flags set in <code>rv</code> (interpreted as a bit field)
     * as in this instance?
     *
     * @param rv an <code>int</code> value taken as a bit field
     * @return <code>true</code> if the same flags are set, <code>false</code>
     * otherwise
     */
    public boolean equals (int rv) { return (rv == val); }

    /**
     * Gives an <code>int</code> representation of the flags set in the region
     * defined by <code>mask</code>.
     *
     * @param mask a <code>LFlags</code> value representing a range of flags
     * whose values should be returned (i.e. set or unset).
     * @return an <code>int</code> representation of the flag values in the 
     * indicated region. The result should be taken as a bit field, with
     * all regions not identified in <code>mask</code> zeroed. The important
     * flag values retain their relative positioning in the bit field.
     */
    public int get (LFlags mask) { return get (mask.val); }

    /** 
     * Gives an <code>int</code> representation the flags set in the 
     * region defined by <code>mask</code> (interpreted as a bit field).
     * @param mask an <code>int</code> value representing a range of flags
     * whose values should be returned (i.e. set or unset).
     * @return an <code>int</code> representation of the flag values in the 
     * indicated region. The result should be taken as a bit field, with
     * all regions not identified in <code>mask</code> zeroed. The important
     * flag values retain their relative positioning in the bit field.
     */
    public int get (int mask) { return mask & val; }

    /**
     * Gives a <code>String</code> representation of the flags set in the
     * masked area. A representation will not be built dynamically according
     * flags set. Instead, a table is referenced - an inexact match results in
     * a <code>null</code> return value.
     * @param mask an <code>int</code> value
     * @return the <code>String</code> representation, <code>null</code> if 
     * none exists
     */
    public String toString (int mask) { 
        Assert.apology (!flagStrings.isEmpty (), NO_FLAG_STRING_MAP);
        return (String) flagStrings.get (new Integer (mask));
    }
    
    public String toString() {
        int bitMask = 1;
        boolean first = false;
        String verbose = new String("");
        if (contains(bitMask)) {
            verbose += toString(bitMask);
            first = true;
        }
        for (int i = 0; i < 31; i++) {
            bitMask *= 2;
            if (contains(bitMask)) {
                if (first) verbose += " + ";
                first = true;
                verbose += toString(bitMask);
            }
        }
        return verbose;
    }

    /**
     * Returns a reference to this object (already writable)
     * @return a <code>LFlags</code> value
     * @see LFConst
     */
    public LFlags writable () { 
        return this;
    }


}





