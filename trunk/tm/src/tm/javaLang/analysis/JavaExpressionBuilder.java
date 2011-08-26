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

import java.util.Hashtable;

import tm.clc.analysis.CGRAssertion;
import tm.clc.analysis.CGRAssignment;
import tm.clc.analysis.CGRBinaryFloatingOp;
import tm.clc.analysis.CGRBinaryIntegralOp;
import tm.clc.analysis.CGRBinaryLogicalOp;
import tm.clc.analysis.CGRBinaryPointerOp;
import tm.clc.analysis.CGRConditionalNode;
import tm.clc.analysis.CGRError;
import tm.clc.analysis.CGRIncrement;
import tm.clc.analysis.CGRNullexpTest;
import tm.clc.analysis.CGROperandBranchNode;
import tm.clc.analysis.CGROperandNode;
import tm.clc.analysis.CGRParentheses;
import tm.clc.analysis.CGRSequentialNode;
import tm.clc.analysis.CGRTypeTest;
import tm.clc.analysis.CGRTypeidOperandTest;
import tm.clc.analysis.CGRUnaryFloatingOp;
import tm.clc.analysis.CGRUnaryIntegralOp;
import tm.clc.analysis.CGRUnimplemented;
import tm.clc.analysis.CodeGenRule;
import tm.clc.analysis.ExpressionBuilder;
import tm.clc.analysis.ExpressionPtr;
import tm.clc.analysis.RuleBase;
import tm.clc.ast.TypeNode;
import tm.javaLang.ast.TyArithmetic;
import tm.javaLang.ast.TyBoolean;
import tm.javaLang.ast.TyClass;
import tm.javaLang.ast.TyFloating;
import tm.javaLang.ast.TyIntegral;
import tm.javaLang.ast.TyJava;
import tm.javaLang.ast.TyNull;
import tm.javaLang.ast.TyPointer;
import tm.javaLang.ast.TyPrimitive;
import tm.javaLang.ast.TyRef;
import tm.javaLang.parser.JavaParserConstants;
import tm.utilities.Assert;


public class JavaExpressionBuilder extends ExpressionBuilder implements JavaParserConstants{
    public static final String R_COMPLETE_TYPE =
        "ensure object has complete type";
    public static final String R_SIZEOF =
        "sizeof operator applied to an expression or typeid";
    public static final String R_MAKE_TEMPORARY =
        "generate a directive to make a temporary variable";
    public static final String R_CONSTRUCTOR_CALL =
        "generate a constructor call expression";
    public static final String R_INITIALIZE_SUBOBJECT =
        "inititialize subobject";
    public static final String R_CONSTRUCTOR_NAME =
        "build constructor name";
    public static final String R_CONVERT_ARGUMENT =
        "convert method argument";
    public static final String  R_PRIM_CONVERSION =
        "bidirectional primitive conversion";
    public static final String  R_PRIM_ASSIGN_CONVERSION =
    	"primitive assignment conversion";
	public static final String  R_REF_CONVERSION =
        "reference conversion";
    public static final String R_CASTING_CONVERT =
        "generate a cast";
    public static final String R_IMPLICIT_CONVERSION =
        "generate an implicit conversion sequence";
    public static final String RN_FETCH_MODIFIABLE =
        "fetch modifiable lvalue";
    public static final String R_INSTANCE_OF =
        "instanceof operator";
    public static final String T_UT_CLASS_0 =
        "test that operand 0 is of class type";
    public static final String T_UT_CLASS_1 =
        "test that operand 1 is of class type";
    public static final String T_DERIVES =
        "test that type of operand 1 equals or derives from type of operand 0";
    public static final String T_GREATER_EQUAL_CVQ =
        "test that cvq of operand 0 >= cvq of operand 1";
    private Java_CTSymbolTable symTab;
    private Hashtable unaryOperators, binaryOperators;
    private CodeGenRule ternary;
    static private JavaExpressionBuilder theBuilder = null;

    private JavaExpressionBuilder (Java_CTSymbolTable st){
        super("");
        symTab = st;
        rb = new RuleBase();
        buildTables();
    }
    
    static public JavaExpressionBuilder getBuilder(Java_CTSymbolTable st){
    	if( theBuilder == null)
    		theBuilder = new JavaExpressionBuilder(st);
    	return theBuilder;
    }
    /**
     * Build internal <code>rulesets</code> and add to <code>ruleBase</code>.
     * <br>Also build any rule tables, matching operand types to operators.
     *
     */
    protected void buildTables() {
        buildCommonRules();
        buildOperatorRules();
    }

    protected void buildCommonRules(){
        super.buildCommonRules();

        rb.put (R_LVALUE, new CGRAssertion (new CGRLvalueTest ()));

//        rb.put (R_IMPLICIT_CONVERSION,
//                new CGRConversionSequence (symbolTable));

        // ?? where can I get/put the parenthesis strings?
        rb.put (R_PARENTHESES_EXPRESSION, new CGRParentheses ("(", ")"));

        // CGRConditionalNode tests
        rb.put (T_NULLEXP, new CGRNullexpTest ());
        rb.put (T_LVALUE_FN, new CGRUnimplemented (T_LVALUE_FN));
        rb.put (T_TYPEID_OPERAND, new CGRTypeidOperandTest ());
        rb.put (T_REFERENCE, new CGRTypeTest (TyRef.class));
//        rb.put (T_OVERLOADED_OPERATOR, new CGROverloadedTest (symbolTable));
//        rb.put (T_GLOBALLY_QUALIFIED, new CGRGlobalQualTest ());
        rb.put (T_UT_CLASS_0, new CGRTypeTest (TyClass.class, 0));
        rb.put (T_UT_CLASS_1, new CGRTypeTest (TyClass.class, 1));
//        rb.put (T_DERIVES, new CGRDerivesTest ());
//        rb.put (T_GREATER_EQUAL_CVQ, new CGRGeqCvqTest ());

//        rb.put (R_COMPLETE_TYPE,
//                new CGRAssertion (new CGRCompleteTypeTest ()));
//        rb.put (R_PTR_TO_CLASS_OBJECT,
//                new CGRUnimplemented (R_PTR_TO_CLASS_OBJECT));
//        rb.put (R_COMPOSITE_PTR_CONVERSION,
//                new CGRCompositePointerConversion ());
//        rb.put(R_CONVERT_TO_POINTER,
//               new CGRConvertToPointer ());
//
        // TBD TBD TBD The following code is duplicated from Eb_Initialization
        // and should be consolidated.
        // 8.5.3 destination type a reference
        // note: see 8.5.3-4 for definition of reference-related and
        //       reference-compatible
//        CodeGenRule rs_reference_related =
//            new CGRConditionalNode
//                ((CGRTest) rb.get (T_EQUIVALENT_TYPES),
//                 rb.get (R_NONE),
//                 new CGRAssertion ((CGRTest) rb.get (T_DERIVES),
//                                    "initializer is not reference-related"));
//
//        CodeGenRule rs_reference_compatible =
//            new CGRSequentialNode
//                (new CGRAssertion ((CGRTest) rb.get (T_GREATER_EQUAL_CVQ),
//                                    "initializer is not reference-compatible"),
//                 rs_reference_related);
        // END TBD  END TBD  END TBD


/* Same conversion rule (Section 5.3 of JLS) as for assignment except that
 * only widening conversions accepted for primitives.
 * Convert arguments prior to call.
 * Assumes operand 0 is an IdExp with the parameter type
 * (except value class parameters are passed by reference,
 * so the type is modifed to a reference to the parameter type,
 * in that case.)
 * Operands 1 is the argument.
 * The result is left in operand 1.
 **/
        CodeGenRule rRefConversion =
            new CGRConditionalNode(new CGRWideningOrEqualTest(),
                    new CGRWideningReference(), new CGRError("Reference conversion must be widening"));
        rb.put(R_REF_CONVERSION, rRefConversion);

        CodeGenRule rPrimConversion = new CGRPrimitiveConversion(); // Bidirectional primitive conversion
        rb.put(R_PRIM_CONVERSION,  rPrimConversion);

  /* primitive assignment conversion, used for assignment and initialization, which allows narrowing
   * at compile time (section 5.2 of JLS).
   **/
        CodeGenRule rnPrimAssignConversion = new CGRConditionalNode(new CGRWideningOrEqualTest(),
                new CGRPrimitiveConversion(), new CGRAssignmentNarrowing());
        rb.put(R_PRIM_ASSIGN_CONVERSION,rnPrimAssignConversion);
        
        CodeGenRule rnConvertR = new CGRConditionalNode(
            new CGRTypeConformanceTest(), // if both opands are primitive
                new CGRConditionalNode(new CGRWideningOrEqualTest(),
                    rPrimConversion, new CGRError("Primitive conversion must be widening")),
                    rRefConversion);

        rb.put( R_CONVERT_ARGUMENT,
             new CGROperandBranchNode (rnConvertR,
                 new CodeGenRule [] {rb.get (R_NONE), rb.get (R_FETCH)}) );

        // 5.5 Casting Conversions
        CodeGenRule rRefCastingConversion =
            new CGRConditionalNode(new CGRWideningOrEqualTest(),
                    new CGRWideningReference(), new CGRNarrowingReference());
        CodeGenRule rnCastingConvertR = new CGRConditionalNode(
            new CGRTypeConformanceTest(), // if both operands are primitive
                rPrimConversion, rRefCastingConversion);
       
        // Casting:
        // Left operand is a type, right operand is an expression.
        //  (0) fetch the right operand
        //  (1) Four cases
        //       (a) If one is primitive and the other is not,
        //             then an error
        //       (b) Else If both types are primitive
        //            do a primitive converion (rPrimConversion)
        //       (c) Else (both are nonprimitive) 
        //            (c0) If right can be widened to the left type or they are equal types
        //                   do a widening reference conversion  (CGRWideningReference)
        //            (c1) Else they are incomparable or narrowing is possible
        //                   do a narrowing reference conversion (CGRNarrowingReference)
        //  (2) Move the right operand to the result position (CGRCast).
        //             
        rb.put( R_CASTING_CONVERT,
            new CGRSequentialNode(new CGRCast(),
             new CGROperandBranchNode (rnCastingConvertR,
                 new CodeGenRule [] {rb.get (R_NONE), rb.get (R_FETCH)}) ));




//        CodeGenRule rn_init_assignment =
//            new CGRSequentialNode
//                (new CGRAssignment (),
//                 new CGROperandBranchNode
//                    (rb.get (R_CONVERT_RVALUE), new CodeGenRule []
//                        {rb.get (R_LVALUE), rb.get (R_FETCH)}));
//        rb.put (R_ASSIGNMENT_INITIALIZATION, rn_init_assignment);

        // Check the return type
//        rb.put (R_VALID_RETURN_TYPE,
//                new CGRAssertion (new CGRValidReturnTypeTest ()));

        // overloaded operator call

        // make scratch var
//        rb.put (R_MAKE_TEMPORARY, new CGRMakeTemporary (-1, 0));
    }


    private void buildOperatorRules(){
        unaryOperators = new Hashtable ();
        binaryOperators = new Hashtable ();

        rb.put(R_ID_EXPRESSION,  new CGRIdExp (symTab));
        rb.put (R_UNARY_INTEGRAL_OP,
                new CGRUnaryIntegralOp (OpTable.getUnaryArith()));
        rb.put (R_UNARY_FLOATING_OP,
                new CGRUnaryFloatingOp (OpTable.getUnaryArith()));
        OperandTable lOperands, rOperands;

        // this node indicates that the rule sequence is not implemented,
        // not necessarily that the required expression nodes are not available
        CodeGenRule rnUnimplemented = rb.get (R_UNIMPLEMENTED_RULE);


        // terminal fetch is a common pattern
        CodeGenRule rnFetchTerminal = rb.get (R_FETCH);

        // steps 1 and 2 are a standard arithmetic sequence
        CodeGenRule rnStdArithmetic =
            new CGROperandNode(new CGRArithmeticConversion (), rnFetchTerminal);

        CodeGenRule rnStdReference =
            new CGROperandNode( new CGRBinaryReferenceConversion (), rnFetchTerminal) ;
        
        CodeGenRule rBinaryIntegralOp =
            new CGRBinaryIntegralOp (OpTable.getBinaryArith(), TyBoolean.get());

        CodeGenRule rBinaryFloatingOp =
            new CGRBinaryFloatingOp (OpTable.getBinaryArith(), TyBoolean.get()) ;
        
        CodeGenRule rBinaryPointerOp =
            new CGRBinaryPointerOp( OpTable.getBinaryLogical(), TyBoolean.get()) ;
        
        CodeGenRule rnIntegralArithmetic =
            new CGRSequentialNode( rBinaryIntegralOp, rnStdArithmetic );

        CodeGenRule rnFloatingArithmetic =
            new CGRSequentialNode( rBinaryFloatingOp, rnStdArithmetic);

        CodeGenRule rnReferenceArithmetic =
            new CGRSequentialNode( rBinaryPointerOp, rnStdReference ) ;
        
        // Binary operators

        // binary arithmetic conversion operators
        // rules are:
        // 1. fetch
        // 2. arithmetic conversion (Binary Numeric Promotion - 5.6.2 of JLS)
        // 3. build expression
        // multiplicative (*, /, %) per 15.17
        // additive for numeric only ( - ) per 15.18.2
        // Relational operators (<, <=, >, >=) per 15.20
        lOperands = new OperandTable ();
        rOperands = new OperandTable ();
        rOperands.put (TyFloating.class, rnFloatingArithmetic);
        rOperands.put (TyIntegral.class, rnIntegralArithmetic);
        lOperands.put (TyIntegral.class, rOperands);

        rOperands = new OperandTable ();
        rOperands.put (TyArithmetic.class, rnFloatingArithmetic);
        lOperands.put (TyFloating.class, rOperands);

        binaryOperators.put ("*", lOperands);
        binaryOperators.put ("/", lOperands);
        binaryOperators.put ("-", lOperands);
        binaryOperators.put ("%", lOperands);
        binaryOperators.put ("<", lOperands);
        binaryOperators.put (">", lOperands);
        binaryOperators.put ("<=", lOperands);
        binaryOperators.put (">=", lOperands);

        // + same as above but add strings
        lOperands = new OperandTable ();
        rOperands = new OperandTable ();
        rOperands.put (TyClass.class, new CGRUnimplemented("String concatenation")); // String concatenation
        rOperands.put (TyJava.class, new CGRSequentialNode(
            new CGRUnimplemented("String concatenation"), new CGRUnimplemented("String conversion"))); // String concatenation
        lOperands.put (TyClass.class, rOperands);

        rOperands = new OperandTable ();
        rOperands.put (TyIntegral.class, rnIntegralArithmetic);
        rOperands.put (TyFloating.class, rnFloatingArithmetic);
        lOperands.put (TyIntegral.class, rOperands);

        rOperands = new OperandTable ();
        rOperands.put (TyArithmetic.class, rnFloatingArithmetic);
        lOperands.put (TyFloating.class, rOperands);
        binaryOperators.put ("+", lOperands);

        // shift operators
        // <<, >>
        // rules
        // 1. fetch
        // 2. perform integral promotions
        // 3. create expression (type is type of left operand)
        CodeGenRule rnShift =
            new CGROperandNode(rBinaryIntegralOp,
                 new CGRSequentialNode(new CGRIntegralPromotion (), rnFetchTerminal));

        lOperands = new OperandTable ();
        rOperands = new OperandTable ();
        rOperands.put (TyIntegral.class, rnShift);
        lOperands.put (TyIntegral.class, rOperands);
        binaryOperators.put ("<<", lOperands);
        binaryOperators.put (">>", lOperands);
        binaryOperators.put (">>>", lOperands);


        // equality
        // ==, !=
        // TODO. Does the next line make sense?
        CodeGenRule rnLogical = new CGROperandNode(
            new CGRBinaryLogicalOp (OpTable.getBinaryLogical(),
                                    TyBoolean.get ()),
                                    rnFetchTerminal );

        lOperands = new OperandTable ();
        rOperands = new OperandTable ();
        rOperands.put (TyPointer.class, rnReferenceArithmetic);
        rOperands.put (TyNull.class, rnReferenceArithmetic);
        lOperands.put (TyPointer.class, rOperands);
        lOperands.put (TyNull.class, rOperands);

        rOperands = new OperandTable ();
        rOperands.put (TyBoolean.class, rnLogical);
        lOperands.put (TyBoolean.class, rOperands);

        rOperands = new OperandTable ();
        rOperands.put (TyIntegral.class, rnIntegralArithmetic);
        rOperands.put (TyFloating.class, rnFloatingArithmetic);
        lOperands.put (TyIntegral.class, rOperands);

        rOperands = new OperandTable ();
        rOperands.put (TyArithmetic.class, rnFloatingArithmetic);
        lOperands.put (TyFloating.class, rOperands);

        binaryOperators.put ("==", lOperands);
        binaryOperators.put ("!=", lOperands);

        // Bitwise and Logical Operators
        // per 15.22 of JLS
        // , &, ^, |

        lOperands = new OperandTable ();
        rOperands = new OperandTable ();
        rOperands.put (TyIntegral.class, rnIntegralArithmetic);
        lOperands.put (TyIntegral.class, rOperands);

        rOperands = new OperandTable ();
        rOperands.put (TyBoolean.class, rnLogical);
        lOperands.put (TyBoolean.class, rOperands);

        binaryOperators.put ("&", lOperands);
        binaryOperators.put ("|", lOperands);
        binaryOperators.put ("^", lOperands);

        // Conditional Logical Operators
        // && (15.23), ||(15.24)
        lOperands = new OperandTable ();
        rOperands = new OperandTable ();
        rOperands.put (TyBoolean.class, rnLogical);
        lOperands.put (TyBoolean.class, rOperands);
        binaryOperators.put ("&&", lOperands);
        binaryOperators.put ("||", lOperands);

        // assignment
        // general rules
        // 1. left operand modifiable lvalue
        // 2. fetch roperand
        // 3. if loperand is a reference, assignment applied to object referred to
        //   -- the fetch will have already occurred, but this may impact how the
        //      assignment expression node is constructed.
        // 4. standard conversion sequence on roperand if loperand is not a class
        //  (copy assignment operator if loperand is a class)
        // 6. build assignment expression ** nonstandard, fetch loperand

        rb.put (R_SHORTHAND_ASSIGNMENT_EXPRESSION,
                new CGRUnimplemented (R_SHORTHAND_ASSIGNMENT_EXPRESSION));

// standard assignment expression ( = )
// in Java, distinguish between reference and primitive assignment
// which use different conversion rules
        CodeGenRule rnPrimAssignment = new CGRSequentialNode (
                new CGRAssignment(), // do assignment after prepping by
            // confirming left is lvalue, fetching rvalue, then converting rvalue
                new CGROperandBranchNode(rb.get(R_PRIM_ASSIGN_CONVERSION),
                    new CodeGenRule [] {rb.get (R_LVALUE), rb.get (R_FETCH)})
            );

        CodeGenRule rnRefAssignment = new CGRSequentialNode (
                new CGRAssignment(), // do assignment after prepping by
            // confirming left is lvalue, fetching rvalue, then converting rvalue
                new CGROperandBranchNode( rb.get(R_REF_CONVERSION),
                    new CodeGenRule [] {rb.get (R_LVALUE), rb.get (R_FETCH)})
            );

        lOperands = new OperandTable ();
        rOperands = new OperandTable ();
        rOperands.put (TyPointer.class, rnRefAssignment);
        rOperands.put (TyNull.class, rnRefAssignment);
        lOperands.put (TyPointer.class, rOperands);
        rOperands = new OperandTable ();
        rOperands.put (TyPrimitive.class, rnPrimAssignment);
        lOperands.put (TyPrimitive.class, rOperands);

        binaryOperators.put ("=", lOperands);

//        CodeGenRule rnOpPrimAssignment = new CGRSequentialNode (rnPrimAssignment, new CGROpOpAssignGlue ());
//        CodeGenRule rnOpRefAssignment = new CGRSequentialNode (rnRefAssignment, new CGROpOpAssignGlue ());

// Compound assignment (JLS 15.26.2). Can only be applied to Strings (+=) and primitives
// Conversion rules are different than either assignment or arithmetic
// Instead of implied conversion you get implied casting which means bidirectional
// primitive conversion is permitted
        // *=, /=, %=, -=

        CodeGenRule rnIaAssign = new CGRSequentialNode(new CGRAssignment(),
            new CGROpOpNode(rnIntegralArithmetic, true));
        CodeGenRule rnFaAssign = new CGRSequentialNode(new CGRAssignment(),
            new CGROpOpNode(rnFloatingArithmetic, true));
        lOperands = new OperandTable ();
        rOperands = new OperandTable ();
        rOperands.put (TyIntegral.class, rnIaAssign);
        rOperands.put (TyFloating.class, rnFaAssign);
        lOperands.put (TyIntegral.class, rOperands);
        rOperands = new OperandTable ();
        rOperands.put (TyArithmetic.class, rnFaAssign);
        lOperands.put (TyFloating.class, rOperands);

        binaryOperators.put ("*=", lOperands);
        binaryOperators.put ("/=", lOperands);
        binaryOperators.put ("%=", lOperands);
        binaryOperators.put ("-=", lOperands);

        // +=
        lOperands = new OperandTable ();
        rOperands = new OperandTable ();
        rOperands.put (TyClass.class, new CGRUnimplemented("String concatenation"));
        rOperands.put(TyJava.class, new CGRSequentialNode(
            new CGRUnimplemented("String concatenation"), new CGRUnimplemented("String conversion")));
        lOperands.put (TyClass.class, rOperands);

        rOperands = new OperandTable ();
        rOperands.put (TyIntegral.class, rnIaAssign);
        rOperands.put (TyFloating.class, rnFaAssign);
        lOperands.put (TyIntegral.class, rOperands);

        rOperands = new OperandTable ();
        rOperands.put (TyPrimitive.class, rnFaAssign);
        lOperands.put (TyFloating.class, rOperands);

        binaryOperators.put ("+=", lOperands);

        // <<=, >>=
        CodeGenRule rnShiftAssign = new CGRSequentialNode(new CGRAssignment(),
            new CGROpOpNode(rnShift, true));
        rOperands.put (TyIntegral.class, rnShiftAssign);
        lOperands.put (TyIntegral.class, rOperands);
        binaryOperators.put ("<<=", lOperands);
        binaryOperators.put (">>=", lOperands);
        binaryOperators.put (">>>=", lOperands);

        // &=, |=, ^=
        lOperands = new OperandTable ();
        rOperands = new OperandTable ();
        rOperands.put (TyBoolean.class, new CGRSequentialNode(new CGRAssignment(),
            new CGROpOpNode(rnLogical,false)));
        lOperands.put (TyBoolean.class, rOperands);

        rOperands = new OperandTable ();
        rOperands.put (TyIntegral.class, rnIaAssign);
        lOperands.put (TyIntegral.class, rOperands);
        binaryOperators.put ("&=", lOperands);
        binaryOperators.put ("|=", lOperands);
        binaryOperators.put ("^=", lOperands);

        // comma
        // ,
        lOperands = new OperandTable ();
        rOperands = new OperandTable ();
        rOperands.put (TypeNode.class, rnUnimplemented);
        lOperands.put (TypeNode.class, rOperands);
        binaryOperators.put (OpTable.getImage(COMMA), lOperands);

// TODO
        // 5.2.1 subscripting : postfix-expression [expression]
//        rb.put (R_SUBSCRIPT, new CGRSubscript ();
        // 1. fetch operands
        // ** note --- OpArraySubscript requires unfetched lvalue as
        //    array operand, so proceeding differently here.
        // 2. one operand should be a pointer, the other integral or enum
        // 3. the pointer should be to a completely defined type
        // 4. build the expression
/*        CodeGenRule rsArrayPrep =
            new CGRSequentialNode
            (rb.get (R_CONVERT_TO_REFERENCE),
             rb.get (R_COMPLETE_TYPE));
        CodeGenRule rnSubscriptArray =
            new CGROperandBranchNode
                (rb.get (R_SUBSCRIPT),
                 new CodeGenRule [] {rsArrayPrep,
                                     rb.get (R_FETCH)});

        CodeGenRule rnSubscriptArrayRight =
            new CGRSequentialNode
                (rnSubscriptArray,
                 new CGRSwapOperands (0,1));

        CodeGenRule rnSubscriptPtr =
            new CGROperandBranchNode
                (rb.get (R_SUBSCRIPT),
                 new CodeGenRule [] {rb.get (RN_PTR_OPERAND_FETCH),
                                     rb.get (R_FETCH)});

        CodeGenRule rnSubscriptPtrRight =
            new CGRSequentialNode
                (rnSubscriptPtr,
                 new CGRSwapOperands (0,1));

        lOperands = new OperandTable ();
        rOperands = new OperandTable ();
        rOperands.put (TyIntegral.class, rnSubscriptPtr);
        lOperands.put (TyPointer.class, rOperands);
        rOperands = new OperandTable ();
        rOperands.put (TyIntegral.class, rnSubscriptArray);
        lOperands.put (TyJavaArray.class, rOperands);
        rOperands = new OperandTable ();
        rOperands.put (TyPointer.class, rnSubscriptPtrRight);
        rOperands.put (TyJavaArray.class, rnSubscriptArrayRight);
        lOperands.put (TyIntegral.class, rOperands);
        binaryOperators.put (OpTable.get (OPEN_BRACKET), lOperands);*/


        // 5.3.1 *** unary operators ***
        OperandTable operands;
        CodeGenRule rnUnaryFloating = new CGROperandNode(rb.get(R_UNARY_FLOATING_OP),rnFetchTerminal);;
        CodeGenRule rnUnaryIntegral = new CGROperandNode(rb.get(R_UNARY_INTEGRAL_OP),
            new CGRSequentialNode(new CGRIntegralPromotion(),rnFetchTerminal));


        // + : positive / value-of;  - : negation
        // 1. fetch operand
        // 2. perform integral promotion if integral
        // 3. generate expression
        operands = new OperandTable ();
        operands.put (TyFloating.class, rnUnaryFloating);
        operands.put (TyIntegral.class, rnUnaryIntegral);
        unaryOperators.put ("+", operands);
        unaryOperators.put ("-", operands);


        // ! : logical not
        // 1. fetch operand
        // 2. generate the expression
        operands = new OperandTable ();
        operands.put (TyBoolean.class,
                    new CGROperandNode(rb.get(R_UNARY_INTEGRAL_OP), rnFetchTerminal));
        unaryOperators.put ("!", operands);

        // ~ : bitwise not
        // 1. fetch operand
        // 2. perform integral promotions
        // 3. generate the expression
        operands = new OperandTable ();
        operands.put (TyIntegral.class, rnUnaryIntegral);
        unaryOperators.put ("~", operands);

        // 5.2.6 : prefix increment/decrement
        // 1. operand must be of arithmetic type
        // 2. operand must be modifiable lvalue
        // ** removed 3. fetch operand ** do not fetch operand, OpIncrement et al need the TyRef to assign.
        // 4. build expression

        // ++
        rb.put (R_PREFIX_INCREMENT, new CGRIncrement (true, true));
        CodeGenRule rnPrefixIncrement =
            new CGROperandNode (rb.get (R_PREFIX_INCREMENT),
                                  rb.get (R_LVALUE));
        operands = new OperandTable ();
        operands.put (TyArithmetic.class, rnPrefixIncrement);
        unaryOperators.put ("++", operands);

        // --
        rb.put (R_PREFIX_DECREMENT, new CGRIncrement (true, false));
        CodeGenRule rnPrefixDecrement =
            new CGROperandNode (rb.get (R_PREFIX_DECREMENT), rb.get (R_LVALUE));
        operands = new OperandTable ();
        operands.put (TyArithmetic.class, rnPrefixDecrement);
        unaryOperators.put ("--", operands);

// PostFix increment & Decrement (which are not classified as Unary operators, per se)
// Section 15.14 of JLS

        rb.put(R_POSTFIX_INCREMENT,  new CGROperandNode(
            new CGRIncrement(false, true),  rb.get(R_LVALUE)));
        rb.put(R_POSTFIX_DECREMENT,  new CGROperandNode(
            new CGRIncrement(false, false),  rb.get(R_LVALUE)));



        // 5.16 *** ternary conditional operator (? :) ***
        rb.put (R_TERNARY_CONDITIONAL_EXPRESSION,
                new CGRUnimplemented (R_TERNARY_CONDITIONAL_EXPRESSION));
        // first operand is converted to bool (rnLogicalOperand)

        // second and third operands undergo a barrage of comparisons and
        // possible conversions.
        // ** current implementation skips all evaluation involving these
        // operands
        CodeGenRule rsTca = new CGROperandBranchNode
            (rb.get (R_TERNARY_CONDITIONAL_EXPRESSION),
             new CodeGenRule []
                {rnFetchTerminal, rnFetchTerminal, rnFetchTerminal});
      }

    public void apply (ExpressionPtr exp) {

        CodeGenRule ruleset = null;
        switch (exp.operandCount ()) {
        case 1:
            // unary expression
            ruleset = findRuleset (exp, exp.op (), unaryOperators);
            break;
        case 2:
            // binary expression
            ruleset = findRuleset (exp, exp.op (), binaryOperators);
            break;
        case 3:
            // ternary expression
            ruleset = ternary;
            break;
        default:
            // invalid operands
            Assert.check ("Invalid operand count for " + exp.toString());
        }

        // apply the rules
        ruleset.apply (exp);
    }
}



