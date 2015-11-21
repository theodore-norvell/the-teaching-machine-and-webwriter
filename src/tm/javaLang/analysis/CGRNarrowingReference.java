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
 * CGRNarrowingReference.java
 *
 * Created on July 29, 2003, 11:02 AM
 */

package tm.javaLang.analysis;

import tm.clc.analysis.CodeGenRule;
import tm.clc.analysis.ExpressionPtr;
import tm.clc.ast.TypeNode;
import tm.javaLang.ast.OpJavaDownConversion;
import tm.javaLang.ast.TyJava;
import tm.javaLang.ast.TyPointer;
import tm.utilities.Assert;

/**
 *
 * @author  mpbl
 *
 * Implements the narrowing reference conversion rule of Section 5.1.5 of JLS,
 * Requires two operands, the left one defining the target type, the right one
 * the type to be converted.
 * Result is conversion of right operand, replacing (enclosing) the existing operands.
 * Assertion: conversion is known to be narrowing
 * 
 */
public class CGRNarrowingReference extends CodeGenRule {
   
    /**
     * <P><strong>Precondition:</strong>
     * <ul><li> <code>exp.get_type (0)</code> is a TyPointer object
     *                representing the type to be cast to.
     *     </li>
     *     <li> <code>exp.get(1)</code> is an expression node of pointer type
     *     </li>
     *     <li> The type of (1) is not equal to and can not be widened to that of (0)
     *     </li>
     * </ul>
     * <p><strong>Postcondition:</strong>
     * <ul>
     *     <li> If the type of (1) can not be narrowed to the type of (0) then
     *          a compile time error is generated.
     *     </li>
     *     <li> Else code is generated to to the narrowing at run time and item (1)
     *          of <code>exp</code> is <b>replaced</b> with that code.
     *     </li>
     * </ul>
     */
   public void apply(ExpressionPtr exp) {
       TypeNode toType = exp.get_type (0) ;
       Assert.check( toType instanceof TyPointer ) ;
       TypeNode fromType = exp.get_type (1) ;
       Assert.check( fromType instanceof TyPointer ) ;
       Assert.error( ((TyJava)fromType).isReachableByWideningFrom((TyJava)toType),
               "Cast can never succeed." ) ;
       exp.set( new OpJavaDownConversion( toType,
                    "(" + exp.opid.getName() + ")",exp.get(1)), 1 );
    }
}
