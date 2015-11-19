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
import tm.utilities.Assert;
import tm.virtualMachine.VMState;

/** Integer constants.
  Can also be used for chars (in C/C++) and bools (in C/C++).
*/
public class ConstInt extends DefaultExpressionNode
{
    public ConstInt( TypeNode t, String image, long value ) {
        super("ConstInt") ;
        set_type( t ) ;
        set_syntax( new String[]{ image } ) ;
        set_selector( SelectorAlways.construct() ) ;
        set_stepper( new StepperConstInt( value ) ) ;
        set_integral_constant_value( value ) ;
        setUninteresting( true ) ; }
}
        
class StepperConstInt extends StepperBasic {

        private long value ;
        
        StepperConstInt( long value ) { this.value = value ; }
        
        public AbstractDatum inner_step( ExpressionNode nd, VMState vms ) {
            
	    	// New datum on scratch
	    	    Clc_ASTUtilities util
	    	        = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
		        AbstractIntDatum d
		            = (AbstractIntDatum) util.scratchDatum(nd.get_type(), vms) ;
		    // Give it a value
		        d.putValue( this.value ) ;
		    
		     return d ; }
}