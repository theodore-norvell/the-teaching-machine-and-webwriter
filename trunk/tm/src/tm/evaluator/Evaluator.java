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

package tm.evaluator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.Timer;

import tm.backtrack.BTTimeManager;
import tm.interfaces.CodeLineI ;
import tm.interfaces.EvaluatorInterface;
import tm.interfaces.Inputter;
import tm.interfaces.RegionInterface;
import tm.interfaces.STEntry;
import tm.interfaces.SelectionInterface ;
import tm.interfaces.SourceCoords;
import tm.interfaces.StatusConsumer;
import tm.interfaces.TMFileI ;
import tm.interfaces.TMStatusCode;
import tm.interfaces.CodeLine ;
import tm.interfaces.ViewableST;
import tm.languageInterface.ExpressionInterface;
import tm.languageInterface.Language;
import tm.languageInterface.NodeInterface;
import tm.utilities.Assert;
import tm.utilities.Debug;
import tm.utilities.TMException;
import tm.utilities.TMFile;
import tm.virtualMachine.Evaluation;
import tm.virtualMachine.ExpressionEvaluation;
import tm.virtualMachine.FunctionEvaluation;
import tm.virtualMachine.TriggeredCall;
import tm.virtualMachine.VMState;

abstract class StopTest {
        abstract boolean stop0( VMState vms, int initStackSize, Evaluation initialTop ) ;
        abstract boolean midstep( VMState vms, int initStackSize, Evaluation initialTop ) ;
        abstract boolean stop1( VMState vms, int initStackSize, Evaluation initialTop ) ;

        /** Are any ExpressionInterface evaluations above or at the
         *  top StatementInterface uninteresting?
         */
        protected boolean existsUninterestingEvaluationOnStack( VMState vms ) {
            // This could really slow things down.
            // Might want to consider a way to incrementally
            // track the number of uninteresting evaluation on the
            // stack.
            for( int i=0, sz=vms.stackSize() ; i < sz ; ++i ) {
                Evaluation e = vms.getEvaluation(sz-1-i) ;
                NodeInterface selected = e.getSelected() ;
                if( selected != null && selected.isUninteresting() ) {
                    return true ; }
                if( e instanceof FunctionEvaluation ) {
                    return false ; } }
            return false ; }

        protected boolean stopWorthyTop( VMState vms ) {
            Evaluation top = vms.top() ;
            if( top instanceof ExpressionEvaluation ) return true ;
            else {
                NodeInterface selectedNode = top.getSelected() ;
                return selectedNode != null && selectedNode.isStopWorthy() ; }
        }

        protected boolean doneOrSelectedAndInteresting(VMState vms ) {
            // Precondition: The vms evaluation stack must not be empty.
            return (  !(vms.top() instanceof ExpressionEvaluation)
                    || vms.top().isDone()
                    || vms.top().getSelected() != null )
                && !existsUninterestingEvaluationOnStack(vms) ; }

        protected boolean currentLineIsInCurrentDisplaySelection( VMState vms ) {
            // Check whether the current line is currently visible.
            if( vms.getCurrentCoords() == SourceCoords.UNKNOWN ) return false ;
            SelectionInterface selection = vms.getCodeStore().getSelection() ;
            CodeLine currentLine = vms.getCodeStore().getCodeLine( vms.getCurrentCoords() ) ;
            if( currentLine == null ) return false ;
            return currentLine.isSelected(selection) ; }
}

class microStepTest extends StopTest {
        // One step.
        boolean stop0( VMState vms, int initStackSize, Evaluation initialTop ) {
            return true ; }
        boolean midstep( VMState vms, int initStackSize, Evaluation initialTop ) {
            return true ; }
        boolean stop1( VMState vms, int initStackSize, Evaluation initialTop ) {
            return true ; } }

class initialTest extends StopTest {
        // Don't do anything yet
        boolean stop0( VMState vms, int initStackSize, Evaluation initialTop ) {
            return true ;}

        // Don't do any thing yet
        boolean midstep( VMState vms, int initStackSize, Evaluation initialTop ) {
            return false ; }

        // Advance until the top is an expression and
        // either there is a selected node, or the evaluation is
        // done.
        boolean stop1( VMState vms, int initStackSize, Evaluation initialTop ) {
            return stopWorthyTop( vms )
                && currentLineIsInCurrentDisplaySelection( vms )
                && doneOrSelectedAndInteresting(vms) ; } }

class goForwardTest extends StopTest {
        boolean stop0( VMState vms, int initStackSize, Evaluation initialTop ) {
            return true ;}

        // Do at least one advance.
        boolean midstep( VMState vms, int initStackSize, Evaluation initialTop ) {
            return true ; }

        // Advance until the top is an expression and
        // either there is a selected node, or the evaluation is
        // done.
        boolean stop1( VMState vms, int initStackSize, Evaluation initialTop ) {
            return stopWorthyTop( vms )
                && currentLineIsInCurrentDisplaySelection( vms )
                && doneOrSelectedAndInteresting(vms) ; } }

/** Step to next expression on same or outer level */
class intoExpTest extends StopTest {

    boolean stop0( VMState vms, int initStackSize, Evaluation initialTop ) {
        if( initialTop instanceof ExpressionEvaluation )
            return vms.stackSize() <= initStackSize
              && !( vms.top() instanceof ExpressionEvaluation )  ;
        else
            return true ; }
    boolean midstep( VMState vms, int initStackSize, Evaluation initialTop ) {
        if( initialTop instanceof ExpressionEvaluation )
            return false ;
        else
            return true ; }
    boolean stop1( VMState vms, int initStackSize, Evaluation initialTop ) {
        return stopWorthyTop( vms )
            && currentLineIsInCurrentDisplaySelection( vms )
            && doneOrSelectedAndInteresting(vms) ; } }

/** Step to next expression regardless of level */
class intoSubTest extends StopTest {
        boolean stop0( VMState vms, int initStackSize, Evaluation initialTop ) {
            if( initialTop instanceof ExpressionEvaluation )
                return !( vms.top() instanceof ExpressionEvaluation) ;
            else
                return true ; }

        boolean midstep( VMState vms, int initStackSize, Evaluation initialTop ) {
            if( initialTop instanceof ExpressionEvaluation )
                return false ;
            else
                return true ; }

        boolean stop1( VMState vms, int initStackSize, Evaluation initialTop ) {
            return stopWorthyTop( vms )
                && currentLineIsInCurrentDisplaySelection( vms )
                && doneOrSelectedAndInteresting(vms) ; } }

class overAllTest extends StopTest {
        // Step to next expression on shallower level
        boolean stop0( VMState vms, int initStackSize, Evaluation initialTop ) {
            return true ; }
        boolean midstep( VMState vms, int initStackSize, Evaluation initialTop ) {
            return false ; }
        boolean stop1( VMState vms, int initStackSize, Evaluation initialTop ) {
            return vms.stackSize() < initStackSize
                && stopWorthyTop( vms )
                && currentLineIsInCurrentDisplaySelection( vms )
                && doneOrSelectedAndInteresting(vms) ; } }

class toCursorTest extends StopTest {
        // Step to a particular line
        private String fileName ;
        private int lineNumber ;
        toCursorTest(String fn, int ln ) { fileName = fn; lineNumber = ln ; }
        boolean stop0( VMState vms, int initStackSize, Evaluation initialTop ) {
            SourceCoords sc = vms.getCurrentCoords() ;
            return lineNumber==sc.getLineNumber()
                && sc.getFile().getFileName().equals( fileName ) ; }
        boolean midstep( VMState vms, int initStackSize, Evaluation initialTop ) {
            return false ; }
        boolean stop1( VMState vms, int initStackSize, Evaluation initialTop ) {
            return stopWorthyTop( vms )
                && doneOrSelectedAndInteresting(vms) ; } }


public class Evaluator implements EvaluatorInterface, CommandStringInterpreter.CommandsI
{
    
    Language lang ;
    StatusConsumer statusReporter ;
    VMState vms ;
    Refreshable observer ;
    
    // If the Timer is null, then we are in normal mode
    // otherwise we are in autoStep mode.
    Timer autoStepTimer = null ;
    private int autoStepRate = 50 ;
    private int delay;
    {updateDelay() ;}
    private boolean autoStepStopRequested;
    private boolean runStopRequested;
    private Inputter inputter ;

	public Evaluator( Language lang,
                      StatusConsumer statusReporter,
                      Refreshable observer,
                      SelectionInterface initialSelection, 
                      Inputter inputter,
                      int boStatic, int toStatic,
                      int boHeap, int toHeap,
                      int boStack, int toStack,
                      int boScratch, int toScratch)
            throws Throwable {
        this.lang = lang ;
        this.statusReporter = statusReporter ;
        this.observer = observer ;
        this.inputter = inputter ;
        
        // Create a time manager to synchronize back tracking
        BTTimeManager timeMan = new BTTimeManager() ;

        // Create the symbol table
        ViewableST symtab = lang.makeSymTab( timeMan ) ;

        // Create a virtual machine state .
        vms = new VMState(  timeMan,
                            boStatic, toStatic, boHeap, toHeap,
                            boStack, toStack, boScratch, toScratch,
                            symtab ) ;
        setSelection( initialSelection ) ;
    }

    public void compile( TMFile tmFile ) {
        Assert.check( statusReporter.getStatusCode() == TMStatusCode.READY_TO_COMPILE ) ;
        try {
            statusReporter.setStatus( TMStatusCode.READY_TO_COMPILE,
                                      lang.getName()+" Compiling " + tmFile ) ;
            /*dbg */Debug.getInstance().msg(Debug.CURRENT, "Compiling "+tmFile ) ;/* */
        
            lang.compile( tmFile, vms ) ;
            statusReporter.setStatus( TMStatusCode.COMPILED,
                                      lang.getName()+" Compiled with no errors.") ;
            /*dbg */Debug.getInstance().msg(Debug.CURRENT, "Done cmpiling "+tmFile ) ;/* */
            vms.setDefaultLine( new SourceCoords( tmFile, 1 ) );
        }
        catch (TMException e) {
            if( e.getSourceCoords() != SourceCoords.UNKNOWN ) {
                vms.setDefaultLine( e.getSourceCoords() ) ; }
            e.printStackTrace( System.out ) ;
            statusReporter.setStatus(TMStatusCode.DID_NOT_COMPILE, lang.getName()+"Error: "+e.getMessage() ) ;
            statusReporter.attention( e.getMessage(), e ) ;
        }
        catch( Throwable e ) {
            e.printStackTrace( System.out ) ;
            statusReporter.setStatus(TMStatusCode.DID_NOT_COMPILE,
                                     lang.getName()+" Unexpected Exception: "+e.getMessage() ) ;
            statusReporter.attention( "Unexpected exception: "+e.getMessage(), e ) ; }
    }
    
    public void refresh() {
        /**/Debug.getInstance().msg(Debug.BACKTRACK, " Evaluator refreshes its observers." ) ; /**/
        try{ vms.turnOnProtection() ;
        	observer.refresh() ; }
       finally { vms.turnOffProtection() ; }
    }

//  Calls to advance or retard the state  //
////////////////////////////////////////// /

    public void initialize() {
        int status = statusReporter.getStatusCode() ;
        Assert.check( status == TMStatusCode.COMPILED ) ;
        try {
            lang.initializeTheState( vms ) ;
            statusReporter.setStatus( TMStatusCode.READY,
                                      lang.getName()+" Ready") ; }
        catch (TMException e) {
            if( e.getSourceCoords() != SourceCoords.UNKNOWN ) {
                vms.setDefaultLine( e.getSourceCoords() ) ; }
            else {
                e.setSourceCoords( vms.getCurrentCoords() ) ; }
                
            statusReporter.setStatus( TMStatusCode.EXECUTION_FAILED,
                                      lang.getName()+"Error: "+e.getMessage() ) ;
            statusReporter.attention( e.getMessage() ) ;
            // Dump the exception
            Debug.getInstance().msg( Debug.EXECUTE, e ) ;
            return ;
        }
        catch( Throwable e ) {
            statusReporter.setStatus( TMStatusCode.EXECUTION_FAILED,
                                      lang.getName()+" Unexpected Exception." ) ;
            e.printStackTrace( System.out ) ;
            statusReporter.attention( "Unexpected exception:"+e.getMessage(), e ) ; 
            return ;
        }

        initialSteps() ;
    }

    public void addInputString( String input ) {
        vms.checkpoint() ;
        vms.getConsole().addInputString( input ) ;
        refresh() ; }

    public void addProgramArgument( String argument ) {
        lang.addProgramArgument(argument) ;
    }
    
    public void goBack() {
        /**/Debug.getInstance().msg(Debug.BACKTRACK, ">>Evaluator.goBack()" ) ; /**/
        if( autoStepTimer != null ) return ;
        vms.undo() ;
        refresh() ;
        /**/Debug.getInstance().msg(Debug.BACKTRACK, "<<Evaluator.goBack()" ) ; /**/ }
    
    public void redo() {
        /**/Debug.getInstance().msg(Debug.BACKTRACK, ">>Evaluator.redo()" ) ; /**/
        if( autoStepTimer != null ) return ;
        vms.redo() ;
        refresh() ;
        /**/Debug.getInstance().msg(Debug.BACKTRACK, "<<Evaluator.redo()" ) ; /**/
    }
    
    public void go(String commandString ) {
        /**/Debug.getInstance().msg(Debug.BACKTRACK, ">>Evaluator.go()" ) ; /**/
        if( statusReporter.getStatusCode() != TMStatusCode.READY ) return ;
    	if( autoStepTimer != null ) return ;
    	vms.checkpoint() ;
    	new CommandStringInterpreter(commandString, this).interpretGoCommand(  ) ;
    	refresh() ;
        /**/Debug.getInstance().msg(Debug.BACKTRACK, "<<Evaluator.go()" ) ; /**/
    }

	public void microStep() {
        /**/Debug.getInstance().msg(Debug.BACKTRACK, ">>Evaluator.microStep()" ) ; /**/
        if( autoStepTimer != null ) return ;
        vms.checkpoint() ;
        microStepCommand() ;
        refresh() ;
        /**/Debug.getInstance().msg(Debug.BACKTRACK, "<<Evaluator.microStep()" ) ; /**/}
	
	/** Not really for public use*/
	public void microStepCommand() { stepForward ( new microStepTest() ) ; }

    public void goForward() {
        /**/Debug.getInstance().msg(Debug.BACKTRACK, ">>Evaluator.goForward()" ) ; /**/
        if( statusReporter.getStatusCode() != TMStatusCode.READY ) return ;
        if( autoStepTimer != null ) return ;
        vms.checkpoint() ;
        goForwardCommand() ; 
        refresh() ;
        /**/Debug.getInstance().msg(Debug.BACKTRACK, "<<Evaluator.goForward()" ) ; /**/ }
	
	/** Not really for public use*/
	public void goForwardCommand() { stepForward ( new goForwardTest() ) ;  }

    private void initialSteps() {
        /**/Debug.getInstance().msg(Debug.BACKTRACK, ">>Evaluator.initialSteps()" ) ; /**/
        Assert.check( statusReporter.getStatusCode() == TMStatusCode.READY )  ;
        if( autoStepTimer != null ) return ;
        vms.checkpoint() ;
        stepForward ( new initialTest() ) ; 
        refresh() ;
        /**/Debug.getInstance().msg(Debug.BACKTRACK, "<<Evaluator.initialSteps()" ) ; /**/ }

    public void intoExp() {
        /**/Debug.getInstance().msg(Debug.BACKTRACK, ">>Evaluator.intoExp()" ) ; /**/
        if( statusReporter.getStatusCode() != TMStatusCode.READY ) return ;
        if( autoStepTimer != null ) return ;
        vms.checkpoint() ;
        intoExpCommand() ;
        refresh() ;
        /**/Debug.getInstance().msg(Debug.BACKTRACK, "<<Evaluator.intoExp()" ) ; /**/ }
	
	/** Not really for public use*/
	public void intoExpCommand() { stepForward ( new intoExpTest() ) ;  }

    public void intoSub() {
        /**/Debug.getInstance().msg(Debug.BACKTRACK, ">>Evaluator.intoSub()" ) ; /**/
        if( statusReporter.getStatusCode() != TMStatusCode.READY ) return ;
        if( autoStepTimer != null ) return ;
        vms.checkpoint() ;
        intoSubCommand() ;
        refresh() ; 
        /**/Debug.getInstance().msg(Debug.BACKTRACK, "<<Evaluator.intoSub()" ) ; /**/ }
	
	/** Not really for public use*/
	public void intoSubCommand() { stepForward ( new intoSubTest() ) ;  }

    public void overAll() {
        /**/Debug.getInstance().msg(Debug.BACKTRACK, ">>Evaluator.overAll()" ) ; /**/
        if( statusReporter.getStatusCode() != TMStatusCode.READY ) return ;
        if( autoStepTimer != null ) return ;
        vms.checkpoint() ;
        overAllCommand() ;
        refresh() ; 
        /**/Debug.getInstance().msg(Debug.BACKTRACK, "<<Evaluator.overAll()" ) ; /**/ }
	
	/** Not really for public use*/
	public void overAllCommand() { stepForward ( new overAllTest() ) ;  }

    public void toCursor(String fileName, int lineNumOneBased) {
        /**/Debug.getInstance().msg(Debug.BACKTRACK, ">>Evaluator.toCursor()" ) ; /**/
        if( statusReporter.getStatusCode() != TMStatusCode.READY ) return ;
        if( autoStepTimer != null ) return ;
        vms.checkpoint() ;
        stepForward( new toCursorTest(fileName, lineNumOneBased) ) ;
        refresh() ;
        /**/Debug.getInstance().msg(Debug.BACKTRACK, "<<Evaluator.toCursor()" ) ; /**/ }
    
    public void toBreakPoint() {
        /**/Debug.getInstance().msg(Debug.BACKTRACK, ">>Evaluator.toBreakPoint()" ) ; /**/
    	// TODO
    	toBreakPointCommand() ;
        /**/Debug.getInstance().msg(Debug.BACKTRACK, "<<Evaluator.toBreakPoint()" ) ; /**/
    }
	
	/** Not really for public use*/
	public void toBreakPointCommand() { Assert.toBeDone() ;  }
    
    public void autoStep() {
        if( statusReporter.getStatusCode() != TMStatusCode.READY ) return ;
        if( autoStepTimer != null ) return ;
        vms.checkpoint() ;
        autoStepStopRequested = false ;
        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                autoStepAdvance() ;
            }} ;

        autoStepTimer = new Timer( delay, listener ) ;
        autoStepTimer.setRepeats(true) ;
        autoStepTimer.start();

    }

    public void setAutoStepRate(int rate) {
        //System.out.println( "Evaluator.setAutoStepRate("+rate+")") ;
        if( rate < 0 ) rate = 0 ;
        else if( rate > 100 ) rate = 100 ;
        autoStepRate  = rate;
        updateDelay() ;
        refresh() ;
        //System.out.println( "rate = "+rate) ;
        //System.out.println( "delay = "+delay) ;
    }
    
    public void run() {
        if( autoStepTimer != null ) return ;
        vms.checkpoint() ;
        this.runStopRequested = false ;
        RUNLOOP:
            while( true ) {
                stepForward ( new goForwardTest() ) ;
                if( this.runStopRequested )
                    break RUNLOOP ;
                int status = statusReporter.getStatusCode() ;
                switch( status ) {
                case TMStatusCode.EXECUTION_COMPLETE:
                case TMStatusCode.EXECUTION_FAILED:
                    break RUNLOOP ;
                case TMStatusCode.READY:
                    if( vms.getEvaluationState() != VMState.EVALUATION_STATE_RUNNING )
                        break RUNLOOP ;
                    else
                        break ;
                default: Assert.check( false ) ; } }
        refresh() ; }

    public void stop( ) {
        if( autoStepTimer != null ) {
            autoStepStopRequested = true ; }
        else if( statusReporter.getStatusCode() == TMStatusCode.BUSY_EVALUATING ) {
            // This request must come from a callback.
            runStopRequested = true ;
        }
    }

    public void setSelection(SelectionInterface selection) {
        vms.getCodeStore().setSelection( selection ) ; }

    public SelectionInterface getSelection() {
        return vms.getCodeStore().getSelection() ; }

// The Evaluator Interface //
/////////////////////////////
    public int getNumSTEntries() {
        return vms.getSymbolTable().size() ; }

    public STEntry getSymTabEntry(int index) {
        return vms.getSymbolTable().getEntry( index ) ; }


    /** The number of entries in the current stack frame */
    public int varsInCurrentFrame() {
         return vms.getSymbolTable().varsInCurrentFrame() ; }

    /** The number of entries in the global frame. */
    public int varsInGlobalFrame() {
        return vms.getSymbolTable().varsInGlobalFrame() ; }

    public Enumeration<TMFile> getSourceFiles() {
        return vms.getCodeStore().getSourceFiles() ; 
    }
    
    public SourceCoords getCodeFocus() {
        return vms.getCurrentSelectedCoords() ; }

    public int getNumSelectedCodeLines(TMFileI tmFile, boolean allowGaps) {
        return vms.getCodeStore().getNumSelectedCodeLines( (TMFile)tmFile, allowGaps ) ;
    }

//    public CodeLine getSelectedCodeLine(TMFile tmFile, boolean allowGaps, int index) {
//        return vms.getCodeStore().getSelectedCodeLine( tmFile, allowGaps, index ) ;
//    }
    
    public CodeLineI getSelectedCodeLine(TMFileI tmFile, boolean allowGaps, int index) {
        return vms.getCodeStore().getSelectedCodeLine( (TMFile)tmFile, allowGaps, index ) ;
    }
    
    public String getOutputLine(int l) {
        return vms.getConsole().getOutputLine( l ) ; }

    public int getNumOutputLines() {
        return vms.getConsole().getOutputSize() ; }

    public String getConsoleLine(int l) {
        return vms.getConsole().getConsoleLine( l ) ; }

    public int getNumConsoleLines() {
        return vms.getConsole().getConsoleSize() ; }

    public String getPCLocation() {
        return Integer.toString( getCodeFocus().getLineNumber() ) ; }

    public String getExpression() {
        String exprString ;
        if( ! vms.isEmpty() && vms.top() instanceof ExpressionEvaluation ) {
            ExpressionInterface exp = (ExpressionInterface) vms.top().getRoot() ;
            exprString =  exp.toString(vms) ; }
        else {
            exprString = "" ; }
        return exprString ; }

    public RegionInterface getStaticRegion() {
        return vms.getStore().getStatic() ; }

    public RegionInterface getStackRegion() {
        return vms.getStore().getStack() ; }

    public RegionInterface getHeapRegion() {
        return vms.getStore().getHeap() ; }

    public RegionInterface getScratchRegion() {
        return vms.getStore().getScratch() ; }

    public int getLanguage() {
        return lang.getLanguage() ;
    }

    public boolean isInAuto() {
        return autoStepTimer != null ;
    }
    
    public int getAutoStepRate() {
        return autoStepRate ;
    }

    @Override
    public BTTimeManager getTimeManager() {
        return vms.getTimeManager() ;
    }

// Private part //
//////////////////
    
    private void killOffTheTimer() {
        // Nullifying the timer means autostep mode is off.
        autoStepTimer.stop();
        autoStepTimer = null ; }
    
    private void autoStepAdvance() {
        Assert.check( autoStepTimer != null ) ;
        //long now = System.currentTimeMillis() ;
        //System.out.println("Step: "+now ) ;
        if( autoStepStopRequested  ) {
            killOffTheTimer() ;  }
        else {
            stepForward ( new intoSubTest() ) ;
            int status = statusReporter.getStatusCode() ;
            if( status == TMStatusCode.READY 
            && vms.getEvaluationState() == VMState.EVALUATION_STATE_RUNNING) {
                //System.out.println( "next delay = "+delay) ;
                autoStepTimer.setDelay( delay ) ; }
            else {
                killOffTheTimer() ; } } 
        refresh() ;
    }

    private void stepForward( StopTest stopTest) {
        // Step until the stopTest object says to stop.
        int status = statusReporter.getStatusCode() ;
        Assert.check( status == TMStatusCode.READY )  ;
        String langName = lang.getName() ;
        try {
            // TODO This limit should be configurable.
            final int SAFETY_COUNTER_LIMIT = 50000000 ;
            vms.setDatumFocus( null ) ;
            int safetyCounter = 0 ;
            int initStackSize = vms.stackSize() ;
            Evaluation initialTop = vms.isEmpty() ? null : vms.top() ;
            TMFile latestFile = null ;
            vms.setEvaluationState( VMState.EVALUATION_STATE_RUNNING ) ;
            statusReporter.setStatus( TMStatusCode.BUSY_EVALUATING, langName +" Evaluating" ) ;

            // Advance until stop0
            while(   ! vms.isEmpty()
                  && vms.getEvaluationState() == VMState.EVALUATION_STATE_RUNNING
                  && safetyCounter < SAFETY_COUNTER_LIMIT
                  && ! stopTest.stop0(vms, initStackSize, initialTop) ) {
                latestFile = getCodeFocus().getFile() ;
                advance() ;
                if( safetyCounter % 10000 == 0 ) Debug.getInstance().msg(Debug.DISPLAY, "safetyCounter: "+safetyCounter) ;
                ++ safetyCounter ; }

            // Optional extra advance
            if(       ! vms.isEmpty()
                   && vms.getEvaluationState() == VMState.EVALUATION_STATE_RUNNING
                   && safetyCounter < SAFETY_COUNTER_LIMIT
                   && stopTest.midstep(vms, initStackSize, initialTop) ) {
                latestFile = getCodeFocus().getFile() ;
                advance() ; }

            // Advance until stop1
            while(   ! vms.isEmpty()
                  && vms.getEvaluationState() == VMState.EVALUATION_STATE_RUNNING
                  && safetyCounter < SAFETY_COUNTER_LIMIT
                  && ! stopTest.stop1(vms, initStackSize, initialTop) ) {
                latestFile = getCodeFocus().getFile() ;
                advance() ;
                if( safetyCounter % 10000 == 0 ) Debug.getInstance().msg(Debug.EXECUTE, "safetyCounter: "+safetyCounter) ;
                ++ safetyCounter ; }
            
            if( vms.isEmpty() ) {
                statusReporter.setStatus( TMStatusCode.EXECUTION_COMPLETE,
                                          langName+" Execution complete") ;
                if( latestFile != null ) {
                    SourceCoords lastLine = vms.getCodeStore().getCoordsOfLastLine(latestFile) ;
                    vms.setDefaultLine( lastLine ); } }
            else if( vms.getEvaluationState() != VMState.EVALUATION_STATE_RUNNING ) {
                switch( vms.getEvaluationState() ) {
                case VMState.EVALUATION_STATE_NEEDINPUT :
                    // Prompt the user for more input
                    inputter.requestMoreInput( this );
                	statusReporter.setStatus( TMStatusCode.READY,
                            langName+" Waiting for input") ;
                    break;
                case VMState.EVALUATION_STATE_TERMINATED:
                    statusReporter.setStatus( TMStatusCode.EXECUTION_COMPLETE,
                            langName+" Program has self terminated") ;
                } }
            else if( safetyCounter >= SAFETY_COUNTER_LIMIT ) {
                statusReporter.setStatus( TMStatusCode.READY,
                                          langName+" Step limit exceeded. Possible infinite loop.") ; }
            else {
                statusReporter.setStatus( TMStatusCode.READY,
                                          langName+" Ready") ; } }
        catch( tm.utilities.TMException e ) {
            if( e.getSourceCoords() != SourceCoords.UNKNOWN ) {
                vms.setDefaultLine( e.getSourceCoords() ) ; }
            else {
                e.setSourceCoords( vms.getCurrentCoords() ) ; }
            e.printStackTrace( System.out ) ;
            statusReporter.setStatus( TMStatusCode.EXECUTION_FAILED,
                                      langName+" Error: "+e.getMessage() ) ;
            statusReporter.attention( e.getMessage(), e ) ;
            // vms.undo() ; // 2011 July 4. Not sure why we used to do an undo here. TSN.
        }
        catch( Throwable e ) {
            statusReporter.setStatus( TMStatusCode.EXECUTION_FAILED,
                                      langName+" Unexpected exception "+e.getMessage() ) ;
            e.printStackTrace(System.out) ;
            statusReporter.attention( "Unexpected exception: "+e.getMessage(), e ) ; } }
    
    private void advance() {
        vms.advance() ;
        
        // Deal with any resulting triggered calls.
        while( vms.existsTriggeredCall() ) {
            int initialStackSize = vms.stackSize() ;
            TriggeredCall callInfo = vms.getTriggeredCall() ;
            lang.callSubroutine(vms, callInfo.functionName, callInfo.args) ;
            while( vms.stackSize() > initialStackSize
                && vms.getEvaluationState() == VMState.EVALUATION_STATE_RUNNING) {
                vms.advance() ; }
        }
    }
    
    private void updateDelay() {
        // 30+2000^{(r+100)/200}-2000^{1/2}
        double ln2k = Math.log(2000) ;
        int r = 100 - autoStepRate ;
        delay = (int)(30 + Math.exp(ln2k * (r+100.0) / 200.0) - Math.sqrt(2000)) ;
    }
    
    public interface Refreshable {
        public void refresh() ;
    }

	public void setStatusCode(int statusCode) {
		vms.setStatusCode( statusCode ) ;
	}

	public int getStatusCode() {
		return vms.getStatusCode() ;
	}

	public void setStatusMessage(String message) {
		vms.setStatusMessage(message) ;
	}

	public String getStatusMessage() {
		return vms.getStatusMessage() ;
	}
}