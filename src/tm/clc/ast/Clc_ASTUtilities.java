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
import tm.clc.datum.AbstractRefDatum;
import tm.clc.rtSymTab.RT_Symbol_Table;
import tm.interfaces.Datum;
import tm.languageInterface.StatementInterface;
import tm.utilities.Assert;
import tm.virtualMachine.FunctionEvaluation;
import tm.virtualMachine.MemRegion;
import tm.virtualMachine.VMState;

abstract public class Clc_ASTUtilities {

    public AbstractDatum staticDatum( TypeNode type, VMState vms, String name ) {
        MemRegion staticRegion = vms.getStore().getStatic() ;
        return type.makeDatum(vms, staticRegion, name) ; }

    public AbstractDatum pushDatum( TypeNode type, VMState vms, String name ) {
        MemRegion stack = vms.getStore().getStack() ;
        return type.makeDatum(vms, stack, name) ; }


    public AbstractDatum scratchDatum( TypeNode type, VMState vms ) {
        MemRegion scratch = vms.getStore().getScratch() ;
        return type.makeDatum(vms, scratch, "" ) ; }

    public AbstractRefDatum scratchRef( VMState vms, TypeNode pointeeType ) {
        TyAbstractRef refType = getRefType( (TypeNode) pointeeType ) ;
        AbstractRefDatum d = (AbstractRefDatum)  scratchDatum( refType, vms ) ;
        return d ; }

    public AbstractRefDatum scratchRef( VMState vms, AbstractDatum pointee ) {
        AbstractRefDatum d = scratchRef( vms, (TypeNode) pointee.getType() ) ;
        d.putValue( pointee ) ;
        return d ; }


    public AbstractRefDatum scratchRef( VMState vms, AbstractDatum pointee, String name ) {
        AbstractRefDatum d = scratchRef( vms, pointee ) ;
        d.putValueString( name ) ;
        return d ; }

    public AbstractDatum scratchBoolean( VMState vms, TypeNode t, boolean truthValue ) {
        AbstractDatum ad = scratchDatum( t, vms ) ;
        Assert.check( ad instanceof AbstractIntDatum ) ;
        AbstractIntDatum aid = (AbstractIntDatum) ad ;
        aid.putValue( truthValue ? 1 : 0 ) ;
        return aid ; }

    public boolean isTrue(AbstractDatum d) {
        Assert.check( d instanceof AbstractIntDatum ) ;
        return ((AbstractIntDatum)d).getValue() != 0 ; }
    
    public void startTriggeredFunctionCall( VMState vms, Object functionKey ) {
        RT_Symbol_Table symtab = (RT_Symbol_Table) vms.getSymbolTable() ;
        AbstractFunctionDefn temp = symtab.getFunctionDefn( functionKey ) ;
        Assert.apology(temp!=null, "Could not find function with key of {0}.", functionKey.toString() ) ;
        FunctionDefnCompiled defn = (FunctionDefnCompiled) temp ;
        // Prepare the symbol table
            // The Function should have no recipient. (I.e. a static function in Java or a nonmember function in C++).
            symtab.enterFunction( null ) ;
        // Mark the stack
            vms.getStore().setStackMark() ;

        //Create a scratch var to hold the result
            // Assume the result type is void.
            TypeNode retType = getVoidType() ;
            AbstractDatum resultDatum = scratchDatum(  retType, vms ) ;

        // Push a new function evaluation on the evaluation stack
            StatementInterface firstStatement = defn.getBodyLink().get() ;
            FunctionEvaluation funEval = new FunctionEvaluation( vms, firstStatement, null, resultDatum ) ;
            vms.push( funEval ) ; 
        // TODO Problem: We need to pop the function result after the function completes
            // Maybe the best thing to do is to not have function results for void functions!
    }

    /*** Do this one later, if at all
     public AbstractDatum scratchCopy( VMState vms, AbstractDatum original ) {
        MemRegion mr = vms.getStore().getScratch() ;
        int address = mr.findSpace( original.getNumBytes() ) ;
        Assert.apology( address >= 0, "Scratch space overflow" ) ;
        AbstractDatum d = original.copyTo(address, "") ;
        vms.getStore().addDatum(d) ;
        return d ; }
     ***/

    /** An integer type for temp variables */
    public abstract TypeNode getIntType() ;

    /** A type for booleans */
    public abstract TypeNode getBooleanType() ;

    /** A floating type for temp variables */
    public abstract TypeNode getFloatingType() ;

    /** A reference type for temp variables */
    public abstract TyAbstractRef getRefType( TypeNode target_type ) ;

    /** An void type for temp variables */
    public abstract TypeNode getVoidType() ;

    /** Is this datum a "string"
     * This is used for passing strings to native Java methods.
     * @param d
     * @return if the datum can be represented as a java String object.
     */
    public abstract boolean isString( AbstractDatum d ) ;

    /** Find a java Class that corresponds to a datum's type.
     * This is used for calling Java methods via reflection.
     * @param d
     * @return
     */
    public abstract Class getNativeClass( AbstractDatum d ) ;

    /** Find a java Object that corresponds to a datum's value.
     * This is used for calling Java methods via reflection.
     * @param d
     * @return
     */
    public abstract Object getNativeValue( AbstractDatum d, VMState vms) ;

    /** Change a datum's value based on a Java object.
     * This is used for calling Java methods via reflection.
     * @param d A datum to value it's value changed.
     * @param nativeResult An object of the appropriate type. E.g. Integer for int datums.
     * @param vms The virtual machine's state.
     * @return 
     */
    public abstract void putNativeValue( AbstractDatum d, Object nativeResult, VMState vms ) ;
}