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
import java.util.Vector;

import javax.swing.Timer;

import tm.backtrack.BTTimeManager;
import tm.interfaces.CodeLine;
import tm.interfaces.EvaluatorInterface;
import tm.interfaces.RegionInterface;
import tm.interfaces.SelectionInterface;
import tm.interfaces.STEntry;
import tm.interfaces.SourceCoords;
import tm.interfaces.StatusConsumer;
import tm.interfaces.StatusProducer;
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
import tm.virtualMachine.VMCodeLine;
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
            VMCodeLine currentLine = vms.getCodeStore().getCodeLine( vms.getCurrentCoords() ) ;
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


public class Evaluator implements EvaluatorInterface
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

    public Evaluator( Language lang,
                      StatusConsumer statusReporter,
                      Refreshable observer,
                      SelectionInterface initialSelection, 
                      int boStatic, int toStatic,
                      int boHeap, int toHeap,
                      int boStack, int toStack,
                      int boScratch, int toScratch)
            throws Throwable {
        this.lang = lang ;
        this.statusReporter = statusReporter ;
        this.observer = observer ;
        
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
        Assert.check( statusReporter.getStatusCode() == StatusProducer.NO_FILE_LOADED ) ;
        try {
            statusReporter.setStatus( StatusProducer.NO_FILE_LOADED,
                                      lang.getName()+" Compiling " + tmFile ) ;
            /*dbg */Debug.getInstance().msg(Debug.CURRENT, "Compiling "+tmFile ) ;/* */
        
            lang.compile( tmFile, vms ) ;
            statusReporter.setStatus( StatusProducer.FILE_LOADED,
                                      lang.getName()+" No errors") ;
            /*dbg */Debug.getInstance().msg(Debug.CURRENT, "Done cmpiling "+tmFile ) ;/* */
            vms.setDefaultLine( new SourceCoords( tmFile, 1 ) );
        }
        catch (TMException e) {
            if( e.getSourceCoords() != SourceCoords.UNKNOWN ) {
                vms.setDefaultLine( e.getSourceCoords() ) ; }
            e.printStackTrace( System.out ) ;
            statusReporter.setStatus(StatusProducer.NO_FILE_LOADED, lang.getName()+"Error: "+e.getMessage() ) ;
            statusReporter.attention( e.getMessage(), e ) ;
        }
        catch( Throwable e ) {
            e.printStackTrace( System.out ) ;
            statusReporter.setStatus(StatusProducer.NO_FILE_LOADED,
                                     lang.getName()+" Unexpected Exception: "+e.getMessage() ) ;
            statusReporter.attention( "Unexpected exception: "+e.getMessage(), e ) ; }
    }
    
    public void refresh() {
        observer.refresh() ;
    }

//  Calls to advance or retard the state  //
////////////////////////////////////////// /

    public void initialize() {
        int status = statusReporter.getStatusCode() ;
        Assert.check( status == StatusProducer.FILE_LOADED ) ;
        try {
            lang.initializeTheState( vms ) ;
            statusReporter.setStatus( StatusProducer.READY,
                                      lang.getName()+" Ready") ; }
        catch (TMException e) {
            if( e.getSourceCoords() != SourceCoords.UNKNOWN ) {
                vms.setDefaultLine( e.getSourceCoords() ) ; }
            else {
                e.setSourceCoords( vms.getCurrentCoords() ) ; }
                
            statusReporter.setStatus( StatusProducer.EXECUTION_FAILED,
                                      lang.getName()+"Error: "+e.getMessage() ) ;
            statusReporter.attention( e.getMessage() ) ;
            // Dump the exception
            Debug.getInstance().msg( Debug.EXECUTE, e ) ;
            return ;
        }
        catch( Throwable e ) {
            statusReporter.setStatus( StatusProducer.EXECUTION_FAILED,
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
        if( autoStepTimer != null ) return ;
        vms.undo() ;
        refresh() ; }

    public void microStep() {
        if( autoStepTimer != null ) return ;
        vms.checkpoint() ;
        stepForward ( new microStepTest() ) ; 
        refresh() ;}

    public void goForward() {
        if( statusReporter.getStatusCode() != StatusProducer.READY ) return ;
        if( autoStepTimer != null ) return ;
        vms.checkpoint() ;
        stepForward ( new goForwardTest() ) ; 
        refresh() ;}

    private void initialSteps() {
        Assert.check( statusReporter.getStatusCode() == StatusProducer.READY )  ;
        if( autoStepTimer != null ) return ;
        vms.checkpoint() ;
        stepForward ( new initialTest() ) ; 
        refresh() ;}

    public void intoExp() {
        if( statusReporter.getStatusCode() != StatusProducer.READY ) return ;
        if( autoStepTimer != null ) return ;
        vms.checkpoint() ;
        stepForward( new intoExpTest() ) ;
        refresh() ; }

    public void intoSub() {
        if( statusReporter.getStatusCode() != StatusProducer.READY ) return ;
        if( autoStepTimer != null ) return ;
        vms.checkpoint() ;
        stepForward( new intoSubTest() ) ;
        refresh() ; }

    public void overAll() {
        if( statusReporter.getStatusCode() != StatusProducer.READY ) return ;
        if( autoStepTimer != null ) return ;
        vms.checkpoint() ;
        stepForward( new overAllTest() ) ;
        refresh() ; }

    public void toCursor(String fileName, int lineNumOneBased) {
        if( statusReporter.getStatusCode() != StatusProducer.READY ) return ;
        if( autoStepTimer != null ) return ;
        vms.checkpoint() ;
        stepForward( new toCursorTest(fileName, lineNumOneBased) ) ;
        refresh() ; }
    
    public void autoStep() {
        if( statusReporter.getStatusCode() != StatusProducer.READY ) return ;
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
                case StatusProducer.EXECUTION_COMPLETE:
                case StatusProducer.EXECUTION_FAILED:
                    break RUNLOOP ;
                case StatusProducer.READY:
                    if( vms.getEvaluationState() != vms.EVALUATION_STATE_RUNNING )
                        break RUNLOOP ;
                    else
                        break ;
                default: Assert.check( false ) ; } }
        refresh() ; }

    public void stop( ) {
        if( autoStepTimer != null ) {
            autoStepStopRequested = true ; }
        else if( statusReporter.getStatusCode() == StatusProducer.BUSY_EVALUATING ) {
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

    public int getNumSelectedCodeLines(TMFile tmFile, boolean allowGaps) {
        return vms.getCodeStore().getNumSelectedCodeLines( tmFile, allowGaps ) ;
    }

    public CodeLine getSelectedCodeLine(TMFile tmFile, boolean allowGaps, int index) {
        return vms.getCodeStore().getSelectedCodeLine( tmFile, allowGaps, index ) ;
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
        long now = System.currentTimeMillis() ;
        //System.out.println("Step: "+now ) ;
        if( autoStepStopRequested  ) {
            killOffTheTimer() ;  }
        else {
            stepForward ( new intoSubTest() ) ;
            int status = statusReporter.getStatusCode() ;
            if( status == StatusProducer.READY 
            && vms.getEvaluationState() == vms.EVALUATION_STATE_RUNNING) {
                //System.out.println( "next delay = "+delay) ;
                autoStepTimer.setDelay( delay ) ; }
            else {
                killOffTheTimer() ; } } 
        refresh() ;
    }

    private void stepForward( StopTest stopTest) {
        // Step until the stopTest object says to stop.
        int status = statusReporter.getStatusCode() ;
        Assert.check( status == StatusProducer.READY )  ;
        String langName = lang.getName() ;
        try {
            // TODO This limit should be configurable.
            final int SAFETY_COUNTER_LIMIT = 50000000 ;
            vms.setDatumFocus( null ) ;
            int safetyCounter = 0 ;
            int initStackSize = vms.stackSize() ;
            Evaluation initialTop = vms.isEmpty() ? null : vms.top() ;
            TMFile latestFile = null ;
            vms.setEvaluationState( vms.EVALUATION_STATE_RUNNING ) ;
            statusReporter.setStatus( StatusProducer.BUSY_EVALUATING, langName +" Evaluating" ) ;

            // Advance until stop0
            while(   ! vms.isEmpty()
                  && vms.getEvaluationState() == vms.EVALUATION_STATE_RUNNING
                  && safetyCounter < SAFETY_COUNTER_LIMIT
                  && ! stopTest.stop0(vms, initStackSize, initialTop) ) {
                latestFile = getCodeFocus().getFile() ;
                advance() ;
                if( safetyCounter % 10000 == 0 ) Debug.getInstance().msg(Debug.DISPLAY, "safetyCounter: "+safetyCounter) ;
                ++ safetyCounter ; }

            // Optional extra advance
            if(       ! vms.isEmpty()
                   && vms.getEvaluationState() == vms.EVALUATION_STATE_RUNNING
                   && safetyCounter < SAFETY_COUNTER_LIMIT
                   && stopTest.midstep(vms, initStackSize, initialTop) ) {
                latestFile = getCodeFocus().getFile() ;
                advance() ; }

            // Advance until stop1
            while(   ! vms.isEmpty()
                  && vms.getEvaluationState() == vms.EVALUATION_STATE_RUNNING
                  && safetyCounter < SAFETY_COUNTER_LIMIT
                  && ! stopTest.stop1(vms, initStackSize, initialTop) ) {
                latestFile = getCodeFocus().getFile() ;
                advance() ;
                if( safetyCounter % 10000 == 0 ) Debug.getInstance().msg(Debug.EXECUTE, "safetyCounter: "+safetyCounter) ;
                ++ safetyCounter ; }
            
            if( vms.isEmpty() ) {
                statusReporter.setStatus( StatusProducer.EXECUTION_COMPLETE,
                                          langName+" Execution complete") ;
                if( latestFile != null ) {
                    SourceCoords lastLine = vms.getCodeStore().getCoordsOfLastLine(latestFile) ;
                    vms.setDefaultLine( lastLine ); } }
            else if( vms.getEvaluationState() != vms.EVALUATION_STATE_RUNNING ) {
                switch( vms.getEvaluationState() ) {
                case VMState.EVALUATION_STATE_NEEDINPUT :
                    // Prompt the user with a nonmodal input frame.
                    InputFrame inputFrame = new InputFrame( this ) ;
                    statusReporter.setStatus( StatusProducer.READY,
                            langName+" Waiting for input") ;
                    break;
                case VMState.EVALUATION_STATE_TERMINATED:
                    statusReporter.setStatus( StatusProducer.EXECUTION_COMPLETE,
                            langName+" Program has self terminated") ;
                } }
            else if( safetyCounter >= SAFETY_COUNTER_LIMIT ) {
                statusReporter.setStatus( StatusProducer.READY,
                                          langName+" Step limit exceeded. Possible infinite loop.") ; }
            else {
                statusReporter.setStatus( StatusProducer.READY,
                                          langName+" Ready") ; } }
        catch( tm.utilities.TMException e ) {
            if( e.getSourceCoords() != SourceCoords.UNKNOWN ) {
                vms.setDefaultLine( e.getSourceCoords() ) ; }
            else {
                e.setSourceCoords( vms.getCurrentCoords() ) ; }
            e.printStackTrace( System.out ) ;
            statusReporter.setStatus( StatusProducer.EXECUTION_FAILED,
                                      langName+" Error: "+e.getMessage() ) ;
            statusReporter.attention( e.getMessage(), e ) ;
            // vms.undo() ; // 2011 July 4. Not sure why we used to do an undo here. TSN.
        }
        catch( Throwable e ) {
            statusReporter.setStatus( StatusProducer.EXECUTION_FAILED,
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
                && vms.getEvaluationState() == vms.EVALUATION_STATE_RUNNING) {
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
}