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

package tm.cpp.ast;

import tm.clc.ast.Clc_ASTUtilities;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.Stepper;
import tm.clc.ast.StepperBuiltInCommon;
import tm.clc.datum.AbstractDatum;
import tm.clc.datum.AbstractFloatDatum;
import tm.clc.datum.AbstractIntDatum;
import tm.cpp.datum.CharDatum;
import tm.cpp.datum.DatumUtilities;
import tm.cpp.datum.ObjectDatum;
import tm.cpp.datum.PointerDatum;
import tm.cpp.datum.RefDatum;
import tm.interfaces.Datum;
import tm.utilities.Assert;
import tm.virtualMachine.Console;
import tm.virtualMachine.Memory;
import tm.virtualMachine.VMState;


public class Cpp_StepperBuiltIn extends StepperBuiltInCommon {

    public Cpp_StepperBuiltIn( int op_code ) {
        super( op_code ) ; }
        

    protected void output_step( ExpressionNode nd, VMState vms ) {
    
        // The node shouldn't already be mapped.
            Assert.check( vms.top().at( nd ) == null ) ;
                
        // Clear the selection
            vms.top().setSelected( null ) ;
            
        AbstractDatum leftDatum = (AbstractDatum) vms.top().at( nd.child_exp(0) ) ;
        AbstractDatum rightDatum = (AbstractDatum) vms.top().at( nd.child_exp(1) ) ;
        
        Assert.check( leftDatum instanceof RefDatum ) ;
        Assert.check( ((RefDatum)leftDatum).deref() != null ) ;
        Assert.check( ((RefDatum)leftDatum).deref() instanceof ObjectDatum ) ;
        
        if( rightDatum.getType() instanceof TyPointer
                && ((TyPointer)rightDatum.getType()).getPointeeType()
                    instanceof TyChar ) {
            // Special case for char *.
                int addr = ((PointerDatum) rightDatum).getValue() ;
                Memory theMem = vms.getMemory() ;
                while( true ) {
                    byte b = theMem.getByte(addr) ;
                    if( b == 0 )
                        break ;
                    else {
                        char c = (char) b ;
                        vms.getConsole().putchar( c ) ; }
                    addr += 1 ; } }
        else {
            rightDatum.output( vms ) ;
        }
        vms.top().map(nd, leftDatum) ;
    }
    
    
    protected void input_step( ExpressionNode nd, VMState vms ) {
    
        // The node shouldn't already be mapped.
            Assert.check( vms.top().at( nd ) == null ) ;
                
        // Clear the selection
            vms.top().setSelected( null ) ;
        
        AbstractDatum leftDatum = (AbstractDatum) vms.top().at( nd.child_exp(0) ) ;
        
        Assert.check( leftDatum instanceof RefDatum ) ;
        Assert.check( ((RefDatum)leftDatum).deref() != null ) ;
        Assert.check( ((RefDatum)leftDatum).deref() instanceof ObjectDatum ) ;
        Console console = vms.getConsole() ;
        
       switch( op_code ) {
        case EOF: case FAIL: {
            long value = (op_code==EOF ? console.eof() : console.fail()) ? 1 : 0 ;
                
            // New datum on scratch
                Clc_ASTUtilities util
                    = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
                AbstractIntDatum d
                    = (AbstractIntDatum) util.scratchDatum(util.getBooleanType(), vms) ;                
            // Give it a value
                d.putValue( value ) ;
                
            vms.top().map(nd, d) ; }
        break ;
        case GET: {
            AbstractDatum rightDatum = (AbstractDatum) vms.top().at( nd.child_exp(1) ) ;
            Assert.check( rightDatum instanceof RefDatum ) ;
            Datum d = ((RefDatum)rightDatum).deref() ;
            Assert.check( d != null ) ;
            Assert.check( d instanceof CharDatum ) ;
            CharDatum charVar = (CharDatum) d ;
            
            char inputChar = console.peekChar(0) ;
            if( inputChar == '\uffff' ) {
                // There is no more input. Stop the
                // evaluation so the user can add input.
                vms.setEvaluationState(vms.EVALUATION_STATE_NEEDINPUT) ; }
            else if( inputChar == 0 ) {
                console.setFailBit() ; 
                vms.top().map(nd, leftDatum) ;}
            else {
                console.consumeChars(1) ;
                charVar.putValue( (long) inputChar ) ;
                vms.top().map(nd, leftDatum) ; } }
        break;
        case INPUT : {
            AbstractDatum rightDatum = (AbstractDatum) vms.top().at( nd.child_exp(1) ) ;
            boolean success ;
            
            if( rightDatum instanceof RefDatum ) {
                RefDatum refDatum = (RefDatum) rightDatum ;
                Datum targetDatum = refDatum.deref() ;
                Assert.apology(targetDatum != null, "Missing object" ) ;
                AbstractDatum targetAsAbstractDatum = (AbstractDatum) targetDatum ;
                success = targetAsAbstractDatum.input( vms ) ;  }
            else {
                Assert.check( rightDatum instanceof PointerDatum ) ;
                success = DatumUtilities.inputString( vms, (PointerDatum) rightDatum ) ; }
                
            if( success ) {
                vms.top().map(nd, leftDatum) ; }
            else {
                vms.setEvaluationState(vms.EVALUATION_STATE_NEEDINPUT) ; } }
        break ;
        default:
            Assert.check(false) ;
        }
    }
}