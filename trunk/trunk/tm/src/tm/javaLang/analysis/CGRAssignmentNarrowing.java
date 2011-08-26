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
import tm.javaLang.ast.TyByte;
import tm.javaLang.ast.TyChar;
import tm.javaLang.ast.TyIntegral;
import tm.javaLang.ast.TyJava;
import tm.javaLang.ast.TyLong;
import tm.javaLang.ast.TyShort;
import tm.utilities.Assert;


/**
 * Implements the special narrowing conversion for assignment of Section 5.1.2 of JLS,
 * Requires two operands, the left one defing the target type, the right one
 * the type to be converted.
 * Result is conversion of right operand, replacing (enclosing) the existing operands.
 * Assertion: conversion is known to be narrowing
 */
public class CGRAssignmentNarrowing extends CodeGenRule {

    public void apply(ExpressionPtr exp) {
        TyJava varType =  (TyJava)exp.get_base_type (0);
        TyJava expType =  (TyJava)exp.get_type (1);
        // need to check for constant expression (integralconstant) and
        // representability max sizes from short, long, char classes
        // plus some of these checks are screwed
        Assert.error(exp.get(1).is_integral_constant(),
                        "Can only apply narrowing conversion to integer constants");
        Assert.error(expType instanceof TyIntegral && !(expType instanceof TyLong),
                        "Cannot apply narrowing conversion to " + expType.typeId());
        Assert.error(varType instanceof TyByte || varType instanceof TyShort
                        || varType instanceof TyChar,
                        "Cannot narrow " + expType.typeId() + " to " + varType.typeId());
        long value = exp.get(1).get_integral_constant_value();
        Assert.error(varType instanceof TyByte && value <= Byte.MAX_VALUE && value >= Byte.MIN_VALUE ||
            varType instanceof TyShort && value <= Short.MAX_VALUE && value >= Short.MIN_VALUE ||
            varType instanceof TyChar && value <= Character.MAX_VALUE && value >= Character.MIN_VALUE,
            String.valueOf(value) + " is too big or too small to be held in a " + varType.typeId());
        exp.set ( new OpArithmeticConversion((TypeNode)varType, "", exp.get(1)), 1);
    }
}


