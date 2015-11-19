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
import tm.utilities.Assert;
import tm.virtualMachine.VMState;

/** A class for evaluating parentheses and other do-nothing
 * operators.
 */
public class OpParentheses extends DefaultExpressionNode
{
    /* The syntax is not fixed. So one could use this
     * for do-nothing operators such as the unary + in C++ .
     * 
     * 
     * @param t should be the same as its operands
     * @param open Normally "("
     * @param close Normally ")"
     * @param operand The operand expression.
     */
    public OpParentheses( TypeNode t,
                   String open, String close,
                   ExpressionNode operand ) {
        super("OpParentheses", operand) ;
        set_type( t ) ;
        set_syntax( new String[]{ open, close } ) ;
        set_selector( SelectorLeftToRight.construct() ) ;
        set_stepper( StepperOpParentheses.construct() ) ;
        
        // Compute constant value.
        if( operand.is_integral_constant() ) {
            set_integral_constant_value( operand.get_integral_constant_value() ) ; }
    }
}

class StepperOpParentheses extends StepperBasic {

    private static StepperOpParentheses singleton ;
    
    static StepperOpParentheses construct() {
        if( singleton == null ) singleton = new StepperOpParentheses() ;
        return singleton ; }
        
    public AbstractDatum inner_step( ExpressionNode nd, VMState vms ) {
	  	// Get value of operand
	   	    Object d = vms.top().at( nd.child_exp(0) ) ;
	   	    return (AbstractDatum) d ; }
}