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
import tm.cpp.ast.TyArray;
import tm.cpp.ast.TyPointer;
import tm.utilities.Assert;

/** 
 * Generates the AST representation for converting an expression to
 * a pointer value.
 */
public class CGRConvertToPointer extends CodeGenRule {
    private StandardConversions sc = StandardConversions.getInstance ();

    private static final String CANNOT_CONVERT = 
        "Can not convert {0} to pointer.";

    public void apply (ExpressionPtr exp) { 
        
        if ( exp.get_base_type() instanceof TyPointer) {}
        else if ( exp.get_base_type() instanceof TyArray ) {
            ExpressionNode converted = 
                sc.makeArrayToPointerConversion (exp.get (), "");
            exp.set (converted); }
        else if (  exp.get().is_integral_constant() &&
                   exp.get().get_integral_constant_value()==0 ) { 
            /*Null pointer, we'll leave it*/ }
        else {
            Assert.apology (CANNOT_CONVERT, 
                            new String [] {exp.get_type().getTypeString ()}); }
    }
}