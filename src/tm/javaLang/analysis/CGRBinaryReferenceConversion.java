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
 * Created on 25-Aug-2005 by Theodore S. Norvell. 
 */
package tm.javaLang.analysis;

import tm.clc.analysis.CodeGenRule;
import tm.clc.analysis.ExpressionPtr;
import tm.clc.ast.TypeNode;
import tm.javaLang.ast.TyJava;
import tm.javaLang.ast.TyNull;
import tm.javaLang.ast.TyPointer;
import tm.utilities.Assert;

/**
 * Implements conversion rules outlined in JLS (2ed) 15.21.3 for
 * converting before comparing with == and !=.
 */
public class CGRBinaryReferenceConversion extends CodeGenRule {

    public void apply (ExpressionPtr exp) {

        TypeNode leftType =  exp.get_type (0);
        TypeNode rightType =  exp.get_type (1);


        if( leftType instanceof TyNull || rightType instanceof TyNull ) {
            // null can convert to any reference type.
            // There is no need to actually convert.
            return ;
        }
        
        Assert.check( leftType instanceof TyPointer && rightType instanceof TyPointer ) ;
        
        if( leftType.equal_types( rightType )
            || ((TyJava)leftType).isReachableByWideningFrom( (TyJava)rightType ) 
            || ((TyJava)rightType).isReachableByWideningFrom( (TyJava)leftType ) ) {
            // One can be converted to the other.
            // There is no need to actually perform a conversion
            return ;
        }
        
        Assert.error( "Neither reference type can be converted to the other") ;
    }
}
