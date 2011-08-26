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
import tm.clc.analysis.CodeGenRule;
import tm.clc.analysis.Declaration;
import tm.clc.analysis.DeclarationSet;
import tm.clc.analysis.ExpressionPtr;
import tm.clc.analysis.FunctionDeclaration;
import tm.clc.analysis.LFConst;
import tm.clc.analysis.ScopeHolder;
import tm.clc.analysis.ScopedName;
import tm.clc.ast.ExpId;
import tm.clc.ast.ExpThisMember;
import tm.clc.ast.OpMemberCall;
import tm.cpp.ast.ExpFunctionName;
import tm.cpp.ast.TyRef;
import tm.utilities.Assert;
import tm.utilities.Debug;

/**
 * Generates the AST representation of an id expression.
 */
public class CGRIdExp extends CodeGenRule {

    private static final String NO_MATCHING_ENTITY = 
        "No entity found matching id {0}.";
    private static final String NO_MATCHING_NONTYPE = 
        "No non-type entity found matching id {0}";

    CTSymbolTable symbolTable;
    /**
     * Creates a new rule instance
     * @param st the compile-time symbol table for id resolution
     */
    public CGRIdExp (CTSymbolTable st) {
        this.symbolTable = st;
    }

    public void apply (ExpressionPtr exp) {
        // name lookup
        DeclarationSet candidates = symbolTable.lookup (exp.id ());
        
        // if lookup returns nothing, error 
        if (candidates.isEmpty ()) 
            Assert.apology (NO_MATCHING_ENTITY,  exp.id().getName ());

        // if the matches are function declarations, we need to create
        // a placeholder expression, which will be used down the line to
        // make a function call expression
        Declaration firstMember = candidates.getFirstMember () ;
        if ( firstMember instanceof FunctionDeclaration) {
            ExpFunctionName efn = new ExpFunctionName (exp.id ()) ;
            exp.set (efn);
            
            // For member functions set the classScope field of the efn.
            // Added TSN 2002 Oct 8.
                ScopeHolder enclosingScopeHolder = firstMember.getEnclosingBlock() ;
                if( enclosingScopeHolder instanceof ClassSH ) {
                    efn.setClassScope( (ClassSH) enclosingScopeHolder ); }
            
        } else { // id expression

            // look for the match among the candidates
            // a single candidate is the match
            Declaration match = candidates.getSingleMember ();
            
            if (match == null) { // multiple candidates
                // ambiguity resolution -- 
                // 3.3.7, 3.4.4
                // ** elaborated type specifier - the "class x" case is 
                // handled by declare_class (and "struct x") -- we need a 
                // similar situation for "enum y", or need to handle 
                // elaborated type specifiers (for enum) in this method, 
                // which can't be done without more information (unless the 
                // ScopedName carried the type specifier in the "enum y" 
                // case)
                // currently "enum y" is "sorry not implemented", so 
                // we can look for the first non-type match
                for (Enumeration e = candidates.elements (); 
                     match == null && e.hasMoreElements (); ) {
                    Declaration pm = (Declaration) e.nextElement ();
                    if (pm.getCategory().intersects (Cpp_LFlags.NONTYPE)) 
                        match = pm;
                }
                // error if nothing found
                if (match == null) 
                    Assert.apology (NO_MATCHING_NONTYPE, exp.op().getName ()); 
            }
            
            // access modifiers -- is the related entity accessible?
            

            
            // create expression if single matching entity accessible
            // type to pass is a reference to the entity
            if (match.getCategory().contains (Cpp_LFlags.CLASS_MEMBER) &&
                (!match.hasSpecifier (Cpp_Specifiers.SP_STATIC))) {
                d.msg (Debug.COMPILE, "data member access expression created");
                // data member, use ExpThisMember
                ExpThisMember et = new ExpThisMember
                    (new TyRef (match.getType ()),
                     exp.id().getName (),
                     match.getRelativePath (),
                     match.getName ());
                if( match.hasIntegralConstantValue() ) {
                    et.set_integral_constant_value( match.getIntegralConstantValue()); }
                exp.set (et);
            } else {
                // other var, use ExpId
                d.msg (Debug.COMPILE, "id expression created with runtime id " + 
                       ((ScopedName) match.getRuntimeId ()).getName ());
                ExpId en = new ExpId 
                    (new TyRef (match.getType ()), exp.id().getName (), 
                     (ScopedName) match.getRuntimeId ());
                if( match.hasIntegralConstantValue() ) {
                    en.set_integral_constant_value( match.getIntegralConstantValue()); }
                exp.set (en);
            }
        }
    }
}
