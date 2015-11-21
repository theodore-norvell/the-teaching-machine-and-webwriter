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
 * Created on 24-Jun-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package tm.javaLang.analysis;

import tm.clc.analysis.CodeGenRule;
import tm.clc.analysis.Declaration;
import tm.clc.analysis.DeclarationSet;
import tm.clc.analysis.ExpressionPtr;
import tm.clc.analysis.ScopedName;
import tm.clc.ast.ExpThis;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.NodeList;
import tm.javaLang.ast.TyClass;
import tm.javaLang.ast.TyRef;
import tm.utilities.Assert;

/**
 * @author user
 *
 * Create code for calls of the form this(...) and super(...)
 */
public class CGRExplicitConstructorCall extends CodeGenRule {

    private boolean isCallToSuper ;
    private Java_CTSymbolTable symbolTable ;
    
    public CGRExplicitConstructorCall( boolean isCallToSuper, Java_CTSymbolTable symtab) {
        this.isCallToSuper = isCallToSuper ;
        this.symbolTable = symtab ;
    }
    
    /**
     * <P><strong>Precondition:</strong>
     * <ul><li>exp.id() is the FQN for the class
     *     </li>
     *     <li>exp.get_list(0) is the constructor arguments
     *     </li>
     *     <li>exp.get(1) is the outer object or isn't there if there isn't an outer object
     *     </li>
     * </ul>
     * <p><strong>Postcondition:</strong>
     * <ul>
     *     <li> <code>exp.get()</code> is the constructor call.
     *     </li>
     * </ul>
     */
    public void apply(ExpressionPtr exp) {
        ScopedName classFQN = exp.id() ;
        NodeList arguments = exp.get_list(0) ;
        ExpressionNode outer = exp.operandCount() == 1 ?  null : exp.get( 1 ) ;
        if( outer != null ) {
            //TODO worry about the outer object
            Assert.check( false ) ;
        }
        
        // Look up the class
        DeclarationSet candidates = symbolTable.lookup (exp.id (),Java_LFlags.CLASS_LF);
        
        // if lookup returns nothing, error 
        if (candidates == null || candidates.isEmpty ()) 
            Assert.error ("No class found matching id " +  exp.id().getName ());

        // look for the match among the candidates
        Declaration classDeclThis = candidates.getSingleMember ();
        TyClass tyClassThis = (TyClass) classDeclThis.getType();

        // The recipient is this object.
        
        
        TyClass theClass ;
        ExpressionNode recipient ;
        if( isCallToSuper ) {
            Declaration classDeclSuper = ((SHType)classDeclThis.getDefinition()).getTheSuperType().getClassDeclaration() ;
            TyClass tyClassSuper = (TyClass) classDeclSuper.getType() ;
            theClass = tyClassSuper ;
            exp.opid = classDeclSuper.getName() ;
            recipient = makeThis( tyClassSuper, "super", new int[] {0} ) ;
        }
        else {
            theClass = tyClassThis ;
            recipient = makeThis( tyClassThis, "this", new int[0] ) ;
        }
        
        exp.set( recipient, 1) ;
        exp.set( theClass, 2 ) ;
        
        CGRConstructorCall cgrConstructorCall = new CGRConstructorCall( symbolTable ) ;  
        cgrConstructorCall.apply( exp ) ;

    }
    
    private ExpressionNode makeThis( TyClass tyClass, String str, int[] path ) {
        TyRef tyRef = new TyRef(tyClass) ;
        return new ExpThis(tyRef, str, path) ;
    }

}
