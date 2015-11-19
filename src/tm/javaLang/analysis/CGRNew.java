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

import tm.clc.analysis.* ;
import tm.clc.ast.ExpArgument;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.NodeList;
import tm.clc.ast.TypeNode;
import tm.javaLang.ast.ExpNew;
import tm.javaLang.ast.TyClass;
import tm.javaLang.ast.TyPointer;
import tm.javaLang.ast.TyRef;
import tm.utilities.Assert;


/**
 * Generates the AST representation of an id expression.
 */
public class CGRNew extends CodeGenRule {
    
    Java_CTSymbolTable symbolTable ;
    
    public CGRNew(Java_CTSymbolTable symtab) {
        symbolTable = symtab ;  
    }
    
    /** Create an ExpNew node.
     * Preconditions: exp.get_list(0) is the argument list,
     * exp.id() is the class as a scoped name, and
     * exp.opid is the name of the class as a scoped name suitable for display.

     * Postcondition exp.get() represents the "new" expression.
     * @param exp
     */
    public void apply (ExpressionPtr exp) {
        // name lookup
        DeclarationSet candidates = symbolTable.lookup (exp.id (),Java_LFlags.CLASS_LF);
        
        // if lookup returns nothing, error 
        if (candidates == null || candidates.isEmpty ()) 
            Assert.error ("No class found matching id " +  exp.id().getName ());

        // look for the match among the candidates
        Declaration classDecl = candidates.getSingleMember ();
        TyClass tyClass = (TyClass) classDecl.getType();

        ExpressionNode recipient = new ExpArgument(new TyRef(tyClass), 0);
        
        exp.set( recipient, 1) ;
        exp.set( tyClass, 2) ;
        
        CGRConstructorCall cgrConstructorCall = new CGRConstructorCall( symbolTable ) ;  
        cgrConstructorCall.apply( exp ) ;
        
    	ExpressionNode constructorCall = exp.get() ;
        NodeList arguments = exp.get_list(0);

        TyPointer tyPointer = new TyPointer();
        tyPointer.addToEnd(tyClass);
        
        ExpressionNode en = new ExpNew(tyPointer, arguments, constructorCall);
        exp.set(en);
    }
}


