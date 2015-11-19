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
import tm.clc.datum.AbstractIntDatum;
import tm.utilities.Assert;
import tm.virtualMachine.VMState;

/** Integer operations */
public class OpInt extends DefaultExpressionNode
{

    /*package*/ int operator ;

    /** Binary operations */
    public OpInt( TypeNode t,
                   int operator,
                   String operator_image,
                   ExpressionNode left_operand,
                   ExpressionNode right_operand ) {

        super("OpInt", left_operand, right_operand) ;
        set_syntax( new String[]{ "", " "+operator_image+" ", "" } ) ;

        this.operator = operator ;
        set_type( t ) ;
        set_selector( SelectorLeftToRight.construct() ) ;
        set_stepper( StepperOpInt2.construct() ) ;

        // Compute constant value.
        if( left_operand.is_integral_constant()
         && right_operand.is_integral_constant() ) {
            int numberOfBytes = t.getNumBytes() ;
            long val = Arithmetic.do_arith(
                                numberOfBytes,
                                operator,
                                left_operand.get_integral_constant_value(),
                                right_operand.get_integral_constant_value() ) ;
            set_integral_constant_value( val ) ; }
    }

    /** Unary operations */
    public OpInt( TypeNode t,
                   int operator,
                   String operator_image,
                   ExpressionNode operand ) {

        super("int op", operand) ;
        set_syntax( new String[]{operator_image, "" } ) ;

        this.operator = operator ;
        set_type( t ) ;
        set_selector( SelectorLeftToRight.construct() ) ;
        set_stepper( StepperOpInt1.construct() ) ;

        // Compute constant value.
        if( operand.is_integral_constant() ) {
            int numberOfBytes = t.getNumBytes() ;
            long val = Arithmetic.do_arith(
                                numberOfBytes,
                                operator,
                                operand.get_integral_constant_value() ) ;
            set_integral_constant_value( val ) ; }
    }

    public String formatNodeData() {
        return super.formatNodeData() +
               " #"+operator ;
    } ;
}

class StepperOpInt2 extends StepperBasic {

    private static StepperOpInt2 singleton ;

    static StepperOpInt2 construct() {
        if( singleton == null ) singleton = new StepperOpInt2() ;
        return singleton ; }

    public AbstractDatum inner_step( ExpressionNode nd, VMState vms ) {

        // Get values of operands
            Object xd = vms.top().at( nd.child_exp(0) ) ;
            Assert.check( xd instanceof AbstractIntDatum ) ;
            AbstractIntDatum xid = (AbstractIntDatum) xd ;
            long x = xid.getValue() ;

            Object yd = vms.top().at( nd.child_exp(1) ) ;
            Assert.check( yd instanceof AbstractIntDatum ) ;
            AbstractIntDatum yid = (AbstractIntDatum) yd ;
            long y = yid.getValue() ;

        // Compute the value
            int operator = ((OpInt) nd).operator ;
            int numberOfBytes = nd.get_type().getNumBytes() ;
            long value = Arithmetic.do_arith( numberOfBytes, operator, x, y ) ;

        // New datum on scratch
            Clc_ASTUtilities util
                = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
            AbstractIntDatum d
                =(AbstractIntDatum) util.scratchDatum(nd.get_type(), vms) ;
        // Give it a value
            d.putValue( value ) ;

        return d ; }
}



class StepperOpInt1 extends StepperBasic {

    private static StepperOpInt1 singleton ;

    static StepperOpInt1 construct() {
        if( singleton == null ) singleton = new StepperOpInt1() ;
        return singleton ; }

    public AbstractDatum inner_step( ExpressionNode nd, VMState vms ) {
        // Get values of operand
            Object xd = vms.top().at( nd.child_exp(0) ) ;
            Assert.check( xd instanceof AbstractIntDatum ) ;
            AbstractIntDatum xid = (AbstractIntDatum) xd ;
            long x = xid.getValue() ;

        // Compute the value
            int operator = ((OpInt) nd).operator ;
            int numberOfBytes = nd.get_type().getNumBytes() ;
            long value = Arithmetic.do_arith( numberOfBytes, operator, x ) ;

        // New datum on scratch
             Clc_ASTUtilities util
                = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
             AbstractIntDatum d
                = (AbstractIntDatum) util.scratchDatum(nd.get_type(), vms) ;
        // Give it a value
                d.putValue( value ) ;

        return d ; }
}