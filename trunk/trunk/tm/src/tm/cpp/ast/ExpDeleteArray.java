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
import tm.clc.ast.TyAbstractPointer;
import tm.clc.ast.TypeNode;
import tm.clc.datum.AbstractDatum;
import tm.clc.datum.AbstractIntDatum;
import tm.clc.datum.AbstractPointerDatum;
import tm.clc.datum.AbstractRefDatum;
import tm.interfaces.Datum;
import tm.utilities.Assert;
import tm.virtualMachine.MemRegion;
import tm.virtualMachine.Store;
import tm.virtualMachine.VMState;

/** Support for C++'s delete[] operator. */
public class ExpDeleteArray extends DefaultExpressionNode
{
    public ExpDeleteArray(TypeNode t,
        ExpressionNode operand )
    {
        super("ExpDeleteArray", operand) ;
        set_type( t ) ;
        set_syntax( new String[]{ "delete[] ", "" } ) ;
        set_selector( SelectorLeftToRight.construct() ) ;
        set_stepper( StepperExpDeleteArray.construct() ) ;
        setUninteresting( false ) ;
    }
}

class StepperExpDeleteArray extends StepperBasic {

    private static StepperExpDeleteArray singleton ;
    
    static StepperExpDeleteArray construct() {
        if( singleton == null ) singleton = new StepperExpDeleteArray() ;
        return singleton ; }
        
    public AbstractDatum inner_step( ExpressionNode nd, VMState vms ) {
        // Get values of operand
            Object xd = vms.top().at( nd.child_exp(0) ) ;
            Assert.check( xd instanceof AbstractPointerDatum ) ;
            AbstractPointerDatum ptr = (AbstractPointerDatum) xd ;
            
       // Obtain the int type for the language
            Clc_ASTUtilities util
                = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
            TypeNode intType = util.getIntType() ;
            
        if( ! ptr.isNull() ) {
        
            int startAddress = ptr.getValue() ;
            int addressOfSizeInt = startAddress - intType.getNumBytes() ;
            
            // Get the heap and check the pointer is legal
                Store store = vms.getStore() ;
                MemRegion heap = store.getHeap() ;
                int boHeap = heap.getAddress() ;
                int toHeap = boHeap+heap.getNumBytes() ;
                Assert.apology( boHeap <= addressOfSizeInt
                        && addressOfSizeInt < toHeap, 
                        "Pointer in delete not pointing to heap" ) ;
            // Create a reference to the int
                AbstractRefDatum refToInt = util.scratchRef(vms, intType) ;
                refToInt.putValue( addressOfSizeInt ) ;
            
            // Obtain the size int
                Datum yd = refToInt.deref() ;
                Assert.apology( yd != null && yd instanceof AbstractIntDatum,
                        "Heap corrupted or bad pointer value." ) ;
                AbstractIntDatum ydi = (AbstractIntDatum) yd ;
                long count = ydi.getValue() ;
                Assert.apology( count >= 0, "Heap corrupted or bad pointer value." ) ;
            
            // Remove the size int from the heap
                store.removeDatum(ydi) ;
                
            // Create a type representing "pointer to array of ... of ..."
                TypeNode ptrType = (TypeNode) ptr.getType() ;
                Assert.check( ptrType instanceof TyAbstractPointer ) ;
                TypeNode new_object_type = ((TyAbstractPointer)ptrType).getPointeeType() ;
               
                TyArray tyArray = new TyArray() ;
                tyArray.setNumberOfElements((int) count) ;
                tyArray.addToEnd( new_object_type ) ;
                TyPointer tyPointerArray = new TyPointer() ;
                tyPointerArray.addToEnd( tyArray ) ;
                
            // Create a scratch pointer
                AbstractPointerDatum tpd =
                    (AbstractPointerDatum) util.scratchDatum(tyPointerArray, vms) ;
                tpd.putValue( startAddress ) ;
            
            // Delete the datum
                Datum d = tpd.deref() ;
                Assert.apology( d != null,
                        "Heap corrupted or bad pointer value." ) ;
                store.removeDatum(d) ;
        }
            
        // Construct and return a void datum 
            TypeNode voidType = util.getVoidType() ;
            AbstractDatum voidd = util.scratchDatum(voidType, vms) ;
            return voidd ; }
}