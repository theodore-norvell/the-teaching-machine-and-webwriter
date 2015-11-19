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
import tm.clc.datum.AbstractPointerDatum;
import tm.utilities.Assert;
import tm.virtualMachine.VMState;

/** Pointer operations:
  * ptr+int, ptr-int, ptr-ptr and
  * comparisons ptr==ptr, ptr!=ptr, ptr<ptr, ptr>ptr, ptr<=ptr, ptr>=ptr*/
public class OpPointer extends DefaultExpressionNode
{

    /*package*/ int operator ;

    /** Binary operations */
    public OpPointer( TypeNode t,
                   int operator,
                   String operator_image,
                   ExpressionNode left_operand,
                   ExpressionNode right_operand ) {

        super("OpPointer", left_operand, right_operand) ;
        set_syntax( new String[]{ "", " "+operator_image+" ", "" } ) ;

        this.operator = operator ;
        set_type( t ) ;
        set_selector( SelectorLeftToRight.construct() ) ;
        set_stepper( StepperOpPointer2.construct() ) ;
    }

    public String formatNodeData() {
        return super.formatNodeData() +
               " #"+operator ;
    }
}

class StepperOpPointer2 extends StepperBasic {

    private static StepperOpPointer2 singleton ;

    static StepperOpPointer2 construct() {
        if( singleton == null ) singleton = new StepperOpPointer2() ;
        return singleton ; }

    public AbstractDatum inner_step( ExpressionNode nd, VMState vms ) {

        // Get values of operands
            Object xd = vms.top().at( nd.child_exp(0) ) ;
            Object yd = vms.top().at( nd.child_exp(1) ) ;

        // Compute the value
            int operator = ((OpPointer) nd).operator ;
            int value ;

            switch( operator ) {
            case Arithmetic.SUBTRACT_POINTER_POINTER : {
                Assert.check( xd instanceof AbstractPointerDatum ) ;
                AbstractPointerDatum xid = (AbstractPointerDatum) xd ;
                int x = xid.getValue() ;

                Assert.check( yd instanceof AbstractPointerDatum ) ;
                AbstractPointerDatum ypd = (AbstractPointerDatum) yd ;
                int y = ypd.getValue() ;

                int item_size = ((TyAbstractPointer)nd.child_exp(0).get_type())
                                .getPointeeType().getNumBytes() ;
                value = (int) Arithmetic.do_ptr_arith(operator, x, y, item_size ) ;
            break ;  }
            case Arithmetic.EQUAL :
            case Arithmetic.NOT_EQUAL :
            case Arithmetic.LESS :
            case Arithmetic.GREATER :
            case Arithmetic.LESS_OR_EQUAL :
            case Arithmetic.GREATER_OR_EQUAL : {
                Assert.check( xd instanceof AbstractPointerDatum ) ;
                AbstractPointerDatum xid = (AbstractPointerDatum) xd ;
                int x = xid.getValue() ;

                Assert.check( yd instanceof AbstractPointerDatum ) ;
                AbstractPointerDatum ypd = (AbstractPointerDatum) yd ;
                int y = ypd.getValue() ;

                int numberOfBytes = nd.get_type().getNumBytes() ;
                value = (int) Arithmetic.do_arith( numberOfBytes, operator, x, y ) ;
            break ;  }
            case Arithmetic.ADD_POINTER_INT:
            case Arithmetic.SUBTRACT_POINTER_INT : {
                int whichIsPointer = 0 ;
                if( yd instanceof AbstractPointerDatum ) {
                    Object t = yd ; yd = xd ; xd = t ;
                    whichIsPointer = 1 ;}
                Assert.check( xd instanceof AbstractPointerDatum ) ;
                AbstractPointerDatum xid = (AbstractPointerDatum) xd ;
                int x = xid.getValue() ;

                Assert.check( yd instanceof AbstractIntDatum ) ;
                AbstractIntDatum yid = (AbstractIntDatum) yd ;
                int y = (int) yid.getValue() ;

                int item_size = ((TyAbstractPointer)nd.child_exp(whichIsPointer).get_type())
                                .getPointeeType().getNumBytes() ;
                value = Arithmetic.do_ptr_arith( operator, x, y, item_size ) ; }
            break  ;
            default: Assert.check(false) ; value = 0 ; }

        // New datum on scratch
            Clc_ASTUtilities util
                = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
            switch( operator ) {
            case Arithmetic.SUBTRACT_POINTER_POINTER :
            case Arithmetic.EQUAL :
            case Arithmetic.NOT_EQUAL :
            case Arithmetic.LESS :
            case Arithmetic.GREATER :
            case Arithmetic.LESS_OR_EQUAL :
            case Arithmetic.GREATER_OR_EQUAL : {
                AbstractIntDatum d
                    =(AbstractIntDatum) util.scratchDatum(nd.get_type(), vms) ;
                d.putValue( value ) ;
              return d ; }
            default : {
                AbstractPointerDatum d
                    =(AbstractPointerDatum) util.scratchDatum(nd.get_type(), vms) ;
                d.putValue( value ) ;
              return d ; } }
    }
}



