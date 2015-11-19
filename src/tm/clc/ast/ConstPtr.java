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
import tm.utilities.Assert;
import tm.virtualMachine.VMState;

/** Pointer constants.
*/
public class ConstPtr extends DefaultExpressionNode
{
    public ConstPtr( TypeNode t, String image, AbstractDatum pointee ) {
        this(t, image, pointee.getAddress() ) ; }
        
    public ConstPtr( TypeNode t, String image, int value ) {
        super("ConstPtr") ;
        set_type( t ) ;
        set_syntax( new String[]{ image } ) ;
        set_selector( SelectorAlways.construct() ) ;
        set_stepper( new StepperConstPtr( value ) ) ;
        setUninteresting( true ) ; }
}
        
class StepperConstPtr extends StepperBasic {

        private int value ;
        
        StepperConstPtr( int value ) { this.value = value ; }
        
        public AbstractDatum inner_step( ExpressionNode nd, VMState vms ) {
            
	    	// New datum on scratch
	    	    Clc_ASTUtilities util
	    	        = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
		        AbstractPointerDatum d
		            = (AbstractPointerDatum) util.scratchDatum(nd.get_type(), vms) ;
		    // Give it a value
		        d.putValue( this.value ) ;
		    
		     return d ; }
}