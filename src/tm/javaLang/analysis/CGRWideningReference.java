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
 * CGRWideningReference.java
 *
 * Created on July 24, 2003, 12:38 PM
 */

package tm.javaLang.analysis;

import tm.clc.analysis.CodeGenRule;
import tm.clc.analysis.ExpressionPtr;
import tm.clc.ast.OpArithmeticConversion;
import tm.clc.ast.TypeNode;
import tm.javaLang.ast.TyJava;

/**
 *
 * @author  mpbl
 *
 * Implements the widening reference conversion rule of Section 5.1.4 of JLS,
 * Requires two operands, the left one defining the target type, the right one
 * the type to be converted.
 * Result is conversion of right operand, replacing (enclosing) the existing operand.
 * Assertion: conversion is known to be identity or widening
 */
public class CGRWideningReference extends CodeGenRule {
    
   public void apply(ExpressionPtr exp) {
        TypeNode leftType =  exp.get_base_type (0);
        TyJava rightType =  (TyJava)exp.get_type (1);
        boolean isCast = exp.opcat.getName().equalsIgnoreCase("cast");
        if (!isCast && ((TypeNode)leftType).equal_types( (TypeNode)rightType))
            return;
        
        String opImage = isCast ? "(" + exp.opid.getName() + ")" : "";
        
        exp.set ( new OpArithmeticConversion( leftType,
                                              opImage,
                                              exp.get(1) ),
                  1 );
    }
}
