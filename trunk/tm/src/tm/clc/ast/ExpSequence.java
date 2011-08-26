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
import tm.virtualMachine.VMState;

/** A sequence of expressions as in the comma operator. */

public class ExpSequence extends DefaultExpressionNode
{
    public ExpSequence( TypeNode t,
        String operator_image,
        ExpressionNode left_operand,
        ExpressionNode right_operand )
    {
                   
        super("ExpSequence", left_operand, right_operand) ;
        set_syntax( new String[]{ "", " "+operator_image+" ", "" } ) ;
        
        set_type( t ) ;
        set_selector( SelectorLeftToRight.construct() ) ;
        set_stepper( StepperExpSequence.construct() ) ;
    }
}

class StepperExpSequence extends StepperBasic {

    private static StepperExpSequence singleton ;
    
    static StepperExpSequence construct() {
        if( singleton == null ) singleton = new StepperExpSequence() ;
        return singleton ; }
        
    public AbstractDatum inner_step( ExpressionNode nd, VMState vms ) {
        // Return value of right operand
	  	    return (AbstractDatum) vms.top().at( nd.child_exp(1) ) ;
	}
}