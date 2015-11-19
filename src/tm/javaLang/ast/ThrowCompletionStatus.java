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

import tm.interfaces.Datum;
import tm.utilities.Assert;
import tm.virtualMachine.AbruptCompletionStatus;
import tm.virtualMachine.VMState;

/**
 * <p>Title: The Teaching Machine</p>
 * <p>Description: </p>
 * <p>Company: Memorial University</p>
 * @author Theodore Norvell
 * @version 1.0
 */

public class ThrowCompletionStatus implements AbruptCompletionStatus {
    Datum thrownDatum ;
    public ThrowCompletionStatus( Datum thrownObject ) {
        this.thrownDatum = thrownObject ; }

    public void canNotBeHandled(VMState vMState) {
        Assert.error( "Throw with no catch. Type is "+thrownDatum.getTypeString() ) ;
    }
}