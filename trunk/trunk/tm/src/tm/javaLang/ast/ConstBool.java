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
import tm.clc.ast.SelectorAlways;
import tm.clc.ast.StepperBasic;
import tm.clc.datum.AbstractDatum;
import tm.javaLang.datum.BooleanDatum;
import tm.virtualMachine.VMState;

/** ConstBool constants.
 boolean constants.
*/
public class ConstBool extends DefaultExpressionNode
{
    public ConstBool( boolean value ) {
        super("ConstBool") ;
        set_type(  TyBoolean.get() ) ;
        set_syntax( new String[]{ value ? "true" : "false" } ) ;
        set_selector( SelectorAlways.construct() ) ;
        set_stepper( new StepperConstBool( value ) ) ;
        set_integral_constant_value( value?1:0 ) ;
        setUninteresting( true ) ; }
}

class StepperConstBool extends StepperBasic {

        private boolean value ;

        StepperConstBool( boolean value ) { this.value = value ; }

        public AbstractDatum inner_step( ExpressionNode nd, VMState vms ) {

            // New datum on scratch
                Clc_ASTUtilities util = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
                BooleanDatum d = (BooleanDatum) util.scratchDatum(nd.get_type(), vms) ;
            // Give it a value
                d.putValue( this.value?1:0 ) ;

             return d ; }
}