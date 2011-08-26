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

import tm.clc.analysis.CodeGenRule;
import tm.clc.analysis.ExpressionPtr;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.OpDeref;
import tm.clc.ast.TyAbstractPointer;
import tm.clc.ast.TypeNode;
import tm.cpp.ast.TyRef;
import tm.utilities.Assert;


/**
 * Generates the AST expression node representing a pointer dereference.
 */
public class CGRDereference extends CodeGenRule {

    public CGRDereference () { super (); }
    public CGRDereference (int pos) { super (pos); }

    /**
     * Generates an AST representation of pointer dereference
     *
     * @param exp contains the expression to dereference, will contain
     * the dereference expression
     */
    public void apply (ExpressionPtr exp) { 
        // dereference
        ExpressionNode ref = exp.get (start);
        String op_image = exp.op().getName ();
        TypeNode et = exp.get_base_type (start);
        Assert.check (et instanceof TyAbstractPointer);
        TypeNode pt = ((TyAbstractPointer) et).getPointeeType ();

        ExpressionNode deref = new OpDeref (new TyRef (pt), op_image, ref);

        exp.set (deref);
    }
    
}
