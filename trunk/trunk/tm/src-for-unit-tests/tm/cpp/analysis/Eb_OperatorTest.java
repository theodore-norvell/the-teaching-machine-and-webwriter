package tm.cpp.analysis;

import tm.clc.analysis.*;
import tm.clc.ast.*;
import tm.cpp.ast.*;
import tm.cpp.parser.ParserConstants;
import tm.utilities.Debug;

import junit.framework.*;
import java.util.*;

/**
 * unit tests for Eb_Operator
 */
public class Eb_OperatorTest extends ExpressionBuilderTest {

        public Eb_OperatorTest () { this ("Eb_OperatorTest"); }
        public Eb_OperatorTest (String name) {
                super (name);
                eb = cem.eb_Operator;
        }



        /**
         * Testing arithmetic expressions. Operators *,/,%, +,- are tested as seen in
         * the following code:
         * <pre>
         * int x;
         * float y;
         * x = 5 * 1;
         * x = x * 1;
         * x = 1 * x;
         * x = x * x;
         * y = 1.0 * 5.0;
         * y = y * 1.0;
         * y = 1.0 * 1;
         * y = y * 1;
         *</pre>
         * <ul>Notes:
         * <li>modulus (%) is as above without the statements involving non-integral
         *     operands.
         * <li>addition also includes operations with pointer and integral operands.
         * <li>subtraction is per addition, plus operations with two pointer operands.
         * </ul>
         */
        public void testArithmeticExpressions () {
                ScopedName op = new Cpp_ScopedName (OpTable.get (STAR));
                standardArithmeticTests (op);
                op = new Cpp_ScopedName (OpTable.get (SLASH));
                standardArithmeticTests (op);
                op = new Cpp_ScopedName (OpTable.get (PERCENT));
                standardIntegralArithmeticTests (op);
                op = new Cpp_ScopedName (OpTable.get (PLUS));
                standardArithmeticTests (op);
                pointerAdditionTests (op);
                op = new Cpp_ScopedName (OpTable.get (MINUS));
                standardArithmeticTests (op);
                pointerSubtractionTests (op);
        }

        /**
         * Testing expressions as seen in the following code:
         * <pre>
         * int x;
         * float y;
         * x = 5 op 1;
         * x = x op 1;
         * x = 1 op x;
         * x = x op x;
         * y = 1.0 op 5.0;
         * y = y op 1.0;
         * y = 1.0 op 1;
         * y = y op 1;
         * y = y op x;
         * y = x op y;
         *</pre>
         */
        private void standardArithmeticTests (ScopedName op) {

                // first perform integral arithmetic tests
                standardIntegralArithmeticTests (op);


                // use eb_BuiltInExpression to generate the expression for 5.0 op 1.0
                exp = applyExp (op, ffive, fone);

                // we should have:
                // OpFloat (ConstFloat, ConstFloat)
                validate (exp, OpFloat.class, ConstFloat.class, ConstFloat.class,
                                  false);
                validate (exp, tyFloat, true);

                // y op 1.0
                exp = applyExp (op, y, fone);

                // we should have:
                // OpFloat (ExpFetch (ExpId), ConstFloat)
                validate (exp, OpFloat.class,
                                  new Object [] {ExpFetch.class, ExpId.class},
                                  new Object [] {ConstFloat.class},
                                  false);
                validate (exp, tyFloat, true);

                // 1.0 op y
                exp = applyExp (op, fone, y);

                // we should have:
                // OpFloat (ConstFloat, ExpFetch (ExpId))
                validate (exp, OpFloat.class,
                                  new Object [] {ConstFloat.class},
                                  new Object [] {ExpFetch.class, ExpId.class},
                                  false);
                validate (exp, tyFloat, true);

                // y op y
                exp = applyExp (op, y, y);

                // we should have:
                // OpFloat (ExpFetch (ExpId), ExpFetch (ExpId))

                validate (exp, OpFloat.class,
                                  new Object [] {ExpFetch.class, ExpId.class},
                                  new Object [] {ExpFetch.class, ExpId.class},
                                  false);
                validate (exp, tyFloat, true);


                // 1.0 op 1
                exp = applyExp (op, fone, one);

                // we should have:
                // OpFloat (ConstFloat, OpArithmeticConversion(ConstInt))

                validate (exp, OpFloat.class,
                                  new Object [] {ConstFloat.class},
                                  new Object [] {OpArithmeticConversion.class,
                                                                 ConstInt.class},
                                  false);
                validate (exp, tyFloat, tyFloat, tyFloat, true);

                // y op 1
                exp = applyExp (op, y, one);

                // we should have:
                // OpFloat (ExpFetch (ExpId), OpArithmeticConversion(ConstInt))

                validate (exp, OpFloat.class,
                                  new Object [] {ExpFetch.class, ExpId.class},
                                  new Object [] {OpArithmeticConversion.class,
                                                                 ConstInt.class},
                                  false);
                validate (exp, tyFloat, tyFloat, tyFloat, true);

                // y op x
                exp = applyExp (op, y, x);

                // we should have:
                // OpFloat (ExpFetch (ExpId),
                //           OpArithmeticConversion (ExpFetch (ExpId)))

                validate (exp, OpFloat.class,
                                  new Object [] {ExpFetch.class, ExpId.class},
                                  new Object [] {OpArithmeticConversion.class,
                                                                 ExpFetch.class, ExpId.class},
                                  false);
                validate (exp, tyFloat, tyFloat, tyFloat, true);

                // x op y
                exp = applyExp (op, x, y);

                // we should have:
                // OpFloat (OpArithmeticConversion (ExpFetch (ExpId)),
                //           ExpFetch (ExpId))

                validate (exp, OpFloat.class,
                                  new Object [] {OpArithmeticConversion.class,
                                                                 ExpFetch.class, ExpId.class},
                                  new Object [] {ExpFetch.class, ExpId.class},
                                  false);
                validate (exp, tyFloat, tyFloat, tyFloat, true);

        }

        /**
         * Testing expressions as seen in the following code:
         * <pre>
         * int x;
         * x = 5 op 1;
         * x = x op 1;
         * x = 1 op x;
         * x = x op x;
         *</pre>
         */
        private void standardIntegralArithmeticTests (ScopedName op) {

                // use eb_BuiltInExpression to generate the expression for 5 op 1
                exp = applyExp (op, five, one);

                // we should have:
                // OpInt (ConstInt, ConstInt)
                validate (exp, OpInt.class, ConstInt.class, ConstInt.class, false);
                validate (exp, tyInt, true);

                // x op 1
                exp = applyExp (op, x, one);
                System.out.println( exp.ppToString (4, 80));

                // we should have:
                // OpInt (ExpFetch (ExpId), ConstInt)

                validate (exp, OpInt.class,
                                  new Object [] {ExpFetch.class, ExpId.class},
                                  new Object [] {ConstInt.class},
                                  false);
                validate (exp, tyInt, true);

                // 1 op x
                exp = applyExp (op, one, x);

                // we should have:
                // OpInt (ConstInt, ExpFetch (ExpId))

                validate (exp, OpInt.class,
                                  new Object [] {ConstInt.class},
                                  new Object [] {ExpFetch.class, ExpId.class},
                                  false);
                validate (exp, tyInt, true);

                // x op x
                exp = applyExp (op, x, x);

                // we should have:
                // OpInt (ExpFetch (ExpId), ExpFetch (ExpId))

                validate (exp, OpInt.class,
                                  new Object [] {ExpFetch.class, ExpId.class},
                                  new Object [] {ExpFetch.class, ExpId.class},
                                  false);
                validate (exp, tyInt, true);
        }

        /**
         * Testing expressions as seen in the following code, assuming an initialized
         * int *p:
         * <pre>
         * p = p op 1;
         * p = 1 op p;
         *</pre>
         */
        private void pointerAdditionTests (ScopedName op) {

                // p op 1
                exp = applyExp (op, p, one);

                // we should have:
                // OpPointer (ExpFetch (ExpId), ConstInt)

                validate (exp, OpPointer.class,
                                  new Object [] {ExpFetch.class, ExpId.class},
                                  new Object [] {ConstInt.class},
                                  false);
                validate (exp, tyPointer, true);

                // 1 op p
                exp = applyExp (op, one, p);

                // we should have:
                // OpPointer (ConstInt, ExpFetch (ExpId))

                validate (exp, OpPointer.class,
                                  new Object [] {ConstInt.class},
                                  new Object [] {ExpFetch.class, ExpId.class},
                                  false);
                validate (exp, tyPointer, true);
        }

        /**
         * Testing expressions as seen in the following code, assuming an initialized
         * int *p:
         * <pre>
         * p = p op 1;
         * p = p op p;
         *</pre>
         */
        private void pointerSubtractionTests (ScopedName op) {

                // p op 1
                exp = applyExp (op, p, one);

                // we should have:
                // OpPointer (ExpFetch (ExpId), ConstInt)

                validate (exp, OpPointer.class,
                                  new Object [] {ExpFetch.class, ExpId.class},
                                  new Object [] {ConstInt.class},
                                  false);
                validate (exp, tyPointer, true);

                // p op p
                exp = applyExp (op, p, p);

                // we should have:
                // OpPointer (ExpFetch (ExpId), ExpFetch (ExpId))

                validate (exp, OpPointer.class,
                                  new Object [] {ExpFetch.class, ExpId.class},
                                  new Object [] {ExpFetch.class, ExpId.class},
                                  false);
                validate (exp, tyInt, tyPointer, tyPointer, true);

        }

        /**
         * Testing assignment as seen in the following code:
         * <pre>
         * int x;
         * float y;
         * x = 5;
         * x = x;
         * y = x;
         * x = y;
         *</pre>
         */
        public void testAssignment () {
                ScopedName op = new Cpp_ScopedName (OpTable.get (ASSIGN));

                // use eb_Operator to generate the expression for x = 5
                exp = applyExp (op, x, five);

                // we should have:
                // OpAssign (ExpId, ConstInt)

                validate (exp, OpAssign.class,
                                  new Object [] {ExpId.class},
                                  new Object [] {ConstInt.class},
                                  false);
                assertTrue (exp.get_type () instanceof TyRef);

                // use eb_Operator to generate the expression for x = x
                exp = applyExp (op, x, x);

                // we should have:
                // OpAssign (ExpId, ExpFetch (ExpId))

                validate (exp, OpAssign.class,
                                  new Object [] {ExpId.class},
                                  new Object [] {ExpFetch.class, ExpId.class},
                                  false);
                assertTrue (exp.get_type () instanceof TyRef);

                // use eb_Operator to generate the expression for y = x
                exp = applyExp (op, y, x);

                // we should have:
                // OpAssign (ExpId, OpArithmeticConversion(ExpFetch (ExpId)))

                validate (exp, OpAssign.class,
                                  new Object [] {ExpId.class},
                                  new Object [] {OpArithmeticConversion.class,
                                                                 ExpFetch.class, ExpId.class},
                                  false);
                assertTrue (exp.get_type () instanceof TyRef);

                // use eb_Operator to generate the expression for x = y
                exp = applyExp (op, x, y);

                // we should have:
                // OpAssign (ExpId, OpArithmeticConversion(ExpFetch (ExpId)))

                validate (exp, OpAssign.class,
                                  new Object [] {ExpId.class},
                                  new Object [] {OpArithmeticConversion.class,
                                                                 ExpFetch.class, ExpId.class},
                                  false);
                assertTrue (exp.get_type () instanceof TyRef);
                assertTrue (exp.child_exp(1).get_type () instanceof TyInt);

        }

        /**
         * Testing binary logical operations (and, or) as seen in the following code:
         * <pre>
         * int x;
         * float y;
         * bool b;
         * int [] a;
         * int * p;
         * b op true;
         * b op 1;
         * b op b;
         * x op true;
         * y op true;
         * c op true;
         * a op true;
         * p op true;
         * x op b;
         * x op y;
         * x op c;
         * x op a;
         * x op p;
         *</pre>
         */
        public void testBinaryLogical () {
                binaryLogicalTests (new Cpp_ScopedName (OpTable.get (AND)));
                binaryLogicalTests (new Cpp_ScopedName (OpTable.get (OR)));
        }

        private void binaryLogicalTests (ScopedName op) {

                // use eb_Operator to generate the expression for b op true
                exp = applyExp (op, b, _true);

                // we should have:
                // OpLogical (ExpFetch (ExpId), ConstInt)
                validate (exp, OpLogical.class,
                                  new Object [] {ExpFetch.class, ExpId.class},
                                  new Object [] {ConstInt.class},
                                  false);
                validate (exp, tyBool, true);

                // use eb_Operator to generate the expression for b op 1
                exp = applyExp (op, b, one);

                // we should have:
                // OpLogical (ExpFetch (ExpId), OpArithmeticConversion(ConstInt))
                validate (exp, OpLogical.class,
                                  new Object [] {ExpFetch.class, ExpId.class},
                                  new Object [] {OpArithmeticConversion.class,
                                                                 ConstInt.class},
                                  false);
                validate (exp, tyBool, true);

                // use eb_Operator to generate the expression for b op b
                exp = applyExp (op, b, b);

                // we should have:
                // OpLogical (ExpFetch (ExpId), ExpFetch (ExpId))
                validate (exp, OpLogical.class,
                                  new Object [] {ExpFetch.class, ExpId.class},
                                  new Object [] {ExpFetch.class, ExpId.class},
                                  false);
                validate (exp, tyBool, true);

                // use eb_Operator to generate the expression for x op true
                exp = applyExp (op, x, _true);

                // we should have:
                // OpLogical (OpArithmeticConversion (ExpFetch (ExpId)), ConstInt)
                validate (exp, OpLogical.class,
                                  new Object [] {OpArithmeticConversion.class,
                                                                 ExpFetch.class, ExpId.class},
                                  new Object [] {ConstInt.class},
                                  false);
                validate (exp, tyBool, true);

                // use eb_Operator to generate the expression for y op true
                exp = applyExp (op, y, _true);

                // we should have:
                // OpLogical (OpArithmeticConversion (ExpFetch (ExpId)), ConstInt)
                validate (exp, OpLogical.class,
                                  new Object [] {OpArithmeticConversion.class,
                                                                 ExpFetch.class, ExpId.class},
                                  new Object [] {ConstInt.class},
                                  false);
                validate (exp, tyBool, true);


                // for future expansion..
                applyBadExp (op, c, _true);

                // use eb_Operator to generate the expression for a op true
                exp = applyExp (op, a, _true);

                // we should have:
                // OpLogical (OpArithmeticConversion (OpAddressOf (ExpFetch (ExpId))), ConstInt)
                validate (exp, OpLogical.class,
                                  new Object [] {OpArithmeticConversion.class,
                                                                 OpAddressOf.class, ExpFetch.class,
                                                                 ExpId.class},
                                  new Object [] {ConstInt.class},
                                  false);
                validate (exp, tyBool, true);

                // use eb_Operator to generate the expression for p op true
                exp = applyExp (op, p, _true);

                // we should have:
                // OpLogical (OpArithmeticConversion (ExpFetch (ExpId)), ConstInt)
                validate (exp, OpLogical.class,
                                  new Object [] {OpArithmeticConversion.class,
                                                                 ExpFetch.class, ExpId.class},
                                  new Object [] {ConstInt.class},
                                  false);
                validate (exp, tyBool, true);

                // use eb_Operator to generate the expression for x op b
                exp = applyExp (op, x, b);

                // we should have:
                // OpLogical (OpArithmeticConversion (ExpFetch (ExpId)), ExpFetch (ExpId))
                validate (exp, OpLogical.class,
                                  new Object [] {OpArithmeticConversion.class,
                                                                 ExpFetch.class, ExpId.class},
                                  new Object [] {ExpFetch.class, ExpId.class},
                                  false);
                validate (exp, tyBool, true);

                // use eb_Operator to generate the expression for x op x
                exp = applyExp (op, x, x);

                // we should have:
                // OpLogical (OpArithmeticConversion (ExpFetch (ExpId)), OpArithmeticConversion (ExpFetch (ExpId)))
                validate (exp, OpLogical.class,
                                  new Object [] {OpArithmeticConversion.class,
                                                                 ExpFetch.class, ExpId.class},
                                  new Object [] {OpArithmeticConversion.class,
                                                                 ExpFetch.class, ExpId.class},
                                  false);
                validate (exp, tyBool, true);

                // use eb_Operator to generate the expression for x op y
                exp = applyExp (op, x, y);

                // we should have:
                // OpLogical (OpArithmeticConversion (ExpFetch (ExpId)), OpArithmeticConversion (ExpFetch (ExpId)))
                validate (exp, OpLogical.class,
                                  new Object [] {OpArithmeticConversion.class,
                                                                 ExpFetch.class, ExpId.class},
                                  new Object [] {OpArithmeticConversion.class,
                                                                 ExpFetch.class, ExpId.class},
                                  false);
                validate (exp, tyBool, true);

        }


}

