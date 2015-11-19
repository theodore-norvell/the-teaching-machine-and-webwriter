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
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.OpArithmeticConversion;
import tm.clc.ast.TypeNode;
import tm.javaLang.ast.TyByte;
import tm.javaLang.ast.TyChar;
import tm.javaLang.ast.TyDouble;
import tm.javaLang.ast.TyFloat;
import tm.javaLang.ast.TyFloating;
import tm.javaLang.ast.TyInt;
import tm.javaLang.ast.TyLong;
import tm.javaLang.ast.TyShort;


/**
 * Implements arithmetic conversion rules, requiring two operands
 * result is conversion of one or both operands according to Sections
 * 5.6.2(known as Binary Numeric Promotion) of JLS, replacing (enclosing) the existing
 * operands.
 */
public class CGRArithmeticConversion extends CodeGenRule {

    public void apply (ExpressionPtr exp) {

        TypeNode leftType =  exp.get_type (0);
        TypeNode rightType =  exp.get_type (1);


        if (leftType.equal_types(rightType)) {
            if (leftType instanceof TyShort
                || leftType instanceof TyByte
                || leftType instanceof TyChar )
                convertBothToInt(exp);
        }
        else {
            boolean convertLeft;
            if (rightType instanceof TyDouble || leftType instanceof TyDouble)
                convertLeft = rightType instanceof TyDouble;
            else if (rightType instanceof TyFloat || leftType instanceof TyFloat)
                convertLeft = rightType instanceof TyFloat;
            else if(rightType instanceof TyLong || leftType instanceof TyLong)
                convertLeft = rightType instanceof TyLong;
            else if(rightType instanceof TyInt || leftType instanceof TyInt)
                convertLeft = rightType instanceof TyInt;
            else {
                convertBothToInt(exp);
                return;
            }
            ExpressionNode convert = convertLeft ?
                new OpArithmeticConversion(rightType, "",exp.get(0)) :
                new OpArithmeticConversion(leftType, "",exp.get(1));
            convert.setUninteresting(!(leftType instanceof TyChar ||
                rightType instanceof TyChar ||
                rightType instanceof TyFloating && !(leftType instanceof TyFloating)||
                !(rightType instanceof TyFloating) && leftType instanceof TyFloating));
            exp.set (convert ,convertLeft ? 0 : 1);
        }
    }

    private void convertBothToInt(ExpressionPtr exp){
        ExpressionNode left = new OpArithmeticConversion(TyInt.get(), "", exp.get(0));
        ExpressionNode right = new OpArithmeticConversion(TyInt.get(), "", exp.get(1));
        left.setUninteresting(true);
        right.setUninteresting(true);
        exp.set (left , 0);
        exp.set (right , 1);
    }



}



