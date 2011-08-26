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

import tm.clc.analysis.CodeGenRule;
import tm.clc.analysis.ExpressionPtr;
import tm.clc.ast.OpArithmeticConversion;
import tm.clc.ast.TypeNode;

/**
 * Implements the widening primitive conversion rule of Section 5.1.2 of JLS,
 * Requires two operands, the left one defing the target type, the right one
 * the type to be converted.
 * Result is conversion of right operand, replacing (enclosing) the existing operands.
 * Assertion: conversion is known to be identity or widening
 */
public class CGRWideningPrimitive extends CodeGenRule {

    public void apply (ExpressionPtr exp) {
        TypeNode leftType =  exp.get_base_type (0);
        TypeNode rightType =  exp.get_type (1);
        if (leftType.equal_types(rightType)) return;  // identity conversion, always allowed
        exp.set ( new OpArithmeticConversion(leftType, "",exp.get(1)), 1);
    }
}


