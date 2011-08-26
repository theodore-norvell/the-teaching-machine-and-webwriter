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
import tm.clc.analysis.ScopedName;
import tm.clc.ast.NodeList;
import tm.utilities.Assert;

/**
 * Generates the AST representation of an overloaded <em>delete</em> operator, 
 * as a specialized case of function call.
 */
public class CGROverloadedDelete extends CGRFunctionCall {
    public CGROverloadedDelete (CodeGenRule value_argument_conversion_rule,
                                CodeGenRule reference_argument_conversion_rule,
                                CodeGenRule rs_convert_no_parameter_argument) {
            super (value_argument_conversion_rule,
                   reference_argument_conversion_rule,
                   rs_convert_no_parameter_argument,
                   true); }
    /** 
     * The delete method takes no arguments
     * @return an empty list, regardless of what's in <code>exp</code>
     */
    protected NodeList build_args_list (ExpressionPtr exp) {
        // delete method takes no arguments
        NodeList args_nl = new NodeList ();
        return args_nl;
    }
}
