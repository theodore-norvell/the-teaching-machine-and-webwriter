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

import java.util.Vector;

import tm.clc.analysis.CGRAssignment;
import tm.clc.analysis.CGRConditionalNode;
import tm.clc.analysis.CGRError;
import tm.clc.analysis.CGROperandNode;
import tm.clc.analysis.CGRSequentialNode;
import tm.clc.analysis.CGRTest;
import tm.clc.analysis.CGRUnimplemented;
import tm.clc.analysis.CTSymbolTable;
import tm.clc.analysis.CodeGenRule;
import tm.clc.analysis.ExpressionPtr;
import tm.clc.analysis.RuleBase;
import tm.clc.analysis.ScopedName;
import tm.clc.ast.ConstInt;
import tm.clc.ast.ExpSequence;
import tm.clc.ast.ExpThis;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.NodeList;
import tm.clc.ast.OpArraySubscript;
import tm.clc.ast.OpAssign;
import tm.clc.ast.TyAbstractClass;
import tm.clc.ast.TyAbstractClassDeclared;
import tm.clc.ast.TypeNode;
import tm.cpp.ast.ConstStr;
import tm.cpp.ast.ExpFunctionName;
import tm.cpp.ast.TyArray;
import tm.cpp.ast.TyChar;
import tm.cpp.ast.TyClass;
import tm.cpp.ast.TyFundamental;
import tm.cpp.ast.TyPointer;
import tm.cpp.ast.TyRef;
import tm.cpp.datum.ArrayDatum;
import tm.cpp.datum.CharDatum;
import tm.utilities.Assert;
import tm.utilities.Debug;

/**
 * <code>ExpressionBuilder</code> responsible for initialization and destruction in C++,
 * covering:
 * <ul>
 * <li>function call (direct initialization)</li>
 * <li>assignment (copy initialization)</li>
 * <li>default initialization</li>
 * <li>destruction</li>
 * </ul>
 * <p>Initialization is treated seperately from other instances of assignment
 *    and function call primarily due to the way they are presented to the
 *    analysis package by the parser. It also permits seperation and
 *    concentration of those rules that are peculiar to the initialization
 *    situation.
 * <ul>Notes:
 * <li>aggregate initialization (as a special case of assignment) is not
 *     currently implemented</li>
 * <li>initialization of char arrays and char pointers with string literals
 *     is implemented as a special case</li>
 * <li>initialization of dynamic variables is handled via <code>Eb_Unary</code>
 *     (via the rules for the <code>new</code> operator); rules are shared.</li>
 * </ul>
 */
public class Eb_Initialization extends CppExpressionBuilder {

    public static final String AGGREGATE_REQUIRED =
        "An array or POD class type is required, have {0}";

    public static final String UNINITIALIZED_REFERENCE =
        "cannot leave references uninitialized";


    /** placeholder operator id for no initializer */
    public static final ScopedName NO_INIT_SN =
        new Cpp_ScopedName ("no initializer");

    /** placeholder operator id for <em>zero-initialization</em>
     * <ul>meaning :
     * <li>set storage to converted 0 (scalar)</li>
     * <li>each non-static data member and base-class subobject is
     *     zero-initialized (non-union class)</li>
     * <li>storage for first data member is zero-initialized (union)</li>
     * <li>storage for each element is zero-initialized (array)</li>
     * </ul>
     * <br>note that no zero-initialization is performed for references
     */
    public static final ScopedName ZERO_INIT_SN =
        new Cpp_ScopedName ("zero initialization");

    /** placeholder operator id for <em>default-initialization</em>
     * <ul>meaning :
     * <li>call of default constructor (non-POD class types)</li>
     * <li>default-initialization of elements (arrays)</li>
     * <li>zero initialization (otherwise)</li>
     * </ul>
     * <br>Note that references cannot be default initialized.
     */
    public static final ScopedName DEFAULT_INIT_SN =
        new Cpp_ScopedName ("default initialization");

    /** placeholder operator id for <em>copy-initialization</em>
     * <ul>equivalent to the form <code>T x = a;</code>, occurs in :
     * <li>argument passing and function return</li>
     * <li>throwing/handling an exception</li>
     * <li>aggregate initialization (brace enclosed lists)</li>
     * </ul>
     */
    public static final ScopedName COPY_INIT_SN =
        new Cpp_ScopedName ("copy initialization");

    /** placeholder operator id for <em>direct-initialization</em>
     * <ul>equivalent to the form <code>T x(a);</code>, occurs in :
     * <li><code>new</code> expressions</li>
     * <li><code>static_cast</code> expressions</li>
     * <li>functional notation type conversions</li>
     * <li>member initializers</li>
     * </ul>
     */
    public static final ScopedName DIRECT_INIT_SN =
        new Cpp_ScopedName ("direct initialization");

    /** placeholder operator id for <quot>aggregate-initialization</quot>,
     *  or the initialization of arrays and aggregate class objects using
     *  <em>brace-enclosed lists</em>.
     */
    public static final ScopedName AGGREGATE_INIT_SN =
        new Cpp_ScopedName ("aggregate initialization");

    /** placeholder operator id for <quot>destruction</quot>,
     */
    public static final ScopedName DESTRUCTION_SN =
        new Cpp_ScopedName ("destruction");

    /* rule sequence for initialization of subobjects (base class objects)*/
    private CodeGenRule rs_init_subobject;

    /**
     * Creates a new <code>Eb_Initialization</code> instance.
     *
     * @param ruleBase a reference to the set of all <code>CodeGenRules</code>
     */
    public Eb_Initialization (RuleBase ruleBase, CTSymbolTable symbolTable) {
        super (ruleBase, symbolTable, "initialization");
    }

    public ExpressionNode make_init_subobject_exp
        (TypeNode base_t, TypeNode class_t, Initializer init) {

        Vector args = new Vector ();
        args.addElement (base_t);
        args.addElement (class_t);

        if (init != null) {
            NodeList args_nl = init.getArgs ();
            for (int i = 0; i < args_nl.length (); i++) {
                args.addElement (args_nl.get (i));
            }
        }
        Object [] args_array = new Object [args.size ()];
        args.copyInto (args_array);
        ExpressionPtr exp =
            new ExpressionPtr (new Cpp_ScopedName (), args_array);
        rs_init_subobject.apply (exp);
        return exp.get ();
    }

    /** build rulesets, add to rulebase and operand tables */
    protected void buildTables () {

        OperandTable loperands, roperands;

        CodeGenRule r_init = this;

        // rule sequence to call subobject constructor
        rs_init_subobject =
            new CGRSequentialNode
                (rb.get (R_FUNCTION_CALL),
                 new CGRSequentialNode
                     (rb.get (R_RESOLVE_FN_ID),
                      new CGRSubobjectName ()));
        rb.put (R_INITIALIZE_SUBOBJECT, rs_init_subobject);

        // reference initialization

        CodeGenRule rs_reference_init =
            new CGRSequentialNode
                (new CGRAssignment (),
                 rb.get( R_CONVERT_REFERENCE_INITIALIZER ) );

        // zero initialization

        CodeGenRule rs_zero_scalar =
            new CGRSequentialNode
                (rb.get (R_ASSIGNMENT_INITIALIZATION),
                 new CGRNullConstant ());

        CodeGenRule rs_zero_array =
            new CGRConditionalNode
                (new CGREmptyArrayTest (0),
                 rb.get (R_NONE),
                 new CGRSequentialNode
                     (new CGROperandNode
                         (new CGRCommaCombine (), r_init),
                      new CGRElementwise ()));

        // luck plays a big role in this implementation -- assumes:
        // - class is at operand[0]
        // - no other operands, generating a default constructor call
        // - default constructor for POD class is generated
        // !! need to validate that this works.
        CodeGenRule rs_zero_classobj = rb.get (R_CONSTRUCTOR_CALL);
        /*		CodeGenRule rs_zero_classobj =
            new CGRSequentialNode
                (new CGROperandNode
                    (new CGRCommaCombine (), r_init),
                 new CGRElementwise ());
        */

        loperands = new OperandTable ();
        loperands.put (TyFundamental.class, rs_zero_scalar);
        loperands.put (TyPointer.class, rs_zero_scalar);
        loperands.put (TyArray.class, rs_zero_array);
        loperands.put (TyClass.class, rs_zero_classobj);
        rulesets.put (ZERO_INIT_SN.getName (), loperands);

        // default initialization
        // interestingly, default initialization uses identical rule sequences
        rulesets.put (DEFAULT_INIT_SN.getName (), loperands);


        // aggregate initialization
        // ** initialization of aggregate class objects with brace-enclosed
        //    lists is not currently supported
        CodeGenRule rs_aggregate_classobj =
            new CGRUnimplemented ("initialization of aggregate class " +
                                   "objects with brace-enclosed lists");

        // ** initialization of arrays with brace-enclosed lists not
        //    currently supported
        CodeGenRule rs_aggregate_array =
            new CGRUnimplemented ("initialization of arrays with " +
                                   "brace-enclosed lists");

        loperands = new OperandTable ();
        loperands.put (TyArray.class, rs_aggregate_array);
        loperands.put (TyClass.class, rs_aggregate_classobj);
        rulesets.put (AGGREGATE_INIT_SN.getName (), loperands);


        // direct initialization

        // into scalar
        // !! conversion sequences are not pertinent here in general.
        CodeGenRule rs_direct_scalar = rb.get (R_ASSIGNMENT_INITIALIZATION);
        // into reference
        //CodeGenRule rs_direct_ref =
        //	new CGRUnimplemented ("direct initialize references");

        // into class object

        CodeGenRule rs_direct_classobj = rb.get (R_CONSTRUCTOR_CALL);


        loperands = new OperandTable ();
        roperands = new OperandTable ();
        roperands.put (TypeNode.class, rs_direct_scalar);			       loperands.put (TyFundamental.class, roperands);
        loperands.put (TyPointer.class, roperands);
        roperands = new OperandTable ();
        roperands.put (TypeNode.class, rs_direct_classobj);
        loperands.put (TyClass.class, roperands);
        // loperands.put (TyRef.class, rs_direct_ref); -- all reference inits go through copy
        // ruleset
        rulesets.put (DIRECT_INIT_SN.getName (), loperands);

        // copy initialization
        // same as direct for scalar vars
        CodeGenRule rs_copy_scalar = rs_direct_scalar;

        // into class, 8.5-14
        CGRTest t_derives = new CGRDerivesTest ();
        CodeGenRule rs_copy_classobj =
            new CGRConditionalNode
            (t_derives,
             rs_direct_classobj,
             new CGRSequentialNode
             (rs_direct_classobj,
              rb.get (R_IMPLICIT_CONVERSION)));

        // into reference
        CodeGenRule rs_copy_ref = rs_reference_init;
        //			new CGRUnimplemented ("copy initialize references");

        // into array -- aggregate initialization supported elsewhere, just
        // string literals into char arrays here
        CodeGenRule rs_copy_array = new CGRStringLiteralInit ();

        loperands = new OperandTable ();
        roperands = new OperandTable ();
        roperands.put (TypeNode.class, rs_copy_scalar);
        loperands.put (TyFundamental.class, roperands);
        loperands.put (TyPointer.class, roperands);
        roperands = new OperandTable ();
        roperands.put (TypeNode.class, rs_copy_array);
        loperands.put (TyArray.class, roperands);
        roperands = new OperandTable ();
        roperands.put (TypeNode.class, rs_copy_classobj);
        loperands.put (TyClass.class, roperands);
        roperands = new OperandTable ();
        roperands.put (TypeNode.class, rs_copy_ref);
        loperands.put (TyRef.class, roperands);
        rulesets.put (COPY_INIT_SN.getName (), loperands);

        // no initialization
        // .. represent ... ?
        CodeGenRule r_ref_no_init =
            new CGRError (UNINITIALIZED_REFERENCE);
        loperands = new OperandTable ();
        loperands.put (TyRef.class, r_ref_no_init);
        loperands.put (TypeNode.class, rb.get (R_NONE));
        rulesets.put (NO_INIT_SN.getName (), loperands);

        // destruction

        CodeGenRule rs_destruct_array =
            new CGRConditionalNode
                (new CGREmptyArrayTest (0),
                 rb.get (R_NONE),
                 new CGRSequentialNode
                     (new CGROperandNode
                         (new CGRCommaCombine (), r_init),
                      new CGRElementwise ()));

        // - class is at operand[0]
        // - default destructor for POD class is generated
        CodeGenRule rs_destruct_classobj = rb.get (R_DESTRUCTOR_CALL);

        loperands = new OperandTable ();
        loperands.put (TyArray.class, rs_destruct_array);
        loperands.put (TyClass.class, rs_destruct_classobj);
        rulesets.put (DESTRUCTION_SN.getName (), loperands);

        // 8.5 No initializer
        // if dest is a reference
        //     1. error - need initializer
        // if dest type is a class
        //   if non-POD
        //     1. if const, make sure default constructor is user-defined
        //     2. default-initialize (call default constructor)
        //   if POD
        //     1. if const, or contains const elements, error
        //     2. leave uninitialized
        // else (non-class, non-ref)
        //     1. if const or components are const (non-class - say array
        //        element and pointer type case?), error - need initializer
        //     2. leave uninitialized






        // 8.5.2 destination type array of char, initializer a String literal
        // - if size of array is specified, ensure string literal will fit
        // - copy each character in sequence
        // - add a trailing '\0'

        // 8.5.1 destination type an array (previous case excepted)
        // - aggregate initialization not supported - case covered

        // 8.5-14 destination type a class type - all cases covered
        // - aggregate initialization not supported (8.5.1) - case covered
        // - direct-initialization or copy-initialization with source type
        //   same class or derived from destination type
        //   1. consider constructors
        //   2. resolve to best match or ambiguous
        //   3. (if match found) use constructor
        // - copy-initialization (remaining cases)
        //   1. consider user-defined conversion sequences that can convert
        //      source type to destination type or derived class thereof
        //   2. resolve to best match or ambiguous
        //   3. (if match found) use conversion function:
        //      - if a constructor, initialize a temporary
        //      - use the result of the function to direct-initialize according
        //        to above rules.


        // 8.5-14 source type a class type (remaining cases) - cases covered
        // 1. conversion functions are considered
        // 2. resolve to best match or ambiguous
        // 3. (if match found) use conversion function

        // 8.5-14 remaining cases - cases covered.
        //   1. if source/dest types are different (disregarding cvq)
        //      use standard conversions to convert initializer to
        //      cv-unqualified version of destination type.
        //   2. copy-initialize (standard assignment for these types)
        //

    }

    /**
     * Creates a new <code>ExpFunctionName</code> for a subobject
     * constructor call.
     */
    public class CGRSubobjectName extends CodeGenRule {
        /**
         * Builds an <code>ExpFunctionName</code> consisting of the
         * name of the class (a constructor call). Removes the class
         * type from the list of args once complete. The class object will
         * be represented by an <code>ExpThis</code>, in contrast with
         * external constructor calls.
         */
        public void apply (ExpressionPtr exp) {

            TypeNode t;
            ExpressionNode c_obj = null;
            // first operand is subobject type
            t = exp.get_base_type (0);
            // second operand is class type
            TypeNode c_type = exp.get_base_type (1);

            // remove exp 0,1 (class types - just want constructor args now)
            exp.removeOperand (0);
            exp.removeOperand (0);

            Assert.check (t instanceof TyAbstractClassDeclared);
            ScopedName fqn = ((TyAbstractClassDeclared) t).getFullyQualifiedName ();
            ScopedName id = new Cpp_ScopedName (fqn.getTerminalId ());

            ExpFunctionName efn = new ExpFunctionName (id);

            // class scope (constructor lookup) is scope of subobject
            ClassSH c_scope =
                (ClassSH) (symbolTable.getClassDeclaration
                            ((TyAbstractClass) t).getDefinition ());
            efn.setClassScope (c_scope);

            // class object is this pointer
            TyPointer c_type_ptr = new TyPointer ();
            c_type_ptr.addToEnd (c_type);
            ExpressionNode c_object = new ExpThis (c_type_ptr, "");
            c_object.setUninteresting (true);
            efn.setClassObject (c_object);

            exp.set (efn);
        }
    }



    /**
     * Creates a zero literal, the NULL constant, what have you
     */
    public class CGRNullConstant extends CodeGenRule
        implements FundamentalTypeUser {

        /**
         * Creates the zero literal, adds to end of operands
         */
        public void apply (ExpressionPtr exp) {
            exp.addOperand (new ConstInt (ctyInt, "0", 0));
        }
    }

    /** tests for empty array */
    public class CGREmptyArrayTest extends CGRTest {
        public CGREmptyArrayTest (int pos) { super (pos); }
        public boolean applies (ExpressionPtr exp) {
            boolean result;
            TypeNode et = exp.get_base_type (start);
            d.msg (Debug.COMPILE, ">>empty array test : " + et.getTypeString ());
            result = ((et instanceof TyArray) &&
                      (((TyArray) et).getNumberOfElements () <= 0));
            return result;
        }
    }

    /**
     * Takes an expression with an aggregate type (array, POD class)
     * and makes each element an operand for further work
     */
    public class CGRElementwise extends CodeGenRule
        implements FundamentalTypeUser {

        /**
         * Takes an expression with aggregate type and makes each
         * element an operand for further work
         */
        public void apply (ExpressionPtr exp) {
            d.msg (Debug.COMPILE, ">>elementwise");
            TypeNode type = exp.get_base_type (0);
            // currently just checking for array - aggregate class to come
            if (! (type instanceof TyArray))
                Assert.apology (AGGREGATE_REQUIRED,
                                type.getTypeString ());

            TyArray array = (TyArray) type;
            TypeNode elementType = array.getElementType ();
            ExpressionNode arrayExp = exp.get (0);
            for (int i = 0; i < array.getNumberOfElements (); i++) {
                // !! get rid of direct use of square brackets
                ExpressionNode e = new OpArraySubscript
                    (new TyRef (elementType), "[", "]", arrayExp,
                     new ConstInt (ctyInt, Integer.toString (i), i));
                exp.set (e, i);
            }
        }
    }


    /**
     * Combines operand expressions into one long sequence of
     * expressions via the comma operator
     */
    public class CGRCommaCombine extends CodeGenRule
        implements FundamentalTypeUser {

        /**
         * Combines operand expressions into one long sequence of
         * expressions via the comma operator
         */
        public void apply (ExpressionPtr exp) {
            d.msg (Debug.COMPILE, ">>comma combine");
            ExpressionNode combined = null;
            int operandCount = exp.operandCount ();
            switch (operandCount) {

            case 0: // null expression
                break;

            case 1: // expression is at operand[0]
                combined = exp.get (0);
                break;

            default: // comma combine
                combined = exp.get (operandCount - 1);
                for (int i = operandCount - 2; i >= 0; i--) {
                    ExpressionNode loperand = exp.get (i);
                    combined = new ExpSequence (combined.get_type (),
                                                 OpTable.get (COMMA),
                                                 loperand, combined);
                }
            }

            exp.set (combined);
        }
    }

    /**
     * Initialization of char arrays with string literals
     */
    public class CGRStringLiteralInit extends CodeGenRule
        implements FundamentalTypeUser {
        final String INIT_DESC =
            "string literal initialization of char arrays (aggregate " +
            "initialization of arrays is not supported)";
        final String LITERAL_TOO_LARGE =
            "string literal larger than char array being initialized";
        /**
         * Initialization of char array (operand 0) with string literal
         * (operand 1)
         */
        public void apply (ExpressionPtr exp) {
            d.msg (Debug.COMPILE, ">>empty array test");
            // is operand 0 a char array ?
            TypeNode t0 = exp.get_base_type (0);
            if ((!(t0 instanceof TyArray)) ||
                (! (((TyArray) t0).getElementType () instanceof TyChar))) {
                Assert.apology (INVALID_OPERAND, new String []
                    {t0.getTypeString (), INIT_DESC});
            }
            TyArray ta = (TyArray) t0;
            TyChar tc = (TyChar) ta.getElementType ();
            TyRef tyCharRef = new TyRef( tc ) ;
            ExpressionNode arrayExp = exp.get (0);

            // is operand 1 a string literal ?
            ExpressionNode e1 = exp.get (1);
            if (! (e1 instanceof ConstStr)) {
                Assert.apology (INVALID_OPERAND,
                                new String [] {e1.name (), INIT_DESC});
            }
            ConstStr cs = (ConstStr) e1;
            int cs_length = cs.length () ;

            // has the size of the char array been specified ?
            int elcount = ta.getNumberOfElements ();
            if (elcount >= 0) {
                // will the string literal fit ?
                if (elcount < cs_length) {
                    Assert.apology (LITERAL_TOO_LARGE);
                }
            } else {
                // set the size of the array accordingly
                ta.setNumberOfElements (cs_length);
            }

            ArrayDatum cs_value = cs.getValue();
            // for each character in the string literal
            //   initialize the corresponding array element (assignment)
            //   comma-combine this expression with what's been done so far
            ExpressionNode initExp = null;
            for (int i = elcount - 1; i >= 0; i--) {
                // !! get rid of direct use of square brackets
                ExpressionNode aSub = new OpArraySubscript
                    (tyCharRef, "[", "]", arrayExp,
                     Literals.make_int_const (Integer.toString (i), 10));

                long charByteVal;
                String charImage ;
                if( i < cs_length ) {
                    charByteVal = ((CharDatum) cs_value.getElement(i)).getValue() ;
                    charImage = "'"+cs.getImageOfCharacter(i)+"'" ; }
                else {
                    charByteVal = 0 ;
                    charImage = "\'\\0\'" ; }

                ExpressionNode charVal = new ConstInt( ctyChar, charImage, charByteVal );

                ExpressionNode elAssign =
                    new OpAssign (aSub.get_type (), "=", aSub, charVal);

                initExp = (initExp == null)
                    ? elAssign
                    : new ExpSequence (initExp.get_type (),
                                        OpTable.get (COMMA), elAssign,
                                        initExp);
            }

            exp.set (initExp);
        }

    }

}


