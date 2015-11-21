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
import tm.clc.datum.AbstractFloatDatum;
import tm.clc.datum.AbstractIntDatum;
import tm.utilities.Assert;
import tm.virtualMachine.VMState;

/** Floating point operations */
public class OpFloat extends DefaultExpressionNode
{

    /*package*/ int operator ;

    /** Binary operations */
    public OpFloat( TypeNode t,
                   int operator,
                   String operator_image,
                   ExpressionNode left_operand,
                   ExpressionNode right_operand ) {

        super("OpFloat", left_operand, right_operand) ;
        set_syntax( new String[]{ "", " "+operator_image+" ", "" } ) ;

        this.operator = operator ;
        set_type( t ) ;
        set_selector( SelectorLeftToRight.construct() ) ;
        set_stepper( StepperOpFloat2.construct() ) ;
    }

    /** Unary operations */
    public OpFloat( TypeNode t,
                   int operator,
                   String operator_image,
                   ExpressionNode operand ) {

        super("float op", operand) ;
        set_syntax( new String[]{operator_image, "" } ) ;

        this.operator = operator ;
        set_type( t ) ;
        set_selector( SelectorLeftToRight.construct() ) ;
        set_stepper( StepperOpFloat1.construct() ) ;

    }

    public String formatNodeData() {
        return super.formatNodeData() +
               " #"+operator ;
    } ;
}

class StepperOpFloat2 extends StepperBasic {

    private static StepperOpFloat2 singleton ;

    static StepperOpFloat2 construct() {
        if( singleton == null ) singleton = new StepperOpFloat2() ;
        return singleton ; }

    public AbstractDatum inner_step( ExpressionNode nd, VMState vms ) {

        // Get values of operands
            Object xd = vms.top().at( nd.child_exp(0) ) ;
            Assert.check( xd instanceof AbstractFloatDatum ) ;
            AbstractFloatDatum xid = (AbstractFloatDatum) xd ;
            double x = xid.getValue() ;

            Object yd = vms.top().at( nd.child_exp(1) ) ;
            Assert.check( yd instanceof AbstractFloatDatum ) ;
            AbstractFloatDatum yid = (AbstractFloatDatum) yd ;
            double y = yid.getValue() ;

        // Make a datum
            Clc_ASTUtilities util
                = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
            AbstractDatum ad = util.scratchDatum(nd.get_type(), vms) ;

        // Compute the value
            int operator = ((OpFloat) nd).operator ;
            switch( operator ) {
            case Arithmetic.EQUAL :
            case Arithmetic.NOT_EQUAL :
            case Arithmetic.LESS :
            case Arithmetic.LESS_OR_EQUAL :
            case Arithmetic.GREATER :
            case Arithmetic.GREATER_OR_EQUAL : {
                long value = Arithmetic.do_comparison( operator, x, y ) ;
                AbstractIntDatum d = (AbstractIntDatum) ad ;
                // Give it a value
                d.putValue( value ) ;
                break ; }
            default: {
                double value = Arithmetic.do_arith( operator, x, y ) ;
                AbstractFloatDatum d = (AbstractFloatDatum) ad ;
                // Give it a value
                d.putValue( value ) ;  }
        }
        return ad ; }
}



class StepperOpFloat1 extends StepperBasic {

    private static StepperOpFloat1 singleton ;

    static StepperOpFloat1 construct() {
        if( singleton == null ) singleton = new StepperOpFloat1() ;
        return singleton ; }

    public AbstractDatum inner_step( ExpressionNode nd, VMState vms ) {
        // Get values of operand
            Object xd = vms.top().at( nd.child_exp(0) ) ;
            Assert.check( xd instanceof AbstractFloatDatum ) ;
            AbstractFloatDatum xid = (AbstractFloatDatum) xd ;
            double x = xid.getValue() ;

        // Compute the value
            int operator = ((OpFloat) nd).operator ;
            double value = Arithmetic.do_arith( operator, x ) ;

        // New datum on scratch
             Clc_ASTUtilities util
                = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
             AbstractFloatDatum d
                = (AbstractFloatDatum) util.scratchDatum(nd.get_type(), vms) ;
        // Give it a value
                d.putValue( value ) ;

        return d ; }
}