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
import tm.utilities.Assert;
import tm.virtualMachine.VMState;

/** Conversions between integers, floating point numbers, and pointers. */
public class OpArithmeticConversion extends DefaultExpressionNode
{
 
    public OpArithmeticConversion( TypeNode t,
                   String operator_image,
                   ExpressionNode operand ) {
        this(t, operator_image, "", operand ) ; }
        
    public OpArithmeticConversion( TypeNode t,
                   String pre_image,
                   String post_image,
                   ExpressionNode operand ) {
                   
        super("OpArithmeticConversion", operand) ;
        set_syntax( new String[]{pre_image, post_image } ) ;
        
        set_type( t ) ;
        set_selector( SelectorLeftToRight.construct() ) ;
        set_stepper( StepperOpArithmeticConversion.construct() ) ;
        
        // Compute constant value.
        if( operand.is_integral_constant() ) {
            set_integral_constant_value( operand.get_integral_constant_value() ) ; }
    }
}

class StepperOpArithmeticConversion extends StepperBasic {

    private static StepperOpArithmeticConversion singleton ;
    
    static StepperOpArithmeticConversion construct() {
        if( singleton == null ) singleton = new StepperOpArithmeticConversion() ;
        return singleton ; }
        
    public AbstractDatum inner_step( ExpressionNode nd, VMState vms ) {
    
	  	// Get value of the operand
	  	    Object xd = vms.top().at( nd.child_exp(0) ) ;
	  	    long long_value ;
	  	    double double_value ;
	  	    if( xd instanceof AbstractIntDatum ) {
	  	        AbstractIntDatum xid = (AbstractIntDatum) xd ;
	   	        long_value = xid.getValue() ;
	   	        double_value = (double) long_value ; }
	   	    else if( xd instanceof AbstractPointerDatum ) {
	  	        AbstractPointerDatum xpd = (AbstractPointerDatum) xd ;
	   	        long_value = xpd.getValue() ;
	   	        double_value = (double) long_value ; }
	   	    else if( xd instanceof AbstractFloatDatum ) {
	  	        AbstractFloatDatum xfd = (AbstractFloatDatum) xd ;
	   	        double_value = xfd.getValue() ;
	   	        long_value = (long) double_value ; }
	   	    else {
	   	        Assert.check(false) ; 
	   	        double_value = long_value = 0 ; }
	   	        
	   	// New datum on scratch
	        Clc_ASTUtilities util
	    	    = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
	    	AbstractDatum d = util.scratchDatum(nd.get_type(), vms) ;
	    	
	    // Give it a value
	    
	  	    if( d instanceof AbstractIntDatum ) {
	  	        AbstractIntDatum id = (AbstractIntDatum) d ;
	   	        id.putValue( long_value ) ; }
	   	    else if( d instanceof AbstractPointerDatum ) {
	  	        AbstractPointerDatum pd = (AbstractPointerDatum) d ;
	   	        pd.putValue( (int)long_value ) ; }
	   	    else if( d instanceof AbstractFloatDatum ) {
	  	        AbstractFloatDatum fd = (AbstractFloatDatum) d ;
	   	        fd.putValue( double_value ) ; }
	   	    else {
	   	        Assert.check(false) ; }
	    
	    return d ; }
}