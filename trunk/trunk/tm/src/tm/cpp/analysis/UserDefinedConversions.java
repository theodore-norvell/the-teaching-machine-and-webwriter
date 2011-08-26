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

import java.util.Enumeration;

import tm.clc.analysis.CTSymbolTable;
import tm.clc.analysis.Declaration;
import tm.clc.analysis.DeclarationSet;
import tm.clc.analysis.FunctionDeclaration;
import tm.clc.analysis.LFlags;
import tm.clc.ast.TyAbstractClassDeclared;
import tm.clc.ast.TypeNode;
import tm.cpp.ast.TyRef;
import tm.utilities.Assert;

/**
 * Identifies user-defined conversions
 * <br>Can also build the AST representation of the conversion.
 */
public class UserDefinedConversions implements Cpp_LFlags {
    private static final String CONVERSION_AMBIGUOUS = 
        "Sorry, conversion from {0} to {1} is ambiguous";
    StandardConversions sc = StandardConversions.getInstance ();
    Cpp_CTSymbolTable st;

    
    public UserDefinedConversions (CTSymbolTable st) {
        this.st = (Cpp_CTSymbolTable) st;
    }

    
    /** 
     * Provide the implicit conversion sequence required given the 
     * parameters. May involve a single user-defined conversion. 
     * <br>An apology will be generated if no implicit conversion sequence
     * exists for the two types
     * @param from the type to convert from
     * @param to the type to convert to
     * @return the implicit conversion sequence to apply 
     */
    public ConversionSequence getConversionSequence (TypeNode from, 
                                                     TypeNode to) {
        return this.getUserDefinedConversionSequence (from, to, -1);
    }

    /** 
     * Provide the implicit conversion sequence required given the 
     * parameters. May involve a single user-defined conversion. 
     * <br>An apology will be generated if no implicit conversion sequence
     * exists for the two types
     * @param from the type to convert from
     * @param to the type to convert to
     * @param flags constraining the conversions that can be performed in 
     * the context of the call
     * @return the implicit conversion sequence to apply 
     */
    public ConversionSequence getUserDefinedConversionSequence 
        (TypeNode from, TypeNode to, int flags) {
        boolean suppressed = false; // is this conversion suppressed by flags ?
        ConversionSequence seq =
            new ConversionSequence (ConversionSequence.USER_DEFINED_CS);


        // 1. 0..1 standard conversion sequence
        // 2. 1 user-defined conversion
        // 3. 0..1 standard conversion sequence

        // case 1: built-in type f --> TyClass t
        // possible routes:
        // - constructor exists in t taking an f or something that f can
        //   be converted to via standard conversion sequence
        // there can be only one possible sequence, otherwise conversion
        // is ambiguous

        // case 2: TyClass f --> built-in type t
        // possible routes:
        // - conversion function exists in f converting to t or something
        //   that can be converted to t via standard conversion sequence
        // there can be only one possible sequence, otherwise conversion
        // is ambiguous

        // case 3: TyClass f --> TyClass t 
        // possible routes:
        // - equivalent - no conversion required
        // - constructor exists in t taking an f or superclass thereof
        // - conversion function exists in f converting to t or subclass thereof

        if (from.equal_types (to)) { /*CHECK*/
            seq = sc.getStandardConversionSequence (from, to, flags); 

        } else {
            DeclarationSet ds;
            ConversionSequence scs;
            boolean sequenceFound = false;
            if (to instanceof TyAbstractClassDeclared) {
                // find the class declaration
                Declaration c_decl = st.lookup 
                    (((TyAbstractClassDeclared) to).getFullyQualifiedName ())
                    .getSingleMember (); 

                Assert.check (c_decl != null && 
                               c_decl.getDefinition () instanceof ClassSH);
                ClassSH c_scope = (ClassSH) c_decl.getDefinition ();
                
                // look through the constructors for candidate conversion
                // constructors
                ds = c_scope.lookup (new LFlags (M_MEMBER_FN + 
                                                 M_CONSTRUCTOR + 
                                                 M_CONVERSION));
                for (Enumeration els = ds.elements (); 
                     els.hasMoreElements (); ) {
                    FunctionDeclaration fd = 
                        (FunctionDeclaration) els.nextElement ();
                    TypeNode tto = fd.getParameter (0);
                    if (tto instanceof TyRef)
                        tto = ((TyRef) tto).getPointeeType ();
                    
                    scs = sc.getStandardConversionSequence 
                        (from, tto, flags);
                    
                    if (scs != null) {
                        
                        if (!sequenceFound) {
                            sequenceFound = true;
                            seq.addConversions (scs);
                            seq.addConversion (sc.USER_DEFINED, fd);
                            
                        } else { // error - ambiguous conversion
                            Assert.apology (CONVERSION_AMBIGUOUS, 
                                            new String []
                                {from.getTypeString (), 
                                 to.getTypeString ()});
                        }
                    }
                }
            }

            if (from instanceof TyAbstractClassDeclared) {
                // find the class declaration
                Declaration c_decl = st.lookup 
                    (((TyAbstractClassDeclared) from).getFullyQualifiedName ())
                    .getSingleMember (); 

                Assert.check (c_decl != null && 
                               c_decl.getDefinition () instanceof ClassSH);
                ClassSH c_scope = (ClassSH) c_decl.getDefinition ();

                // look for candidate conversion functions
                ds = c_scope.lookup (new LFlags (M_MEMBER_FN + 
                                                 M_OPERATOR + 
                                                 M_CONVERSION));
                for (Enumeration els = ds.elements (); 
                     els.hasMoreElements (); ) {
                    FunctionDeclaration fd = 
                        (FunctionDeclaration) els.nextElement ();

                    TypeNode ftt = fd.getType ();
                    if (ftt instanceof TyRef) 
                        ftt = ((TyRef) ftt).getPointeeType ();

                    scs = sc.getStandardConversionSequence (ftt, to, flags);
                    if (scs != null) {
                        if (!sequenceFound) {
                            sequenceFound = true;
                            seq.addConversion (sc.USER_DEFINED, fd);
                            seq.addConversions (scs);
                        } else // error - ambiguous conversion
                            Assert.apology (CONVERSION_AMBIGUOUS, 
                                            new String []
                                {from.getTypeString (), to.getTypeString ()});
                    }
                }
                    
            }
        }

        return seq;
    }
    
}
