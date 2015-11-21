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

package tm.cpp.ast;

import tm.clc.ast.ExpressionNode;
import tm.clc.ast.NodeList;
import tm.clc.ast.TyAbstractPointer;
import tm.clc.ast.TypeNode;
import tm.utilities.Assert;

/** New operator for array allocations. */
public class ExpNewArray extends ExpAbsNew
{
    /* package */ NodeList operands_for_display ;
    
    public ExpNewArray( TypeNode t,
                          boolean show_init,
                          ExpressionNode operand,
                          ExpressionNode initialization )

    {
        super(t, operand, initialization) ;
         
        // Syntax
        // 0 compute a string for the type id.
        //   Use "$" as a place holder to mark where the 
        //   flexible dimension goes.
        Assert.check( t instanceof TyAbstractPointer ) ;
        TypeNode pointee = ((TyAbstractPointer)t).getPointeeType() ;
        Assert.check( pointee instanceof TyCpp ) ;
        String typeId = ((TyCpp)pointee).typeId( "[$]", false ) ;
        int dollar = typeId.indexOf("$") ;
        
        // 1 Compute the first and second string
        //   We ignore placement for the time being.
        String preSyntax = "new ("+typeId.substring(0, dollar) ;
        String postSyntax = typeId.substring( dollar+1 ) +")" ;
        if( show_init ) postSyntax += "()" ;
            
        set_syntax( new String[]{ preSyntax, postSyntax } ) ;
    }
}