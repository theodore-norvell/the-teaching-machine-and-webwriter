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

import tm.clc.analysis.Declaration;
import tm.clc.analysis.LFlags;
import tm.clc.analysis.ScopedName;
import tm.clc.analysis.SpecifierSet;
import tm.clc.ast.TypeNode;
import tm.cpp.ast.TyArray;
import tm.cpp.ast.TyClass;
import tm.cpp.ast.TyFloating;
import tm.cpp.ast.TyFun;
import tm.cpp.ast.TyFundamental;
import tm.cpp.ast.TyIntegral;
import tm.cpp.ast.TyPointer;
import tm.cpp.ast.TyRef;
import tm.utilities.Assert;
import tm.utilities.Debug;

/** 
 * Extracts the AST representation of a type given a set of 
 * specifiers/modifiers, and the type name if user-defined.
 * <br>See ISO 7.1.5.2, Table 7 for simple type specifier rules.
 */
public class TypeExtractor implements Cpp_Specifiers, Cpp_LFlags, 
                                      FundamentalTypeUser {

    private static final String CANNOT_EXTRACT_TYPE = 
        "cannot extract a type given the information provided";

    private static Hashtable types = new Hashtable ();
    static {
        // no SIGNED / UNSIGNED designation
        // char
        types.put (new Integer (SP_CHAR), tyChar);
        // ???
        // types.put (new Integer (SP_WCHAR_T), tyUnsignedChar);
        // bool
        types.put (new Integer (SP_BOOL), tyBool);
        // short
        types.put (new Integer (SP_SHORT * 100), tyShortInt);
        // short int
        types.put (new Integer (SP_SHORT * 100 + SP_INT), tyShortInt);
        // int
        types.put (new Integer (SP_INT), tyInt);
        // long
        types.put (new Integer (SP_LONG * 100), tyLong);
        // long int
        types.put (new Integer (SP_LONG * 100 + SP_INT), tyLong);
        // float
        types.put (new Integer (SP_FLOAT), tyFloat);
        // double
        types.put (new Integer (SP_DOUBLE), tyDouble);
        // long double
        types.put (new Integer (SP_LONG * 100 + SP_DOUBLE), tyLongDouble);
        // void
        types.put (new Integer (SP_VOID), tyVoid);
        
        // SIGNED
        int sd = SP_SIGNED * 10000;
        // signed
        types.put (new Integer (sd ), tyInt);
        // signed char
        types.put (new Integer (sd + SP_CHAR), tySignedChar);
        // signed short
        types.put (new Integer (sd + SP_SHORT * 100), tyShortInt);
        // signed short int
        types.put (new Integer (sd + SP_SHORT * 100 + SP_INT), tyShortInt);
        // signed int
        types.put (new Integer (sd + SP_INT), tyInt);
        // signed long
        types.put (new Integer (sd + SP_LONG * 100), tyLong);
        // signed long int
        types.put (new Integer (sd + SP_LONG * 100 + SP_INT), tyLong);
        
        // UNSIGNED
        sd = SP_UNSIGNED * 10000;
        // unsigned 
        types.put (new Integer (sd), tyUnsignedInt);
        // unsigned char
        types.put (new Integer (sd + SP_CHAR), tyUnsignedChar);
        // unsigned short
        types.put (new Integer (sd + SP_SHORT * 100), tyUnsignedShortInt);
        // unsigned short int
        types.put (new Integer (sd + SP_SHORT * 100 + SP_INT), 
                   tyUnsignedShortInt);
        // unsigned int
        types.put (new Integer (sd + SP_INT), tyUnsignedInt);
        // unsigned long
        types.put (new Integer (sd + SP_LONG * 100), tyUnsignedLong);
        // unsigned long int
        types.put (new Integer (sd + SP_LONG * 100 + SP_INT), 
                   tyUnsignedLong);
    }

    private static Debug debug = Debug.getInstance ();

    private Cpp_CTSymbolTable symtab;

    /** 
     * Creates a new type extractor instance. 
     * @param symbol_table the compile-time symbol table to use 
     * when locating user-defined types
     */
    public TypeExtractor (Cpp_CTSymbolTable symbol_table) {
        this.symtab = symbol_table;
    }

    /** 
     * Extract the runtime representation of a type given the 
     * specifiers/modifiers
     * @param spec_set the specifiers/modifiers and possibly type name
     * @return the type
     */
    public TypeNode extract_type (SpecifierSet spec_set) {
        TypeNode type = null;

        // cv-qualification
        int cvq = 0;
        
        if (spec_set.contains (SP_CONST)) cvq |= CVQ_CONST;
        if (spec_set.contains (SP_VOLATILE)) cvq |= CVQ_VOLATILE;
        
        // type generation
        ScopedName type_name = spec_set.get_type_name ();
        if (type_name == null) { 
            // fundamental type
            int d_sign = 
                (spec_set.contains (SP_SIGNED)) ? SP_SIGNED 
                : (spec_set.contains (SP_UNSIGNED)) ? SP_UNSIGNED
                : 0;
            
            int d_size = 
                (spec_set.contains (SP_SHORT)) ? SP_SHORT
                : (spec_set.contains (SP_LONG)) ? SP_LONG
                : 0;
            
            int d_type = 0;
            for (int i = SP_CHAR; i <= SP_VOID && d_type == 0; i++)
                if (spec_set.contains (i) && 
                    !(i == SP_SHORT) && !(i == SP_LONG) && 
                    !(i == SP_SIGNED) && !(i == SP_UNSIGNED)) {
                    d_type = i;
                    break;
                }
            
            

            if (d_type + d_size + d_sign== 0) { 
                // no type name and no type id. ExtractType was called
                // for a declaration with no type information. This can 
                // happen when parsing constructor declarations/definitions.
                // - we return a tyNone (no type TypeNode).
                type = tyNone;
            } else {
                TyFundamental typeRep = (TyFundamental) types.get 
                    (new Integer (d_sign * 10000 + d_size * 100 + d_type)); 
                if (typeRep == null) Assert.apology (CANNOT_EXTRACT_TYPE);
            
                type = typeRep.getQualified (cvq);
            }
        } else { // user-defined type
            // ** lookup contexts
            LFlags flags = TYPE_LF.writable ();
            // elaborated type specifier ?
            for (int i = SP_CLASS; i <= SP_ENUM; i++) 
                if (spec_set.contains (i)) {
                    flags.set (ELABTYPE);
                    break;
                }
            
            Declaration match = symtab.lookup (type_name, flags).getSingleMember ();
            if (match == null) Assert.apology (CANNOT_EXTRACT_TYPE);
            type = match.getType ();
            if (type == null) Assert.apology (CANNOT_EXTRACT_TYPE);
        }
        
        return type;
    }

    /**
     * Gives the type categorization related to the provided type. This
     * is most often used as a <code>Declaration</code> category.
     */
    public int categorizeType (TypeNode t) { 
        int category = 0;
        if (t instanceof TyFundamental) {
            category = (t instanceof TyIntegral) 
                ? VTB
                : (t instanceof TyFloating) 
                ? VTC
                : VTA; // void
        } else if (t instanceof TyRef) { 
            category = VTH;
        } else if (t instanceof TyPointer) {
            category = VTD;
        } else if (t instanceof TyArray) {
            // are the elements POD or non-POD ?
            int elCat = categorizeType (((TyArray) t).getElementType ());
            category = ((POD & elCat) != 0) 
                ? VTE 
                : VTF;
        } else if (t instanceof TyFun) {
            category = VTH;
        } else if (t instanceof TyClass) { // Case added by TSN 2002 Sep 4.
            category = categorizeClass ( ((TyClass)t).getDeclaration () ) ;
        } else { // user defined type 
            Assert.check(false);
        }
        return category;
    }



    /**
     * Gives the type categorization related to the provided user-defined type.
     * <br>This is determined from scratch - the <code>Declaration</code>'s 
     * <code>category</code> attribute is unused and untouched.
     * @param d the compile-time representation of the type. 
     */
    public int categorizeClass (Declaration d) {
        TyClass ct = (TyClass) d.getType ();
        int category = 0;
        if (ct.isDefined ()) {

            ClassSH cscope = (ClassSH) d.getDefinition ();

            boolean aggregate = true, pod = true;

            LFlags [] aggregateTest = new LFlags [] 
                { // user-defined constructors ?
                new LFlags (M_MEMBER_FN + M_USER_DEFINED + M_CONSTRUCTOR), 
                // private or protected non-static data members ?
                new LFlags (M_DATA_MEMBER + M_PRIVATE + M_AUTO),
                new LFlags (M_DATA_MEMBER + M_PROTECTED + M_AUTO), 
                // base classes ?
                new LFlags (M_BASE_CLASS),
                // virtual functions ? 
                new LFlags (M_MEMBER_FN + M_VIRTUAL)
                    };

            aggregate = cscope.lookup (aggregateTest).isEmpty ();
            debug.msg (Debug.COMPILE, "aggregate : " + aggregate);

            LFlags [] podTest = new LFlags []
            {   // non-static data members of type reference ?
                new LFlags (M_DATA_MEMBER + M_AUTO + M_REFERENCE),
                // non-static data members of type pointer to member or array 
                // thereof ? -- not currently supported
                // non-static data members of type non-POD or array thereof ?
                new LFlags (M_DATA_MEMBER + M_AUTO + M_NON_POD),
                // user-defined destructor ?
                new LFlags (M_MEMBER_FN + M_USER_DEFINED + M_DESTRUCTOR),
                // user-defined assignment operator ?
                new LFlags (M_MEMBER_FN + M_USER_DEFINED + M_COPY + M_OPERATOR)
                    };

            pod = aggregate && cscope.lookup (podTest).isEmpty ();
            debug.msg (Debug.COMPILE, "pod : " + pod);
            
            category = (aggregate) ? ((pod) ? VTE : VTF) : VTG;
            
        } else { 
            // Give a temporary class designation so that broad
            // categorizations will work. 
            // The final designation will be made when the type is 
            // complete.
            category = INCOMPLETE_CLASS;
        }
        
        return category;
   
    }

    /** 
     * Determines various properties of a function definition, suitable for 
     * building its compile-time representation.
     * @param fd the function definition object
     * @return an <code>LFlags</code> object containing relevant categorizations
     */
    public LFlags getFunctionCategories (ScopedName name) {
        // determining function type:
        // member function:
        // - if qualified name, the qualifier preceding the terminal id is 
        //   a class name
        // - else we are in a class scope and there is no friend specifier
        // regular function:
        // - if an unqualified id:  in scope other than class scope, or in class 
        //   scope but has a friend specifier
        // - if a qualified id:  anywhere when the qualifier preceding the terminal 
        //   id isn't a class name

        // !! this is here as a placeholder -- will the function definition object
        //    hold specifiers ? (friend, static, inline etc)
        SpecifierSet spec_set = null;

        LFlags fnCategory = null;
        ClassSH owningC = symtab.memberOf (name);
        boolean inClass = owningC != null;
        debug.msg (Debug.COMPILE, "inClass is " + inClass);
        boolean friend = (spec_set != null && 
                          spec_set.contains (SP_FRIEND));
            
        fnCategory = (!inClass || friend) 
            ? Cpp_LFlags.REG_FN_LF 
            : Cpp_LFlags.MEM_FN_LF; 

        // set type group
        fnCategory = fnCategory.writable ();
        fnCategory.set (Cpp_LFlags.VTH);

        // member function qualification
        if (fnCategory.contains (Cpp_LFlags.MEM_FN)) {
            fnCategory.set (Cpp_LFlags.CLASS_MEMBER); // redundant, but used
            Declaration classDecl = owningC.getOwnDeclaration ();
            
            // constructor ?
            if ( name.getTerminalId ()
          		 .equals(classDecl.getName().getTerminalId ()) ) {
                fnCategory.set (Cpp_LFlags.CONSTRUCTOR); }
            else if ( name.getTerminalId ()
            		  .equals("~" + classDecl.getName().getTerminalId ()) ) {
                        fnCategory.set (Cpp_LFlags.DESTRUCTOR);
                    }
        } 

        // ** always user-defined, autogenerated methods will unset this flag
        fnCategory.set (Cpp_LFlags.USER_DEFINED);
        
        return fnCategory;
        
    }


    
}
