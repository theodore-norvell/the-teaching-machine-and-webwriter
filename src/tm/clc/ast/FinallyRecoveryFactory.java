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

import tm.virtualMachine.AbruptCompletionStatus;
import tm.virtualMachine.Recovery;
import tm.virtualMachine.VMState;


/** 
 * @author Theodore Norvell
 * @version 1.0
 */

public class FinallyRecoveryFactory extends ClcRecoveryFactory {
    public FinallyRecoveryFactory() {
    }

    public Recovery makeRecovery( VMState vms ) {
        return new FinallyRecovery( vms ) ; }

    public String getDescription() {
        return "FinallyRecoveryFactory " ; }

    private class FinallyRecovery extends ClcRecovery {

        public FinallyRecovery( VMState vms ) {
            super( vms ) ;}

        public boolean canHandle(AbruptCompletionStatus acs) {
            return true ;
        }

        public void handle(AbruptCompletionStatus acs) {
            super.handle( acs );
            vms.startFinally( acs );
        }
    }
}