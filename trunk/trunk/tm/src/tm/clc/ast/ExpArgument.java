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

import tm.clc.datum.AbstractDatum;
import tm.utilities.Assert;
import tm.virtualMachine.VMState;

/** Fetch an argument to this function. */
public class ExpArgument extends DefaultExpressionNode
{
    int number ;
    
    public ExpArgument( TypeNode t, int number) {
        super("ExpArgument") ;
        this.number = number ;
        set_type( t ) ;
        set_syntax( new String[]{ "arg"+number+"" } ) ;
        setUninteresting( true ) ;
        set_selector( SelectorAlways.construct() ) ;
        set_stepper( StepperExpArgument.construct() ) ; }
}

class StepperExpArgument extends StepperBasic {

    private static StepperExpArgument singleton ;
    
    static StepperExpArgument construct() {
        if( singleton == null ) singleton = new StepperExpArgument() ;
        return singleton ; }
        
    public AbstractDatum inner_step( ExpressionNode nd, VMState vms ) {
        int number = ((ExpArgument) nd).number ;
        
        AbstractDatum d = (AbstractDatum) vms.getArgument( number ) ;
        
        return d ; 
    }
}    