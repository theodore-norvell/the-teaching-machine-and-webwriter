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

package tm.javaLang.ast;

import tm.clc.ast.Clc_ASTUtilities;
import tm.clc.ast.DefaultExpressionNode;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.NoExpNode;
import tm.clc.ast.NodeList;
import tm.clc.ast.SelectorAlways;
import tm.clc.ast.Stepper;
import tm.clc.ast.TyAbstractPointer;
import tm.clc.ast.TypeNode;
import tm.clc.datum.AbstractDatum;
import tm.clc.datum.AbstractPointerDatum;
import tm.clc.datum.AbstractRefDatum;
import tm.utilities.Assert;
import tm.virtualMachine.ExpressionEvaluation;
import tm.virtualMachine.MemRegion;
import tm.virtualMachine.Store;
import tm.virtualMachine.VMState;


/** ExpNew Represent a java "new" expression (non array).
 * 
 * @author theo, jon, & michael b
 *
 */

public class ExpNew extends DefaultExpressionNode{
    NodeList initializer_args;
    ExpressionNode initialization;
    ExpressionNode stateDummy;

    /** Contructor
     * 
     * @param t The type of the resulting expression.
     * @param initializer_args The arguments to the constructor.
     * @param initialization The call to the constructor.
     */
    public ExpNew(TypeNode t, NodeList initializer_args,
                  ExpressionNode initialization) {
        super("ExpNew");
        Assert.check(initialization != null);
        this.initialization = initialization;
        this.stateDummy = new NoExpNode();
        set_type(t);
        set_selector(SelectorAlways.construct());
        set_stepper(StepperNew.construct());

        this.initializer_args = initializer_args;

        // Syntax
        // 0 compute a string for the type id.
        Assert.check(t instanceof TyAbstractPointer);
        TypeNode pointee = ((TyAbstractPointer)t).getPointeeType();
        String preSyntax = "new " + pointee.getTypeString();

        // 1 compute the syntax array
        String [] syntax = new String[initializer_args.length() + 1];
        if(initializer_args.isEmpty()) {
            syntax[0] = preSyntax + "()";
        } else {
            syntax[0] = preSyntax + "(";
            for(int i = 1; i < initializer_args.length(); ++i) {
             syntax[i] = ",";
            }
            syntax[initializer_args.length()] = ")";
        }

        set_syntax(syntax);
    }

    public String toString(VMState vms) {
        return toString(vms, initializer_args);
    }

    protected NodeList getChildrenForDump() {
        NodeList nl = new NodeList() ;
        for( int i=0 ; i<initializer_args.length() ; ++i )
            nl.addLastChild( initializer_args.get(i) ) ;
        nl.addLastChild( initialization );
        return nl ;
    }
}

class StepperNew implements Stepper {

    static StepperNew construct() {
        if(instance == null) instance = new StepperNew();
        return instance;
    }

    public void step(ExpressionNode nd, VMState vms) {
        ExpNew nd1 = (ExpNew) nd;
        // The node shouldn't already be mapped.
        Assert.check(vms.top().at(nd) == null);
        // Clear the selection
        vms.top().setSelected(null);

        if(vms.top().at(nd1.stateDummy) == null) {
            // Step 0. Allocate the datum and start initialization.
            Clc_ASTUtilities util
             = (Clc_ASTUtilities) vms.getProperty("ASTUtilities");

            // Get the types
            Assert.check(nd1.get_type() instanceof TyAbstractPointer);
            TyAbstractPointer ptr_type = (TyAbstractPointer) nd1.get_type();
            TypeNode new_object_type = ptr_type.getPointeeType();

            // Find some space
            int space = new_object_type.getNumBytes();

            Store store = vms.getStore();
            MemRegion heapRegion = store.getHeap();
            int address = 0;
            // TODO The following line could throw an exception.
            // Ideally we should do a garbage collection and
            // retry.
            address = heapRegion.findSpace(space);

            // Make the heap datum.
            AbstractDatum new_object
             = new_object_type.makeMemberDatum(vms, address, null, "");
            new_object.defaultInitialize() ;
            store.addDatum(new_object);

            // Make a scratch pointer
            AbstractPointerDatum ptr
             = (AbstractPointerDatum) util.scratchDatum(ptr_type, vms);
            ptr.putValue(address);

            // Initialization

            // Map the state dummy so that next time we are in at step 1.
            // We use the stateDummy also to pass the actual pointer
            // value of the new expression on to step 1.
            vms.top().map(nd1.stateDummy, ptr);

            // Put a reference to the new data
            // in the vms as argument 0.
            AbstractRefDatum new_object_ref
             = util.scratchRef(vms, new_object_type);
            new_object_ref.putValue(address);
            vms.pushNewArgumentList();
            vms.addArgument(new_object_ref);

            // Push an evalautation for the initialization.
            ExpressionEvaluation ee
             = new ExpressionEvaluation(vms, nd1.initialization);
            vms.push(ee);

        } else {
            // Step 1
            vms.popArgumentList();
            Object ptr = vms.top().at(nd1.stateDummy);
            // Map this node to the pointer.
            vms.top().map(nd1, ptr);
        }
    }

    private static StepperNew instance;
}