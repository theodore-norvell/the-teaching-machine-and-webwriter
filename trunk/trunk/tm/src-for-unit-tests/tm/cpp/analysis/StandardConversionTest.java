package tm.cpp.analysis;

import tm.clc.analysis.*;
import tm.clc.ast.*;

import tm.cpp.ast.*;


import junit.framework.*;

import java.util.Vector;

/**
 * tests C++ implementation of standard conversion rules
 */

public class StandardConversionTest extends TestCase
    implements tm.cpp.analysis.TestConstantUser {

        StandardConversions sc;
        static boolean[] uninteresting = new boolean[1] ;
        static TyInt tyInt = TyInt.get ();
        static TyUnsignedInt tyUnsignedInt = TyUnsignedInt.get ();
        static TyLong tyLong = TyLong.get ();
        static TyBool tyBool = TyBool.get ();
        static TyChar tyChar = TyChar.get ();
        static TySignedChar tySignedChar = TySignedChar.get ();
        static TyFloat tyFloat = TyFloat.get ();
        static TyDouble tyDouble = TyDouble.get ();
        static TyLongDouble tyLongDouble = TyLongDouble.get ();
        static TyArray tyArrayInt = new TyArray ();
        static TyArray tyArray2d = new TyArray ();
        static TyPointer tyPointerInt = new TyPointer ();
        static TyPointer tyPointerPtr = new TyPointer ();
        static {
                tyPointerInt.addToEnd (tyInt);
                tyPointerPtr.addToEnd (tyPointerInt);
            tyArrayInt.setNumberOfElements(1) ;
                tyArrayInt.addToEnd (tyInt);
            tyArray2d.setNumberOfElements(1) ;
                tyArray2d.addToEnd (tyArrayInt);
        }

    public StandardConversionTest () { super ("StandardConversionTest"); }
    public StandardConversionTest (String name) { super (name); }

    protected void setUp () {
                sc = StandardConversions.getInstance ();
        }

        public void testConversionsToBool () {
                assertTrue (sc.determine (tyArrayInt, tyBool, uninteresting) ==
                                StandardConversions.PTR_TO_BOOL);

                assertTrue (sc.determine (tyPointerPtr, tyBool, uninteresting) ==
                                StandardConversions.PTR_TO_BOOL);

                assertTrue (sc.determine (tyBool, tyBool, uninteresting) ==
                                StandardConversions.EQUIVALENT_TYPES);

                assertTrue (sc.determine (tyInt, tyBool, uninteresting) ==
                                StandardConversions.BOOL_CONVERSION);

        }

        public void testConversionsToInt () {
                assertTrue (sc.determine (tyChar, tyInt, uninteresting) ==
                                StandardConversions.INT_PROMOTION);

                assertTrue (sc.determine (tyLong, tyInt, uninteresting) ==
                                StandardConversions.INT_CONVERSION);

                assertTrue (sc.determine (tyInt, tyInt, uninteresting) ==
                                StandardConversions.EQUIVALENT_TYPES);

                assertTrue (sc.determine (tyFloat, tyInt, uninteresting) ==
                                StandardConversions.FLOAT_INT_CONVERSION);

                assertTrue (sc.determine (tyArrayInt, tyInt, uninteresting) ==
                                StandardConversions.NONE);
        }

        public void testConversionsToLong () {
                assertTrue (sc.determine (tyBool, tyLong, uninteresting) ==
                                StandardConversions.INT_CONVERSION);

                assertTrue (sc.determine (tyInt, tyLong, uninteresting) ==
                                StandardConversions.INT_CONVERSION);

                assertTrue (sc.determine (tyLong, tyLong, uninteresting) ==
                                StandardConversions.EQUIVALENT_TYPES);

                assertTrue (sc.determine (tyFloat, tyLong, uninteresting) ==
                                StandardConversions.FLOAT_INT_CONVERSION);

                assertTrue (sc.determine (tyArrayInt, tyLong, uninteresting) ==
                                StandardConversions.NONE);
        }

        public void testConversionsToFloat () {
                assertTrue (sc.determine (tyInt, tyFloat, uninteresting) ==
                                StandardConversions.FLOAT_INT_CONVERSION);

                assertTrue (sc.determine (tyFloat, tyFloat, uninteresting) ==
                                StandardConversions.EQUIVALENT_TYPES);

                assertTrue (sc.determine (tyDouble, tyFloat, uninteresting) ==
                                StandardConversions.FLOAT_CONVERSION);

                assertTrue (sc.determine (tyArrayInt, tyFloat, uninteresting) ==
                                StandardConversions.NONE);
        }

        public void testConversionsToDouble () {
                assertTrue (sc.determine (tyInt, tyDouble, uninteresting) ==
                                StandardConversions.FLOAT_INT_CONVERSION);

                assertTrue (sc.determine (tyDouble, tyDouble, uninteresting) ==
                                StandardConversions.EQUIVALENT_TYPES);

                assertTrue (sc.determine (tyFloat, tyDouble, uninteresting) ==
                                StandardConversions.FLOAT_PROMOTION);

                assertTrue (sc.determine (tyArrayInt, tyDouble, uninteresting) ==
                                StandardConversions.NONE);
        }

        public void testConversionsToPointer () {

                assertTrue (sc.determine (tyInt, tyPointerInt, uninteresting) ==
                                StandardConversions.PTR_CONVERSION);

                assertTrue (sc.determine (tyArrayInt, tyPointerInt, uninteresting) ==
                                StandardConversions.ARRAY_TO_POINTER);

                assertTrue (sc.determine (tyPointerInt, tyPointerInt, uninteresting) ==
                                StandardConversions.EQUIVALENT_TYPES);

                assertTrue (sc.determine (tyPointerPtr, tyPointerInt, uninteresting) ==
                                StandardConversions.NONE);

                assertTrue (sc.determine (tyArray2d, tyPointerInt, uninteresting) ==
                                StandardConversions.NONE);

                assertTrue (sc.determine (tyArray2d, tyPointerPtr, uninteresting) ==
                                StandardConversions.ARRAY_TO_POINTER);

        }

        public void testConversionsToArray () {
                assertTrue (sc.determine (tyInt, tyArrayInt, uninteresting) ==
                                StandardConversions.NONE);

                assertTrue (sc.determine (tyArrayInt, tyArrayInt, uninteresting) ==
                                StandardConversions.EQUIVALENT_TYPES);

                assertTrue (sc.determine (tyPointerInt, tyArrayInt, uninteresting) ==
                                StandardConversions.EQUIVALENT_TYPES);

                assertTrue (sc.determine (tyPointerPtr, tyArrayInt, uninteresting) ==
                                StandardConversions.NONE);

                assertTrue (sc.determine (tyArray2d, tyArrayInt, uninteresting) ==
                                StandardConversions.NONE);

                assertTrue (sc.determine (tyPointerPtr, tyArray2d, uninteresting) ==
                                StandardConversions.EQUIVALENT_TYPES);

        }

        public void testArithmeticConversions () {
                Vector results;
                ExpressionNode expA, expB, sub;

                // long double, boolean
                expA = new ConstFloat (tyLongDouble, "2.0", 2.0);
                expB = new ConstInt (tyBool, "true", 1);

                results = sc.makeArithmeticConversionExpressions (expA, expB);
                // should have ConstFloat,
                // OpArithmeticConversion (OpArithmeticConversion (ConstInt))
                expA = (ExpressionNode) results.elementAt (0);
                expB = (ExpressionNode) results.elementAt (1);
                assertTrue (expA instanceof ConstFloat);
                assertTrue (expA.get_type().equal_types (tyLongDouble));
                assertTrue (expB instanceof OpArithmeticConversion);
                assertTrue (expB.get_type().equal_types (tyLongDouble));
                sub = expB.child_exp (0);
                assertTrue (sub instanceof OpArithmeticConversion);
                assertTrue (sub.get_type().equal_types (tyInt));
                sub = sub.child_exp (0);
                assertTrue (sub instanceof ConstInt);
                assertTrue (sub.get_type().equal_types (tyBool));


                // boolean, boolean
                expA = new ConstInt (tyBool, "true", 1);
                expB = new ConstInt (tyBool, "true", 1);

                results = sc.makeArithmeticConversionExpressions (expA, expB);
                // should have OpArithmeticConversion (ConstInt),
                // OpArithmeticConversion (ConstInt)
                expA = (ExpressionNode) results.elementAt (0);
                expB = (ExpressionNode) results.elementAt (1);
                assertTrue (expA instanceof OpArithmeticConversion);
                assertTrue (expA.get_type().equal_types (tyInt));
                sub = expA.child_exp (0);
                assertTrue (sub instanceof ConstInt);
                assertTrue (sub.get_type().equal_types (tyBool));
                assertTrue (expB instanceof OpArithmeticConversion);
                assertTrue (expB.get_type().equal_types (tyInt));
                sub = expB.child_exp (0);
                assertTrue (sub instanceof ConstInt);
                assertTrue (sub.get_type().equal_types (tyBool));

                // int, unsigned int
                expA = new ConstInt (tyInt, "2", 2);
                expB = new ConstInt (tyUnsignedInt, "2", 2);

                results = sc.makeArithmeticConversionExpressions (expA, expB);
                // should have OpArithmeticConversion (ConstInt), ConstInt
                expA = (ExpressionNode) results.elementAt (0);
                expB = (ExpressionNode) results.elementAt (1);
                assertTrue (expA instanceof OpArithmeticConversion);
                assertTrue (expA.get_type().equal_types (tyUnsignedInt));
                sub = expA.child_exp (0);
                assertTrue (sub instanceof ConstInt);
                assertTrue (sub.get_type().equal_types (tyInt));
                assertTrue (expB instanceof ConstInt);
                assertTrue (expB.get_type().equal_types (tyUnsignedInt));


                // double, long double
                expA = new ConstFloat (tyDouble, "2.0", 2.0);
                expB = new ConstFloat (tyLongDouble, "2.0", 2.0);

                results = sc.makeArithmeticConversionExpressions (expA, expB);
                // should have OpArithmeticConversion (ConstFloat), ConstFloat
                expA = (ExpressionNode) results.elementAt (0);
                expB = (ExpressionNode) results.elementAt (1);
                assertTrue (expA instanceof OpArithmeticConversion);
                assertTrue (expA.get_type().equal_types (tyLongDouble));
                sub = expA.child_exp (0);
                assertTrue (sub instanceof ConstFloat);
                assertTrue (sub.get_type().equal_types (tyDouble));
                assertTrue (expB instanceof ConstFloat);
                assertTrue (expB.get_type().equal_types (tyLongDouble));


                // char, signed char
                expA = new ConstInt (tyChar, "a", 'a');
                expB = new ConstInt (tySignedChar, "a", 'a');

                results = sc.makeArithmeticConversionExpressions (expA, expB);
                // should have OpArithmeticConversion (ConstInt),
                // OpArithmeticConversion (ConstInt)
                expA = (ExpressionNode) results.elementAt (0);
                expB = (ExpressionNode) results.elementAt (1);
                assertTrue (expA instanceof OpArithmeticConversion);
                assertTrue (expA.get_type().equal_types (tyInt));
                sub = expA.child_exp (0);
                assertTrue (sub instanceof ConstInt);
                assertTrue (sub.get_type().equal_types (tyChar));
                assertTrue (expB instanceof OpArithmeticConversion);
                assertTrue (expB.get_type().equal_types (tyInt));
                sub = expB.child_exp (0);
                assertTrue (sub instanceof ConstInt);
                assertTrue (sub.get_type().equal_types (tySignedChar));

                // long, char
                expA = new ConstInt (tyLong, "2", 2);
                expB = new ConstInt (tyChar, "a", 'a');

                results = sc.makeArithmeticConversionExpressions (expA, expB);
                // should have ConstInt,
                // OpArithmeticConversion (OpArithmeticConversion (ConstInt))
                expA = (ExpressionNode) results.elementAt (0);
                expB = (ExpressionNode) results.elementAt (1);
                assertTrue (expA instanceof ConstInt);
                assertTrue (expA.get_type().equal_types (tyLong));
                assertTrue (expB instanceof OpArithmeticConversion);
                assertTrue (expB.get_type().equal_types (tyLong));
                sub = expB.child_exp (0);
                assertTrue (sub instanceof OpArithmeticConversion);
                assertTrue (sub.get_type().equal_types (tyInt));
                sub = sub.child_exp (0);
                assertTrue (sub instanceof ConstInt);
                assertTrue (sub.get_type().equal_types (tyChar));



        }



}

