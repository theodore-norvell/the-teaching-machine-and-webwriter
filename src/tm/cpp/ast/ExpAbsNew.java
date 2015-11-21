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
import tm.clc.ast.NoExpNode;
import tm.clc.ast.SelectorAlways;
import tm.clc.ast.SelectorLeftToRight;
import tm.clc.ast.Stepper;
import tm.clc.ast.TyAbstractPointer;
import tm.clc.ast.TypeNode;
import tm.clc.datum.AbstractDatum;
import tm.clc.datum.AbstractIntDatum;
import tm.clc.datum.AbstractPointerDatum;
import tm.clc.datum.AbstractRefDatum;
import tm.utilities.Assert;
import tm.virtualMachine.ExpressionEvaluation;
import tm.virtualMachine.MemRegion;
import tm.virtualMachine.Store;
import tm.virtualMachine.VMState;

/** New operators */
abstract public class ExpAbsNew extends DefaultExpressionNode
{
    /*package*/ ExpressionNode initialization ;
    /*package*/ ExpressionNode stateDummy ;

    public ExpAbsNew( TypeNode t, ExpressionNode initialization )
    {
        super("ExpAbsNew") ;
        this.initialization = initialization ;
        this.stateDummy = new NoExpNode() ;
        set_type( t ) ;
        set_selector( SelectorAlways.construct() ) ;
        set_stepper( StepperAbsNew.construct() ) ;
    }

    public ExpAbsNew( TypeNode t, ExpressionNode size, ExpressionNode initialization )
    {
        super("ExpAbsNew", size) ;
        this.initialization = initialization ;
        this.stateDummy = new NoExpNode() ;
        set_type( t ) ;
        set_selector( SelectorLeftToRight.construct() ) ;
        set_stepper( StepperAbsNew.construct() ) ;
    }
}

class StepperAbsNew implements Stepper {

    private static StepperAbsNew singleton ;

    static StepperAbsNew construct() {
        if( singleton == null ) singleton = new StepperAbsNew() ;
        return singleton ; }

    public void step( ExpressionNode nd, VMState vms ) {
        ExpAbsNew nd1 = (ExpAbsNew) nd ;
        // The node shouldn't already be mapped.
            Assert.check( vms.top().at( nd ) == null ) ;
        // Clear the selection
		    vms.top().setSelected( null ) ;

		if( vms.top().at( nd1.stateDummy ) == null ) {
		// Step 0
	    	    Clc_ASTUtilities util
	                = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
	            TypeNode intType = util.getIntType() ;

		    // Get the types
		        Assert.check( nd1.get_type() instanceof TyAbstractPointer ) ;
    		    TyAbstractPointer ptr_type = (TyAbstractPointer) nd1.get_type() ;
	    	    TypeNode new_object_type = ptr_type.getPointeeType() ;

	    	// Is this an array allocation?
	    	    boolean isArrayAllocation = nd.childCount() == 1 ;
	    	    int count = 1;
	    	    if( isArrayAllocation ) {
	    	        AbstractIntDatum id = (AbstractIntDatum) vms.top().at( nd.child_exp(0) ) ;
	    	        count = (int) id.getValue() ;
	    	        TyArray tyArray = new TyArray() ;
	    	        tyArray.setNumberOfElements(count) ;
	    	        tyArray.addToEnd( new_object_type ) ;
	    	        new_object_type = tyArray ; }

    		// Find some space
    		    int sizeOfDatum = new_object_type.getNumBytes() ;
    		    int space ;
    		    if( isArrayAllocation ) space = sizeOfDatum + intType.getNumBytes() ;
    		    else space = sizeOfDatum ;

	    	    Store store = vms.getStore() ;
		        MemRegion heapRegion = store.getHeap() ;
		        int address ;
    		    try{ address = heapRegion.findSpace( space ) ; }
	    	    catch( tm.utilities.ApologyException e ) {
		            // NOTE if space can not be found, an apology is
		            // thrown.  This is not in accordance with C++ or
		            // Java semantics.  For C++, we should either
    		        // return 0 or throw a C++ exception depending on the
	    	        // placement options.
		            throw e ;
		        }

		    // If it is an array allocation then allocate an integer to hold the
		    // count.
		        if( isArrayAllocation ) {
		            AbstractIntDatum countDatum
		                = (AbstractIntDatum) intType.makeMemberDatum(vms, address, null, "") ;
		            store.addDatum( countDatum ) ;
		            countDatum.putValue( count ) ;
		            //System.out.println( "Putting " +count+ " at locn " +countDatum.getAddress()+
		            //                    " to " +(countDatum.getAddress()+countDatum.getNumBytes()-1)) ;
		            address += intType.getNumBytes() ; }


		    // Make the heap datum.
		        AbstractDatum new_object
		                = new_object_type.makeMemberDatum(vms, address, null, "") ;
		        store.addDatum( new_object ) ;

    		// Make a scratch pointer
	            AbstractPointerDatum ptr
	                = (AbstractPointerDatum) util.scratchDatum(ptr_type, vms) ;
    	        ptr.putValue( address ) ;

    		// Initialization
	    	if( nd1.initialization != null && count != 0 ) {

	            // Map the state dummy so that next time we are in at step 1
	            // We use the stateDummy also to pass the actual pointer
	            // value of the new expression on to step 1.
	                vms.top().map( nd1.stateDummy, ptr ) ;

		        // Put a reference to the new data
		        // in the vms as argument 0.
    		        AbstractRefDatum new_object_ref
	    	            = util.scratchRef( vms, new_object_type ) ;
	    	        new_object_ref.putValue( address ) ;
    		        vms.pushNewArgumentList() ;
        		    vms.addArgument( new_object_ref ) ;

	    	    // Push an evalautation for the initialization.
		            ExpressionEvaluation ee
		                = new ExpressionEvaluation(vms, nd1.initialization ) ;
    		        vms.push( ee ) ; }
	        else {
	            // We are done
	            // Map this node to the pointer.
	                vms.top().map(nd1, ptr) ;
	        }
	    }

        else {
            // Step 1

            vms.popArgumentList() ;
            Object ptr = vms.top().at( nd1.stateDummy ) ;
	        // Map this node to the pointer.
	            vms.top().map(nd1, ptr) ; }
	}
}