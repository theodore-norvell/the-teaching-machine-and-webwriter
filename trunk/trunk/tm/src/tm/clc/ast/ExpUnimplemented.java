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

import tm.utilities.Assert;
import tm.virtualMachine.VMState;

public class ExpUnimplemented extends DefaultExpressionNode
{
    public ExpUnimplemented( TypeNode t, String message ) {
        super("unimplemented expression") ;
        set_type( t ) ;
        set_syntax( new String[]{ message } ) ;
        set_selector( SelectorAlways.construct() ) ;
        set_stepper( new StepperExpUnimplemented( message ) ) ; }
}
        
class StepperExpUnimplemented implements Stepper {
        private String message ;
        
        StepperExpUnimplemented( String message ) { this.message = message ; }
        
        public void step( ExpressionNode nd, VMState vms ) {
            // Aplogize profusely.
            Assert.apology( "Sorry. This expression is not yet implemented. "+message ) ;
        }
}