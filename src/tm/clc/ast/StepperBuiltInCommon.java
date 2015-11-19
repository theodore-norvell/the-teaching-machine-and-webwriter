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

/*
 * Created on May 31, 2005
 *
 */
package tm.clc.ast;

import java.util.Random;

import tm.clc.datum.AbstractDatum;
import tm.clc.datum.AbstractFloatDatum;
import tm.clc.datum.AbstractIntDatum;
import tm.interfaces.SourceCoords;
import tm.scripting.ScriptManager;
import tm.utilities.Assert;
import tm.virtualMachine.VMState;

/** Implement builtin functions common to Java and C++
 * @author theo and mpbl
 *
 */

/*
 * TODO The class could be refactored,
 * to cut down on the case statements (see RANDOM). Probably subclass
 * it or maybe use delegation. Would probably make a good example
 * for the design course.
 */

public abstract class StepperBuiltInCommon implements Stepper {


    public static final int
    
        OUTPUT = 0,
        PUT = 1,
        INPUT = 2,
        GET = 3,
        IABS = 4,
        FABS = 5,
        SIN = 6,
        COS = 7,
        TAN = 8,
        ASIN = 9,
        ACOS = 10,
        ATAN = 11,
        EXP = 12,
        LOG = 13,
        LOG10 = 14,
        CEIL = 15,
        FLOOR = 16,
        SQRT = 17,
        ATAN2 = 18,
        POW = 19, 
        FAIL = 20,
        EOF = 21 ,
        SIGNUM = 22 ,
        ROUND = 23,
        COSH = 24,
        SINH = 25,
        TANH = 26,
        EXPM1 = 27,
        LOG1P = 28,
        HYPOT = 29,
        FMAX = 30,
        FMIN = 31,
        IMAX = 32,
        IMIN = 33,
        TODEGS = 34,
        TORADS = 35,
        RANDOM = 36,
        RINT = 37,
        CBRT = 38,
        IEEEREM = 39,
        ULP_DOUBLE = 40,
        ULP_FLOAT = 41,
        RAND = 42,
        SRAND = 43,
        ASSERT = 44,
    
    // added 2007.04.09 by mpbl
        SCRIPT_RELAY = 90,
    	SCRIPT_SNAPSHOT = 91,
    // added 2008.05.09 by mpbl
	    SCRIPT_REFERENCE = 92,
		SCRIPT_COMPARE_REF = 93,
    // added 2009.03.04 by mpbl
    	SCRIPT_RELAY_RTN_INT = 94,
    	SCRIPT_RELAY_RTN_DOUBLE = 95;
	   	
    
    protected int op_code = -1 ;
    
    private static Random random = null ;
    
    public StepperBuiltInCommon( int op_code ) {
        this.op_code = op_code ; }
        
    /** 
     * @see tm.clc.ast.Stepper#step(tm.clc.ast.ExpressionNode, tm.virtualMachine.VMState)
     */
    public void step( ExpressionNode nd, VMState vms ) {
        switch( op_code ) {
        case OUTPUT : case PUT :
            output_step( nd, vms ) ;
        break ;
        
        case GET  : case INPUT : case FAIL : case EOF :
            input_step( nd, vms ) ;
        break ;
        
        case RANDOM :
            empty_to_float_step(nd, vms);
        break;
        
        case RAND :
            empty_to_int_step(nd, vms) ;
        break ;

        case ASSERT :
        case SRAND :
            int_to_void_step(nd, vms) ;
        break ;
            
        case IABS :
            int_to_int_step( nd, vms ) ;
        break ;
        
        case FABS : case SIN  : case COS : case TAN : case ASIN :
        case ACOS : case ATAN : case EXP  : case LOG  : case LOG10 :
        case CEIL : case FLOOR : case SQRT : case SIGNUM : case ROUND :
        case COSH : case SINH : case TANH : case TODEGS : case TORADS :
        case RINT : case CBRT : case ULP_DOUBLE : case ULP_FLOAT :
            float_to_float_step( nd, vms ) ;
        break ;
        
        case ATAN2 : case POW : case HYPOT : case FMAX : case FMIN :
        case IEEEREM :
            float_x_float_to_float_step( nd, vms ) ;
        break ;
        
        case IMAX : case IMIN :
            int_x_int_to_int_step( nd, vms ) ;
        break ;
        
        case SCRIPT_RELAY : case SCRIPT_SNAPSHOT :  // added 2007.04.09 by mpbl
        case SCRIPT_REFERENCE : case SCRIPT_COMPARE_REF :  // added 2008.05.09 by mpbl
        case SCRIPT_RELAY_RTN_INT : case SCRIPT_RELAY_RTN_DOUBLE : // added 2009.03.04 by mpbl
        	scriptStep(nd, vms);
        break;
        
        default: Assert.check( false ) ; }
    }

    abstract protected void output_step( ExpressionNode nd, VMState vms ) ;
    
    abstract protected void input_step( ExpressionNode nd, VMState vms ) ;

    private void empty_to_float_step( ExpressionNode nd, VMState vms ) {
        
            // The node shouldn't already be mapped.
                Assert.check( vms.top().at( nd ) == null ) ;
                    
            // Clear the selection
                vms.top().setSelected( null ) ;
                           
            // Compute the value
                double value ;
                switch( op_code ) {
                case RANDOM :
                    if( random == null ) random = new java.util.Random(1) ;
                    value = random.nextFloat()  ;
                break ;
                default: Assert.check(false) ; value = 0 ;
                }
                    
            AbstractFloatDatum d = putFloatResult(nd, vms, value);
            
            vms.top().map(nd, d) ;
        }

    private void empty_to_int_step( ExpressionNode nd, VMState vms ) {
        
        // The node shouldn't already be mapped.
            Assert.check( vms.top().at( nd ) == null ) ;
                
        // Clear the selection
            vms.top().setSelected( null ) ;
                       
        // Compute the value
            long value ;
            switch( op_code ) {
            case RAND :
                if( random == null ) random = new java.util.Random(1) ;
                value = random.nextInt() % 32767 ;
                if( value < 0 ) value = - value ;
            break ;
            default: Assert.check(false) ; value = 0 ;
            }
                
        putIntResult(nd, vms, value);
    }
    
    private void int_to_void_step( ExpressionNode nd, VMState vms ) {
        
            // The node shouldn't already be mapped.
                Assert.check( vms.top().at( nd ) == null ) ;
                    
            // Clear the selection
                vms.top().setSelected( null ) ;
                     
            // Get the operand value
                long x = getIntOperand( nd, vms, 0 ) ;
                
            // Compute the value
                switch( op_code ) {
                case SRAND :
                    if( random == null ) random = new java.util.Random(1) ;
                    random.setSeed( x ) ;
                case ASSERT :
                    if( x== 0 ) {
                        // Assertion tripped.
                        // The TM will throw an error.
                        SourceCoords sc = vms.getCurrentCoords() ;
                        vms.getConsole().putString("Assertion failed at "+sc.toString()+"\n") ;
                        vms.setEvaluationState( vms.EVALUATION_STATE_TERMINATED ) ;
                    }
                break ;
                default: Assert.check(false) ;
                }
/*                    
            // New datum on scratch
                Clc_ASTUtilities util
                    = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
                AbstractDatum d
                    =  util.scratchDatum(nd.get_type(), vms) ;
            
            vms.top().map(nd, d) ; */
                putVoidResult(nd, vms);
        }
    
    private void int_to_int_step( ExpressionNode nd, VMState vms ) {
    
        // The node shouldn't already be mapped.
            Assert.check( vms.top().at( nd ) == null ) ;
                
        // Clear the selection
            vms.top().setSelected( null ) ;
        
        // Get the operand value
            long x = getIntOperand( nd, vms, 0 ) ;
                
        // Compute the value
            Assert.check( op_code == IABS ) ;
            long value = x < 0 ? -x : x ;
                
        putIntResult(nd, vms, value ) ;
    }
            
    private void float_to_float_step( ExpressionNode nd, VMState vms ) {
    
        // The node shouldn't already be mapped.
            Assert.check( vms.top().at( nd ) == null ) ;
                
        // Clear the selection
            vms.top().setSelected( null ) ;
    
        // Get values of operands
            double x = getFloatOperand( nd, vms, 0 ) ;
                
        // Compute the value
            double value ;
            switch( op_code ) {
            case FABS :
                value = x < 0 ? -x : x ;
            break ;
            case SIN  :
                value = Math.sin(x) ;
            break ;
            case COS :
                value = Math.cos(x) ;
            break ;
            case TAN :
                value = Math.tan(x) ;
            break ;
            case ASIN :
                value = Math.asin(x) ;
            break ;
            case ACOS :
                value = Math.acos(x) ;
            break ;
            case ATAN :
                value = Math.atan(x) ;
            break ;
            case EXP  :
                value = Math.exp(x) ;
            break ;
            case EXPM1  :
                // This won't give as good a result as the real expm1
                // function.
                value = Math.exp(x-1.0) ;
            break ;
            case LOG  :
                value = Math.log(x) ;
            break ;
            case LOG10 :
                value = Math.log(x) / Math.log( 10 ) ;
            break ;
            case LOG1P :
                // This won't give as good a result as the real log1p
                // function.
                value = Math.log(x+1.0);
            break ;
            case CEIL :
                value = Math.ceil(x) ;
            break ;
            case FLOOR :
                value = Math.floor(x) ;
            break ;
            case SIGNUM :
                if(x == Double.NaN ) {
                    value = x ; }
                else if( x == 0.0 ) {
                    value = x ; }
                else if( x < 0 ) {
                    value = -1.0 ; }
                else {
                    value = +1.0 ; }
            break ;
            case ROUND :
                value = Math.round(x) ;
            break ;
            case SQRT :
                value = Math.sqrt(x) ;
            break ;
            case SINH  :
                value = (pow(Math.E, x) - pow(Math.E, -x)) / 2.0 ;
            break ;
            case COSH :
                value = (pow(Math.E, x) + pow(Math.E, -x)) / 2.0 ;
            break ;
            case TANH :
                value = (pow(Math.E, x) - pow(Math.E, -x))
                      / (pow(Math.E, x) + pow(Math.E, -x)) ;
            break ;
            case TODEGS :
                value = x * 57.2957795 ;
            break ;
            case TORADS :
                value = x / 57.2957795 ;
            break ;
            case RINT :
                value = Math.rint(x) ;
            break ;
            case CBRT :
                if( x == Double.NaN ) value = Double.NaN ;
                else if( x < 0 ) value = -pow( -x, 1.0/3.0);
                else value = pow(x,1.0/3.0) ;
            break ;
            case ULP_DOUBLE :
                if( x == Double.NaN )
                    value = Double.NaN ;
                else if( x == Double.NEGATIVE_INFINITY || x == Double.POSITIVE_INFINITY )
                    value = Double.POSITIVE_INFINITY ;
                else if( x == 0.0 )
                    value = Double.MIN_VALUE ;
                else {
                    /* TODO
                     * Fix this algorithm.
                     */
                    Assert.apology("ulp is not implemented") ;
                    value = 0.0 ; }
            break ;
            case ULP_FLOAT :
                // The assumption is that converting from a float
                // to a double loses nothing and is invertable.
                float xf = (float) x ; // Get back to the float.
                if( xf == Float.NaN )
                    value = Float.NaN ;
                else if( xf == Float.NEGATIVE_INFINITY || xf == Float.POSITIVE_INFINITY )
                    value = Float.POSITIVE_INFINITY ;
                else if( xf == 0.0 )
                    value = Float.MIN_VALUE ;
                else {
                    /* TODO
                     * Fix this algorithm.
                     */
                    Assert.apology("ulp is not implemented") ;
                    value = 0.0 ; }
            break ;
            default: Assert.check(false) ; value = 0 ;
            }
                
        AbstractFloatDatum d = putFloatResult(nd, vms, value);
        
        // NOTE: mpbl May 2008 I believe this is superfluous
        
        vms.top().map(nd, d) ;
    }

    private void int_x_int_to_int_step( ExpressionNode nd, VMState vms ) {
        
            // The node shouldn't already be mapped.
                Assert.check( vms.top().at( nd ) == null ) ;
                    
            // Clear the selection
                vms.top().setSelected( null ) ;
        
            // Get values of operands
                long x = getIntOperand( nd, vms, 0 ) ;
                long y = getIntOperand( nd, vms, 1 ) ;
                    
            // Compute the value
                long value ;
                switch( op_code ) {
                case IMAX  :
                    value = Math.max(x, y) ;
                break ;
                case IMIN  :
                    value = Math.min(x, y) ;
                break ;
                default: Assert.check(false) ; value = 0 ;
                }
                    
            putIntResult( nd, vms, value ) ;
        }

    private void float_x_float_to_float_step( ExpressionNode nd, VMState vms ) {
    
        // The node shouldn't already be mapped.
            Assert.check( vms.top().at( nd ) == null ) ;
                
        // Clear the selection
            vms.top().setSelected( null ) ;
    
        // Get values of operands
            double x = getFloatOperand( nd, vms, 0 ) ;
            double y = getFloatOperand( nd, vms, 1 ) ;
                
        // Compute the value
            double value ;
            switch( op_code ) {
            case ATAN2 :
                value = Math.atan2(x, y) ;
            break ;
            case POW  :
                // The following is not quite right for Java
                // because it will not throw an exception.
                value = pow(x, y);
            break ;
            case HYPOT  :
                    if( x == Double.POSITIVE_INFINITY
                     || x == Double.NEGATIVE_INFINITY
                     || y == Double.POSITIVE_INFINITY
                     || y == Double.NEGATIVE_INFINITY ) {
                        value = Double.POSITIVE_INFINITY ; }
                    else if( x == Double.NaN || y == Double.NaN ) {
                        value = Double.NaN ; }
                    else {
                        value = pow(x*x + y*y, 0.5) ; }
            break ;
            case FMAX  :
                value = Math.max(x, y) ;
            break ;
            case FMIN  :
                value = Math.min(x, y) ;
            break ;
            case IEEEREM  :
                value = Math.IEEEremainder(x, y) ;
            break ;
            default: Assert.check(false) ; value = 0 ;
            }
                
        AbstractFloatDatum d = putFloatResult(nd, vms, value);
    }
    
    private void to_string_step( ExpressionNode nd, VMState vms ){
    	
    }
    
    private void scriptStep( ExpressionNode nd, VMState vms ) {
        // The node shouldn't already be mapped.
        Assert.check( vms.top().at( nd ) == null ) ;
            
    // Clear the selection
        vms.top().setSelected( null ) ;
        Assert.check(nd instanceof OpAbsFuncCall);
        Assert.check( ((OpAbsFuncCall)nd).isStatic() );
        switch (op_code){
        case SCRIPT_RELAY: {
            ScriptManager.getManager().relayCall(nd,vms);
            putVoidResult( nd, vms) ;
            break; }
        case SCRIPT_RELAY_RTN_INT: {
            int n = ScriptManager.getManager().relayCallRtnInt(nd,vms);
            putIntResult( nd, vms, n) ;
            break; }
        case SCRIPT_RELAY_RTN_DOUBLE: {
            double x = ScriptManager.getManager().relayCallRtnDouble(nd,vms);
            putFloatResult( nd, vms,x) ;
            break; }
        case SCRIPT_SNAPSHOT: {
            ScriptManager.getManager().snapShot(nd,vms);
            putVoidResult( nd, vms) ;
            break; }          
        case SCRIPT_REFERENCE : {
            ScriptManager.getManager().setReference(nd,vms);
            putVoidResult( nd, vms) ;
            break; }            
        case SCRIPT_COMPARE_REF:{
            boolean value = ScriptManager.getManager().compareReference(nd,vms);
            putIntResult( nd, vms, value ? 1 : 0 ) ;
            break; }            
/*        case SCRIPT_COMPARE_DATUMS: {
            boolean value = ScriptManager.getManager().compareDatums(nd,vms);
            putIntResult( nd, vms, value ? 1 : 0 ) ;
            break; }   */        
        }
/* 
        // New datum on scratch
        Clc_ASTUtilities util
            = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
        AbstractDatum d
            =  util.scratchDatum(nd.get_type(), vms) ;
    
    vms.top().map(nd, d) ;
 */   	
    }
    
    private long getIntOperand( ExpressionNode nd, VMState vms, int operand ) {

        Object d = vms.top().at( nd.child_exp(operand) ) ;
        Assert.check( d instanceof AbstractIntDatum ) ;
        AbstractIntDatum id = (AbstractIntDatum) d ;
        return id.getValue() ;
    }
    
    private double getFloatOperand( ExpressionNode nd, VMState vms, int operand ) {

        Object d = vms.top().at( nd.child_exp(operand) ) ;
        Assert.check( d instanceof AbstractFloatDatum ) ;
        AbstractFloatDatum fd = (AbstractFloatDatum) d ;
        return fd.getValue() ;
    }

    /**
     * @param nd
     * @param vms
     * @param value
     */
    private void putIntResult(ExpressionNode nd, VMState vms, long value) {
        // New datum on scratch
            Clc_ASTUtilities util
                = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
            AbstractIntDatum d
                =(AbstractIntDatum) util.scratchDatum(nd.get_type(), vms) ;
        // Give it a value
            d.putValue( value ) ;
        
        vms.top().map(nd, d) ;
    }

    /**
     * @param nd
     * @param vms
     * @param value
     * @return
     */
    private AbstractFloatDatum putFloatResult(ExpressionNode nd, VMState vms, double value) {
        // New datum on scratch
            Clc_ASTUtilities util
                = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
            AbstractFloatDatum d
                =(AbstractFloatDatum) util.scratchDatum(nd.get_type(), vms) ;
        // Give it a value
            d.putValue( value ) ;
            
        // Map the node to the datum.
            vms.top().map(nd, d) ;
        return d;
    }
    
    /**
     * @param nd
     * @param vms
     */
    private void putVoidResult(ExpressionNode nd, VMState vms) {
	    // New datum on scratch
	    Clc_ASTUtilities util
	        = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
	    AbstractDatum d
	        =  util.scratchDatum(nd.get_type(), vms) ;
	    // Map the node to the datum.
		vms.top().map(nd, d) ;
    }


    /** Compute the x to the power y, returning NaN in case of exception.
     * @param x
     * @param y
     * @return
     */
    private double pow(double x, double y) {
        double value;
        try {
            value = Math.pow(x, y) ; }
        catch( ArithmeticException e ) {
            value = Double.NaN ; }
        return value;
    }
}
