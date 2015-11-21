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

import tm.clc.analysis.CodeGenRule;
import tm.clc.analysis.ExpressionPtr;
import tm.clc.analysis.FunctionDeclaration;
import tm.clc.analysis.LFConst;
import tm.clc.analysis.ScopedName;
import tm.clc.ast.ExpId;
import tm.clc.ast.ExpUnimplemented;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.NodeList;
import tm.clc.ast.OpFuncCall;
import tm.clc.ast.OpMemberCall;
import tm.clc.ast.OpThisMemberCall;
import tm.clc.ast.TyAbstractClass;
import tm.clc.ast.TyAbstractFun;
import tm.clc.ast.TyAbstractPointer;
import tm.clc.ast.TyAbstractRef;
import tm.clc.ast.TypeNode;
import tm.cpp.ast.ExpFunctionName;
import tm.cpp.ast.TyClass;
import tm.cpp.ast.TyRef;
import tm.utilities.Assert;
import tm.utilities.Debug;

/**
 * Generates the AST representation of a function call, including
 * member functions.
 */
public class CGRFunctionCall extends CodeGenRule {
    private boolean operator_fn ;
    private CodeGenRule value_argument_conversion_rule ;
    private CodeGenRule reference_argument_conversion_rule ;
    private CodeGenRule rs_convert_no_parameter_argument ;
    private static final ScopedName dummySN = new Cpp_ScopedName("dummy") ;

    /**
     * Create a new rule instance.
     * @param operator_fn indicates whether this rule instance is for
     * overloaded operator functions
     */
    public CGRFunctionCall (CodeGenRule value_argument_conversion_rule,
                            CodeGenRule reference_argument_conversion_rule,
                            CodeGenRule rs_convert_no_parameter_argument,
                            boolean operator_fn ) {
        this.operator_fn = operator_fn;
        this.value_argument_conversion_rule = value_argument_conversion_rule ;
        this.reference_argument_conversion_rule = reference_argument_conversion_rule ;
        this.rs_convert_no_parameter_argument = rs_convert_no_parameter_argument ;
    }

    /**
     * Create a new rule instance for regular function notation
     * (not overloaded operator, constructor).
     */
    public CGRFunctionCall (CodeGenRule value_argument_conversion_rule,
                            CodeGenRule reference_argument_conversion_rule,
                            CodeGenRule rs_convert_no_parameter_argument) {
            this (value_argument_conversion_rule,
                  reference_argument_conversion_rule,
                  rs_convert_no_parameter_argument,
                  false); }

    public void apply (ExpressionPtr exp) {

        Assert.check (exp.get () instanceof ExpFunctionName);
        ExpFunctionName efn = (ExpFunctionName) exp.get ();

        FunctionDeclaration fd = efn.getMatch().declaration;
        String fn_name = efn.getName().getName (); // ! :}
        // The name of an operator in the efn includes the
        // operator keyword. (I'm not sure why, but mine is not...)
        // We don't want the gentle user to see this, so we
        // fudge the name for dispay purposes
            if( operator_fn ) {
                Assert.check( fn_name.startsWith("operator") ) ;
                fn_name = fn_name.substring(8) ; }
        TypeNode returnType =
            ((TyAbstractFun) fd.getType ()).returnType ();


        Debug.getInstance().msg(Debug.COMPILE, "Function name is "+fn_name);
        Debug.getInstance().msg(Debug.COMPILE, "Function declaration is "+fd);
        // perform conversions on arguments.
        perform_argument_conversions( exp, fd ) ;

        // make the function call expression
        NodeList args_nl = build_args_list (exp);

        ExpressionNode fc_exp;

        ExpressionNode classobj = efn.getClassObject ();
        if (fd.getCategory().contains (Cpp_LFlags.MEM_FN) &&
            !fd.hasSpecifier (Cpp_Specifiers.SP_STATIC) ) {
            // non static member function
            // need to get a relative path
            Assert.check (classobj != null);
            TypeNode classobjType = classobj.get_type() ;
            Assert.check( classobjType instanceof TyAbstractPointer ) ;
            TypeNode typeOfRecipient = ((TyAbstractPointer) classobjType).getPointeeType()  ;
            Assert.check( typeOfRecipient instanceof TyClass ) ;
            ClassSH classSH = (ClassSH) ((TyClass)typeOfRecipient).getDeclaration().getScopeHolder() ;

            ScopedName sn_path = new Cpp_ScopedName ();
            classSH.buildRelativePath (fd, sn_path);

            String accessOp = efn.getMemberAccessOperator ();
            boolean suppress = fd.getCategory().contains (Cpp_LFlags.CONSTRUCTOR)
			                 ||fd.getCategory().contains (Cpp_LFlags.DESTRUCTOR);
            boolean recipientIsImplicitlyThis = efn.getRecipientImplicitlyThis() ;
            Assert.check (suppress || recipientIsImplicitlyThis || (accessOp != null));

            if( recipientIsImplicitlyThis ) {
                d.msg (Debug.COMPILE, "building member call with classobj:\n" +
                         classobj.ppToString (3, 80));

                // TODO revisit for Virtual functions.
                fc_exp = new OpThisMemberCall (returnType,
                                               fn_name,
                                               fd.getRuntimeId (),
                                               false,
                                               fd.getRelativePath (sn_path),
                                               args_nl);

            } else {
                d.msg (Debug.COMPILE, "building member call with classobj:\n" +
                         classobj.ppToString (3, 80));
                
//              TODO revisit for Virtual functions.
                fc_exp = new OpMemberCall (returnType, fn_name, accessOp,
                                           operator_fn, suppress,
                                           fd.getRuntimeId (), false, 
                                           classobj,
                                           fd.getRelativePath (sn_path),
                                           args_nl);
            }

            // place the class type in operand[0] for possible further use
            exp.removeAllOperands ();
            exp.set (classobj.get_type (), 0);
        } else {
            d.msg (Debug.COMPILE, "runtime id for function call is "
                   + fd.getRuntimeId());
            d.msg (Debug.COMPILE, "fqn for function call is "
                   + fd.getName().getName ());
            fc_exp = new OpFuncCall (returnType, fn_name,
                                       operator_fn, fd.getRuntimeId (),
                                       args_nl);
        }
        exp.set (fc_exp);

    }

    /**
     * Performs the conversions specified in the sequences for each argument
     * @param exp the expression being built
     * @param sequences the conversion sequences for each argument
     */

    protected void perform_argument_conversions( ExpressionPtr exp, FunctionDeclaration fd ) {
        int argumentCount = exp.operandCount() ;
        int parameterCount = fd.getParameterCount() ;

        // Check the number of arguments.
        if( fd.hasEllipsis() ) {
            Assert.check( argumentCount >= parameterCount ) ; }
        else {
            Assert.check(argumentCount == parameterCount) ;  }

        Debug.getInstance().msg(Debug.COMPILE, "Function declaration is "+fd);

        int numberOfArgumentsToConvert = argumentCount ;
        for (int i = 0; i < numberOfArgumentsToConvert ; i++) {
            ExpressionNode argument = exp.get(i) ;
            
            TypeNode parameterType  ;
            if( i < parameterCount) {
                parameterType = fd.getParameter( i ) ;
                Debug.getInstance().msg(Debug.COMPILE, "Parameter is "+parameterType.typeId()); }
            else {
                parameterType = null ;
                Debug.getInstance().msg(Debug.COMPILE, "Parameter is absent");}

            if( parameterType == null ) {
                Debug.getInstance().msg(Debug.COMPILE, "Using no parameter rule");
                
                // Apply the argument conversion rule to
                ExpressionPtr ptr = new ExpressionPtr( argument ) ;
                rs_convert_no_parameter_argument.apply( ptr ) ;
                
                // Extract the transformed argument.
                argument = ptr.get() ; }
            else {
                CodeGenRule conversion_rule ;
            
                if( parameterType instanceof TyAbstractRef ) {
                    Debug.getInstance().msg(Debug.COMPILE, "Using reference rule");
                    conversion_rule = reference_argument_conversion_rule ; }
                else {
                    Debug.getInstance().msg(Debug.COMPILE, "Using value rule");
                    conversion_rule = value_argument_conversion_rule ; }

                // Exception. We always pass objects by reference. The
                // call to the copy constructor is in the function body.
                if( parameterType != null &&  parameterType instanceof TyAbstractClass ) {
                    Debug.getInstance().msg(Debug.COMPILE, "Making type reference");
                    parameterType = new TyRef( parameterType ) ; }

                // Use the type to create a dummy node.
                ExpressionNode parameterStandIn = new ExpId( new TyRef( parameterType ), "dummy", dummySN ) ;

                // Apply the argument conversion rule to
                ExpressionPtr ptr = new ExpressionPtr( dummySN,
                        new Object [] {parameterStandIn, argument} ) ;
                conversion_rule.apply( ptr ) ;

                // Extract the transformed argument.
                argument = ptr.get(1) ; }
            exp.set( argument, i );
        }
    }




    /**
     * Builds the AST representation of the function arguments
     * @param exp the expression being built
     * @return the function args as a <code>NodeList</code>
     */
    protected NodeList build_args_list (ExpressionPtr exp) {
        NodeList args_nl = new NodeList ();
        for (int i = 0; i < exp.operandCount (); i++) {
            args_nl.addLastChild (exp.get (i));
        }
        return args_nl;
    }
}
