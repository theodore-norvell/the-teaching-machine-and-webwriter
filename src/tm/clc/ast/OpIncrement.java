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
import tm.clc.datum.AbstractPointerDatum;
import tm.clc.datum.AbstractRefDatum;
import tm.utilities.Assert;
import tm.virtualMachine.VMState;

/** Increment and decrement operators. */
public class OpIncrement extends DefaultExpressionNode
{
    boolean pre ;
    boolean up ;
    
    public OpIncrement( TypeNode t,
        boolean pre,
        boolean up,
        String operator_image,
        ExpressionNode operand )
  {
        super("OpIncrement", operand) ;
        set_type( t ) ;
        if( pre ) set_syntax( new String[]{ operator_image, "" } ) ;
        else      set_syntax( new String[]{ "", operator_image } ) ;
        this.pre = pre ;
        this.up = up ;
        set_selector( SelectorLeftToRight.construct() ) ;
        set_stepper( StepperOpIncrement.construct() ) ;
    }
}

class StepperOpIncrement extends StepperBasic {

    private static StepperOpIncrement singleton ;
    
    static StepperOpIncrement construct() {
        if( singleton == null ) singleton = new StepperOpIncrement() ;
        return singleton ; }
        
    public AbstractDatum inner_step( ExpressionNode nd, VMState vms ) {
        boolean pre = ((OpIncrement)nd).pre ;
        boolean up = ((OpIncrement)nd).up ;
        
        // Get values of operand
            Object xd = vms.top().at( nd.child_exp(0) ) ;
            Assert.check( xd instanceof AbstractRefDatum ) ;
            AbstractRefDatum ref = (AbstractRefDatum) xd ;
            AbstractDatum d = (AbstractDatum) ref.deref() ;
        
        // What is the type of d
        if( d instanceof AbstractIntDatum ) {
            AbstractIntDatum id = (AbstractIntDatum) d ; 
            long value = id.getValue() ;
            long newValue = value + (up ? 1 : -1) ;
            id.putValue(newValue) ;
            if( pre ) {
                return id ; }
            else {
                Clc_ASTUtilities util
                = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
                AbstractIntDatum res
                    = (AbstractIntDatum) util.scratchDatum(nd.get_type(), vms) ;
                res.putValue( value ) ;
                return res ; } }
       else if( d instanceof AbstractFloatDatum ) {
            AbstractFloatDatum fd = (AbstractFloatDatum) d ; 
            double value = fd.getValue() ;
            double newValue = value + (up ? 1.0 : -1.0) ;
            fd.putValue(newValue) ;
            if( pre ) {
                return fd ; }
            else {
                Clc_ASTUtilities util
                = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
                AbstractFloatDatum res
                    = (AbstractFloatDatum) util.scratchDatum(nd.get_type(), vms) ;
                res.putValue( value ) ;
                return res ; } }
       else if( d instanceof AbstractPointerDatum ) {
            AbstractPointerDatum pd = (AbstractPointerDatum) d ; 
            int value = pd.getValue() ;
            TypeNode pointeeType = (TypeNode) pd.getPointeeType() ;
            int size = pointeeType.getNumBytes() ;
            int newValue  = value + (up ? size : -size) ; ;
            pd.putValue(newValue) ;
            if( pre ) {
                return pd ; }
            else {
                Clc_ASTUtilities util
                = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
                AbstractPointerDatum res
                    = (AbstractPointerDatum) util.scratchDatum(nd.get_type(), vms) ;
                res.putValue( value ) ;
                return res ; } }
        else {
            Assert.check( false ) ; return null ; }
    }
}