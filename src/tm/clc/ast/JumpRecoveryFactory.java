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
import tm.virtualMachine.BasicRecovery;
import tm.virtualMachine.Recovery;
import tm.virtualMachine.VMState;

/** Make JumpRecovery objects
 * <p>Title: The Teaching Machine</p>
 * <p>Description: </p>
 * <p>Company: Memorial University</p>
 * @author Theodore Norvell
 * @version 1.0
 */

public class JumpRecoveryFactory extends ClcRecoveryFactory {
    private Object tag ;

    public JumpRecoveryFactory( Object tag ) {
        this.tag = tag ;
    }

    public Recovery makeRecovery( VMState vms ) {
        return new JumpRecovery( vms ) ; }

    public String getDescription() {
        return "JumpRecoveryFactory "+tag ; }

    private class JumpRecovery extends ClcRecovery {

        public JumpRecovery( VMState vms ) {
            super( vms ) ;}

        public boolean canHandle(AbruptCompletionStatus acs) {
            if( acs instanceof JumpCompletionStatus ) {
                Object jumpTag = ((JumpCompletionStatus)acs).tag ;
                return tag.equals( jumpTag ) ; }
            else {
               return false; }
        }
    }
}