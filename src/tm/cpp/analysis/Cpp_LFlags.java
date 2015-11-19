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

package tm.cpp.analysis;

import java.util.Hashtable;

import tm.clc.analysis.LFConst;
import tm.clc.analysis.LFlags;

/**
 * static definitions of C++ lookup/type flags and related <code>LFConst</code>
 * objects
 */
public interface Cpp_LFlags { 

	/** just loads <code>String</code> representations of some flags 
	 * for messages and debugging 
	 */
	public static FlagStringLoader fls = new FlagStringLoader ();
	
	/** flag indicating nothing set/specified */
	public static final int EMPTY = LFlags.EMPTY;
    public static final LFConst EMPTY_LF = LFlags.EMPTY_LF;

    /** language entities flag range */
    public static final int LELEMENT  = LFlags.LELEMENT;

    /** variable type flag range */
    public static final int VTYPE     = LFlags.VTYPE;
    public static final int VNONE     = LFlags.VNONE;

    /** lookup context / directive flag range */
    public static final int LCONTEXT = LFlags.LCONTEXT;
    public static final int LDEFAULT = LFlags.LDEFAULT;

	/** flag indicating a variable */
	public static final int VARIABLE = LFlags.VARIABLE;
	public static final LFConst VARIABLE_LF = new LFConst (VARIABLE);

	/** flag indicating a regular function */
    public static final int REG_FN    = 0x2;
    public static final LFConst REG_FN_LF = new LFConst (REG_FN);

	/** flag indicating a member function */
    public static final int MEM_FN    = 0x4;
    public static final LFConst MEM_FN_LF = new LFConst (MEM_FN);

	/** flag indicating an enumeration */
    public static final int ENUM      = 0x8;
    public static final LFConst ENUM_LF = new LFConst (ENUM);

	/** flag indicating a typedef */
    public static final int TYPEDEF   = 0x10;
    public static final LFConst TYPEDEF_LF = new LFConst (TYPEDEF);

	/** flag indicating a class, struct or union */
	public static final int CLASS = LFlags.CLASS;
    public static final LFConst CLASS_LF = new LFConst (CLASS);

	/** flag indicating a namespace */
    public static final int NAMESPACE = 0x40;
    public static final LFConst NAMESPACE_LF = new LFConst (NAMESPACE);

	/** flag indicating a label */
    public static final int LABEL     = 0x80;
    public static final LFConst LABEL_LF = new LFConst (LABEL);

    // language element categories
    /** FUNCTION  = REG_FN + MEM_FN *** (defined in LFlags) */
	public static final int FUNCTION = LFlags.FUNCTION;

	/** language element flag indicating a type */
    public static final int TYPE      = ENUM + TYPEDEF + CLASS;
	public static final LFConst TYPE_LF = new LFConst (TYPE);
    
    public static final int FUNCTION_OR_TYPDEF = FUNCTION + TYPEDEF ;

	/** language element flag indicating an entity that can be used as an 
	 * id qualification
	 */
    public static final int QUALIFIER = TYPEDEF + CLASS + NAMESPACE;
 
	/** language element flag indicating an element that can have an <em>extended
	 * type specifier</em>
	 */
	public static final int EXTENDED  = CLASS + ENUM;

	/** language element flag indicating an entity containing scope */
    public static final int SCOPED    = FUNCTION + NAMESPACE + CLASS; //typedef

	/** language element flag indicating a non-type */
	public static final int NONTYPE   = FUNCTION + VARIABLE; // hide class or

	// enum declarations in same scope unless elaborated type specifier is used
	// 3.3.7
	public static final int ANY       = 0x0;


	// ** var/type categorizations
 
	// ** C++ - specific categorizations
	// seemingly mysterious groupings are defined according to membership in 
	// the many and overlapping type categorizations
	public static final int VT_GROUP = 0xFF000;
	public static final int VTA = 0x1000;  // void
	public static final int VTB = 0x2000;  // integral
	public static final int VTC = 0x4000;  // floating
	public static final int VTD = 0x8000;  // enum, ptr, ptr-to-mem
	public static final int VTE = 0x10000; // POD array, POD class 
	public static final int VTF = 0x20000; // non-POD array, non-POD class >
	// .. without user-defined constructors, non-public non-static data mems, 
	// .. without base-classes, virtual fns
	public static final int VTG = 0x40000; // rest of non-POD classes
	public static final int VTH = 0x80000; // function or reference

	public static final int FUNDAMENTAL = VTA + VTB + VTC;
	public static final int COMPOUND = VTD + VTE + VTF + VTG + VTH;
	public static final int OBJECT_TYPE = VTB + VTC + VTD + VTE + VTF + VTG;
	public static final int INTEGRAL = VTB;
	public static final int FLOATING = VTC;
	public static final int ARITHMETIC = VTB + VTC;
	public static final int SCALAR =  VTB + VTC + VTD;
	public static final int AGGREGATE = VTE + VTF;
	public static final int POD = VTB + VTC + VTD + VTE + VTF;
	public static final int NON_POD = VTF + VTG;

	public static final int INCOMPLETE_CLASS = VTG; // most restrictive guess

	// member modifiers
	public static final int USER_DEFINED = 0x100000;
	public static final int CONSTRUCTOR  = 0x200000;
	public static final int CLASS_MEMBER = 0x400000;
	public static final int DESTRUCTOR   = 0x800000;

    // ** lookup context / directives

	/** lookup context flag indicating the id is qualified as a class or 
	 * object member
	 */
    public static final int CLASSREF = 0x1000; // class or object member ref
    public static final LFConst CLASSREF_LF = new LFConst (CLASSREF);

	/** lookup context flag indicating the id is encountered in a declaration 
	 * statement
	 */
    public static final int DECLSTAT = 0x2000; // declaration statement
    public static final LFConst DECLSTAT_LF = new LFConst (DECLSTAT);

	/** lookup context flag indicating the search should halt in the nearest
	 * enclosing namespace
	 */
    public static final int ENCLNS = 0x4000; // search to enclosing namespace
    public static final LFConst ENCLNS_LF = new LFConst (ENCLNS);

	/** lookup context flag indicating the search should search the class
	 * heirarchy
	 */
	public static final int CLASSHIER = 0x8000; // class hierarchy lookup
	public static final LFConst CLASSHIER_LF = new LFConst (CLASSHIER);

	/** lookup context flag indicating the search should follow the rules for 
	 * qualified id lookup 
	 */
	public static final int QUALIFIED = 0x10000; // use qualified lookup rules
	public static final LFConst QUALIFIED_LF = new LFConst (QUALIFIED);

	/** lookup context flag indicating the search is for a prior declaration 
	 * of the id
	 */
	public static final int PRIORDECL = 0x20000; // look for prior declaration
	public static final LFConst PRIORDECL_LF = new LFConst (PRIORDECL);

	/** lookup context flag indicating the search is for an id used in the 
	 * context of a qualified destructor expression
	 */
	public static final int QUALDEST = 0x40000; // qualified destructor lookup
	public static final LFConst QUALDEST_LF = new LFConst (QUALDEST);

	/** lookup context flag indicating the search is occurring in the context of an 
	 * elaborated type specifier (e.g. class C, struct S)
	 */
	public static final int ELABTYPE = 0x80000; // elaborated type specifier precedes id
	public static final LFConst ELABTYPE_LF = new LFConst (ELABTYPE);

	//combinations
	/* combination lookup context flag indicating local and class hierarchy 
	 * search only.
	 */
	public static final int INTERNAL = CLASSHIER + QUALIFIED + QUALDEST + PRIORDECL;

	/** combination lookup context flag indicating local scope search only. */ 
	public static final int LOCAL = PRIORDECL;


	/**
	 * Class member type lookup flags. 
	 * <br>All flags prefixed with <code>M_</code> are in this category.
	 * <br>These flags are intended for use only as query attributes when 
	 * looking for a specific range of class element types.
	 * @see ClassSH
	 */
	public static final int M_ENTITY_TYPE = 0xFF;
	public static final int M_DATA_MEMBER = 0x1;
	public static final int M_MEMBER_FN = 0x2;
	public static final int M_BASE_CLASS = 0x4;	
	public static final int M_INNER_CLASS = 0x8;
	public static final int M_FRIEND = 0x10;
	public static final int M_TYPEDEF = 0x20;
	public static final int M_CONSTRUCTOR = 0x100;
	public static final int M_DESTRUCTOR = 0x200;
	public static final int M_OPERATOR = 0x400;
	public static final int M_DEFAULT = 0x800;
	public static final int M_COPY = 0x1000;
	public static final int M_CONVERSION = 0x800000; // OK to be same as M_REFERENCE
	public static final int M_VIRTUAL = 0x2000;
	public static final int M_EXPLICIT = 0x4000;
	public static final int M_STATIC = 0x8000;
	public static final int M_POD = 0x10000;
	public static final int M_CONST = 0x200000;	
	public static final int M_AUTO = 0x40000;
	public static final int M_NON_POD = 0x80000;
	public static final int M_PRIVATE = 0x100000;
	public static final int M_PROTECTED = 0x200000;
	public static final int M_PUBLIC = 0x400000;
	public static final int M_REFERENCE = 0x800000;
	public static final int M_USER_DEFINED = 0x1000000;
	public static final int M_AUTOGEN = 0x2000000;
	public static final int M_RECURSE = 0x4000000;
	public static final int M_DISTINCT = 0x8000000;


	/** just loads string representations into 
	 * <code>LFlags.flagStrings</code> table 
	 */
	public static class FlagStringLoader {
		static { 
			Hashtable fs = LFlags.flagStrings;
			fs.put (new Integer (VARIABLE), "variable");
			fs.put (new Integer (REG_FN), "regular function");
			fs.put (new Integer (MEM_FN), "member function");
			fs.put (new Integer (ENUM), "enumeration");
			fs.put (new Integer (TYPEDEF), "typedef");
			fs.put (new Integer (CLASS), "class, struct or union");
			fs.put (new Integer (NAMESPACE), "namespace");
			fs.put (new Integer (LABEL), "label");
			fs.put (new Integer (FUNCTION), "function");
			fs.put (new Integer (TYPE), "type");
			fs.put (new Integer (QUALIFIER), "qualifier");
			fs.put (new Integer (EXTENDED), "can have extended type specifier");
			fs.put (new Integer (SCOPED), "has a scope");
			fs.put (new Integer (NONTYPE), "non-type");
			fs.put (new Integer (COMPOUND), "compound");
			fs.put (new Integer (SCALAR), "scalar");
			fs.put (new Integer (CLASSREF), "member reference");
			fs.put (new Integer (DECLSTAT), "declaration statement");
			fs.put (new Integer (ENCLNS), "search to enclosing namespace");
			fs.put (new Integer (CLASSHIER), "search class hierarchy");
			fs.put (new Integer (QUALIFIED), "qualified id lookup");
			fs.put (new Integer (PRIORDECL), "search for prior declaration");
			fs.put (new Integer (QUALDEST), "qualified destructor lookup");
			fs.put (new Integer (INTERNAL), "only search locally and class hierarchy");
			fs.put (new Integer (LOCAL), "only search locally");
		}
	}

}

