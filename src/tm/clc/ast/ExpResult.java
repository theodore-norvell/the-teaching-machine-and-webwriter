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
import tm.clc.datum.AbstractRefDatum;
import tm.utilities.Assert;
import tm.virtualMachine.VMState;

/** Obtain a reference to result datum for the current function invocation. */
public class ExpResult extends DefaultExpressionNode
{
    int number ;

    public ExpResult( TypeNode t ) {
        super("ExpResult") ;
        Assert.check( t instanceof TyAbstractRef ) ;
        set_type( t ) ;
        set_syntax( new String[]{ "result" } ) ;
        setUninteresting( true ) ;
        set_selector( SelectorAlways.construct() ) ;
        set_stepper( StepperExpResult.construct() ) ; }
}

class StepperExpResult extends StepperBasic {

    private static StepperExpResult singleton ;

    static StepperExpResult construct() {
        if( singleton == null ) singleton = new StepperExpResult() ;
        return singleton ; }

    public AbstractDatum inner_step( ExpressionNode nd, VMState vms ) {

        // Find the result datum.
        AbstractDatum functionResultDatum
            = (AbstractDatum) vms.topResultDatum() ;

        // Make a scratch reference datum.
        Clc_ASTUtilities util
                = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
        AbstractDatum d
                =(AbstractDatum) util.scratchDatum(nd.get_type(), vms) ;
        Assert.check( d instanceof AbstractRefDatum ) ;
        AbstractRefDatum dref = (AbstractRefDatum) d ;

        // Set the reference datum to point to the result datum
        dref.putValue( functionResultDatum ) ;
        dref.putValueString( "result" ) ;

        return dref ;
    }
}