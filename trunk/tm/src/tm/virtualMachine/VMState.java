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

package tm.virtualMachine;

import java.util.Vector;

import tm.backtrack.BTStack;
import tm.backtrack.BTTimeManager;
import tm.backtrack.BTVar;
import tm.backtrack.BTVector;
import tm.interfaces.Datum;
import tm.interfaces.SelectionInterface;
import tm.interfaces.SourceCoords;
import tm.interfaces.ViewableST;
import tm.languageInterface.NodeInterface;
import tm.utilities.Assert;

public class VMState {


    ///////////////////// F I E L D S ////////////////////

    private BTTimeManager timeMan ;

    private BTStack<Evaluation> stk ;

    private Memory mem ;

    private Store sto ;

    private Console console ;

    private BTVar<Datum> expressionResult ;

    //private BTStack resultDatums ;

    private BTStack<Vector<Datum>> argumentLists ;

    private BTVar<SourceCoords> defaultCurrentLine ;

    private BTStack<Vector<Recovery>> recoveryStack;

    private BTStack<AbruptCompletionStatus> completionStatusStack ;

    private BTVar<Datum> datumFocus ;

    private ViewableST symTab ;
    
    private BTVector<TriggeredCall> triggeredCallsList ;

    // The source files after colourization.
    private CodeStore codeStore;

    private final PropertyList properties  ;

    // A flag to allow the interpreter to stop evaluation
    public static final int EVALUATION_STATE_RUNNING = 0 ;
    public static final int EVALUATION_STATE_TERMINATED = 1 ;
    public static final int EVALUATION_STATE_NEEDINPUT = 2 ;
    private int evaluationState  = EVALUATION_STATE_RUNNING ;

    ///////////////////// P U B L I C  P A R T /////////////////////
    public VMState( BTTimeManager tm,
            int boStatic, int toStatic, int boHeap, int toHeap,
            int boStack, int toStack, int boScratch, int toScratch,
            ViewableST syms ) {
        Assert.check( boStatic==0 && boStatic < toStatic && toStatic+1 == boHeap
                        && boHeap < toHeap && toHeap+1 == boStack && boStack < toStack
                        && toStack+1 == boScratch && boScratch < toScratch ) ;
        // Assert tm == time manager of syms.
        timeMan = tm ;
        properties = new PropertyList( timeMan ) ;
        stk = new BTStack<Evaluation>( timeMan ) ;
        mem = new Memory( timeMan, toScratch ) ;
        sto = new Store( timeMan, mem, boStatic, toStatic, boHeap, toHeap,
            boStack, toStack, boScratch, toScratch, syms) ;
        console = new Console( timeMan ) ;
        expressionResult = new BTVar<Datum>( timeMan ) ;
        //resultDatums = new BTStack( timeMan ) ;
        argumentLists = new BTStack<Vector<Datum>>( timeMan ) ;
        defaultCurrentLine = new BTVar<SourceCoords>( timeMan ) ;
        defaultCurrentLine.set( SourceCoords.UNKNOWN ) ;
        recoveryStack = new BTStack<Vector<Recovery>>( timeMan ) ;
        completionStatusStack = new BTStack<AbruptCompletionStatus>( timeMan ) ;
        datumFocus = new BTVar<Datum>( timeMan ) ; // Initially null
        symTab = syms ;
        triggeredCallsList = new BTVector<TriggeredCall>(timeMan);
        codeStore = new CodeStore() ; }

    public BTTimeManager getTimeManager() {
       return timeMan ; }

//    public void pushResultDatum( Datum d ) {
//        resultDatums.push( d ) ; }
//
//    public void popResultDatum() {
//        resultDatums.pop() ; }

    public void pushNewArgumentList() {
        argumentLists.push( new Vector<Datum>() ) ; }

    public void popArgumentList() {
        argumentLists.pop() ; }

    public void addArgument(Datum arg) {
        Vector<Datum> v = argumentLists.top() ;
        v.addElement( arg  ) ; }

    public Datum getArgument(int i) {
        Vector<Datum> v = argumentLists.top() ;
        return v.elementAt(i) ; }

    public Datum topResultDatum() {
        int i = stk.size() ;
        while(i > 0) {
            i = i - 1 ;
            Evaluation ev = stk.get(i) ;
            if( ev instanceof FunctionEvaluation ) {
                return ((FunctionEvaluation) ev).getResultDatum() ;
            }
        }
        Assert.check("Unreachable") ;
        return null ; }

    public void setExpressionResult( Datum d ) {
        expressionResult.set( d  ); }

    public Datum getExpressionResult() {
        return expressionResult.get() ; }

    public boolean isEmpty() {
        return stk.size() == 0 ; }

    public void push( Evaluation e ) {
        if( e instanceof FunctionEvaluation ) {
            FunctionEvaluation fe = (FunctionEvaluation) e ;
            // TODO Here I am.
        }
        stk.push( e ) ; }

    public void pop() {
        stk.pop() ; }

    public int stackSize() { return stk.size() ; }

    public Evaluation top() {
        return stk.top() ; }

    public Evaluation getEvaluation(int i) {
        return stk.get(i) ; }

    public Store getStore() {
        return sto ; }

    public ViewableST getSymbolTable() {
        return symTab ; }

    public Memory getMemory() {
        return mem ; }

    public Console getConsole(  ) { return console ; }

    public void setDefaultLine(SourceCoords coords) {
        defaultCurrentLine.set( coords ) ; }

    public SourceCoords getCurrentSelectedCoords() {
        return getCurrentCoords( true ) ; }

    public SourceCoords getCurrentCoords() {
        return getCurrentCoords( false ) ; }

    private SourceCoords getCurrentCoords( boolean mustBeSelectedLine ) {
        // Go down the evaluation stack looking for a selected
        // node that has some coordinates associated with it.
        for( int i=stk.size() - 1 ; i >= 0 ; --i ) {
            Evaluation evaluation = stk.get(i) ;
            NodeInterface node = evaluation.getSelected() ;
            if( node != null ) {
                SourceCoords coords = node.getCoords() ;
                if( coords != null && coords != SourceCoords.UNKNOWN ) {
                    SelectionInterface currentSelection = codeStore.getSelection() ;
                    if( mustBeSelectedLine ) {
                        VMCodeLine line = codeStore.getCodeLine( coords ) ;
                        if( line != null && line.isSelected(currentSelection) )
                            return coords ; }
                    else return coords ;  } } }
        // If nothing useful is on the stack.
        return defaultCurrentLine.get() ; }

    public void setDatumFocus( Datum d ) { datumFocus.set( d ) ; }

    public Datum getDatumFocus() { return datumFocus.get() ; }

    public void setEvaluationState( int newVal ) { evaluationState = newVal ; }

    public int getEvaluationState() { return evaluationState ; }

    public CodeStore getCodeStore() { return codeStore ; }

    public void checkpoint() { timeMan.checkpoint() ; }

    public void undo() { timeMan.undo() ; }

    /* Associate an object with a name (not backtrackable!) */
    public void setProperty( String name, Object info ) {
        properties.setProperty( name, info ) ; }

    /* Get the object with a name (else null) */
    public Object getProperty(String name) {
        return properties.getProperty( name ) ; }

    public void advance() {
        if( isEmpty() ) {
            }
        else if( top().isDone() ) {
            if( top() instanceof ExpressionEvaluation ) {
                ExpressionEvaluation ee = (ExpressionEvaluation) top() ;
                Datum d = (Datum) ee.at( ee.getRoot() ) ;
                setExpressionResult( d ) ; }
            pop() ; }
        else if( top().getSelected() != null ) {
            top().getSelected().step( this ) ; }
        else {
            top().getRoot().select( this ) ; } }

    public void pushRecoveryGroup(Vector<RecoveryFactory> factoryList) {
        // Create the Recovery objects for the group
            Vector<Recovery> recoveryList = new Vector<Recovery>() ;
            for( int i = 0 ; i < factoryList.size() ; ++i ) {
                RecoveryFactory factory = factoryList.elementAt(i) ;
                Recovery recovery = factory.makeRecovery(this) ;
                recoveryList.addElement( recovery ) ; }
        // Push a new list of Recovery objects on the recoveryStack
            recoveryStack.push( recoveryList ) ;
    }

    public void popRecoveryGroup() {
        recoveryStack.pop( ) ; }

    public void startFinally(AbruptCompletionStatus acs) {
        // null indicates normal.
        completionStatusStack.push( acs ) ; }

    /** If there is a pending abrupt completion, then rethrow it and return true.
     *  Otherwise return false.
     */

    public boolean endFinally( ) {
        AbruptCompletionStatus status = completionStatusStack.top() ;
        completionStatusStack.pop() ;
        if( status == null ) {
            // This finally block was entered by the front door
            return false ; }
        else {
            abruptCompletion( status ) ;
            return true ; }
    }

    public void abruptCompletion(AbruptCompletionStatus status) {
        // Search for a recovery object that can handle this status
            int i ; // Index into the recovery Stack
            Recovery recovery = null ;
            search: for( i = recoveryStack.size() - 1 ; i >= 0 ; --i ) {
                Vector<Recovery> recoveryList = recoveryStack.get(i) ;
                for( int j = 0 ; j < recoveryList.size() ; ++j ) {
                    recovery = recoveryList.elementAt(j) ;
                    if( recovery.canHandle( status ) ) {
                        break search ; } } }
        if( i == -1 ) {
            // No recovery found.  Run-time error
                status.canNotBeHandled( this ) ; }
        else {
            // Handle the abrupt completion
                recovery.handle( status ); }
    }
    
    public void addTriggeredCall( String functionName, Object[] args) {
        triggeredCallsList.add( new TriggeredCall( functionName, args) ) ; }
    
    public boolean existsTriggeredCall() {
        return triggeredCallsList.size()!=0 ; }
    
    public TriggeredCall getTriggeredCall() {
        TriggeredCall result = triggeredCallsList.get(0) ;
        triggeredCallsList.removeElementAt(0) ;
        return  result ;  }

    class StackMarker {
        private int stackDepth = stk.size() ;
        //private int resultDatumDepth = resultDatums.size() ;
        private int argumentListsDepth = argumentLists.size() ;
        private int recoveryStackDepth = recoveryStack.size() ;
        private int completionStatusStackDepth = completionStatusStack.size() ;

        void restore() {
            stk.trimTo( stackDepth );
            //resultDatums.trimTo( resultDatumDepth );
            argumentLists.trimTo( argumentListsDepth );
            recoveryStack.trimTo( recoveryStackDepth );
            completionStatusStack.trimTo( completionStatusStackDepth ); }
    }
}