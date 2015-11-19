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

import tm.clc.rtSymTab.RT_Symbol_Table;
import tm.utilities.Assert;
import tm.virtualMachine.AbruptCompletionStatus;
import tm.virtualMachine.BasicRecovery;
import tm.virtualMachine.RecoveryFactory;
import tm.virtualMachine.VMState;

/** Make a Recovery that has a link to the next statement
 * <p>Title: The Teaching Machine</p>
 * <p>Description: </p>
 * <p>Company: Memorial University</p>
 * @author Theodore Norvell
 * @version 1.0
 */

public abstract class ClcRecoveryFactory implements RecoveryFactory {
    StatementNodeLink nextLink  = new StatementNodeLink() ;

    public StatementNodeLink next() {
        return nextLink ; }

    abstract public String getDescription() ;

    protected abstract class ClcRecovery extends BasicRecovery {

        private int savedFunctionCallDepth ;

        public ClcRecovery( VMState vms ) {
            super( vms ) ;
            RT_Symbol_Table symtab = (RT_Symbol_Table) vms.getSymbolTable() ;
            savedFunctionCallDepth = symtab.getFunctionCallDepth() ; }

        public void handle(AbruptCompletionStatus acs) {
            // Get the symbol table
                RT_Symbol_Table symtab = (RT_Symbol_Table) vms.getSymbolTable() ;

            // Exit functions
                int numberToPop = symtab.getFunctionCallDepth() - savedFunctionCallDepth;
                Assert.check( numberToPop >= 0  ) ;
                for( ; numberToPop > 0 ; --numberToPop ) {
                    // Kill all local variables
                        symtab.trimVariables( 0, vms );
                    // Remove the function's frame(s) from the symbol table.
                        symtab.exitFunction() ;
                    // Unset the stack mark
                       vms.getStore().unsetStackMark(); }

            // Pop the VMState back to where it was
                super.handle( acs );

            // Select the next node
                vms.top().setSelected( nextLink.get() ) ;
        }
    }
}