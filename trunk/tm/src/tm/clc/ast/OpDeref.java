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
import tm.clc.datum.AbstractPointerDatum;
import tm.clc.datum.AbstractRefDatum;
import tm.utilities.Assert;
import tm.virtualMachine.VMState;

/** Dereference a pointer.
  * The operand should evaluate to a pointer to some type.
  * The type should be a reference to the same type.
  * Implements C++'s unary *. */
public class OpDeref extends DefaultExpressionNode
{
    public OpDeref ( TypeNode t,
               String operator_image,
               ExpressionNode operand )  {
        super("OpDeref", operand) ;
        Assert.check( t instanceof TyAbstractRef ) ;
        set_type( t ) ;
        set_syntax( new String[]{ operator_image, "" } ) ;
        set_selector( SelectorLeftToRight.construct() ) ;
        set_stepper( StepperOpDeref.construct() ) ;
    }
}

class StepperOpDeref extends StepperBasic {

    private static StepperOpDeref singleton ;
    
    static StepperOpDeref construct() {
        if( singleton == null ) singleton = new StepperOpDeref() ;
        return singleton ; }
        
    public AbstractDatum inner_step( ExpressionNode nd, VMState vms ) {
        // Get values of operand
            Object xd = vms.top().at( nd.child_exp(0) ) ;
            Assert.check( xd instanceof AbstractPointerDatum ) ;
            AbstractPointerDatum ptr = (AbstractPointerDatum) xd ;
                
        // Find the item
            int address = ptr.getValue() ;
            TypeNode type = (TypeNode) ptr.getPointeeType() ;
            AbstractDatum item = (AbstractDatum) vms.getStore().chasePointer( address, type ) ;
            if( item==null ) {
                Assert.apology("Pointer dereference fails." ) ; }
            
        // Construct a scratch reference
        // Make a new reference in the scratch region.
            
            Clc_ASTUtilities util
                = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
            AbstractRefDatum ref
                = (AbstractRefDatum) util.scratchDatum(nd.get_type(), vms) ;
                
            ref.putValue(item) ;
        
        // Done
            return ref ; }
}