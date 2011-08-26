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
import tm.interfaces.Datum;
import tm.utilities.Assert;
import tm.virtualMachine.VMState;

/** Convert a reference to a datum
<P> Convert a reference datum to its underlying datum.
*/
public class ExpFetch extends DefaultExpressionNode
{
    /** Constructor.
        @param t The type of the node.
        Normally t.getPointeeType() is the type of the
        variable.  However if the variable is a
        itself of type TyAbstractRef (as in C++'s reference variables)
        then t should be the type of the reference variable.

        @param operand Should evaluate to a reference (TyAbstractRef).
    **/
    public ExpFetch( TypeNode t, ExpressionNode operand ) {

        super("ExpFetch", operand) ;
        set_type( t ) ;
        set_syntax( new String[]{ "", "" } ) ;
        set_selector( SelectorExpFetch.construct() ) ;
        set_stepper( StepperExpFetch.construct() ) ;

        // Compute constant value.
        if( operand.is_integral_constant() ) {
            set_integral_constant_value( operand.get_integral_constant_value() ) ; }
        if( operand.is_floating_constant() ) {
            set_floating_constant_value( operand.get_floating_constant_value() ) ; }
    }
}

class SelectorExpFetch implements Selector {
    private static SelectorExpFetch singleton ;

    static SelectorExpFetch construct() {
        if( singleton == null ) singleton = new SelectorExpFetch() ;
        return singleton ; }

    public void select(ExpressionNode nd, VMState vms) {
        // Get operand and its image.
            ExpressionNode operand = nd.child_exp(0)  ;
            Object obj = vms.top().at( operand ) ;

        if( obj == null ){

            // Child is unmapped
            // Recurse
                operand.select( vms ) ; }

        else {

           // Child is ripe. Highlight the Datum.
                Assert.check( obj instanceof AbstractRefDatum ) ;
                AbstractRefDatum ref = (AbstractRefDatum) obj ;
                AbstractDatum varDatum = (AbstractDatum) ref.deref() ;
                varDatum.putHighlight( Datum.HIGHLIGHTED ) ;

           // Select this one.
               vms.top().setSelected( nd ) ; }
    }
}

class StepperExpFetch extends StepperBasic {

    private static StepperExpFetch singleton ;

    static StepperExpFetch construct() {
        if( singleton == null ) singleton = new StepperExpFetch() ;
        return singleton ; }

    public AbstractDatum inner_step( ExpressionNode nd, VMState vms ) {

        // Get operand datum
            Object obj = vms.top().at( nd.child_exp(0) ) ;
            AbstractRefDatum ref = (AbstractRefDatum) obj ;

        // Find the var datum
            AbstractDatum varDatum = (AbstractDatum) ref.deref() ;

        // Unhighlight it
            varDatum.putHighlight( Datum.PLAIN ) ;

        return varDatum ;
    }
}