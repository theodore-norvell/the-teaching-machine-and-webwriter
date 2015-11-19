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


import java.util.Vector;

import tm.clc.analysis.CTSymbolTable;
import tm.clc.analysis.CodeGenRule;
import tm.clc.analysis.Declaration;
import tm.clc.analysis.DeclarationSet;
import tm.clc.analysis.ExpressionPtr;
import tm.clc.analysis.LFlags;
import tm.clc.analysis.ScopedName;
import tm.clc.ast.ExpArgument;
import tm.clc.ast.ExpUnimplemented;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.TypeNode;
import tm.cpp.ast.ExpFunctionName;
import tm.cpp.ast.TyClass;
import tm.cpp.ast.TyRef;
import tm.utilities.Assert;
import tm.utilities.Debug;

/**
 * Applies function id and overload resolution rules. 
 */
public class CGRResolveFn extends CodeGenRule {
    public static final String CANNOT_RESOLVE_FN = 
        "cannot resolve best function {0} given arguments provided"; 
    private CTSymbolTable symbolTable;
    private OverloadResolver olResolution = OverloadResolver.getInstance ();
    private CodeGenRule r_this;

    /** 
     * Creates a new rule instance
     * @param st the compile-time symbol table to use for id and 
     * overload resolution
     */
    public CGRResolveFn (CTSymbolTable st) { 
        symbolTable = st;
        r_this = new CGRThis (symbolTable);
    }

    public void apply (ExpressionPtr exp) {

        if (!(exp.get () instanceof ExpFunctionName)) return;

        ExpFunctionName efn = (ExpFunctionName) (exp.get ());
        DeclarationSet candidates = null;
        ClassSH cscope = efn.getClassScope ();
        if (cscope != null) { // member call
            try {
                candidates = cscope.lookup (efn.getName (), 
                                            new LFlags (Cpp_LFlags.FUNCTION));
            } catch (Exception e) { Assert.apology (e.getMessage ()); }
        } else {
            // id lookup
            candidates = symbolTable.lookup (efn.getName ());
        }
        // the check for no matches was done prior to this ?
    
        // overload resolution
        Vector args_v = new Vector (exp.operandCount ());
        for (int i = 0; i < exp.operandCount (); i++) 
            args_v.addElement (exp.get_type (i));
        RankedFunction rfn = olResolution.disambiguate (candidates, args_v);
        Assert.apology (rfn != null, CANNOT_RESOLVE_FN, efn.getName().getName ());
        efn.setMatch (rfn);

        LFlags declCat = rfn.declaration.getCategory ();
        // is this a member function?
        if (declCat.intersects (Cpp_LFlags.MEM_FN)) {
            // if no recipient has been set for a non-static member fn
            if ((!rfn.declaration.hasSpecifier (Cpp_Specifiers.SP_STATIC)) && 
                (efn.getClassObject () == null)) {
                d.msg (Debug.COMPILE, "no class object set");
                
                // Nevertheless the cscope should have been set.
                Assert.check (cscope != null);
                // if a constructor
                if (declCat.contains (Cpp_LFlags.CONSTRUCTOR)) {
                    d.msg (Debug.COMPILE, "using ExpArgument for constructor without id exp");
                    // get the class type
                    TypeNode class_t = cscope.getOwnDeclaration().getType ();
                    Assert.check (class_t instanceof TyClass);
                    // use ExpArgument (ref to t, 0)
                    efn.setClassObject (new ExpArgument (new TyRef (class_t), 0));
                } else {
                    // Modified TSN 2002 Oct 23. Calls implicitly
                    // to *this should not use OpMemberCall,
                    // but rather OpThisMemberCall.
                    // Therefore we flag them here.
                    Assert.check( efn.getClassObject () == null ) ;
                    efn.setRecipientIsImplicitlyThis(true) ;
                    
                    // We set the clasObject field anyway,
                    // in case it is needed later (e.g. in CGRFunctionCall).
                    ExpressionPtr e_this = new ExpressionPtr 
                        (new Cpp_ScopedName (), new Object [] { });
                    r_this.apply (e_this);
                    efn.setClassObject (e_this.get ());
                }
            }
        }
    }
}
