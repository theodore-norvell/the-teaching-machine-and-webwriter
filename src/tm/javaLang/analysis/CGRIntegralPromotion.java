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
 * CGRIntegralPromotion.java
 *
 * Created on July 22, 2003, 11:08 AM
 */

package tm.javaLang.analysis;

import tm.clc.analysis.CodeGenRule;
import tm.clc.analysis.ExpressionPtr;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.OpArithmeticConversion;
import tm.clc.ast.TypeNode;
import tm.javaLang.ast.TyArithmetic;
import tm.javaLang.ast.TyByte;
import tm.javaLang.ast.TyChar;
import tm.javaLang.ast.TyInt;
import tm.javaLang.ast.TyShort;
import tm.utilities.Assert;


/**
 *
 * @author  mpbl
 */
/**
 * Implements integral promotion rules, requiring one operand.
 * result is conversion of operand according to Section
 * 5.6.1(known as Unary Numeric Promotion) of JLS, replacing (enclosing) the existing
 * operands.
 */
public class CGRIntegralPromotion extends CodeGenRule {
	
	public CGRIntegralPromotion(int i){
		super(i);
	}
	
	public CGRIntegralPromotion(){
		super(-1);
	}

    public void apply(ExpressionPtr exp) {
        TypeNode opType =  exp.get_type (start);

        Assert.error( opType instanceof TyArithmetic,
                "Illegal unary numeric promotion");

        if (opType instanceof TyShort || opType instanceof TyByte || opType instanceof TyChar) {
            ExpressionNode convert = new OpArithmeticConversion(TyInt.get(), "", exp.get(start));
            exp.set(convert);
            convert.setUninteresting(true);
        }
     }

}
