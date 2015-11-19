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

import tm.clc.analysis.CGRTest;
import tm.clc.analysis.CTSymbolTable;
import tm.clc.analysis.DeclarationSet;
import tm.clc.analysis.ExpressionPtr;
import tm.clc.analysis.LFlags;
import tm.clc.analysis.ScopeHolder;
import tm.clc.analysis.ScopedName;
import tm.clc.analysis.UndefinedSymbolException;
import tm.clc.ast.ExpUnimplemented;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.NodeList;
import tm.clc.ast.OpFuncCall;
import tm.clc.ast.OpMemberCall;
import tm.clc.ast.TypeNode;
import tm.cpp.ast.ExpFunctionName;
import tm.utilities.Assert;

/**
 * Tests for the existence of an appropriate overloaded operator implementation
 */
public class CGROverloadedTest extends CGRTest {
    public static final String OPERATOR = "operator";
    CTSymbolTable symbolTable;
    OverloadResolver overloadResolver = OverloadResolver.getInstance ();

    /** 
     * Creates a new rule instance
     * @param st the compile-time symbol table for lookup
     */
    public CGROverloadedTest (CTSymbolTable st) {
        this.symbolTable = st;
    }

    
    public boolean applies (ExpressionPtr exp) {
        boolean test_result = false;

        // make an args Vector for disambiguation
        Vector argsV = new Vector ();
        for (int i = 0; i < exp.operandCount (); i++) 
            argsV.addElement (exp.get (i));

        ExpFunctionName efn = findOverloadedImplementation 
            (exp.op (), argsV, new LFlags (), null);

        if (efn != null) {
            exp.set (efn);
            test_result = true;
        }

        return test_result;
    }

    protected ExpFunctionName findOverloadedImplementation 
        (ScopedName op, Vector operands, LFlags lc, ScopeHolder scope) {
        ExpFunctionName efn = null;

        // determine operator for lookup and overload resolution flag
        ScopedName opFn;
        int ol_flag;
        String opId = op.getTerminalId ();
        if (opId.startsWith (OPERATOR)) {
            opFn = op;
            ol_flag = OverloadResolver.FN_CALL_OP;
        } else {
            opFn = new Cpp_ScopedName (op);
            opFn.setTerminalId (OPERATOR + opId);
            ol_flag = OverloadResolver.OP_IN_EXP;
        }


        // look for candidate functions
        DeclarationSet candidates = null;
        if (scope != null) 
            try {
                candidates = scope.lookup (opFn, lc);
            } catch (UndefinedSymbolException ok_to_ignore) { }
        else candidates = symbolTable.lookup (opFn, lc);
        
        // disambiguate 
        RankedFunction rf ;
        if (candidates != null) {
            final int sz = operands.size() ;
            Vector args_ty_v = new Vector ( sz );
            for (int i = 0; i < sz; i++) {
                ExpressionNode exp = (ExpressionNode) ( operands.elementAt(i) ) ;
                args_ty_v.addElement( exp.get_type() );
            }
            rf = overloadResolver.disambiguate (candidates, args_ty_v, ol_flag);
        } else {
            rf = null ;
        }

        if (rf != null) { // found a match
            // make a function name expression
            efn = new ExpFunctionName (opFn);
            efn.setMatch (rf);
        }

        return efn;
    }
}
