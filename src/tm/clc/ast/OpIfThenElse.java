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

/** The ?: operators of java and C++ */
public class OpIfThenElse extends DefaultExpressionNode
{
    
    /** Binary operations */
    OpIfThenElse( TypeNode t,
        String first_operator_image, 
        String second_operator_image,
        ExpressionNode first_operand,
        ExpressionNode second_operand,
        ExpressionNode third_operand )

    {
        super("OpIfThenElse", first_operand, second_operand, third_operand) ;
        set_syntax( new String[]{ "", " "+first_operator_image+" ", " "+second_operator_image+" ", "" } ) ;
        
        set_type( t ) ;
        set_selector( SelectorOpIfThenElse.construct() ) ;
        set_stepper( StepperOpIfThenElse.construct() ) ;
    }
}


class SelectorOpIfThenElse implements Selector
{
    private static SelectorOpIfThenElse singleton ;
    
    static SelectorOpIfThenElse construct() {
        if( singleton == null )
            singleton = new SelectorOpIfThenElse() ;
        return singleton ; }
    
    public void select(ExpressionNode nd, VMState vms) {
        
	  	Object xd = vms.top().at( nd.child_exp(0) ) ;
	  	Object yd = vms.top().at( nd.child_exp(1) ) ;
	  	Object zd = vms.top().at( nd.child_exp(2) ) ;
	  	if( yd!=null || zd!=null ) {
	  	    // Finished operands. Select this one.
	  	    vms.top().setSelected( nd ) ; }
        else if( xd == null ) {
            // First operand is not done.
            // Recurse on first operand
            nd.child_exp(0).select( vms ) ; }
        else {
            // Get the utilities object
                Clc_ASTUtilities util
	    	        = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
	    	
	  	    // Get value of first operand
	  	        Assert.check( xd instanceof AbstractIntDatum ) ;
	  	        AbstractIntDatum xdi = (AbstractIntDatum) xd ;
	  	        boolean xIsTrue = util.isTrue( xdi ) ;
	  	    
	  	    int nextChild = xIsTrue ? 1 : 2 ;
            nd.child_exp( nextChild ).select( vms ) ; }
        }
}

class StepperOpIfThenElse extends StepperBasic {

    private static StepperOpIfThenElse singleton ;
    
    static StepperOpIfThenElse construct() {
        if( singleton == null ) singleton = new StepperOpIfThenElse() ;
        return singleton ; }
        
    public AbstractDatum inner_step( ExpressionNode nd, VMState vms ) {
        // Get the utilities object
            Clc_ASTUtilities util
	    	    = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
	    	
	  	// Get value of left operand
	  	    AbstractDatum xd = (AbstractDatum) vms.top().at( nd.child_exp(0) ) ;
	  	    boolean xIsTrue = util.isTrue( xd ) ;
	  	
	  	int nextChild = xIsTrue ? 1 : 2 ;
	  	
	  	// Get value of the chosen child
	  	    Object o = vms.top().at( nd.child_exp(nextChild) ) ;
	  	
	  	// That's it.
	  	    return (AbstractDatum)o ; }
}