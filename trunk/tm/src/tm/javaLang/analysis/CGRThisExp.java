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

package tm.javaLang.analysis;

import tm.clc.analysis.CTSymbolTable;
import tm.clc.analysis.CodeGenRule;
import tm.clc.analysis.Declaration;
import tm.clc.analysis.ExpressionPtr;
import tm.clc.ast.ExpThis;
import tm.clc.ast.ExpressionNode;
import tm.javaLang.ast.TyPointer;
import tm.utilities.Assert;
//import Clc.Ast.TyAbstractClass;
//import Clc.Ast.ExpFetch;
//
//import JavaLang.Analysis.Java_CTSymbolTable;
//
//
/**
 * Generates the AST representation of an id expression.
 */

public class CGRThisExp extends CodeGenRule {

    Java_CTSymbolTable symbolTable;
    /**
     * Creates a new rule instance
     * @param st the compile-time symbol table for id resolution
     */
    public CGRThisExp(CTSymbolTable st) {
        symbolTable = (Java_CTSymbolTable)st;
    }
    
    public void apply(ExpressionPtr exp) {
        Declaration target = null;
        ExpressionNode en;
        if(exp.opid != null) {
            target = symbolTable.lookup(exp.opcat,Java_LFlags.CLASS_LF).getSingleMember();
            Assert.error(target!=null, "Cannot find class " + exp.opid.getName());
            Assert.error(symbolTable.isAccessible(target), "Cannot access class "  + exp.opid.getName());
            en = new ExpThis(
                new TyPointer(target.getType()),
                "this", symbolTable.getRelativePath(target) ); 
        }
        else {
            target = symbolTable.getCurrentScope().getClassDeclaration();
            en = new ExpThis(
                new TyPointer(target.getType()),
                "this");
        }
        exp.set (en);
    }
    
}

