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

import tm.clc.analysis.CGRConditionalNode;
import tm.clc.analysis.CGRError;
import tm.clc.analysis.CGRFetch;
import tm.clc.analysis.CGRIncrement;
import tm.clc.analysis.CGROperandNode;
import tm.clc.analysis.CGRReferenceTest;
import tm.clc.analysis.CGRSequentialNode;
import tm.clc.analysis.CGRTest;
import tm.clc.analysis.CGRUnimplemented;
import tm.clc.analysis.CTSymbolTable;
import tm.clc.analysis.CodeGenRule;
import tm.clc.analysis.ExpressionPtr;
import tm.clc.analysis.RuleBase;
import tm.clc.ast.TypeNode;
import tm.cpp.ast.ExpFunctionName;
import tm.cpp.ast.TyArithmetic;
import tm.cpp.ast.TyBool;
import tm.cpp.ast.TyClass;
import tm.cpp.ast.TyPointer;

/**
 * <code>ExpressionBuilder</code> responsible for postfix expressions in C++:
 * <ul>
 * <li>function call</li>
 * <li>pseudo destructor call</li>
 * <li>class member access</li>
 * <li>increment and decrement</li>
 * <li>type identification</li>
 * </ul>
 * subscripting is treated as a binary operation, and handled in 
 * <code>Eb_Operator</code>. Casts are handled in <code>Eb_Cast</code>.
 */
public class Eb_Postfix extends CppExpressionBuilder {

    public static final String RS_FUNCTION_CALL = 
        "complete rule sequence for function/method call expressions";
    //public static final String RS_MEMBER_EXPRESSION =  // NEVER USED!
    //    "rule sequence for member access expressions (dot notation)";
    //public static final String RS_ARROW_EXPRESSION = // NEVER USED!
    //    "rule sequence for member access expressions (arrow notation)";

    /**
     * Creates a new <code>Eb_Postfix</code> instance.
     *
     * @param ruleBase a reference to the set of all <code>CodeGenRules</code>
     */
    public Eb_Postfix (RuleBase ruleBase, CTSymbolTable symbolTable) { 
        super (ruleBase, symbolTable, "postfix");
    }

    
    protected void buildTables () { 


        OperandTable operands;

        // ** 5.2 : postfix expressions


        // 5.2.2 : function call
        // Details from spec:
        // 5.2.2-1: 
        // - distinguish between ordinary function call and member function 
        //   call
        // - ordinary should be lvalue referring to fn, or have ptr to fn type
        // - ?? language linkage differences: undefined if fn type has lang
        //      linkage different from expression calling it (7.5)
        // - member should be implicit or explicit member access to mem fn, or
        //   ptr to member expression selecting a mem fn. 
        // - ** assuming here that runtime var and/or id of fn will be enough 
        //      to apply member fn in all cases (including implicit
        //      member access), i.e. that we don't need to include an access
        //      to the relevant object (e.g. (*this))
        // - overload resolution needs to take place to get the right fn
        // 5.2.2-2
        // - id resolution will identify when no matching fn is visible 
        // 5.2.2-3
        // - type of expression is type of matching function
        // - the type should be complete object type, reference type or void
        // 5.2.2-4
        // - ** assuming that parameter initialization will take place in the 
        //      AST representation of the call. 
        // - parameters that have object type should be completely defined type
        // - ** assuming that parameter destruction will take place in the 
        //      AST representation of the call.
        // - ** assuming that return type conversion will take place in the 
        //      AST representation of the call.
        // 5.2.2-5
        // - ** modification of parameters taken care of inside function (not
        //   our concern)
        // 5.2.2-6
        // - identification of matching function taken care of by overload
        //   resolution and id lookup. 
        // - ** default parameter values taken care of by AST representation
        // 5.2.2-7
        // - ** extra arguments (ellipsis fn call) currently not supported
        // 5.2.2-8
        // - ** taken care of by AST execution
        // 5.2.2-9
        // - ** not important to expression building
        // 5.2.2-10
        // - ** not important to expression building
        // ADDITIONAL RULES
        // - conversion sequences are built for each argument that requires
        //   one before being passed to the AST representation of the fn call.
        //   The conversion sequence is made available via overload resolution.
        // - operands are fetched where appropriate - ? is this before 
        //   the conversion nodes are built? Or does this occur inside the
        //   AST representation?
        // - access rights for member fns

        // Rules for function calls:
        // 1. distinguish between ordinary fn and member fn
        // 2. 
        // - if ordinary, call exp should be lvalue referring to fn, or have
        //   ptr to fn type
        // - if member, call exp should be implicit or explicit access of mem 
        //   fn, or have ptr to mem type selecting a mem fn.
        // 3. arguments with object type should be completely defined type
        // 4. arguments are fetched where appropriate
        // 5. 
        // - if lvalue referring to fn or mem fn access, use id resolution and
        //   overload resolution to get the correct fn, use the result to get 
        //   the conversion sequences, build the conversion sequences for each
        //   argument
        // - if fn pointer or ptr to mem fn, ** unimplemented **
        // 6. return type of fn must be complete object type, reference type
        //    or void

        rb.put (T_LVALUE_FN, new CGRFunctionIdExpTest ());

        CodeGenRule rn_function_call =
            new CGRSequentialNode 
                (rb.get (R_FUNCTION_CALL), 
                 new CGRSequentialNode
                     (rb.get (R_VALID_RETURN_TYPE),
                     new CGRConditionalNode 
                          ((CGRTest) rb.get (T_LVALUE_FN), 
                           new CGROperandNode 
                               (rb.get (R_NONE),
                                rb.get (R_COMPLETE_TYPE)),
                           rb.get (R_RESOLVE_FN_ID),
                           rb.get (R_FN_PTR))));

        CodeGenRule rs_fn_call_ref_fetch = 
            new CGRSequentialNode 
            (rb.get (R_REFERENCE_LVAL), rn_function_call);
        rb.put (RS_FUNCTION_CALL, rs_fn_call_ref_fetch);			  

        // 5.2.4 : pseudo destructor call

        // 5.2.5 : class member access
        // 1. left operand must be class object or pointer to (complete)
        // 2. fetch left operand
        // 3. if arrow notation (expa->expb) convert expa-> to (*(expa))
        // 4. fetch right operand (will have already been identified in lookup)
        // 5. build the expression (this will also identify the result's type)

        CodeGenRule r_fetch_0 = new CGRFetch (0);
        CodeGenRule rs_reference_lval = 
            new CGRConditionalNode 
            (new CGRReferenceTest (0), r_fetch_0);
        
        CodeGenRule rs_member_dot = 
            new CGRSequentialNode 
                (new CGRMember (symbolTable, true), 
                 new CGRSequentialNode
                     (new CGRConvertToReference(0),
                      rs_reference_lval)); // OpMember expects unfetched lhs

        operands = new OperandTable ();
        operands.put (TyClass.class, rs_member_dot);
        rulesets.put (OpTable.get (DOT), operands);


        // ->
        CodeGenRule rs_member_arrow = 
            new CGRSequentialNode 
                (new CGRMember (symbolTable, false), r_fetch_0);
        // no dereference - AST expression wants the pointer. So we just
        // fetch if an lvalue.

        // ISO 13.5.6 overloaded arrow operator
        CodeGenRule rs_arrow_ol = 
            new CGRSequentialNode
                (rs_member_arrow, rb.get (R_OVERLOADED_OPERATOR));

        operands = new OperandTable ();
        operands.put (TyPointer.class, rs_member_arrow);
        operands.put (TyClass.class, rs_arrow_ol);
        rulesets.put (OpTable.get (ARROW), operands);


        // 5.2.6 : postfix increment/decrement
        // 1. operand must be of arithmetic or pointer type (not boolean for
        //    decrement
        // 2. operand must be modifiable lvalue
        // 3. build expression
       
        // ++
        rb.put (R_POSTFIX_INCREMENT, new CGRIncrement (false, true));
        CodeGenRule rn_postfix_increment = 
            new CGROperandNode (rb.get (R_POSTFIX_INCREMENT),
                                  rb.get (R_MODIFIABLE_LVALUE));
        operands = new OperandTable ();
        operands.put (TyArithmetic.class, rn_postfix_increment);
        operands.put (TyPointer.class, rn_postfix_increment);
        rulesets.put (OpTable.get (PLUSPLUS), operands);

        // --
        rb.put (R_POSTFIX_DECREMENT, new CGRIncrement (false, false));
        CodeGenRule rn_postfix_decrement = 
            new CGROperandNode (rb.get (R_POSTFIX_DECREMENT),
                                  rb.get (R_MODIFIABLE_LVALUE));
        CodeGenRule rn_disallow_boolean_decrement = 
            new CGRError ("cannot decrement boolean");
        operands = new OperandTable ();
        operands.put (TyBool.class, rn_disallow_boolean_decrement);
        operands.put (TyArithmetic.class, rn_postfix_decrement);
        operands.put (TyPointer.class, rn_postfix_decrement);
        rulesets.put (OpTable.get (MINUSMINUS), operands);

        // 5.2.8 : type identification

        CodeGenRule r_typeid = new CGRUnimplemented ("typeid expressions");
        operands = new OperandTable ();
        operands.put (TypeNode.class, r_typeid);
        rulesets.put (OpTable.get (TYPEID), operands);

    }

    /** Tests whether the expression so far is just a function name */
    public class CGRFunctionIdExpTest extends CGRTest {

        public CGRFunctionIdExpTest () {
            messageOnFalse = "expression is not a function name";
        }

        /**
         * Does <code>exp</code> contain a function id ?
         *
         * @param exp contains the expression to test
         */
        public boolean applies (ExpressionPtr exp) {
            return exp.get () instanceof ExpFunctionName;
        }
        
    }
}


