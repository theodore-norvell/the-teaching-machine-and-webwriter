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

import tm.clc.analysis.ScopedName;
import tm.clc.datum.AbstractDatum;
import tm.clc.datum.AbstractRefDatum;
import tm.utilities.Assert;
import tm.virtualMachine.VMState;

/** This is sort of the inverse of ExpFetch. It takes an
  * operand of type T and produces a reference of type
  * reference to T. This node is really only needed in a few
  * special circumstances. An example is in C++ to deal with E.mem
  * where E is an expression like a function call that evaluates
  * to a nonreference. Note that no copy is made of the operand,
  * so it is not suitable for passing rvalues to reference
  * parameters, except when the language permits this to be done
  * with no copy (e.g. in C++ when the object is class and the
  * reference parameter is const). */

public class OpConvertToReference extends DefaultExpressionNode
{
    public OpConvertToReference( TyAbstractRef t,
                             ExpressionNode operand )

    {
        super("OpConvertToReference", operand) ;
        set_type( t ) ;
        set_syntax( new String[]{ "", "" } ) ;
        setUninteresting( true ) ;
        set_selector( SelectorLeftToRight.construct() ) ;
        set_stepper( StepperConvertToReference.construct() ) ; }
}



class StepperConvertToReference extends StepperBasic {

    private static StepperConvertToReference singleton ;

    static StepperConvertToReference construct() {
        if( singleton == null ) singleton = new StepperConvertToReference() ;
        return singleton ; }

    public AbstractDatum inner_step( ExpressionNode nd, VMState vms ) {
        // Get value of operand
            AbstractDatum d = (AbstractDatum) vms.top().at( nd.child_exp(0) ) ;
            Assert.check( !(d instanceof AbstractRefDatum) ) ;

        // Make a new reference in the scratch region.
        // QUESTION: What string should be displayed?
            Clc_ASTUtilities util
                = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
            AbstractRefDatum ref = util.scratchRef( vms, d /*, ??>name???*/ ) ;

        // Done
            return ref ; }
}