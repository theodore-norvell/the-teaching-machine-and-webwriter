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

import tm.clc.analysis.CGRAssertion;
import tm.clc.analysis.CGRConditionalNode;
import tm.clc.analysis.CGROperandBranchNode;
import tm.clc.analysis.CGRSequentialNode;
import tm.clc.analysis.CGRTest;
import tm.clc.analysis.CGRTypeConversion;
import tm.clc.analysis.CGRUnimplemented;
import tm.clc.analysis.CTSymbolTable;
import tm.clc.analysis.CodeGenRule;
import tm.clc.analysis.ExpressionPtr;
import tm.clc.analysis.RuleBase;
import tm.clc.analysis.ScopedName;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.OpArithmeticConversion;
import tm.clc.ast.OpDownConversion;
import tm.clc.ast.TypeNode;
import tm.cpp.ast.TyArray;
import tm.cpp.ast.TyClass;
import tm.cpp.ast.TyFundamental;
import tm.cpp.ast.TyPointer;
import tm.cpp.ast.TyRef;
import tm.cpp.ast.TyVoid;
import tm.cpp.parser.ParserConstants;
import tm.utilities.ApologyException;
import tm.utilities.Assert;
import tm.utilities.Debug;

/**
 * <code>ExpressionBuilder</code> responsible for cast notation in C++
 */
public class Eb_Cast extends CppExpressionBuilder
    implements ParserConstants {

    static final ScopedName SN_CONST_CAST =
        new Cpp_ScopedName (OpTable.get (CONST_CAST));
    static final ScopedName SN_STATIC_CAST =
        new Cpp_ScopedName (OpTable.get (STATIC_CAST));
    static final ScopedName SN_REINTERPRET_CAST =
        new Cpp_ScopedName (OpTable.get (REINTERPRET_CAST));
    static final ScopedName SN_DYNAMIC_CAST =
        new Cpp_ScopedName (OpTable.get (DYNAMIC_CAST));
    static final ScopedName SN_EMPTY =	new Cpp_ScopedName ();
    static final ScopedName SN_IMPLICIT_CAST =	SN_EMPTY;;

    public static final String NO_NONCLASS_QUAL_CONVERSION =
        "cv-qualification conversion is not permitted for {0}";
    public static final String INVALID_CAST_EXPRESSION =
        "cast expression from {1} to {0} invalid";
    public static final String NO_FCN_ARRAY =
        "parameterless functional cast notation cannot be used to initialize array";

    /** ruleset for a static cast to class type */
    CodeGenRule rs_static_cast_to_class;

    /** ruleset for default initialization */
    CodeGenRule rs_default_init;

    /**
     * Creates a new <code>Eb_Cast</code> instance.
     *
     * @param ruleBase a reference to the set of all <code>CodeGenRules</code>
     */
    public Eb_Cast (RuleBase ruleBase, CTSymbolTable symbolTable,
                    CodeGenRule rs_default_initialization) {
        super (ruleBase, symbolTable, "cast");
        rs_default_init = rs_default_initialization;
    }

    /**
     * Generates the AST representation of a cast expression in the cast
     * notation : <code>(T)v</code>
     * @param exp the expression components to this point, will contain
     * the cast expression
     */
    public void castNotationExpression (ExpressionPtr exp) {
        // 5.4 cast notation
        // (T)v
        TypeNode t = exp.get_type (0);
        ExpressionNode v = exp.get (1);

        // - cv-qualifications of T are ignored when T is a non-class type
        boolean ignoreCVQ = false;
        if (!(t instanceof TyClass) &&
            !(exp.get_base_type (0) instanceof TyClass)) {
            ignoreCVQ = true;
            if (t.equal_types (v.get_type ())) { // nothing to do /*CHECK*/
                exp.set (v);
                return;
            }
            //				Assert.apology (NO_NONCLASS_QUAL_CONVERSION,
            //					new String [] {t.name ()});
        }

        // - locate first conversion operation sequence that performs what
        //   we want among this list:
        //   1. const_cast
        //   2. static_cast
        //   3. static_cast, const_cast
        //   4. reinterpret_cast
        //   5. reinterpret_cast, const_cast
        CodeGenRule ruleset;
        if (!t.equal_types (v.get_type ())) { // try static cast /*CHECK*/
            try {
                ruleset = findRuleset (exp, SN_STATIC_CAST);
                ruleset.apply (exp);
                if (exp.get () != null) {
                    exp.set (exp.get (), 1);
                }
            } catch (ApologyException ignore) {
                // exception thrown when ruleset not found
            }
        }
        // try a const cast
        if (!ignoreCVQ) {
            try {
                ruleset = findRuleset (exp, SN_CONST_CAST);
                ruleset.apply (exp);
            } catch (ApologyException ignore) {
                // exception thrown when ruleset not found
            }
        }

        // if no luck so far, try a reinterpret cast possibly followed by a
        // const cast
        if (exp.get () == null) {
            try {
                ruleset = findRuleset (exp, SN_REINTERPRET_CAST);
                ruleset.apply (exp);
                if (exp.get () != null && !ignoreCVQ) {
                    exp.set (exp.get (), 1);
                    try {
                        ruleset = findRuleset (exp, SN_CONST_CAST);
                        ruleset.apply (exp);
                    } catch (ApologyException ignore) {
                        // exception thrown when ruleset not found
                    }
                }
            } catch (ApologyException ignore) {
                // exception thrown when ruleset not found
            }
        }

        // if nothing worked, generate an apology
        if (exp.get () == null) {
            Assert.apology (INVALID_CAST_EXPRESSION, new String []
                {exp.get_type (0).typeId(), exp.get_type (1).typeId ()});
        }

        // - v or T can be ptr to incomplete class type. In this case, even if
        //   there is an inheritance relationship, it's undefined as to
        //   whether static_cast or reinterpret_cast rules are applied

        // - even if the base class type is not accessible:
        //   - ptr to obj of derived type --> ptr to unambiguous base class
        //     type
        //   - lvalue of " " " --> reference to " " "
        //   - ptr to member ..  --> ..
        //   - ptr... to non-virtual base class type --> ptr... to derived
        //     class type

    }

    /**
     * Generates the AST representation of a cast in the functional notation
     * : <code>T(<em>expression_list</em>)</code>
     * @param exp the expression components to this point, will contain
     * the cast expression
     */
    public void functionNotationExpression (ExpressionPtr exp) {
        // 5.2.3 functional notation
        // T(expression-list)
        // - if T specifies a class type, the class type should be complete
        // - if T is a non-class type, cv-qualifiers of T are ignored when
        //   determining type of resulting rvalue

        if (exp.operandCount () == 2) {
            // - if expression-list is single expression v, effect is same as
            //   (T)v
            castNotationExpression (exp);
        } else if (exp.operandCount () > 2) {
            // - if >1 expression, T is a class with a suitably declared
            //   constructor. Result an rvalue.
            rs_static_cast_to_class.apply (exp);
        } else {
            TypeNode t = exp.get_type (0);
            // - if empty expression-list, non-array type T, creates an rvalue of
            //   type T that has undergone default initialization.
            if (t instanceof TyClass) {
                rs_static_cast_to_class.apply (exp);

            } else { // not a class
                if (t instanceof TyArray) {
                    // illegal - apologize
                    Assert.apology (NO_FCN_ARRAY, new String [] {t.typeId ()});

                } else { // default initialization of temporary non-class
                    exp.opcat = Eb_Initialization.DEFAULT_INIT_SN;
                    rs_default_init.apply (exp);
                }
            }
        }
    }


    /** builds rule sequences, adds to rulebase and operand tables */
    protected void buildTables () {

        OperandTable tOperands, eOperands; // types and expressions in T(e)

        CodeGenRule rn_unimplemented = rb.get (R_UNIMPLEMENTED_RULE);


        // 5.2.7 dynamic_cast
        // dynamic_cast<T>(v)
        // case 1: T is a pointer to defined class
        // 1a. if v is ptr to class for which T is accessible base class,
        //     result is ptr to unique subobject (unambiguous)
        // 1b. otherwise, v must be ptr or ref to polymorphic type,
        //     runtime check is applied
        // case 2: T is a pointer to void
        // 1. ensure v is a ptr
        // 2. ensure v is ptr to a complete object
        // 3. result value is pointer to object pointed to by v
        // case 3: T is a reference to defined class
        // 1a. if v is an object of type for which T is accessible base class,
        //     result is ref to unique subobject (unambiguous) (lvalue)
        //     - ?? fetch the lvalue ? what if it's left side?
        // 1b. otherwise, v must be ptr or ref to polymorphic type,
        //     runtime check is applied
        eOperands = new OperandTable ();
        tOperands = new OperandTable ();
        eOperands.put (TyPointer.class, rn_unimplemented);
        eOperands.put (TyRef.class, rn_unimplemented);
        tOperands.put (TyPointer.class, eOperands);
        eOperands = new OperandTable ();
        eOperands.put (TyClass.class, rn_unimplemented);
        eOperands.put (TyPointer.class, rn_unimplemented);
        eOperands.put (TyRef.class, rn_unimplemented);
        tOperands.put (TyRef.class, eOperands);
        rulesets.put (OpTable.get (DYNAMIC_CAST), tOperands);


        // 5.2.11 const_cast
        // const_cast<T>(v)
        // case 1: T is a pointer
        // 1. ensure v is a pointer
        // 2. ensure T and v point to objects of the same type
        // 3. result is pointer v with cv-qualifications from T
        // case 2: T is a reference
        // 1. ensure v is a reference or object
        // 2. ensure T and v refer to objects of the same type
        // 3. result is pointer v with cv-qualifications from T
        CodeGenRule rn_equivalent =
            new CGRAssertion ((CGRTest) rb.get (T_EQUIVALENT_TYPES));

        CodeGenRule rs_ref_class_const_cast =
            new CGRSequentialNode
            (new CGRConstCast (),
             new CGROperandBranchNode
             (rn_equivalent, new CodeGenRule []
                 {rb.get (R_NONE), rb.get (R_CONVERT_TO_REFERENCE)}));

        CodeGenRule rs_const_cast =
            new CGRSequentialNode
            (new CGRConstCast (), rn_equivalent);

        eOperands = new OperandTable ();
        tOperands = new OperandTable ();
        eOperands.put (TyPointer.class, rs_const_cast);
        tOperands.put (TyPointer.class, eOperands);
        eOperands = new OperandTable ();
        eOperands.put (TyClass.class, rs_ref_class_const_cast);
        eOperands.put (TyRef.class, rs_const_cast);
        tOperands.put (TyRef.class, eOperands);
        rulesets.put (OpTable.get (CONST_CAST), tOperands);


        // 5.2.9 static_cast
        // static_cast<T>(v)
        // simple interpretation (ARM):
        // - ensure that there is an (unambiguous) implicit conversion
        //   from T to type of v or vice-versa
        // updated interpretation (ISO):
        // in all cases:
        // - result is lvalue iff T is a reference
        // - cv-qualification is untouched
        // if the cast can be represented as T t(v) (for some temp var t,
        // used as the result of the expression):
        //  - v is used as an lvalue iff initialization uses it in that way
        //  - effect of cast is the same as an expression of this kind (
        //    subject to rules about lvalue-ness and const-ness)
        // otherwise, follow these rules:
        // - v can be converted to void, value of v is discarded.
        // - an lvalue of type cv1 B can be cast to ref to cv2 D, when all of
        //   the following hold:
        //   - D is derived from B
        //   - a valid standard conversion from ptr to D to ptr to B exists
        //   - cv2 >= cv1
        //   - B is not a virtual base class
        //   the result is an lvalue of type cv2 D. If v wasn't in fact a D,
        //   the result is undefined.
        // - inverse of standard conversions other than array-to-pointer,
        //   function-to-pointer, lvalue-to-rvalue, and boolean conversions,
        //   can be performed using static_cast, subject to the following
        //   rules for specific cases:
        //   - integral --> enumeration is ok, unspecified if int value out of
        //     range
        //   - ptr to cv1 B --> ptr to cv2 D, as per rules for ref/object
        //     above
        //   - ptr to member of D of type cv1 T --> ptr to member of B of
        //     type cv2 T, where:
        //     - B a base class of D
        //     - valid standard ptr to member conversions exist
        //     - cv2 >= cv1
        //   - ptr to cv void --> ptr to object type.
        //     - conversion to void ptr and back will yield original object
        //       ptr value

        eOperands = new OperandTable ();
        tOperands = new OperandTable ();

        // cast to void
        // any type --> void
        CodeGenRule rs_static_cast_to_void = new CGRUnimplemented
            ("static_cast conversion to void : static_cast<void>(e)");
        eOperands.put (TypeNode.class, rs_static_cast_to_void);
        tOperands.put (TyVoid.class, eOperands);

        // cast to class
        CodeGenRule rs_constructor_call = rb.get (R_CONSTRUCTOR_CALL);

        rs_static_cast_to_class =
            new CGRSequentialNode
                (rb.get (R_MAKE_TEMPORARY), rs_constructor_call);
        // any type --> class (find suitable constructor)
        eOperands = new OperandTable ();
        eOperands.put (TypeNode.class, rs_static_cast_to_class);
        tOperands.put (TyClass.class, eOperands);

        // cast to reference
        CodeGenRule rs_user_defined_conversion =
            rb.get (R_IMPLICIT_CONVERSION);

        StandardConversions sc = StandardConversions.getInstance ();
        CodeGenRule rs_standard_conversion = new CGRTypeConversion (sc, 1, 0, true, true);
        CodeGenRule rs_standard_static_cast =
            new CGRStandardConversionStaticCast ();
        CodeGenRule r_down_static_cast =
            new CGRDownConversionStaticCast ();

        CodeGenRule rs_static_cast_to_fundamental =
            new CGRConditionalNode
                ((CGRTest)rb.get (T_UT_CLASS_1),
                 rs_user_defined_conversion,
                 rs_standard_static_cast);
        rb.put (R_CAST_TO_FUNDAMENTAL, rs_static_cast_to_fundamental);

        CodeGenRule rs_static_cast_to_class_rp =
            new CGRSequentialNode
                (new CGRConditionalNode
                    ((CGRTest) rb.get (T_NULLEXP),
                     r_down_static_cast,
                     rb.get (R_NONE)),
                 rs_static_cast_to_class);

        CodeGenRule rn_explicit_ref_or_ptr =
            new CGRConditionalNode
                ((CGRTest)rb.get (T_UT_CLASS_0),
                 rs_static_cast_to_class_rp,
                 rs_static_cast_to_fundamental);

        CodeGenRule rs_static_cast_to_ref =
            new CGRSequentialNode
                (rb.get (R_CONVERT_TO_REFERENCE),
                 rn_explicit_ref_or_ptr);

        // class --> ref
        // fundamental --> ref (not arrays, pointers)
        // remember that reference lvalue expressions are 'prefetched'
        eOperands = new OperandTable ();
        eOperands.put (TyClass.class, rs_static_cast_to_ref);
        eOperands.put (TyFundamental.class, rs_static_cast_to_ref);
        tOperands.put (TyRef.class, eOperands);

        // cast to fundamental type
        // class --> fundamental (user-defined conversion)
        // fundamental --> fundamental
        // pointer --> fundamental  (TSN 2002 Oct 14)
        // array --> fundamental (TSN 2002 Oct 14)
        eOperands = new OperandTable ();
        eOperands.put (TyClass.class, rs_user_defined_conversion);
        eOperands.put (TyFundamental.class, rs_standard_static_cast);
        eOperands.put (TyPointer.class, rs_standard_static_cast); // TSN 2002 Oct 14
        eOperands.put (TyArray.class, rs_standard_static_cast); // TSN 2002 Oct 14
        tOperands.put (TyFundamental.class, eOperands);

        // cast to pointer
        CodeGenRule rs_static_cast_to_pointer =
            new CGRSequentialNode
                (rb.get (R_ADDRESS_OF),
                 rn_explicit_ref_or_ptr);

        // class --> pointer (user-defined conversion)
        // pointer --> pointer
        // array --> pointer (standard conversion)
        eOperands = new OperandTable ();
        eOperands.put (TyClass.class, rs_user_defined_conversion);
        eOperands.put (TyPointer.class, rs_static_cast_to_pointer);
        eOperands.put (TyArray.class, rs_standard_conversion);
        tOperands.put (TyPointer.class, eOperands);

        rulesets.put (OpTable.get (STATIC_CAST), tOperands);


        // 5.2.10 reinterpret_cast
        // reinterpret_cast<T>(v)
        // - casts e to T provided (T)e is allowed.
        // - treats ptr and ref types as if they were incomplete (ignored
        //   class hierarchy)
        // - ptr --> integral and integral --> ptr
        // - ptr to fn of type T1 --> ptr to fn of type T2
        // - ptr to T1 --> ptr to T2
        // - ptr to mem of X of type T1 --> ptr to mem of Y of type T2 iff
        //   T1,T2 are either both object types or both function types
        // - obj or ref of type T1 --> ref of type T2 if a reinterpret cast
        //   can be performed for ptr to T1 --> ptr to T2. No temporary is
        //   created, no copy made, no call to constructor or conversion fns
        CodeGenRule rs_reinterpret_cast =
            new CGRUnimplemented ("reinterpret cast expressions");
        eOperands = new OperandTable ();
        eOperands.put (TypeNode.class, rs_reinterpret_cast);
        tOperands.put (TyPointer.class, eOperands);
        rulesets.put (OpTable.get (REINTERPRET_CAST), tOperands);

    }


    /**
     * Applies a standard conversion or reverse standard conversion
     * according to the procedure defined for static_cast (ISO 5.2.9 -1,6)
     */
    public class CGRStandardConversionStaticCast extends CodeGenRule {
        public final String NO_STANDARD_CONVERSION =
            "No standard conversion found between {0} and {1} that is applicable to static_cast";

        private StandardConversions sc = StandardConversions.getInstance ();

        /**
         * Applies a standard conversion or reverse standard conversion
         * according to the procedure defined for static_cast (ISO 5.2.9 -1,6)
         */
        public void apply (ExpressionPtr exp) {

            ExpressionNode fromExp = exp.get (1);
            ExpressionNode conversion = null;

            TypeNode toType = exp.get_type (0);
            TypeNode fromType = fromExp.get_type() ;
            // we don't care about ref
            if (fromType instanceof TyRef) fromType = ((TyRef) fromType).getPointeeType ();
            if (toType instanceof TyRef) toType = ((TyRef) toType).getPointeeType ();

            d.msg (Debug.COMPILE, "converting " + fromType.getTypeString () + " to "
                    + toType.getTypeString ());

            // regular standard conversion ?
            boolean[] uninteresting = new boolean[1] ;
            if ( sc.determine (fromType, toType, uninteresting) != sc.NONE) {
                conversion = sc.makeConversionExpression
                    (fromExp, toType, exp.op().getName ());
            } else {
                // reverse standard conversion ?
                int backward = sc.determine (toType,  fromType, uninteresting);
                switch (backward) {
                case StandardConversions.ARRAY_TO_POINTER:
                case StandardConversions.LVAL_TO_RVAL:
                case StandardConversions.QUALIFICATION:
                case StandardConversions.FUNCTION_TO_POINTER:
                case StandardConversions.BOOL_CONVERSION:
                case StandardConversions.PTR_TO_BOOL:
                case StandardConversions.NONE:
                    // no conversion, error
                    Assert.apology (NO_STANDARD_CONVERSION, new String []
                        {fromType.typeId (), toType.typeId ()});
                    break;
                default:
                    // apply the reverse conversion - always an arithmetic
                    // conversion
                    conversion = new OpArithmeticConversion
                        (toType, exp.op().getName (), fromExp);
                    break;
                }
            }
            exp.set (conversion);

        }
    }

    /**
     * Applies a down cast conversion according to the procedure defined for
     * static_cast (ISO 5.2.9, both ref/lvalue and pointer contexts)
     */
    public class CGRDownConversionStaticCast extends CodeGenRule {
        public final String NO_DOWN_CONVERSION =
            "No down conversion possible between {0} and {1}";
        public final String DOWN_CAST_QUALIFIER_ERROR =
            "Cannot perform down conversion from more cv-qualified {1} to {0}";

        private StandardConversions sc = StandardConversions.getInstance ();

        /**
         * Applies a down cast conversion according to the procedure defined for
         * static_cast (ISO 5.2.9, both ref/lvalue and pointer contexts)
         */
        public void apply (ExpressionPtr exp) {
            TypeNode convertTo = exp.get_type (0);
            ExpressionNode toConvert = exp.get (1);
            ExpressionNode conversion = null;

            // 1. v is not virtual base class type
            // ** virtual class not supported

            // 2. cv T >= cv v
            int ranking =
                sc.rankQualifiers (convertTo.getAttributes (),
                                   toConvert.get_type().getAttributes ());
            if (ranking < 0) {
                Assert.apology (DOWN_CAST_QUALIFIER_ERROR, new String []
                    {toConvert.get_type().typeId (), convertTo.typeId ()});
            }

            // 3. ensure relationship exists
            StandardConversions.ClassConversionRelation crel =
                sc.getClassConversionRelation
                ((TyClass)toConvert.get_type (),
                 (TyClass)convertTo);
            if (crel.conversionType != sc.DOWN_CONVERSION) {
                Assert.apology (NO_DOWN_CONVERSION, new String []
                    {toConvert.get_type().typeId (), convertTo.typeId ()});
            }

            // 4. build down cast expression
            conversion = new OpDownConversion (convertTo,
                                                 exp.op().getName (),
                                                 crel.getPath (), toConvert);
            exp.set (conversion);
        }
    }

}


