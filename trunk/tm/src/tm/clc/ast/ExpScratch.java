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
import tm.clc.datum.AbstractRefDatum;
import tm.utilities.Assert;
import tm.virtualMachine.ExpressionEvaluation;
import tm.virtualMachine.VMState;

/** Create and initialize a temporary datum */
public class ExpScratch extends DefaultExpressionNode
{
    /*package*/ ExpressionNode initialization ;
    /*package*/ ExpressionNode stateDummy ;
    /**
	 * Builds a scratch datum of type t.getPointeeType () and initializes it
	 * using the initialization expression
	 */
    public ExpScratch ( TyAbstractRef t, ExpressionNode initialization )
    {
        super("ExpScratch") ;
        this.initialization = initialization ;
        this.stateDummy = new NoExpNode() ;
        set_type( t ) ;
        set_selector( SelectorAlways.construct() ) ;
        set_stepper( StepperScratch.construct() ) ;
		set_syntax (new String [] {"", ""});
    }
}
		    
class StepperScratch implements Stepper {

    private static StepperScratch singleton ;
    
    static StepperScratch construct() {
        if( singleton == null ) singleton = new StepperScratch () ;
        return singleton ; }
        
    public void step( ExpressionNode nd, VMState vms ) {
        ExpScratch nd1 = (ExpScratch) nd ;
        // The node shouldn't already be mapped.
            Assert.check( vms.top().at( nd ) == null ) ;
        // Clear the selection
		    vms.top().setSelected( null ) ;
		    
		if( vms.top().at( nd1.stateDummy ) == null ) {
		// Step 0
			Clc_ASTUtilities util
				= (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
	            
			// Get the types
			Assert.check( nd1.get_type() instanceof TyAbstractRef ) ;
			TyAbstractRef ref_type = (TyAbstractRef) nd1.get_type() ;
			TypeNode new_object_type = ref_type.getPointeeType() ;
			
			// create a scratch datum
			AbstractDatum obj_d = util.scratchDatum (new_object_type, vms);

			// create a scratch reference to the scratch datum
			AbstractRefDatum ref_d = util.scratchRef (vms, obj_d) ;

			// map the state dummy to the scratch reference
			vms.top().map (nd1.stateDummy, ref_d);

    		// Initialization
	    	Assert.check ( nd1.initialization != null);
			// push a new argument list and add the scratch reference
			vms.pushNewArgumentList() ;
			vms.addArgument( ref_d ) ;
		    
			// Push an evaluation for the initialization.
			ExpressionEvaluation ee 
				= new ExpressionEvaluation(vms, nd1.initialization ) ;
			vms.push( ee ) ; 
	    }
	                
        else { // the dummy is mapped
            // Step 1
            // pop the argument list
            vms.popArgumentList() ;
            Object sd_val = vms.top().at( nd1.stateDummy ) ;
	        // Map this node to the value the state dummy is mapped to.
	            vms.top().map(nd1, sd_val) ; }
	}
}
