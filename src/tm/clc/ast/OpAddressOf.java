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

/** Address of. Convert a reference to a pointer.
  * The operand should evaluate to an AbstractReference.
  * The type must be an AbstractPointer. The type should
  * normally be a pointer to the same type. Implements C++'s
  * unary &. This node can also be used to implement a conversion,
  * implicit or explicit, from an array to a pointer; to do so,
  * use a reference to the array as the operand (typically an ExpId)
  * and for the type, use a pointer to the element type for the array.
  */
public class OpAddressOf extends DefaultExpressionNode
{
    public OpAddressOf ( TypeNode t,
                    String operator_image,
                    ExpressionNode operand)  {
        super("OpAddressOf", operand) ;
        set_type( t ) ;
        set_syntax( new String[]{ operator_image, "" } ) ;
        set_selector( SelectorLeftToRight.construct() ) ;
        set_stepper( StepperOpAddressOf.construct() ) ;
    }
}

class StepperOpAddressOf extends StepperBasic {

    private static StepperOpAddressOf singleton ;
    
    static StepperOpAddressOf construct() {
        if( singleton == null ) singleton = new StepperOpAddressOf() ;
        return singleton ; }
        
    public AbstractDatum inner_step( ExpressionNode nd, VMState vms ) {
	 	// Get values of operand
	   	    Object xd = vms.top().at( nd.child_exp(0) ) ;
	   	    Assert.check( xd instanceof AbstractRefDatum ) ;
	   	    AbstractRefDatum ref = (AbstractRefDatum) xd ;
	   	    
	    // Construct a scratch pointer
	    // Make a new reference in the scratch region.
            
		    Clc_ASTUtilities util
	    	    = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
	    	AbstractPointerDatum ptr
		        = (AbstractPointerDatum) util.scratchDatum(nd.get_type(), vms) ;
		        
		    ptr.putValue(ref.getValue()) ;
		
		// Done
		    return ptr ; }
}