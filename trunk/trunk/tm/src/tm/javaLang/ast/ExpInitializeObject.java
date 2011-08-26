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
import tm.clc.datum.AbstractIntDatum;
import tm.clc.datum.AbstractObjectDatum;
import tm.clc.rtSymTab.RT_Symbol_Table;
import tm.interfaces.Datum;
import tm.utilities.Assert;
import tm.utilities.Debug ;
import tm.virtualMachine.*;


/** ExpInitializeObject Initiates the execution of the "dyamic initialization chain"
 * to execute initializations of fields and exection of initialization blocks.
 * 
 * @author theo and mpbl
 *
 */

public class ExpInitializeObject extends DefaultExpressionNode{

    TyClass theClass ;
    
    /** Contructor
     * 
     * @param theClass The class to be checked and, perhaps, initialized.
     * @param child the expression to be evaluated.
     */
    public ExpInitializeObject(TyClass theClass) {
        super("ExpInitializeObject") ;
        this.theClass = theClass ;
        set_type( TyVoid.get() ) ;
        set_syntax( new String[] {""} ) ;
        set_stepper( StepperExpInitializeObject.construct() ) ;
        set_selector( SelectorAlways.construct() ) ;
        
     }
}

class StepperExpInitializeObject implements Stepper {

    private static StepperExpInitializeObject instance;
    
    static StepperExpInitializeObject construct() {
        if(instance == null) instance = new StepperExpInitializeObject();
        return instance;
    }

    public void step(ExpressionNode nd, VMState vms) {
        ExpInitializeObject nd1 = (ExpInitializeObject) nd;
        // The node shouldn't already be mapped.
        Assert.check(vms.top().at(nd) == null);
        // Clear the selection
        vms.top().setSelected(null);

        // Map this node to a void datum. This will terminate the evaluation.
        Clc_ASTUtilities util = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
        Datum d = util.scratchDatum(nd.get_type(), vms) ;
    
        vms.top().map( nd1, d ) ;
        
        // Get the chain from the symbol table
        RT_Symbol_Table symtab = (RT_Symbol_Table)vms.getSymbolTable() ;
        String chainName = nd1.theClass.getDynamicInitializationChainName( ) ;
        StatementNodeLink p = symtab.getInitializationChain( chainName ) ;
        // Build and push a new evaluation.
        if( Debug.getInstance().isOn() ) {
            StringBuffer buf = new StringBuffer() ;
            buf.append( "Dynamic Initializer\n" ) ;
            StatementPrinter.FormatStatement( buf, p ) ;
            Debug.getInstance().msg(Debug.EXECUTE,  buf.toString() ) ; }
        
        // This node shold only occur in a dynamic context, so there is a recipient.
        AbstractObjectDatum recipient = symtab.getTopRecipient() ;
        Assert.check( recipient != null ) ;
        FunctionEvaluation funEval = new FunctionEvaluation(vms, p.get(), recipient, null  ) ;
        vms.push( funEval ) ;
        
        // We need to call enterFunction here because the
        // return will call exitFuntion.
        symtab.enterFunction( recipient ) ;
        vms.pushNewArgumentList() ;
        vms.getStore().setStackMark() ;
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