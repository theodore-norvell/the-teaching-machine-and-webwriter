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

package tm.virtualMachine;

/**
 * <p>Title: The Teaching Machine</p>
 * <p>Description: </p>
 * <p>Company: Memorial University</p>
 * @author Theodore Norvell
 * @version 1.0
 */

public abstract class BasicRecovery implements Recovery {

    protected VMState vms ;

    private VMState.StackMarker marker ;

    public BasicRecovery(VMState vms) {
        this.vms = vms ;
        marker = vms.new StackMarker() ;
    }

    public void handle(AbruptCompletionStatus acs) {
        marker.restore();
    }
}