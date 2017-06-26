package tm.clc.ast;

import junit.framework.*;

import tm.backtrack.BTTimeManager ;
import tm.clc.datum.* ;
import tm.cpp.CPlusPlusLang ;
import tm.cpp.ast.* ;
import tm.cpp.datum.* ;
import tm.clc.rtSymTab.RT_Symbol_Table;
import tm.interfaces.* ;
import tm.languageInterface.Language ;
import tm.languageInterface.ExpressionInterface;
import tm.virtualMachine.* ;

abstract public class AbstractAstTest extends TestCase {

    protected boolean print ;
    
    protected VMState vms ;
    protected RT_Symbol_Table symtab ;
    
    protected static final int boStatic = 0 ;
    protected static final int toStatic = 999 ;
    protected static final int boHeap = toStatic+1 ;
    protected static final int toHeap = boHeap+1000-1 ;
    protected static final int boStack = toHeap+1 ;
    protected static final int toStack = boStack+1000-1 ;
    protected static final int boScratch = toStack+1 ;
    protected static final int toScratch = boScratch+1000-1 ;

    public AbstractAstTest(String name) { super (name); }

    protected void setUp () {
        if( print ) {
            System.out.println( "--------------------- Start Test --------------------------" ) ; }
            
        // Create a time manager to synchronize back tracking
            BTTimeManager timeMan = new BTTimeManager() ;
            
        // Create the symbol table
            symtab = new RT_Symbol_Table( timeMan ) ;

        // Create a virtual machine state .
            vms = new VMState(  timeMan,
                                boStatic, toStatic, boHeap, toHeap,
                                boStack, toStack, boScratch, toScratch,
                                symtab ) ;
        // Prepare the VMState
            languageSpecificSetUp() ;
    }
    
    abstract protected void languageSpecificSetUp( ) ;
    
    public void tearDown() {
        if( print ) { 
            System.out.println( "******************End Test******************" ) ; } }

    
    protected void advance( ) {
        advance( 1 ) ; }
        
    protected void advance( int reps ) {
        for( int rep =0 ; rep < reps ; ++rep ) {
            if( vms.isEmpty() ) {
                }
            else if( vms.top().isDone() ) {
                if( vms.top() instanceof ExpressionEvaluation ) {
                    ExpressionEvaluation ee = (ExpressionEvaluation) vms.top() ;
                    Datum d = (Datum) ee.at( ee.getRoot() ) ;
                    vms.setExpressionResult( d ) ; 
                    if( print ) {
                        ExpressionInterface exp = (ExpressionInterface) ee.getRoot() ;
                        String exprString =  exp.toString(vms) ;
                        System.out.println( decode(exprString) ) ;
                        System.out.println( "popping expression "+d ) ; } }
                else if( print ) {
                    System.out.println( "popping statement" ) ; }
                vms.pop() ; }
            else if( vms.top().getSelected() != null ) {
                if( print && vms.top() instanceof ExpressionEvaluation ) {
                    ExpressionInterface exp = (ExpressionInterface) vms.top().getRoot() ;
                    String exprString =  exp.toString(vms) ;
                    System.out.println( decode(exprString) ) ;
                    System.out.print( " Step   => " ) ; }
                else if( print ) {
                    System.out.println( "Stepping statement"+vms.top().getSelected().toString() ) ; }
                vms.top().getSelected().step( vms ) ; }
            else {
                if( print && vms.top() instanceof ExpressionEvaluation ) {
                    ExpressionInterface exp = (ExpressionInterface) vms.top().getRoot() ;
                    String exprString =  exp.toString(vms) ;
                    System.out.println( decode(exprString) ) ;
                    System.out.print( " Select => " ) ; }
                vms.top().getRoot().select( vms ) ; } }
    }
    
    String decode( String str ) {
        StringBuffer buf = new StringBuffer() ;
        for( int i = 0, sz = str.length() ; i < sz ; ++ i ) {
            char c = str.charAt(i);		// Next character
            switch (c) {
                case StateInterface.EXP_START_VALUE:
                    buf.append("<<red:") ;
                    break;
//                case StateInterface.MARKER2:
//                    buf.append("<<ul:") ;
//                    break;
                case StateInterface.EXP_START_SELECTED:
                    buf.append("<<ul:") ;
                    break;
                case StateInterface.EXP_START_LVALUE:
                    buf.append("<<blue:") ;
                    break;
                case StateInterface.EXP_END:
                    buf.append(">>") ;
                    break ;
                default:
                    buf.append(c) ;
            }  // Switch
        }  // For loop
        return buf.toString() ;
    }
}