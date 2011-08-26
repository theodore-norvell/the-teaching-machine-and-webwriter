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

/** AND and OR operations */
public class OpLogical extends DefaultExpressionNode
{

    /*package*/ boolean stoppingValue ;
    
    /** Binary operations */
    public OpLogical( TypeNode t,
        int operator,
        String operator_image,
        ExpressionNode left_operand,
        ExpressionNode right_operand )
    {
                   
        super("OpLogical", left_operand, right_operand) ;
        set_syntax( new String[]{ "", " "+operator_image+" ", "" } ) ;
        
        Assert.check( operator == Arithmetic.BOOLEAN_AND
                    || operator == Arithmetic.BOOLEAN_OR ) ;
        stoppingValue = operator == Arithmetic.BOOLEAN_OR ;
        set_type( t ) ;
        set_selector( SelectorOpLogical.construct() ) ;
        set_stepper( StepperOpLogical.construct() ) ;
    }
}


class SelectorOpLogical implements Selector
{
    private static SelectorOpLogical singleton ;
    
    static SelectorOpLogical construct() {
        if( singleton == null ) singleton = new SelectorOpLogical() ;
        return singleton ; }
    
    public void select(ExpressionNode nd, VMState vms) {
        if( vms.top().at(nd.child_exp(0)) == null ) {
            // Neither operand is done.
            // Recurse on first operand
            nd.child_exp(0).select( vms ) ; }
        else {
            // Get the utilities object
                Clc_ASTUtilities util
	    	        = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
	    	
	  	    // Get value of left operand
	  	        Object xd = vms.top().at( nd.child_exp(0) ) ;
	  	        Assert.check( xd instanceof AbstractIntDatum ) ;
	  	        AbstractIntDatum xdi = (AbstractIntDatum) xd ;
	  	        boolean leftIsTrue = util.isTrue( xdi ) ;
	  	    
	  	    boolean stoppingValue = ((OpLogical)nd).stoppingValue ;
	  	    if( leftIsTrue == stoppingValue ) {
	  	        // Time to stop and execute the node.
	  	        vms.top().setSelected( nd ) ; }
	  	        
	  	    else if( vms.top().at(nd.child_exp(1)) == null ) {
                // Second operand is needed
                nd.child_exp(1).select( vms ) ; }
	  	        
	  	    else  {
                // Both operands are evaluated. Compute final result.
	  	        vms.top().setSelected( nd ) ; } } }
}

class StepperOpLogical extends StepperBasic {

    private static StepperOpLogical singleton ;
    
    static StepperOpLogical construct() {
        if( singleton == null ) singleton = new StepperOpLogical() ;
        return singleton ; }
        
    public AbstractDatum inner_step( ExpressionNode nd, VMState vms ) {
        // Get the utilities object
            Clc_ASTUtilities util
	    	    = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
	    	
	  	// Get value of left operand
	  	    AbstractDatum xd = (AbstractDatum) vms.top().at( nd.child_exp(0) ) ;
	  	    boolean leftIsTrue = util.isTrue( xd ) ;
	  	    
	  	if( leftIsTrue == ((OpLogical)nd).stoppingValue ) {
	  	    return util.scratchBoolean(vms, nd.get_type(), leftIsTrue) ; }
	  	else {
	  	    // Get value of right operand
	  	        AbstractDatum yd = (AbstractDatum) vms.top().at( nd.child_exp(1) ) ;
	  	    // It should be mapped by now!
	  	        Assert.apology(yd != null,"Sorry internal error in logical op");
	  	    boolean rightIsTrue = util.isTrue( yd ) ;
	  	    return util.scratchBoolean(vms, nd.get_type(), rightIsTrue) ; }
	}
}
