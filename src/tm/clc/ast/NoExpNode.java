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

/** This class is just a place holder used  when an ExpressionNode
is required, but no expression is actually available.
FOr example it is used in StatDo to indicate there is nothing to do.
*/

import tm.utilities.Assert;
import tm.virtualMachine.VMState;

public class NoExpNode extends ExpressionNode {

    public NoExpNode () { super("no expression") ; }

// Second constructor added 2003.07.30 by mpbl to allow creation of dummy typed nodes
    public NoExpNode(TypeNode type){
        this();
        set_type(type);
    }

    public String toString( VMState vms ) { return "no expression" ; }

    public void step(VMState vms) {
        Assert.check(false) ;
    }

    public void select(VMState vms) {
        Assert.check(false) ;
    }

}
