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

package tm.cpp.analysis;

import tm.clc.analysis.CGRAndTest;
import tm.clc.analysis.CGRAssertion;
import tm.clc.analysis.CGRAssignment;
import tm.clc.analysis.CGRConditionalNode;
import tm.clc.analysis.CGRNotTest;
import tm.clc.analysis.CGRNullexpTest;
import tm.clc.analysis.CGROperandBranchNode;
import tm.clc.analysis.CGROperandNode;
import tm.clc.analysis.CGROrTest;
import tm.clc.analysis.CGRParentheses;
import tm.clc.analysis.CGRSequentialNode;
import tm.clc.analysis.CGRTest;
import tm.clc.analysis.CGRTypeConversion;
import tm.clc.analysis.CGRTypeTest;
import tm.clc.analysis.CGRTypeidOperandTest;
import tm.clc.analysis.CGRUnimplemented;
import tm.clc.analysis.CTSymbolTable;
import tm.clc.analysis.CodeGenRule;
import tm.clc.analysis.Declaration;
import tm.clc.analysis.ExpressionBuilder;
import tm.clc.analysis.ExpressionPtr;
import tm.clc.analysis.RuleBase;
import tm.clc.analysis.ScopedName;
import tm.clc.ast.ExpScratch;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.TyAbstractClass;
import tm.clc.ast.TyAbstractClassDeclared;
import tm.clc.ast.TyAbstractRef;
import tm.clc.ast.TypeNode;
import tm.cpp.ast.ExpFunctionName;
import tm.cpp.ast.TyClass;
import tm.cpp.ast.TyRef;
import tm.cpp.parser.ParserConstants;
import tm.utilities.Assert;
import tm.utilities.Debug;


/**
 * An <code>ExpressionBuilder</code> is a <code>CodeGenRule</code> that serves
 * as a point of access to a specific range of <em>rulesets</em>. The method(s)
 * by which access is provided will vary according to the rulesets covered.
 * <p>The <code>ExpressionBuilder</code> is also responsible for
 * building the rulesets (collections of <code>CGRNode</code>s) that
 * represent and enforce the rules required to generate a range of expressions
 * Generally, this happens once in the lifetime of the
 * <code>ExpressionBuilder</code>, but no constraint is imposed in this regard.
 * <p>Each <code>ExpressionBuilder</code> has access to the generic repository
 * of <code>CodeGenRules</code> represented by the <code>RuleBase</code>, which
 * it uses to construct rulesets.
 */
public abstract class CppExpressionBuilder extends ExpressionBuilder
    implements ParserConstants {
    public static final String R_COMPLETE_TYPE =
        "ensure object has complete type";
    public static final String R_PTR_TO_CLASS_OBJECT =
        "ensure expression has type \"pointer to class X\"";
    public static final String R_COMPOSITE_PTR_CONVERSION =
        "conversion to composite pointer type";
    public static final String R_UNARY_PTR_PLUS_OP =
        "unary + applied to pointer";
    public static final String R_POINTER_OP =
        "generate a pointer operation expression";
    public static final String R_POINTER_POINTER_SUBTRACTION =
        "generate a pointer - pointer (pointer subtraction) expression";
    public static final String R_DEREFERENCE =
        "generate a pointer dereference";
    public static final String R_ADDRESS_OF =
        "generate a pointer (address-of expression)";
    public static final String R_CONVERT_TO_REFERENCE =
        "generate a convert to reference expression";
    public static final String R_CONVERT_TO_POINTER =
        "generate a conversion to a pointer" ;
    public static final String R_REFERENCE_LVAL =
        "perform an extra fetch if an expression evaluates to a reference";

    public static final String R_SIZEOF =
        "sizeof operator applied to an expression or typeid";

    public static final String R_DELETE_EXPRESSION =
        "generate a delete expression";

    public static final String R_FN_PTR =
        "preprocess function pointer expression";

    public static final String R_MAKE_TEMPORARY =
        "generate a directive to make a temporary variable";
    public static final String R_CONSTRUCTOR_CALL =
        "generate a constructor call expression";
    public static final String R_INITIALIZE_SUBOBJECT =
        "inititialize subobject";
    public static final String R_CONSTRUCTOR_NAME =
        "build constructor name";
    public static final String R_DESTRUCTOR_NAME =
        "build destructor name";
    public static final String R_CAST_TO_FUNDAMENTAL =
        "generate a cast to a fundamental type";
    public static final String R_IMPLICIT_CONVERSION =
        "generate an implicit conversion sequence";
    
    public static final String R_DESTRUCTOR_CALL =
        "generate a destructor call expression";

    public static final String RN_PTR_OPERAND_FETCH =
        "fetch pointer operand";
    public static final String RN_FETCH_MODIFIABLE =
        "fetch modifiable lvalue";

    public static final String R_OVERLOADED_OPERATOR =
        "generate an overloaded operator call";
    public static final String T_OVERLOADED_OPERATOR =
        "test for overloaded operator";
    public static final String T_GLOBALLY_QUALIFIED =
        "test for globally qualified id";
    public static final String T_UT_CLASS_0 =
        "test that operand 0 is of class type";
    public static final String T_UT_CLASS_1 =
        "test that operand 1 is of class type";
    public static final String T_DERIVES =
        "test that type of operand 1 equals or derives from type of operand 0";
    public static final String T_GREATER_EQUAL_CVQ =
        "test that cvq of operand 0 >= cvq of operand 1";

    public static boolean buildRules = true;

    protected CTSymbolTable symbolTable;

    public static final String OVERLOADED_OPERATOR_NOT_FOUND =
        "no suitable overloaded function definition found for the operator";


    /**
     * Creates a new <code>CppExpressionBuilder</code> instance.
     * @param ruleBase the set of <code>CodeGenRule</code>s using which
     * rulesets will be constructed and applied.
     */
    public CppExpressionBuilder (RuleBase ruleBase, CTSymbolTable st,
                                 String expType) {
        super (expType);
        symbolTable = st;
        if (ruleBase != this.rb) buildRules = true;
        this.rb = ruleBase;
        if (buildRules) buildCommonRules ();
        buildTables ();
    }

    /** build the rule sequences that are common to several expression
     * builders
     */
    protected void buildCommonRules () {
        super.buildCommonRules ();
        this.buildRules = false;

        rb.put (R_ARITHMETIC_CONVERSION, new CGRArithmeticConversion ());
        rb.put (R_INTEGRAL_PROMOTION, new CGRIntegralPromotion ());
        rb.put (R_BOOLEAN_CONVERSION, new CGRConvertToBoolean ());

        rb.put (R_MODIFIABLE_LVALUE,
                new CGRAssertion (new CGRModifiableLvalueTest ()));
        rb.put (R_LVALUE, new CGRAssertion (new CGRLvalueTest ()));

        StandardConversions sc = StandardConversions.getInstance ();

        rb.put (R_CONVERT_RVALUE, new CGRTypeConversion (sc, 1, 0, false, false));
        rb.put (R_IMPLICIT_CONVERSION,
                new CGRConversionSequence (symbolTable));

        // ?? where can I get/put the parenthesis strings?
        rb.put (R_PARENTHESES_EXPRESSION, new CGRParentheses ("(", ")"));

        // CGRConditionalNode tests
        rb.put (T_NULLEXP, new CGRNullexpTest ());
        rb.put (T_LVALUE_FN, new CGRUnimplemented (T_LVALUE_FN));
        rb.put (T_TYPEID_OPERAND, new CGRTypeidOperandTest ());
        rb.put (T_REFERENCE, new CGRTypeTest (TyRef.class));
        rb.put (T_OVERLOADED_OPERATOR, new CGROverloadedTest (symbolTable));
        rb.put (T_GLOBALLY_QUALIFIED, new CGRGlobalQualTest ());
        rb.put (T_UT_CLASS_0, new CGRTypeTest (TyClass.class, 0));
        rb.put (T_UT_CLASS_1, new CGRTypeTest (TyClass.class, 1));
        rb.put (T_DERIVES, new CGRDerivesTest ());
        rb.put (T_GREATER_EQUAL_CVQ, new CGRGeqCvqTest ());

        rb.put (R_COMPLETE_TYPE,
                new CGRAssertion (new CGRCompleteTypeTest ()));
        rb.put (R_PTR_TO_CLASS_OBJECT,
                new CGRUnimplemented (R_PTR_TO_CLASS_OBJECT));
        rb.put (R_COMPOSITE_PTR_CONVERSION,
                new CGRCompositePointerConversion ());
        rb.put(R_CONVERT_TO_POINTER,
               new CGRConvertToPointer ());

        // 8.5.3 destination type a reference
        // note: see 8.5.3-4 for definition of reference-related and
        //       reference-compatible
        CGRTest test_reference_related =
            new CGROrTest
                ((CGRTest) rb.get (T_EQUIVALENT_TYPES),
                 (CGRTest) rb.get (T_DERIVES));

        CGRTest test_reference_compatible =
            new CGROrTest
                ((CGRTest) rb.get (T_GREATER_EQUAL_CVQ),
                 test_reference_related);

        // Convert arguments prior to call.
        // Assumes operand 0 is an IdExp with the parameter type
        // (except value class parameters are passed by reference,
        // so the type is modifed to a reference to the parameter type,
        // in that case.)
        // Operands 1 is the argument.
        // The result is left in operand 1.
        // There are three major cases:
        //  * Pass by value ofr scalars.
        //  * Pass by value for classes.
        //  * Pass by reference.

        CodeGenRule rs_convert_scalar_argument =
             new CGROperandBranchNode
                (rb.get (R_CONVERT_RVALUE), new CodeGenRule []
                    {rb.get (R_NONE), rb.get (R_FETCH)});

        // Class values are passed by reference and then
        // are copy constructed inside the function as
        // part of parameter initialization.
        CodeGenRule rs_convert_class_value_argument =
           new CGRSequentialNode
               ( rb.get (R_IMPLICIT_CONVERSION),
                 new CGRConvertToReference(1) ) ;

        // TODO put in the right rules for converting
        // arguments that match an ellipsis.
        CodeGenRule rs_convert_no_parameter_argument =
            rb.get (R_FETCH) ;
        
        // Value arguments.  The first argument is a dummy
        // IdExp node. Its type is reference to the parameter
        // type.  Except, if the parameter is a class type,
        // then it will have been replaced with a reference
        // to the class and hence we have a reference to
        // a reference.
        CodeGenRule rs_convert_value_argument =
            new CGRConditionalNode
                (new CGRRefRefTest(0),
                 rs_convert_class_value_argument,
                 rs_convert_scalar_argument) ;

        // TBD. IMPLEMENT THIS RULE
        CodeGenRule r_make_temporary_copy =
            new CGRUnimplemented ("initialization of reference variable by rvalue requiring a copy");

        // See 8.5.3. para 5. Can T2 be implicitly converted to an lvalue of type
        // “cv3 T3,” where “cv1 T1” is reference-compatible with “cv3 T3” 92)?
        // (this conversion is selected by enumerating the applicable conversion
        // functions (13.3.1.6) and choosing the best one through over-load resolution (13.3)).
        // TBD. IMPLEMENT THIS TEST!
        CGRTest t_implicit_conversion_is_possible = new CGRTest() {
            public boolean applies (ExpressionPtr exp) { return false ; } } ;

        // Conversion required for reference variables and parameters.  See ISO 8.5.3
        CodeGenRule rs_convert_reference_initializer =
            // If argument is an lvalue and cv1 T1 is reference-compatible with cv2 T2
            new CGRConditionalNode
                (new CGRAndTest(new CGRLvalueTest (1), test_reference_related),
                 // Then no conversion is needed.
                 rb.get (R_NONE),
                 // Else if the initializer is of class type and implicet conversion is possible
                 new CGRConditionalNode
                     (new CGRAndTest( (CGRTest) rb.get (T_UT_CLASS_1),
                                      t_implicit_conversion_is_possible ),
                      // Then do the conversion
                      rb.get (R_IMPLICIT_CONVERSION),
                      // Else the parameter must be const and not volatile
                      new CGRSequentialNode
                          ( // If the initializer is an rvalue and a class
                            // and cv1 T1 is reference compatable with cv2 T2
                            new CGRConditionalNode
                            (new CGRAndTest( new CGRNotTest( new CGRLvalueTest (1) ),
                                                 new CGRAndTest( (CGRTest) rb.get (T_UT_CLASS_1),
                                                                 test_reference_compatible)),
                             // Then the ISO std gives a choice here. We can construct a temporary
                             // or not.  We choose not to create a temporary
                             new CGRConvertToReference(1),
                             // Else we need to make a temporary.
                             // TBD We'll punt on this for now
                             r_make_temporary_copy ),
                            new CGRConstTest (0) ) ) ) ;

        rb.put( R_CONVERT_VALUE_ARGUMENT, rs_convert_value_argument );
        rb.put( R_CONVERT_REFERENCE_INITIALIZER, rs_convert_reference_initializer );
        rb.put( R_CONVERT_NO_PARAMETER_ARGUMENT, rs_convert_no_parameter_argument) ;

        rb.put (R_DEREFERENCE, new CGRDereference (0));
        rb.put (R_ADDRESS_OF, new CGRAddressOf ());
        rb.put (R_CONVERT_TO_REFERENCE, new CGRConvertToReference ());
        rb.put (R_FN_PTR, new CGRUnimplemented (R_FN_PTR));

        rb.put (R_RESOLVE_FN_ID, new CGRResolveFn (symbolTable));
        rb.put (R_FUNCTION_CALL, new CGRFunctionCall (
            rs_convert_value_argument,
            rs_convert_reference_initializer,
            rs_convert_no_parameter_argument ));

        // ** rulesets

        // one operand a pointer
        CodeGenRule rn_ptr_operand_fetch =
            new CGRSequentialNode
                (rb.get (R_FETCH),
                 new CGRSequentialNode
                     (rb.get (R_CONVERT_TO_POINTER),  //Added TSN 2002 Sept 8
                      rb.get (R_COMPLETE_TYPE)));
        rb.put (RN_PTR_OPERAND_FETCH, rn_ptr_operand_fetch);

        // first make sure we have a modifiable lvalue, then fetch
        CodeGenRule rn_fetch_modifiable =
            new CGRSequentialNode (rb.get (R_FETCH),
                                     rb.get (R_MODIFIABLE_LVALUE));
        rb.put (RN_FETCH_MODIFIABLE, rn_fetch_modifiable);

        rb.put (R_CONSTRUCTOR_NAME,  new CGRConstructorName ());
        CodeGenRule rs_constructor_call =
            new CGRSequentialNode
                (rb.get (R_FUNCTION_CALL),
                 new CGRSequentialNode
                     (rb.get (R_RESOLVE_FN_ID),
                      rb.get (R_CONSTRUCTOR_NAME)));
        rb.put (R_CONSTRUCTOR_CALL, rs_constructor_call);

        rb.put (R_DESTRUCTOR_NAME,  new CGRDestructorName ());
        CodeGenRule rs_destructor_call =
            new CGRSequentialNode
                (rb.get (R_FUNCTION_CALL),
                 new CGRSequentialNode
                     (rb.get (R_RESOLVE_FN_ID),
                      rb.get (R_DESTRUCTOR_NAME)));
        rb.put (R_DESTRUCTOR_CALL, rs_destructor_call);

        CodeGenRule rs_reference_lval =
            new CGRConditionalNode
            ((CGRTest) rb.get (T_REFERENCE), rb.get (R_FETCH));
        rb.put (R_REFERENCE_LVAL, rs_reference_lval);
        dbg.msg (Debug.COMPILE, "added R_REFERENCE_LVAL to rulebase");

        // standard assignment expression
        CodeGenRule rn_assignment_lvalue = rb.get (R_MODIFIABLE_LVALUE);
        //			new CGRSequentialNode (rb.get (R_FETCH),
        //							 rb.get (R_MODIFIABLE_LVALUE));
        CodeGenRule rn_assignment_prep =
            new CGROperandBranchNode
            (rb.get (R_CONVERT_RVALUE),
             new CodeGenRule [] {rn_assignment_lvalue, rb.get (R_FETCH)});
        CodeGenRule rn_std_assignment =
            new CGRSequentialNode (new CGRAssignment (),
                                     rn_assignment_prep);
        rb.put (R_ASSIGNMENT_EXPRESSION, rn_std_assignment);
        CodeGenRule rn_init_assignment =
            new CGRSequentialNode
                (new CGRAssignment (),
                 new CGROperandBranchNode
                    (rb.get (R_CONVERT_RVALUE), new CodeGenRule []
                        {rb.get (R_LVALUE), rb.get (R_FETCH)}));
        rb.put (R_ASSIGNMENT_INITIALIZATION, rn_init_assignment);

        // Check the return type
        rb.put (R_VALID_RETURN_TYPE,
                new CGRAssertion (new CGRValidReturnTypeTest ()));

        // overloaded operator call
        CodeGenRule r_ol =
            new CGRSequentialNode
                (new CGRFunctionCall ( rs_convert_value_argument,
                                       rs_convert_reference_initializer,
                                       rs_convert_no_parameter_argument,
                                       true),
                 new CGRSequentialNode
                     (rb.get (R_VALID_RETURN_TYPE),
                      new CGRSequentialNode
                          (new CGRAssertion
                               (new CGROverloadedTest (symbolTable),
                                OVERLOADED_OPERATOR_NOT_FOUND),
                           new CGROperandNode
                               (rb.get (R_NONE),
                                rb.get (R_COMPLETE_TYPE))))) ;
        rb.put (R_OVERLOADED_OPERATOR, r_ol);

        // make scratch var
        rb.put (R_MAKE_TEMPORARY, new CGRMakeTemporary (-1, 0));
    }

    /**
     * Creates a new <code>ExpFunctionName</code> for a constructor
     * call.
     */
    abstract private class CGRConstructorOrDestructorName extends CodeGenRule {
        /**
         * Builds an <code>ExpFunctionName</code> for a constructor or
         * a destructor. Removes the class
         * type from the list of args once complete.
         */
        public void apply (ExpressionPtr exp) {

            TypeNode t;
            ExpressionNode c_obj = null;
            int opidx = (exp.get () == null) ? 0 : -1;
            t = exp.get_base_type (opidx);
            if (exp.is (ExpressionNode.class, opidx)) {
                dbg.msg (Debug.COMPILE, "using " + exp.get(opidx).ppToString (3, 80) +
                         " as class object, with type " +
                         exp.get_base_type(opidx).getClass().getName());
                c_obj = exp.get (opidx);
            }
            if (opidx != -1)
                // remove exp 0 (class type - just want args now)
                exp.removeOperand (opidx);

            Assert.check (t instanceof TyAbstractClassDeclared);
            Assert.error( ((TyAbstractClassDeclared)t).isDefined(), "Can not construct or destruct an object of incomplete type") ;
            ScopedName fqn = ((TyAbstractClassDeclared) t).getFullyQualifiedName ();
            ScopedName id = className2ScopedName (fqn.getTerminalId ());

            ExpFunctionName efn = new ExpFunctionName (id);
            Declaration decl = symbolTable.getClassDeclaration((TyAbstractClass) t) ;
            Assert.check( decl != null ) ;
            ClassSH c_scope = (ClassSH) (decl.getDefinition ());
            efn.setClassScope (c_scope);
            efn.setClassObject (c_obj);

            exp.set (efn);
        }
        
		abstract protected ScopedName className2ScopedName(String terminalId) ;
    }

    /**
     * Creates a new <code>ExpFunctionName</code> for a constructor
     * call.
     */
    public class CGRConstructorName extends CGRConstructorOrDestructorName {
    	
    	protected ScopedName className2ScopedName(String terminalId) {
			return new Cpp_ScopedName( terminalId ) ;
		}
    }

    /**
     * Creates a new <code>ExpFunctionName</code> for a destructor
     * call.
     */
    public class CGRDestructorName extends CGRConstructorOrDestructorName {
    	
    	protected ScopedName className2ScopedName(String terminalId) {
			return new Cpp_ScopedName( "~" + terminalId ) ;
		}
    }



    /**
     * Applies the conversion sequence required to convert operand 1 to
     * the type of operand 0
     */
    public class CGRConversionSequence extends CodeGenRule {
        private final UserDefinedConversions conversions;
        public CGRConversionSequence (CTSymbolTable st) {
            conversions = new UserDefinedConversions (st);
        }

        public void apply (ExpressionPtr exp) {
            TypeNode from = exp.get_type (1);
            TypeNode to = exp.get_type (0);
            ExpressionNode e = exp.get (1);
            exp.set (conversions.getConversionSequence (from, to).apply (e));
        }
    }

    /**
     * Does the first operand derive the second?
     */
    public class CGRDerivesTest extends CGRTest {
        private StandardConversions sc =
            StandardConversions.getInstance ();
        /**
         */
        public boolean applies (ExpressionPtr exp) {
            boolean derives = false;
            TypeNode t_one = exp.get_base_type (0);
            // First operand may be a reference to a reference.
            if( t_one instanceof TyAbstractRef ) {
                t_one = ((TyAbstractRef) t_one).getPointeeType() ; }
            TypeNode t_two = exp.get_base_type (1);
            if (t_one instanceof TyAbstractClass &&
                t_two instanceof TyAbstractClass) {
                int conv = sc.getClassConversion ((TyAbstractClass) t_two,
                                                  (TyAbstractClass) t_one);
                derives = (conv == sc.UP_CONVERSION ||
                           conv == sc.EQUIVALENT_TYPES);
            }

            return derives;
        }
    }

    /**
     * test: cvq operand[0] >= cvq operand[1]
     */
    public class CGRGeqCvqTest extends CGRTest {
        private StandardConversions sc =
            StandardConversions.getInstance ();
        /**
         */
        public boolean applies (ExpressionPtr exp) {
            int cvq1 = exp.get_base_type (0).getAttributes ();
            int cvq2 = exp.get_base_type (1).getAttributes ();
            dbg.msg (Debug.COMPILE, "comparing " + cvq1 + " and " + cvq2);
            return (cvq1 == cvq2 || sc.rankQualifiers (cvq1, cvq2) > 0);
        }

    }

    /**
     * Build expression to create temp variable in scratch region
     */
    public class CGRMakeTemporary extends CodeGenRule {
        public CGRMakeTemporary (int initIdx, int typeIdx) {
            super (initIdx, typeIdx);
        }
        public void apply (ExpressionPtr exp) {
            ExpressionNode init_exp = exp.get (start);
            TypeNode t = exp.get_type (end);
            if (!(t instanceof TyRef)) t = new TyRef (t);
            ExpressionNode temp_exp = new ExpScratch ((TyRef) t, init_exp);
            exp.set (temp_exp);
        }
    }

}


