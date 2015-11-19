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

package tm.cpp.analysis;

import java.util.Stack ;

import tm.clc.ast.ExpressionNode;
import tm.clc.ast.FinallyRecoveryFactory;
import tm.clc.ast.JumpRecoveryFactory;
import tm.clc.ast.StatBranch;
import tm.clc.ast.StatDecl;
import tm.clc.ast.StatDo;
import tm.clc.ast.StatEndCatch;
import tm.clc.ast.StatEndFinally;
import tm.clc.ast.StatJoin;
import tm.clc.ast.StatJump;
import tm.clc.ast.StatPopRecovery;
import tm.clc.ast.StatPushRecovery;
import tm.clc.ast.StatReturn;
import tm.clc.ast.StatStartFinally;
import tm.clc.ast.StatSwitch;
import tm.clc.ast.StatementNode;
import tm.clc.ast.StatementNodeLink;
import tm.interfaces.SourceCoords;
import tm.utilities.Assert;



/** Build the statement nodes for a C++ program.
 * @author theo
 */
/**
 * @author theo
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Cpp_StatementManager {

	  
    // Switch statements and loops. We need to keep track of
    // the innermost switch statement, the innermost break target
    // and the innermost continue target.
	private Stack switchStack = new Stack();      // Stack of nested Switch statements
    private Stack breakLabelStack = new Stack();
    private Stack continueLabelStack = new Stack() ;
    private Stack recoveryFactoryStack = new Stack() ;
    private int labelCounter = 0;
	  
    Cpp_StatementManager() {
    }
	  
    StatementNodeLink startFunctionBody( StatementNodeLink p, SourceCoords coords ) {
    	Assert.check( switchStack.empty() ) ;
		Assert.check( breakLabelStack.empty() ) ;
		Assert.check( continueLabelStack.empty() ) ;
		Assert.check( recoveryFactoryStack.empty() ) ;
        // a push recovery node at the top
            StatPushRecovery push = new StatPushRecovery( coords, 0 ) ;
            JumpRecoveryFactory jrf = new JumpRecoveryFactory( "return" ) ;
            recoveryFactoryStack.push( jrf ) ;
            push.addRecovery( jrf );
            p.set( push );
        return push.next() ;    
    }
    
    
    /** The function epilogue is where return statements jump to.
     * Typically the postlog is empty, so the next call is to
     * endFunctionBody. But for destructors, the epilogue contains
     * calls to destructors of subobjects and members.
     * @param p
     * @param coords
     */
    StatementNodeLink startFunctionEpilogue( StatementNodeLink p, SourceCoords coords ) {
    	Assert.check( switchStack.empty() ) ;
		Assert.check( breakLabelStack.empty() ) ;
		Assert.check( continueLabelStack.empty() ) ;
		Assert.check( recoveryFactoryStack.size()==1 ) ;
        // A pop recovery and join node 
		    StatPopRecovery pop = new StatPopRecovery( coords, 0 ) ;
		    p.set( pop );
		    p = pop.next() ;
		    StatJoin joinNode = new StatJoin( coords, 0 ) ;
		    p.set( joinNode );
		// Set the join as target of the jrf
		    JumpRecoveryFactory jrf = (JumpRecoveryFactory) recoveryFactoryStack.pop() ;
            jrf.next().set( joinNode ) ;
            return joinNode.next() ;
    }
     
    void endFunctionBody( StatementNodeLink p, SourceCoords coords ) {
    	Assert.check( switchStack.empty() ) ;
		Assert.check( breakLabelStack.empty() ) ;
		Assert.check( continueLabelStack.empty() ) ;
		Assert.check( recoveryFactoryStack.empty() ) ;
        // A return node at the bottom
		    StatReturn returnNode = new StatReturn( coords, 0 ) ;
		    p.set( returnNode );
    }
      
    StatementNodeLink startBreakRegion(
    		    StatementNodeLink p,
                SourceCoords coords,
			    int depth,
			    boolean hasContinue ) {

        // Create a StatPushRecovery node
            StatPushRecovery spr = new StatPushRecovery( coords, depth ) ;
            p.set( spr ) ;

        // Add RecoveryFactory objects to the spr node.
            String label = (labelCounter++) + "" ;
            
            if( hasContinue ) {
            	continueLabelStack.push( label ) ;
                JumpRecoveryFactory continueFactory = new JumpRecoveryFactory( "continue "+label ) ;
                recoveryFactoryStack.push( continueFactory ) ;
                spr.addRecovery( continueFactory );
            }
            
            breakLabelStack.push( label );
            JumpRecoveryFactory breakFactory = new JumpRecoveryFactory( "break "+label ) ;
            recoveryFactoryStack.push( breakFactory ) ;
            spr.addRecovery( breakFactory ) ;
            
        // Return the next link
            return spr.next() ;
    }
    
    StatementNodeLink endBreakRegion(
    		StatementNodeLink p,
			SourceCoords coords,
			int depth,
			boolean hasContinue ) {
    	
        // Create a StatPopRecovery node
            StatPopRecovery pop = new StatPopRecovery(coords, depth) ;
            p.set( pop ) ;
        
        // Pop the stacks
            recoveryFactoryStack.pop() ;
            breakLabelStack.pop() ;
            
            if( hasContinue ) {
            	recoveryFactoryStack.pop() ;
                continueLabelStack.pop() ; }
        
        return pop.next() ;
    }
    
    void setBreakTarget( StatementNode target ) {
    	JumpRecoveryFactory jrf = (JumpRecoveryFactory) recoveryFactoryStack.peek() ;
    	jrf.next().set( target ) ;
    }
    
    void setContinueTarget( StatementNode target ) {
    	JumpRecoveryFactory jrf = (JumpRecoveryFactory) recoveryFactoryStack.elementAt( recoveryFactoryStack.size()-2) ;
    	jrf.next().set( target ) ;
    }
    
    void startSwitchStatement(
    		StatementNodeLink p,
			SourceCoords coords,
			int depth,
			ExpressionNode e,
			StatJoin exit) {
    	StatSwitch switch_node = new StatSwitch( coords, depth, e, exit.next() ) ;
    	p.set( switch_node ) ;
        switchStack.addElement( switch_node ) ;
    }
    
    void endSwitchStatement( ) {
    	switchStack.pop() ;
    }
    
    void addCaseToSwitch(
    		StatementNodeLink p,
			int depth,
			ExpressionNode e ) {
    	Assert.error( switchStack.size() != 0, "Case outside of switch") ;
    	StatSwitch switchNode = (StatSwitch) switchStack.peek() ;
    	Assert.apology( switchNode.getVarDepth() >= depth,
    			        "Sorry: Jump over variable declaration is not allowed in TM" ) ;
    	Assert.check( switchNode.getVarDepth() == depth ) ;
    	switchNode.newCase( e.get_integral_constant_value(), p ) ;
    }
    
    void addDefaultToSwitch(
    		StatementNodeLink p,
			int depth ) {
    	Assert.error( switchStack.size() != 0, "Case outside of switch" ) ;
    	StatSwitch switchNode = (StatSwitch) switchStack.peek() ;
    	Assert.apology( switchNode.getVarDepth() >= depth,
    			        "Sorry: Jump over variable declaration is not allowed in TM" ) ;
    	Assert.check( switchNode.getVarDepth() == depth ) ;
    	Assert.error( ! switchNode.hasDefault(), "Switch already has default") ;
    	switchNode.addDefault( p ) ;
    }
    
    void makeBreak(
    		StatementNodeLink p,
			SourceCoords coords,
			int depth ) {
    	Assert.error( ! breakLabelStack.empty(), "break statement must be within a loop or switch") ;
    	Object label = breakLabelStack.peek() ;
    	StatJump jump = new StatJump( coords, depth, "break "+label ) ;
    	p.set( jump ) ;
    }
    
    void makeContinue(
    		StatementNodeLink p,
			SourceCoords coords,
			int depth ) {
    	Assert.error( ! continueLabelStack.empty(), "continue statement must be within a loop") ;
    	String label = (String) continueLabelStack.peek() ;
    	StatJump jump = new StatJump( coords, depth, "continue "+label ) ;
    	p.set( jump ) ;
    }
    
    void makeReturn(
    		StatementNodeLink p,
			SourceCoords coords,
			int depth ) {
    	StatJump jump = new StatJump( coords, depth, "return" ) ;
    	p.set( jump ) ;
    }
    
    StatementNodeLink startLocalScope(StatementNodeLink p, SourceCoords coords, int depth) {
    	return p ;
    }
    
    StatementNodeLink endLocalScope(StatementNodeLink p, SourceCoords coords, int depth) {
    	return p ;
    }
    
    StatementNodeLink startDestructorScope( StatementNodeLink p,
                                            SourceCoords coords,
			                                int depth ) {
    	// Put a pushRecovery at the start of the block
		    StatPushRecovery pushRecovery = new StatPushRecovery(coords, depth) ;
		    p.set( pushRecovery ) ; p = pushRecovery.next() ;
        // Build and connect a recovery factory
		    FinallyRecoveryFactory frf = new FinallyRecoveryFactory() ;
	        pushRecovery.addRecovery( frf ) ;
            recoveryFactoryStack.push( frf ) ;
        
        return p ;
    	
    }
    
    StatementNodeLink startDestructorCode( StatementNodeLink p,
                                            SourceCoords coords,
			                                int depth ) {
    	// Emit statement nodes that go before the destructor call 
	        StatPopRecovery popRecovery = new StatPopRecovery(coords, depth) ;
	        p.set( popRecovery ) ; p = popRecovery.next() ;
	        StatStartFinally startFinally = new StatStartFinally(coords, depth) ;
	        p.set( startFinally ) ; p = startFinally.next() ;
	        StatJoin join = new StatJoin(coords, depth) ;
	        p.set( join ) ; p = join.next() ;
	    // Hook in the recovery factory for the destructor block
	        FinallyRecoveryFactory frf = (FinallyRecoveryFactory) recoveryFactoryStack.pop() ;
	        frf.next().set( join ) ;
	    
	        return p ; }
    
    StatementNodeLink endDestructorBlock(
    		StatementNodeLink p,
            SourceCoords coords,
		    int depth ) {
		StatEndFinally endFinally = new StatEndFinally(coords, depth) ; 
		p.set( endFinally ) ; p = endFinally.next() ;
		return p ;
    }
    
    StatementNodeLink makeDo(
    		StatementNodeLink p,
			SourceCoords coords,
			int depth,
			ExpressionNode e ) {

	StatDo ddo = new StatDo( coords, depth, e );
	p.set( ddo ) ;
	return ddo.next() ; }
}