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
import tm.clc.datum.AbstractIntDatum;
import tm.clc.datum.AbstractPointerDatum;
import tm.clc.datum.AbstractRefDatum;
import tm.utilities.Assert;
import tm.virtualMachine.VMState;

/** Pointer subscript
  * The left operand should evaluate to
  * a pointer to some type. The type should be a
  * reference to the same type. The right operand should
  * evaluate to an AbstracIntDatum. Combines pointer addition
  * and dereference. */
public class OpPointerSubscript extends DefaultExpressionNode
{

    String open_bracket, close_bracket ;
    
    public OpPointerSubscript ( TypeNode t,
            String open_bracket, String close_bracket,
            ExpressionNode left_operand,
            ExpressionNode right_operand ) {
        super("OpPointerSubscript", left_operand, right_operand) ;
        this.open_bracket = open_bracket ;
        this.close_bracket = close_bracket ;
        set_type( t ) ;
        set_syntax( new String[]{ "", open_bracket, close_bracket } ) ;
        set_selector( SelectorLeftToRight.construct() ) ;
        set_stepper( StepperOpPointerSubscript.construct() ) ;
    }
}


class StepperOpPointerSubscript extends StepperBasic {

    private static StepperOpPointerSubscript singleton ;
    
    static StepperOpPointerSubscript construct() {
        if( singleton == null ) singleton = new StepperOpPointerSubscript() ;
        return singleton ; }
        
    public AbstractDatum inner_step( ExpressionNode nd, VMState vms ) {
	 	// Get values of operands
	   	    Object xd = vms.top().at( nd.child_exp(0) ) ;
	   	    Assert.check( xd instanceof AbstractPointerDatum ) ;
	   	    AbstractPointerDatum ptr = (AbstractPointerDatum) xd ;
	   	    
	   	    Object yd = vms.top().at( nd.child_exp(1) ) ;
	   	    Assert.check( yd instanceof AbstractIntDatum ) ;
	   	    AbstractIntDatum ydi = (AbstractIntDatum) yd ;
	   	    int index = (int) ydi.getValue() ;
	    	    
	    // Find the item
    		int address = ptr.getValue() ;
    		TypeNode type = (TypeNode) ptr.getPointeeType() ;
    		int size = type.getNumBytes() ;
    		int newAddress = (int) ( address + index*size ) ;
    		AbstractDatum item = (AbstractDatum) vms.getStore().chasePointer( newAddress, type ) ;
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