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

package tm.clc.ast;

import tm.clc.datum.AbstractDatum;
import tm.clc.datum.AbstractObjectDatum;
import tm.clc.datum.AbstractPointerDatum;
import tm.clc.rtSymTab.RT_Symbol_Table;
import tm.interfaces.Datum;
import tm.languageInterface.StatementInterface;
import tm.utilities.Assert;
import tm.utilities.Debug;
import tm.virtualMachine.FunctionEvaluation;
import tm.virtualMachine.VMState;

/** An expression node for all kinds of function calls.
<P> This node represents the union of its children and as such
is a bit complicated.  We have to deal with plain function calls,
method calls where the recipient is explicit and method calls
where the recipient is implicit. We also allow the display of the
recipient to be suppressed, even when it is explicit (this is
used for constructors).
*/
public class OpAbsFuncCall  extends DefaultExpressionNode
{
    final ExpressionNode stateDummy ;
    final ExpressionNode resultDummy ;
    final Object function_key ;
    final boolean isMemberCall ;
    final boolean recipientIsThis ;
    final boolean isVirtual ;
    final int [] path ;
    final private boolean suppress_recipient ;
    final ExpressionNode repetitions ;
    final NodeList children_to_display ;

    /** Construct
    @param isMemberCall      This is a call to a method or constructor.
    @param recipientIsThis   The recipient is implicitly the "this" object.
    @param isVirtual           This is a virtual function call
    @param t                   The type of the result
    @param function_name       For display in the Expression Engine
    @param operator            The operator ("." or "->" in C++)
    @param operator_syntax     Used for C++ operator overloading. E.g. cin >> x
    @param suppress_recipient  Do not show the recipient or the operator in the
                               Expression Engine (used for example for constructors).
    @param path                A path to find the recipients subobject.
    @param recipient           Must evaluate to a pointer or reference to an object
                               (must be null when this is not a mthod call or when
                               the recipient is implicetly this).
    @param aruments            The argument list for the call.
    @param repetitions         null or an expression representing the number of objects to
                               be constructed.
    */
    OpAbsFuncCall ( String node_name,
                   boolean isMemberCall,
                   boolean recipientIsThis,
                   TypeNode t,
                   String function_name,
                   String operator,
                   boolean operator_syntax,
                   boolean suppress_recipient,
                   Object function_key,
                   boolean isVirtual,
                   int [] path,
                   ExpressionNode recipient,
                   NodeList arguments,
                   ExpressionNode repetitions )
    {
        super(node_name, new NodeList( arguments ) ) ;
        // Preconditions //
        // Member calls (only) require a recipient
        Assert.check( isMemberCall == (recipientIsThis || recipient != null) ) ;
        // The recipient can not be both implicit and explicit
        Assert.check( !recipientIsThis || recipient == null ) ;
        // The recipient can only be hidden for method calls with an explicit recipient.
        Assert.check( !suppress_recipient || recipient != null ) ;
        // The recipient can only be hidden for method calls with no operator syntax
        Assert.check( !suppress_recipient || !operator_syntax ) ;
        // Operator syntax can not be used with an implicit "this" recipient
        Assert.check( !operator_syntax || !recipientIsThis ) ;
        // Operator syntax can only be used for the right number of arguments.
        Assert.check( !operator_syntax
                    || recipient==null && (arguments.length()==1 || arguments.length()==2)
                    || recipient!=null && (arguments.length()==0 || arguments.length()==1) ) ;
        // Method calls and only method calls have a path
        Assert.check( isMemberCall == (path!=null) ) ;
        // Virtual calls must be member calls
        Assert.check( !isVirtual || isMemberCall ) ;
        // End of preconditions

        // Add the recipient to the list of operands.
            if( isMemberCall && ! recipientIsThis ) {
                addFirstChild( recipient ) ; }
        // Save useful parameters
            this.isMemberCall = isMemberCall ;
            this.recipientIsThis = recipientIsThis ;
            this.isVirtual = isVirtual ;
            this.suppress_recipient = suppress_recipient ;
            this.function_key = function_key ;
            if( isMemberCall ) this.path = path ;
            else this.path = null ;
            this.stateDummy = new NoExpNode() ;
            this.resultDummy = new NoExpNode() ;
            this.repetitions = repetitions ;

        // Set the type
            set_type( t ) ;

        // Set the syntax
            String [] syn ;
            int child_count = childCount() ;
            if( operator_syntax ) {
                Assert.check( child_count == 1 || child_count == 2) ;
                syn = new String[ child_count + 1 ] ;
                children_to_display = children() ;
                if( child_count == 1 ) {
                    syn[1] = " "+function_name+" " ; syn[1] = "" ; }
                else {
                    syn[0] = "" ; syn[1] = " "+function_name+" " ; syn[2] = "" ; } }
            else { // Parentheses syntax
                // Cases.
                //  (a) Nonmember call,
                //  (b) member call with implicet this as recipient,
                //  (c) member call with suppressed recipient,
                //  (d) member call with nonsuppressed recipient
                if( ! isMemberCall || recipientIsThis || suppress_recipient ) {
                    // One of cases (a), (b), or (c)
                    children_to_display = arguments ; }
                else {
                    // case (d)
                    children_to_display = children() ; }
                syn = new String[ children_to_display.length() + 1 ] ;

                int j = 0 ; // j indexes the syn array.
                if( ! isMemberCall || recipientIsThis || suppress_recipient ) {
                    // One of cases (a), (b), or (c)
                    syn[j++] = function_name ; }
                else {
                    // Case (d)
                    syn[j++] = "" ;
                    syn[j++] = operator + function_name ; }

                if( arguments.length() > 0 ) {
                    syn[j-1] += "( " ;
                    for( int i=0 ; i < arguments.length() ; ++i ) {
                        syn[j++] = i==arguments.length()-1 ? " )" : ", " ; } }
                else {
                    syn[j-1] += "()" ; }

                Assert.check(j == syn.length ) ; }

            set_syntax( syn ) ;

        set_selector( SelectorLeftToRight.construct() ) ;

        // Set the stepper.
        set_stepper( StepperAbsFuncCall.construct() ) ;
    }
    
    public boolean isStatic(){return ! isMemberCall;}


    /** Overide toString in order to allow recipient to be suppressed */

    public String toString(VMState vms) {
        return toString( vms, children_to_display ) ; }

    public String formatNodeData() {
        return super.formatNodeData() +
               " "+function_key +
               " "+isMemberCall +
               " "+recipientIsThis +
               " "+suppress_recipient ;
    } ;
}

class StepperAbsFuncCall implements Stepper {

    private static StepperAbsFuncCall singleton ;

    private StepperAbsFuncCall() {}

    static public StepperAbsFuncCall construct( ) {
        if( singleton == null ) singleton = new StepperAbsFuncCall() ;
            return singleton ; }

    public void step( ExpressionNode nd, VMState vms ) {
        OpAbsFuncCall func_call = (OpAbsFuncCall) nd ;
        RT_Symbol_Table symtab = (RT_Symbol_Table) vms.getSymbolTable() ;

        // Get the recipient
            AbstractObjectDatum recipient ;
            if( func_call.isMemberCall && ! func_call.recipientIsThis ) {
                // Get the first child node
                ExpressionNode child = (ExpressionNode) func_call.child(0) ;
                // Get its mapped datum
                AbstractDatum childValue = (AbstractDatum) vms.top().at( child ) ;
                // It should be a pointer or reference
                Assert.check( childValue instanceof AbstractPointerDatum ) ;
                // Dereferencing should give the recipient.
                Datum recipient1 = ((AbstractPointerDatum)childValue).deref() ;
                Assert.check( recipient1 instanceof AbstractObjectDatum ) ;
                recipient = (AbstractObjectDatum) recipient1 ;
                recipient = recipient.getSubObject( func_call.path ) ; }
            else if( func_call.isMemberCall && func_call.recipientIsThis ) {
                recipient = symtab.getTopRecipient() ;
                recipient = recipient.getSubObject( func_call.path ) ; }
            else {
                recipient = null ; }
       
        // Try to find the function definition
            AbstractFunctionDefn defn ;
            // The adapted recipient should be the subobject of the root object that
            // the definition expects as it's recipient. I.e. it should have the
            // same type as the "this" keyword will within the defn.
            AbstractObjectDatum adaptedRecipient ;
            if( func_call.isVirtual ) {
                // Find the root of the subobject tree
                AbstractObjectDatum rootRecipient = recipient.rootObject() ;
                AbstractObjectDatum.VirtualFcnDescription vfd
                    = rootRecipient.findVirtualFunctionDefn( func_call.function_key ) ;
                defn = vfd.defn ;
                adaptedRecipient = vfd.subObj ;
                }
            else {
                defn = symtab.getFunctionDefn( func_call.function_key ) ;
                adaptedRecipient = recipient ; }
            
        // Is the function user defined?
            if( defn instanceof  FunctionDefnCompiled) {
                step_user_defined( nd, vms, (FunctionDefnCompiled) defn, adaptedRecipient ) ; }
            else if ( defn instanceof FunctionDefnBuiltIn ){
                // If the function is not user-defined, it may be built-in.
                // Look for the function in the built-in function database.
                Stepper built_in_stepper = ((FunctionDefnBuiltIn)defn).getStepper() ;
                built_in_stepper.step(nd, vms) ; }
            else {
                Assert.error( "Definition for function or method not found. Key is " + func_call.function_key ) ;
            }
    }


    public void step_user_defined( ExpressionNode nd, VMState vms, FunctionDefnCompiled defn, AbstractObjectDatum recipient ) {

        Assert.apology(((OpAbsFuncCall)nd).repetitions == null,
            "Construction of multiple objects not supported yet." ) ;

        // The node shouldn't already be mapped.
            Assert.check( vms.top().at( nd ) == null ) ;

        // Clear the selection
            vms.top().setSelected( null ) ;

        // Get the node and the its state.
            OpAbsFuncCall func_call = (OpAbsFuncCall) nd ;
            Integer state = (Integer) vms.top().at( func_call.stateDummy ) ;

        if( state == null ) { // First step

            // Go to next state, next time
                vms.top().map( func_call.stateDummy, new Integer(0) ) ;

            // Get the symbol table
                RT_Symbol_Table symtab = (RT_Symbol_Table) vms.getSymbolTable() ;

            // Register all the arguments with the vms
                vms.pushNewArgumentList() ;
                int start ;
                if( func_call.isMemberCall && ! func_call.recipientIsThis )
                    start = 1 ;
                else
                    start = 0 ;
                for( int i = start, sz = func_call.childCount() ; i < sz ; ++ i ) {
                    ExpressionNode child = (ExpressionNode) func_call.child(i) ;
                    AbstractDatum arg = (AbstractDatum) vms.top().at( child ) ;
                    vms.addArgument( arg ) ; }

            // Prepare the symbol table
                symtab.enterFunction( recipient ) ;

            // Mark the stack
               vms.getStore().setStackMark() ;

            //Create a scratch var to hold the result
                TypeNode retType = func_call.get_type() ;
                Clc_ASTUtilities util
                    = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
                AbstractDatum resultDatum = util.scratchDatum(  retType, vms ) ;
                //vms.pushResultDatum( resultDatum ) ;
                // Remember the resultDatum by mapping func_call.resultDatum to it.
                vms.top().map( func_call.resultDummy, resultDatum ) ;
                
                Debug d = Debug.getInstance() ;
                if( d.isOn( Debug.EXECUTE ) ) {
                	Debug.getInstance().msg(Debug.EXECUTE, "Calling user defined or compiler generated function:" );
                	Debug.getInstance().msg(Debug.EXECUTE, defn.toString()); }

            // Push a new function evaluation on the evaluation stack
                StatementInterface firstStatement = defn.getBodyLink().get() ;
                FunctionEvaluation funEval = new FunctionEvaluation( vms, firstStatement, recipient, resultDatum ) ;
                vms.push( funEval ) ; }

        else { // Step 2. Retrieve the result and map it.

            // Reset the state
                vms.top().map( func_call.stateDummy, null ) ;

            // Get the result.
                //AbstractDatum resultDatum = (AbstractDatum) vms.topResultDatum() ;
                //vms.popResultDatum() ;
                AbstractDatum resultDatum = (AbstractDatum) vms.top().at( func_call.resultDummy ) ; 

            // And map the node to it
                vms.top().map( func_call, resultDatum ) ; }
    }
}