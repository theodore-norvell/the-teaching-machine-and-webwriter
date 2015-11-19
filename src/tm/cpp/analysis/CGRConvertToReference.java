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
import tm.clc.ast.OpConvertToReference;
import tm.clc.ast.TyAbstractRef;
import tm.clc.ast.TypeNode;
import tm.cpp.ast.TyRef;
import tm.utilities.Assert;



/** 
 * rvalue->lvalue conversion.... non-standard issue. Does nothing 
 * if the expression is already an lvalue (i.e. has type <em>ref to T</em>).
 */
public class CGRConvertToReference extends CodeGenRule {

    public CGRConvertToReference () { super (); }
    public CGRConvertToReference (int pos) { super (pos); }

    public void apply (ExpressionPtr exp) { 
        ExpressionNode e = exp.get (start);
        TypeNode t = exp.get_type (start);
        if (!(t instanceof TyAbstractRef)) {
            exp.set (new OpConvertToReference (new TyRef (t), e), start);
        }
    }

}
