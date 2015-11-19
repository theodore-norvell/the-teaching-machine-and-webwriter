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

package tm.javaLang.ast;


import tm.clc.ast.Clc_ASTUtilities;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.Stepper;
import tm.clc.ast.StepperBuiltInCommon;
import tm.clc.datum.AbstractDatum;
import tm.clc.datum.AbstractFloatDatum;
import tm.clc.datum.AbstractIntDatum;
import tm.javaLang.datum.CharDatum;
import tm.javaLang.datum.DatumUtilities;
import tm.javaLang.datum.IntDatum;
import tm.javaLang.datum.ObjectDatum;
import tm.javaLang.datum.PointerDatum;
import tm.javaLang.datum.RefDatum;
import tm.interfaces.Datum;
import tm.utilities.Assert;
import tm.virtualMachine.Console;
import tm.virtualMachine.Memory;
import tm.virtualMachine.VMState;

public class Java_StepperBuiltIn extends StepperBuiltInCommon {
	// Java only stepper ops
    public static final int
		MATCHES = 100,     // native String methods
		REPLACEALL = 101,
		REPLACEFIRST = 102,
		SPLIT = 103,
		VALUEOF = 104;


    public Java_StepperBuiltIn( int op_code ) {
        super( op_code ) ; }
        
    // TODO Make sure this is right.
    // TODO Factor out comminallity with C++
    protected void output_step( ExpressionNode nd, VMState vms ) {
    
        // The node shouldn't already be mapped.
            Assert.check( vms.top().at( nd ) == null ) ;
                
        // Clear the selection
            vms.top().setSelected( null ) ;
            
        AbstractDatum leftDatum = (AbstractDatum) vms.top().at( nd.child_exp(0) ) ;
        AbstractDatum rightDatum = (AbstractDatum) vms.top().at( nd.child_exp(1) ) ;
        
//        Assert.check( leftDatum instanceof PointerDatum ) ;
//        Assert.check( ((PointerDatum)leftDatum).deref() != null ) ;
//        Assert.check( ((PointerDatum)leftDatum).deref() instanceof ObjectDatum ) ;
        
        rightDatum.output( vms ) ;
        Clc_ASTUtilities util = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
        Datum voidDatum = util.scratchDatum( TyVoid.get(), vms) ;
        vms.top().map(nd, voidDatum) ;
    }

    
    protected void input_step( ExpressionNode nd, VMState vms ) {
    
        // The node shouldn't already be mapped.
            Assert.check( vms.top().at( nd ) == null ) ;
                
        // Clear the selection
            vms.top().setSelected( null ) ;
        
        AbstractDatum leftDatum = (AbstractDatum) vms.top().at( nd.child_exp(0) ) ;
        
//        Assert.check( leftDatum instanceof RefDatum ) ;
//        Assert.check( ((RefDatum)leftDatum).deref() != null ) ;
//        Assert.check( ((RefDatum)leftDatum).deref() instanceof ObjectDatum ) ;

        Console console = vms.getConsole() ;
       
        long value ;
        boolean valueDetermined ;
        if( console.eof() ) {
            value = -1 ;
            valueDetermined = true ; }
        else {
            char inputChar = console.peekChar(0) ;
            if( inputChar == '\uffff' ) {
                // There is no more input. Stop the
                // evaluation so the user can add input.
                vms.setEvaluationState( vms.EVALUATION_STATE_NEEDINPUT ) ;
                value = 0 ;
                valueDetermined = false ; }
            else if( inputChar == 0 ) {
                console.setFailBit() ; 
                value = -1 ;
                valueDetermined = true ; }
            else {
                console.consumeChars(1) ;
                value = (long) inputChar ;
                valueDetermined = true ; } }
        if( valueDetermined ) {
            Clc_ASTUtilities util = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
            IntDatum intDatum = (IntDatum) util.scratchDatum( TyInt.get(), vms) ;
            vms.top().map(nd, intDatum) ;
            intDatum.putValue( value ) ; }
    }
}