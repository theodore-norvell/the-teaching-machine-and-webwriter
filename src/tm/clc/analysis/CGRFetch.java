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

import tm.clc.ast.ExpFetch;
import tm.clc.ast.ExpId;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.TyAbstractRef;
import tm.clc.ast.TypeNode;
import tm.utilities.Assert;


/**
 * Rule for generating the AST representation of an lvalue-->rvalue 
 * conversion (fetch) of id expressions
 */
public class CGRFetch extends CodeGenRule {

    public CGRFetch () { super (); }
    public CGRFetch (int pos) { super (pos); }

    /**
     * Generates an AST fetch expression.
     *
     * @param exp contains an <code>IdExp</code> on which to apply the 
     * fetch. Will contain the resulting fetch expression.
     */
    public void apply (ExpressionPtr exp) { 

        // expressions referring to functions are also lvalues, but 
        // the AST representation does not use the fetch mechanism to 
        // return a function

        if (exp.get_type (start) instanceof TyAbstractRef) {
            ExpressionNode unfetched = exp.get (start);
            TypeNode t_fetched = exp.get_base_type (start);
            ExpressionNode fetched = new ExpFetch (t_fetched, unfetched);
            exp.set (fetched, start);
        }
    }

}
