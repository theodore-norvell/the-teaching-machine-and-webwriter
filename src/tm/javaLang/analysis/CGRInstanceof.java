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

/*
 * CGRInstanceofTest.java
 *
 * Created on August 17, 2003, 6:02 PM
 */

package tm.javaLang.analysis;

import tm.clc.analysis.CodeGenRule;
import tm.clc.analysis.Declaration;
import tm.clc.analysis.ExpressionPtr;
import tm.javaLang.ast.OpInstanceOf;
import tm.javaLang.ast.TyJava;
import tm.utilities.Assert;



/**
 *
 * @author  mpbl
 */
public class CGRInstanceof extends CodeGenRule {
    
    Java_CTSymbolTable symbolTable;
    
    public CGRInstanceof(Java_CTSymbolTable st){
        super();
        symbolTable = st;
    }
    
	/**
	 * This method tests if the prechosen expression is an lvalue
	 * @param exp expression being built
	 */
	public void apply (ExpressionPtr exp) {
            TyJava leftType =  (TyJava)exp.get_base_type (0);
            Declaration target = symbolTable.lookup (exp.id (),Java_LFlags.TYPE_LF).getSingleMember();
            Assert.error(target !=null, "Can't find target type " + exp.opid.getName());
            exp.set(new OpInstanceOf( exp.get(0),
                          (TyJava)target.getType(),
                          target.getName() ) );            // Now we need an OpNode that actually implements this
        }
}
