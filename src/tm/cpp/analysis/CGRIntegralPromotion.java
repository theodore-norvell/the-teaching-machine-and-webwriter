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

import tm.clc.analysis.CodeGenRule;
import tm.clc.analysis.ExpressionPtr;
import tm.clc.ast.ExpFetch;
import tm.clc.ast.ExpId;
import tm.clc.ast.ExpressionNode;
import tm.cpp.ast.TyIntegral;
import tm.utilities.Assert;

/**
 * Generates the AST representation of integral promotion.
 */
public class CGRIntegralPromotion extends CodeGenRule {
    private StandardConversions sc = StandardConversions.getInstance ();

    public void apply (ExpressionPtr exp) { 

        // integral expression
        if (!(exp.get_type () instanceof TyIntegral)) return;

        ExpressionNode converted = 
            sc.makeIntegralPromotionExpression (exp.get ());
        exp.set (converted);
    }

}
