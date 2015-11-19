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

package tm.cpp.ast;

import tm.clc.ast.Clc_ASTUtilities;
import tm.clc.ast.DefaultExpressionNode;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.SelectorLeftToRight;
import tm.clc.ast.StepperBasic;
import tm.clc.ast.TypeNode;
import tm.clc.datum.AbstractDatum;
import tm.clc.datum.AbstractPointerDatum;
import tm.interfaces.Datum;
import tm.utilities.Assert;
import tm.virtualMachine.MemRegion;
import tm.virtualMachine.Store;
import tm.virtualMachine.VMState;

/**Delete a heap item. */
public class ExpDelete extends DefaultExpressionNode
{
    public ExpDelete(TypeNode t, ExpressionNode operand ) {
        super("ExpDelete", operand) ;
        set_type( t ) ;
        set_syntax( new String[]{ "delete ", "" } ) ;
        set_selector( SelectorLeftToRight.construct() ) ;
        set_stepper( StepperExpDelete.construct() ) ;
        setUninteresting( false ) ; }
}

class StepperExpDelete extends StepperBasic {

    private static StepperExpDelete singleton ;
    
    static StepperExpDelete construct() {
        if( singleton == null ) singleton = new StepperExpDelete() ;
        return singleton ; }
        
    public AbstractDatum inner_step( ExpressionNode nd, VMState vms ) {
	 	// Get values of operand
	   	    Object xd = vms.top().at( nd.child_exp(0) ) ;
	   	    Assert.check( xd instanceof AbstractPointerDatum ) ;
	   	    AbstractPointerDatum ptr = (AbstractPointerDatum) xd ;
	   	    
	   	if( ! ptr.isNull() ) {
	   	
	   	    // Get the heap and check the pointer is legal
	   	        Store store = vms.getStore() ;
	   	        MemRegion heap = store.getHeap() ;
	   	        int boHeap = heap.getAddress() ;
	   	        int toHeap = boHeap+heap.getNumBytes() ;
	   	        Assert.apology( boHeap <= ptr.getValue()
	   	                && ptr.getValue() < toHeap, 
	   	                "Pointer in delete not pointing to heap" ) ;
	   	        Datum d = ptr.deref() ;
	   	        Assert.apology( d != null,
	   	                "Pointer in delete not pointing to anything" ) ;
	   	                
	   	    // Remove the datum
	   	        store.removeDatum(d) ;
	   	}
	   	    
	   	// Construct and return a void datum    
	    	Clc_ASTUtilities util
	                = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
	        TypeNode voidType = util.getVoidType() ;
	        AbstractDatum voidd = util.scratchDatum(voidType, vms) ;
		    return voidd ; }
}