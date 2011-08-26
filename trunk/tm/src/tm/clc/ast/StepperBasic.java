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
import tm.utilities.Assert;
import tm.virtualMachine.VMState;

public abstract class StepperBasic implements Stepper {

    public abstract AbstractDatum inner_step( ExpressionNode nd, VMState vms ) ;
    
    public void step( ExpressionNode nd, VMState vms ) {
        // The node shouldn't already be mapped.
            Assert.check( vms.top().at( nd ) == null ) ;
                
        // Clear the selection
      	    vms.top().setSelected( null ) ;
	    	    
	  	// What should it evaluate to?
	        AbstractDatum d = inner_step( nd, vms ) ;
		        
	    // Map the node to the datum
	        vms.top().map(nd, d) ; }
}