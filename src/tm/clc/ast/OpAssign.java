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

/** Byte for byte copy from one datum to another.
First operand must be a reference datum.  The datum it
refers to is called the target.
Second operand may be any kind of datum and is called the source.
The source and target do not need to have the same types
or even sizes. The copy is made byte for byte from the
start (lowest address byte).  Left-over bytes of the source
are ignored and left-over bytes of the target are zero
filled.  The result is the first operand, so it can be used
in C++ constructs such as (a = 0) = 1 ;
*/
public class OpAssign extends DefaultExpressionNode
{

    /** Constructor
     * @param t This should be the same as the type of the first operand
     * @param operator_image The operator as a string. E.g. "=" in C++.
     * @param left_operand This expression should evaluate to a reference to the target.
     * @param right_operand This expression should evaluate to the source.
     */
    public OpAssign( TypeNode t,
                      String operator_image,
                      ExpressionNode left_operand,
                      ExpressionNode right_operand ) {
        this("OpAssign", t, operator_image,
             left_operand, right_operand) ; }

    /** Constructor
     * @param node_name The name of the node
     * @param t This should be the same as the type of the first operand
     * @param operator_image The operator as a string. E.g. "=" in C++.
     * @param left_operand This expression should evaluate to a reference to the target.
     * @param right_operand This expression should evaluate to the source.
     */
   protected OpAssign( String node_name,
                      TypeNode t,
                      String operator_image,
                      ExpressionNode left_operand,
                      ExpressionNode right_operand ) {
        super(node_name, left_operand, right_operand) ;
        set_type( t ) ;
        set_syntax( new String[]{ "", " "+operator_image+" ", "" } ) ;
        set_selector( SelectorOpAssign.construct_selector_op_assign() ) ;
        set_stepper( StepperOpAssign.construct() ) ; }
}

class SelectorOpAssign extends SelectorLeftToRight {

    private static SelectorOpAssign singleton ;

    static SelectorOpAssign construct_selector_op_assign() {
        if( singleton == null ) singleton = new SelectorOpAssign() ;
        return singleton ; }

    public void select(ExpressionNode nd, VMState vms) {
        super.select( nd, vms ) ;

        if( vms.top().getSelected() == nd ) {
            // Find the target
                Object xd = vms.top().at( nd.child_exp(0) ) ;
                Assert.check( xd instanceof AbstractRefDatum ) ;
                AbstractRefDatum xrd = (AbstractRefDatum) xd ;
                AbstractDatum target = (AbstractDatum) xrd.deref() ;
            // Highlight it
                target.putHighlight( Datum.HIGHLIGHTED ) ; }
    }
}

class StepperOpAssign extends StepperBasic {

    private static StepperOpAssign singleton ;

    static StepperOpAssign construct() {
        if( singleton == null ) singleton = new StepperOpAssign() ;
        return singleton ; }

        public AbstractDatum inner_step( ExpressionNode nd, VMState vms ) {
            // Get values of operands
                Object xd = vms.top().at( nd.child_exp(0) ) ;
                Assert.check( xd instanceof AbstractRefDatum ) ;
                AbstractRefDatum refDatum = (AbstractRefDatum) xd ;
                AbstractDatum target
                    = (AbstractDatum) refDatum.deref() ;

                AbstractDatum source
                    = (AbstractDatum) vms.top().at( nd.child_exp(1) ) ;

            // Unhighlight the target
                target.putHighlight( Datum.PLAIN ) ;

            // Copy the bytes with 0 fill if we run out.
                int source_size = source.getNumBytes() ;
                for( int i=0, sz = target.getNumBytes() ; i<sz ; ++i ) {
                    if( i < source_size ) {
                        target.putByte(i, source.getByte(i)) ; }
                    else {
                        target.putByte(i, 0 ) ; } }

            // If we are assigning to a reference, then we should
            // update the name of the pointee.
                if( target instanceof AbstractRefDatum && source instanceof AbstractRefDatum ) {
                    AbstractRefDatum refTarget = (AbstractRefDatum) target ;
                    AbstractRefDatum refSouce = (AbstractRefDatum) source ;
                    refTarget.putValueString( refSouce.getValueString() ) ; }

            return refDatum ; }
}