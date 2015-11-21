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
 * OpTable.java
 *
 * Created on May 12, 2003, 10:42 AM
 */

package tm.javaLang.analysis;
import tm.clc.ast.Arithmetic;
import tm.javaLang.parser.JavaParserConstants;

/**
 *
 * @author  mpbl This is a simplification of Derek's approach. He uses the Parser constant
 * names as a key to retrieve the string image of the operator. Because operators are not contiguous
 * he uses a fairly convoluted scheme which is simplified here. Instead of using the JavaParserConstant name
 * directly, use the name with a _S added to get the equivalent string image.
 */
public class OpTable implements JavaParserConstants{


        /** An array to map unary arithmetic operator strings to integers for operators defined in
         * <code> Clc.Analysis.Arithmetic</code>
         */
        private static String[] unaryArith;
        private static String[] binaryArith;
        private static String [] binaryLogical;

     static String[] getBinaryArith(){
         if (binaryArith == null) {
             binaryArith = new String[2*Arithmetic.ENTRIES];
             for (int i = 0; i < 2*Arithmetic.ENTRIES; i++)
                 binaryArith[i] = null;  //default
             binaryArith[Arithmetic.MULTIPLY] = getImage(STAR);
             binaryArith[Arithmetic.DIVIDE] = getImage(SLASH);
             binaryArith[Arithmetic.REMAINDER] = getImage(REM);
             binaryArith[Arithmetic.ADD] = getImage(PLUS);
             binaryArith[Arithmetic.SUBTRACT] = getImage(MINUS);
             binaryArith[Arithmetic.SHIFT_LEFT] = getImage(LSHIFT);
             binaryArith[Arithmetic.SHIFT_RIGHT_ZERO_FILL] = getImage(RUNSIGNEDSHIFT);
             binaryArith[Arithmetic.SHIFT_RIGHT_SIGN_FILL] = getImage(RSIGNEDSHIFT);
             binaryArith[Arithmetic.LESS] = getImage(LT);
             binaryArith[Arithmetic.GREATER] = getImage(GT);
             binaryArith[Arithmetic.LESS_OR_EQUAL] = getImage(LE);
             binaryArith[Arithmetic.GREATER_OR_EQUAL] = getImage(GE);
             binaryArith[Arithmetic.EQUAL] = getImage(EQ);
             binaryArith[Arithmetic.NOT_EQUAL] = getImage(NE);
             binaryArith[Arithmetic.BITWISE_AND] = getImage(BIT_AND);
             binaryArith[Arithmetic.BITWISE_XOR] = getImage(XOR);
             binaryArith[Arithmetic.BITWISE_OR] = getImage(BIT_OR);
             binaryArith[Arithmetic.BOOLEAN_AND] = getImage(SC_AND);
             binaryArith[Arithmetic.BOOLEAN_OR] = getImage(SC_OR);
//          /* shorthand assignment begins */
             binaryArith[Arithmetic.MULTIPLY + Arithmetic.ENTRIES] = getImage(STARASSIGN);
             binaryArith[Arithmetic.DIVIDE + Arithmetic.ENTRIES] = getImage(SLASHASSIGN);
             binaryArith[Arithmetic.ADD + Arithmetic.ENTRIES] = getImage(PLUSASSIGN);
             binaryArith[Arithmetic.SUBTRACT + Arithmetic.ENTRIES] = getImage(MINUSASSIGN);
             binaryArith[Arithmetic.BITWISE_AND + Arithmetic.ENTRIES] = getImage(ANDASSIGN);
             binaryArith[Arithmetic.BITWISE_OR + Arithmetic.ENTRIES] = getImage(ORASSIGN);
             binaryArith[Arithmetic.BITWISE_XOR + Arithmetic.ENTRIES] = getImage(XORASSIGN);
             binaryArith[Arithmetic.REMAINDER + Arithmetic.ENTRIES] = getImage(REMASSIGN);
             binaryArith[Arithmetic.SHIFT_LEFT + Arithmetic.ENTRIES] = getImage(LSHIFTASSIGN);
             binaryArith[Arithmetic.SHIFT_RIGHT_SIGN_FILL + Arithmetic.ENTRIES] = getImage(RSIGNEDSHIFTASSIGN);
             binaryArith[Arithmetic.SHIFT_RIGHT_ZERO_FILL + Arithmetic.ENTRIES] = getImage(RUNSIGNEDSHIFTASSIGN);
         }
         return binaryArith;
     }

     static String[] getUnaryArith(){
         if (unaryArith == null) {
             unaryArith = new String[Arithmetic.ENTRIES];
             for (int i = 0; i < Arithmetic.ENTRIES; i++)
                 unaryArith[i] = null;  //default
             unaryArith[Arithmetic.NEGATE] = "-";
             unaryArith[Arithmetic.BOOLEAN_NOT] = "!";
             unaryArith[Arithmetic.BITWISE_NOT] = "~";
             unaryArith[Arithmetic.DONOTHING] = "+";
         }
         return unaryArith;
     }


     static String[] getBinaryLogical(){
         if (binaryLogical == null) {
             binaryLogical = new String[Arithmetic.ENTRIES];
             for (int i = 0; i < Arithmetic.ENTRIES; i++)
                 binaryLogical[i] = null;  //default
             binaryLogical[Arithmetic.EQUAL] = getImage(EQ);
             binaryLogical[Arithmetic.NOT_EQUAL] = getImage(NE);
             binaryLogical[Arithmetic.BOOLEAN_AND] = getImage(SC_AND);
             binaryLogical[Arithmetic.BOOLEAN_OR] = getImage(SC_OR);
             binaryLogical[Arithmetic.BITWISE_AND] = getImage(BIT_AND);
             binaryLogical[Arithmetic.BITWISE_OR] = getImage(BIT_OR);
             binaryLogical[Arithmetic.BITWISE_XOR] = getImage(BIT_OR);
         }
         return binaryLogical;
     }

// Assertion: code represents a legitimate Parser constant (fundamentally uncheckable)
     public static String getImage(int code){
        String image = tokenImage[code];
        if (image.indexOf('"')==-1) return image; // no quotes
        char[] raw = new char[image.length()-2];
        for (int i=0, c=0; i < image.length(); i++){
            char ch = image.charAt(i);
            if (ch != '"')
                raw[c++] = ch;
        }
        return new String(raw);
    }
}

