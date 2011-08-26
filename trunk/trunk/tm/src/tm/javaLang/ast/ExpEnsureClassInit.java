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

package tm.javaLang.ast;

import tm.clc.ast.*;
import tm.clc.rtSymTab.RT_Symbol_Table;
import tm.utilities.Assert;
import tm.utilities.Debug ;
import tm.virtualMachine.*;


/** ExpEnsureClassInit Represents a java class initialization check.
 * <P> When evaluated, this node checks an expression node to see 
 *  if the associated class has been initialized. If not, it carries
 *  out the initialization.</p>
 
 * @author theo and mpbl
 *
 */

public class ExpEnsureClassInit extends DefaultExpressionNode{

    TyClass theClass ;
    
    /** Contructor
     * 
     * @param theClass The class to be checked and, perhaps, initialized.
     * @param child the expression to be evaluated.
     */
    public ExpEnsureClassInit(TyClass theClass,
                  ExpressionNode child) {
        super("ExpEnsureClassInit", child) ;
        this.theClass = theClass ;
        set_type( child.get_type() ) ;
        set_syntax( new String[] {"",""} ) ;
        set_stepper( StepperEnsureClassInit.construct() ) ;
        set_selector( SelectorExpEnsureClassInit.construct() ) ;
        
     }
}

class SelectorExpEnsureClassInit implements Selector {

    private static SelectorExpEnsureClassInit instance;
    
    static SelectorExpEnsureClassInit construct() {
        if(instance == null) instance = new SelectorExpEnsureClassInit();
        return instance;
    }

    /** 
     * @see tm.clc.ast.Selector#select(tm.clc.ast.ExpressionNode, tm.virtualMachine.VMState)
     */
    public void select(ExpressionNode nd, VMState vms) {
        ExpEnsureClassInit nd1 = (ExpEnsureClassInit) nd;
        if( nd1.theClass.isInitialized(vms) ) {
            if( vms.top().at(nd.child_exp(0)) == null ) {
                // Recurse on child
                nd.child_exp(0).select( vms ) ; }
            else {
                // Child is evaluated so select this node
                vms.top().setSelected( nd ) ; } }
        else {
            // The class must be initialized, so select this node
            vms.top().setSelected( nd ) ; }        
    }
}

class StepperEnsureClassInit implements Stepper {

    private static StepperEnsureClassInit instance;
    
    static StepperEnsureClassInit construct() {
        if(instance == null) instance = new StepperEnsureClassInit();
        return instance;
    }

    public void step(ExpressionNode nd, VMState vms) {
        ExpEnsureClassInit nd1 = (ExpEnsureClassInit) nd;
        // The node shouldn't already be mapped.
        Assert.check(vms.top().at(nd) == null);
        // Clear the selection
        vms.top().setSelected(null);
        
        if( nd1.theClass.isInitialized(vms) ) {
            // The class is already initialized.
            Object d = vms.top().at(nd.child_exp(0)) ;
            vms.top().map( nd, d) ; }
        else {
            // The class is not initialized.
            // We must evaluate the initialization chain for the class.
            
            // First figure out which class should be initialized next.
            // it could be this class or it could be a superclass.
            TyClass nextClass = findUninitializedAncestor( nd1.theClass, vms ) ;
            
            // Second set the class as initialized to avoid recursion
            nextClass.setInitialized(vms) ;
            // Get the chain from the symbol table
            RT_Symbol_Table symtab = (RT_Symbol_Table)vms.getSymbolTable() ;
            String chainName = nextClass.getStaticInitializationChainName( ) ;
            StatementNodeLink p = symtab.getInitializationChain( chainName ) ;
            // Build and push a new evaluation.
            if( Debug.getInstance().isOn() ) {
                StringBuffer buf = new StringBuffer() ;
                buf.append( "Static Initializer\n" ) ;
                StatementPrinter.FormatStatement( buf, p ) ;
                Debug.getInstance().msg( Debug.EXECUTE,  buf.toString() ) ; }
            FunctionEvaluation funEval 
            = new FunctionEvaluation(vms, p.get(), null, null ) ;
            vms.push( funEval ) ;
            
            // We need to call enterFunction here because the
            // return will call exitFuntion.
            symtab.enterFunction( null ) ;
            vms.pushNewArgumentList() ;
            vms.getStore().setStackMark() ; }
    }

    /**
     * @param theClass
     * @return
     */
    private TyClass findUninitializedAncestor(TyClass theClass, VMState vms) {
        // In Java, we must initialize the direct superclass,
        // if it is not initialized already.
        // We do not initialize any superinterfaces.
        // Precondition theClass is not initialized.
        
        while( true ) {
            // Invariant: theClass is not initialized
            TyClass parent = theClass.getDirectSuperClass() ;
            if( parent == null || parent.isInitialized(vms) ) break ;
            theClass = parent ; }
        return theClass ; 
    }
}