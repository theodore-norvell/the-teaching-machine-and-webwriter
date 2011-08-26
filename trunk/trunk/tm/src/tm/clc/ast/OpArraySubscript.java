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

import tm.clc.datum.AbstractArrayDatum;
import tm.clc.datum.AbstractDatum;
import tm.clc.datum.AbstractIntDatum;
import tm.clc.datum.AbstractRefDatum;
import tm.clc.datum.AbstractPointerDatum;
import tm.utilities.Assert;
import tm.virtualMachine.VMState;

/** Array subscript
  * The type should be reference to the base type of the array.
  * The left_operand is a reference to the array and the
  * right_operand is the index. */
public class OpArraySubscript extends DefaultExpressionNode
{

    String open_bracket, close_bracket ;
    
    public OpArraySubscript ( TypeNode t,
                 String open_bracket, String close_bracket,
                 ExpressionNode left_operand,
                 ExpressionNode right_operand ) {
        super("OpArraySubscript", left_operand, right_operand) ;
        this.open_bracket = open_bracket ;
        this.close_bracket = close_bracket ;
        set_type( t ) ;
        set_syntax( new String[]{ "", open_bracket, close_bracket } ) ;
        set_selector( SelectorLeftToRight.construct() ) ;
        set_stepper( StepperOpArraySubscript.construct() ) ;
    }
}


class StepperOpArraySubscript extends StepperBasic {

    private static StepperOpArraySubscript singleton ;
    
    static StepperOpArraySubscript construct() {
        if( singleton == null ) singleton = new StepperOpArraySubscript() ;
        return singleton ; }
        
    public AbstractDatum inner_step( ExpressionNode nd, VMState vms ) {
	 	// Get values of operands
	   	    Object xd = vms.top().at( nd.child_exp(0) ) ;
	   	    Assert.check( xd instanceof AbstractPointerDatum ) ;
	   	    AbstractPointerDatum pntrDatum = (AbstractPointerDatum) xd ;
	   	    AbstractDatum arrayAD = (AbstractDatum) pntrDatum.deref() ;
	   	    Assert.check( arrayAD instanceof AbstractArrayDatum ) ;
	   	    AbstractArrayDatum array = (AbstractArrayDatum) arrayAD ;
	   	    
	   	    Object yd = vms.top().at( nd.child_exp(1) ) ;
	   	    Assert.check( yd instanceof AbstractIntDatum ) ;
	   	    AbstractIntDatum ydi = (AbstractIntDatum) yd ;
	   	    int index = (int) ydi.getValue() ;
	    	    
	    // Check the bounds
        // TBD Note that this bounds checking is over exuberant, as
        // it catches  p == &a[N] in C++.
        // For java, of course, we really ought to throw an exception.
            long noe = array.getNumberOfElements() ;
            if( index < 0 || index >= noe ) {
                Assert.apology("Array index out of bounds"); }
                
        // Get the item
	        AbstractDatum item = (AbstractDatum) array.getElement( index ) ;
	        
	    // Construct a scratch reference
	    // Make a new reference in the scratch region.
            
		    Clc_ASTUtilities util
	    	    = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
	    	AbstractRefDatum ref
		        = (AbstractRefDatum) util.scratchDatum(nd.get_type(), vms) ;
		        
		    ref.putValue(item) ;
		     
		    String name = pntrDatum.getValueString()
		                + ((OpArraySubscript)nd).open_bracket
		                + index
		                + ((OpArraySubscript)nd).close_bracket ;
		    ref.putValueString( name ) ;
		
		// Done
		    return ref ; }
}