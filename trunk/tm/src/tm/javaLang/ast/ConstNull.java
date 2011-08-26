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
import tm.javaLang.datum.NullDatum;
import tm.javaLang.datum.PointerDatum;
import tm.virtualMachine.VMState;

/** ConstNull constants.
 boolean constants.
*/

public class ConstNull extends DefaultExpressionNode
{
    public ConstNull( ) {
        super("ConstNull") ;
        set_type(  TyNull.get() ) ;
        set_syntax( new String[] { "null" } ) ;
        set_selector( SelectorAlways.construct() ) ;
        set_stepper( StepperConstNull.construct() ) ;
        setUninteresting( true ) ; }
}
        
class StepperConstNull extends StepperBasic {

    private static StepperConstNull singleton ;
    
    static StepperConstNull construct() {
        if( singleton == null ) singleton = new StepperConstNull() ;
        return singleton ; }
        
        private StepperConstNull() {}
        
        public AbstractDatum inner_step( ExpressionNode nd, VMState vms ) {
            
            // New datum on scratch
                Clc_ASTUtilities util = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
                NullDatum d = (NullDatum) util.scratchDatum(nd.get_type(), vms) ;
                d.putValue(0) ;
            
            return d ; }
}