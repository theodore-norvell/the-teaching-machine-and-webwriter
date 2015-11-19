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
 * StatementWalker.java
 *
 * Created on May 2, 2003, 9:15 AM
 */

package tm.javaLang.parser;
import java.util.Stack;

import tm.clc.analysis.Declaration;
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
import tm.clc.ast.TypeNode;
import tm.clc.ast.VarNode;
import tm.interfaces.SourceCoords;
import tm.javaLang.analysis.Java_CTSymbolTable;
import tm.javaLang.ast.CatchRecoveryFactory;
import tm.javaLang.ast.StatThrow;
import tm.javaLang.ast.TyClass;
import tm.javaLang.ast.TyFun;
import tm.javaLang.ast.TyJavaArray;
import tm.javaLang.ast.TyVoid;
import tm.utilities.Assert;
import tm.utilities.Debug;
import tm.utilities.TMException;
import tm.virtualMachine.VMState;

/**
 * Part of the fourth pass. StatementWalkers walk the parse tree for an outer code block
 * (a method or constructor body or an initialization block), generating the Abstract
 * Syntax Tree (which should really be called the Abstract Code Graph) for the block.
 * 
 * It delegates processing of expressions to separate ExpressionWalker objects
 * 
 * @author  mpbl
 */
public class StatementWalker implements JavaParserTreeConstants {
    private Stack depthStack = new Stack();
    private Stack switchStack = new Stack();      // Stack of nested Switch statements
    private Stack breakLabelStack = new Stack();
    private Stack continueLabelStack = new Stack() ;
    private Stack breakMustUseNameStack = new Stack() ;
    private Java_CTSymbolTable symtab;
    private ExpressionWalker eWalker;
    private TyFun functionType ; // Could be null if statement is not in a method or constructor
    private int labelCounter = 0;
    
    final static byte EXPLICIT_THIS_CALL=0, EXPLICIT_SUPER_CALL=1, IMPLICIT_SUPER_CALL=2, NO_CALL=3 ; 
    

    /** Creates a new instance of StatementWalker for a method or contructor body
     * @param st the compile time symbol table
     * @param eb the expression builder
     * @param funType The type of the method or the constructor
     * */
    public StatementWalker(Java_CTSymbolTable st, TyFun funType) {
        symtab = st;
        eWalker = new ExpressionWalker(st);
        functionType = funType ;
    }

    /** Creates a new instance of StatementWalker for an initialization block
     * @param st the compile time symbol table
     * @param eb the expression builder
     * */
    public StatementWalker(Java_CTSymbolTable st) {
        this( st, null ) ;
    }

    /** Recursive processing of statement nodes
     * @param nStart the parserTree node which acts as the root for the current
     * 			statement
     * @param top the link to the statement node that is at the top of
     * 			the current sequence
     * @param vms the Virtual Machine State
     * @return the link to the statement node that represents the bootom of
     * 			the generated sequence
     */
    StatementNodeLink process(Node nStart, StatementNodeLink top, VMState vms ) {
        return process( nStart, top, vms, null ) ; }

    /** Recursive processing of statement nodes for labelled statements
     * @param nStart the parserTree node which acts as the root for the current
     * 			statement
     * @param top the link to the statement node that is at the top of
     * 			the current sequence
     * @param vms the Virtual Machine State
     * @param label the unique label for the statement
     * @return the link to the statement node that represents the bootom of
     * 			the generated sequence
     */
    StatementNodeLink process(Node nStart, StatementNodeLink top, VMState vms, String label ) {
        SimpleNode n = (SimpleNode) nStart;
        StatementNodeLink bottom ;
        int depth = currentDepth();
        {
            Debug.getInstance().msg(Debug.COMPILE,"Line #" + n.getCoords().getLineNumber()+
                ": Statement Walking in n.id = " + jjtNodeName[n.id] +
                "    n.name = " + ((n.getDecl()==null) ? "" : n.getDecl().getName().toString()));
        }

        try {
            switch(n.id) {
            case JJTMETHODDECLARATION:
            case JJTCONSTRUCTORDECLARATION: {
                /* Most of the processing already handled in fourth pass. This just has to
                 * process its children
                 */
                enterScope(0);
                
                boolean isCompilerGeneratedConstructor = n.id==JJTCONSTRUCTORDECLARATION && n.getBoolean() ;
                
                if( isCompilerGeneratedConstructor ) {
                    SimpleNode classNode = (SimpleNode) n.jjtGetParent().jjtGetParent() ;
                    Declaration decl = classNode.getDecl() ;
                    TyClass theClass = (TyClass)decl.getType() ;
                    StatementNodeLink bodyTop = new StatementNodeLink() ;
                    if( theClass.getDirectSuperClass() == null ) {
                        bottom = bodyTop ; }
                    else {
                        ExpressionNode exp = eWalker.implicitSuperConstructorCall(decl.getName()) ;
                        bottom = makeSuperCall( exp, vms, n.getCoords(), bodyTop); }
                    bottom = makeObjectInitializationStatement(n.getCoords(), bottom, theClass);
                    generateMethodPrologueAndEpilogue( top, n.getCoords(),
                            depth, vms, bodyTop, bottom ) ;
                    bottom = null ;
                }
                else {
                    Assert.check(n.jjtGetNumChildren() > 2, "There is no body "); // body
                    // process the parameters
                    bottom = process(((SimpleNode)n.jjtGetChild(0)).jjtGetChild(0), top, vms);
                    // process the body
                    bottom = process(n.jjtGetChild(2), bottom, vms);
                }
                exitScope();
            }
            break;

            case JJTFORMALPARAMETERS: {
                bottom = top;
                for (int i = 0; i < n.jjtGetNumChildren(); i++) {
                    SimpleNode parameter = (SimpleNode)n.jjtGetChild(i);
                    bottom = processParameter( parameter, i, bottom ) ;
                }
            }
            break;

            case JJTMETHODBODY: {
                
                StatementNodeLink bodyTop = new StatementNodeLink() ; 
                bottom = processChildSequence(n, bodyTop, vms);
                
                generateMethodPrologueAndEpilogue( top, n.getCoords(),
                        depth, vms, bodyTop, bottom ) ;
                bottom = null ;
            }
            break;


            case JJTCONSTRUCTORBODY: {
                StatementNodeLink bodyTop = new StatementNodeLink() ; 
            
           
                // The class is needed later.
                    SimpleNode classNode = (SimpleNode) n.jjtGetParent().jjtGetParent().jjtGetParent() ;
                    Declaration decl = classNode.getDecl() ;
                    TyClass theClass = (TyClass)decl.getType() ;
                    
                // Figure out what sort of this or super call is needed.
                    int kind ;
                    int indexOfFirstStatement ;
                    if (n.getBoolean()) {
                        indexOfFirstStatement = 1 ;
                        kind = ((SimpleNode)n.jjtGetChild(0)).id == JJTTHISEXPLICITCONSTRUCTORINVOCATION
                             ? EXPLICIT_THIS_CALL : EXPLICIT_SUPER_CALL ; }
                    else {
                        // In this case there is an implicit call to super,
                        // Except in the case of java.lang.Object.
                        indexOfFirstStatement = 0 ;
                        if( theClass.getDirectSuperClass() == null ) {
                            // This should only be the case for java.lang.Object
                            kind = NO_CALL ; }
                        else {
                            kind = IMPLICIT_SUPER_CALL ; } }
                    
                // Build the call to another constructor (super or this)
                    if( kind != NO_CALL ) {
                        // Build the call expressionExpressionNode exp ;
                        ExpressionNode exp ;
                        if( kind == IMPLICIT_SUPER_CALL ) {
                            exp = eWalker.implicitSuperConstructorCall(decl.getName()) ; }
                        else {
                            exp = eWalker.process(n.jjtGetChild(0), vms) ; }
                        // Build the call statement
                        bottom = makeSuperCall(exp, vms, n.getCoords(), bodyTop); }
                    else {
                        bottom = bodyTop ;
                    }
                
                // Initialize this object, unless a call to another constructor has done so
                    if( kind != EXPLICIT_THIS_CALL ) {
                        bottom = makeObjectInitializationStatement(n.getCoords(), bottom, theClass);
                  	}
                    
                
                
                bottom = processChildSequence(n, bottom, indexOfFirstStatement, vms);

                generateMethodPrologueAndEpilogue( top, n.getCoords(),
                        depth, vms, bodyTop, bottom ) ;
                bottom = null ;
            }
            break;

            case JJTLABELEDSTATEMENT: {
                Assert.check( label==null ) ;
                label = n.getString() ;
                // Check that this is a fresh label
                    for( int i=breakLabelStack.size()-1 ; i>=0 ; --i ) {
                        if( breakLabelStack.elementAt(i).equals( label ) )
                            Assert.error( "label is already in scope" ); }
                Node child = n.jjtGetChild(0) ;
                int childKind = ((SimpleNode)child).id ;
                if( childKind == JJTWHILESTATEMENT
                ||  childKind == JJTFORSTATEMENT
                ||  childKind == JJTDOSTATEMENT
                ||  childKind == JJTSWITCHSTATEMENT ) {
                    // These statements generate their own handling
                    // of break.
                    bottom = process( child, top, vms, label ) ; }
                else {
                    // Process the body
                        breakLabelStack.push( label ) ;
                        breakMustUseNameStack.push( new Boolean( true ) ) ;
                        StatementNodeLink firstLink = new StatementNodeLink() ;
                        StatementNodeLink lastLink = process( child, firstLink, vms ) ;
                        breakLabelStack.pop() ;
                        breakMustUseNameStack.pop() ;

                    // Join node at the bottom
                        StatJoin join = new StatJoin( n.getCoords(), depth ) ;

                    // Add StatPushRecovery and StatPopRecovery
                        bottom = addBreakAndContinueHandling( top,
                                                              firstLink,
                                                              lastLink,
                                                              join,
                                                              null,
                                                              label,
                                                              n.getCoords(),
                                                              depth ) ;
                        bottom.set( join ) ;
                    bottom = join.next() ;
                }
            }
            break ;

            case JJTBLOCK:
                enterScope(n.getDecl(), depth);
                bottom = processChildSequence(n, top, vms);
                exitScope();
                break;


            case JJTIFSTATEMENT: {
                ExpressionNode decision = eWalker.ensureBoolean(n.jjtGetChild(0), vms ) ;
                StatBranch ifNode = new StatBranch(n.getCoords(), depth, decision);
                StatJoin join = new StatJoin(n.getCoords(), depth);   // Artificial comeTogether node
                top.set(ifNode);
                bottom = process(n.jjtGetChild(1), ifNode.onTrue(), vms);  // then clause
                if (bottom != null) // If it is null, last statement was a return
                    bottom.set(join);
                if (n.jjtGetNumChildren() > 2) {
                    // 2 tailed if.  Process the else part.
                    bottom = process(n.jjtGetChild(2), ifNode.onFalse(), vms);
                    if (bottom != null) bottom.set(join); }
                else {
                    // One tailed if.  Terminate the else link.
                    ifNode.onFalse().set( join ); }

                bottom = join.next();
            }
            break;

            case JJTWHILESTATEMENT: {
                // Push the label on the break and continue label stacks
                    if( label==null ) label = (labelCounter++) + "" ;
                    breakLabelStack.push( label ) ;
                    breakMustUseNameStack.push( new Boolean( false ) ) ;
                    continueLabelStack.push( label ) ;

                // Code the body
                    StatementNodeLink bodyTop = new StatementNodeLink() ;
                    StatementNodeLink loopBottom = process(n.jjtGetChild(1), bodyTop, vms);  // loop body

                // Pop the labels
                    breakLabelStack.pop( ) ;
                    continueLabelStack.pop( ) ;
                    breakMustUseNameStack.pop( ) ;

                // Branch node on top.
                    ExpressionNode decision = eWalker.ensureBoolean(n.jjtGetChild(0), vms) ;
                    StatBranch branch = new StatBranch(n.getCoords(), depth, decision);
                    top.set( branch ) ;

                // Join node on the bottom
                    StatJoin join = new StatJoin( n.getCoords(), depth ) ;
                    branch.onFalse().set( join );

                // Add the StatPushRecovery and StatPopRecovery around the body
                    bottom =  addBreakAndContinueHandling( branch.onTrue(),
                                                           bodyTop,
                                                           loopBottom,
                                                           join,
                                                           branch,
                                                           label,
                                                           n.getCoords(),
                                                           depth ) ;
                // And link it in
                    bottom.set( branch );
                    bottom = join.next() ;
            }
            break;

            case JJTDOSTATEMENT: {
                // Push the label on the break and continue label stacks
                    if( label==null ) label = (labelCounter++) + "" ;
                    breakLabelStack.push( label ) ;
                    breakMustUseNameStack.push( new Boolean( false ) ) ;
                    continueLabelStack.push( label ) ;

                // Code the body
                    StatementNodeLink bodyTop = new StatementNodeLink() ;
                    StatementNodeLink loopBottom = process(n.jjtGetChild(0), bodyTop, vms);  // loop body

                // Pop the labels
                    breakLabelStack.pop( ) ;
                    continueLabelStack.pop( ) ;
                    breakMustUseNameStack.pop( ) ;

                // Join node on top.
                    StatJoin topJoin = new StatJoin( n.getCoords(), depth ) ;
                    top.set( topJoin ) ;

                // Branch and join node on the bottom
                    SimpleNode expn = (SimpleNode) n.jjtGetChild(1) ;
                    ExpressionNode decision = eWalker.ensureBoolean(expn, vms) ;
                    StatBranch branch = new StatBranch(expn.getCoords(), depth, decision);
                    branch.onTrue().set( topJoin );
                    StatJoin bottomJoin = new StatJoin( expn.getCoords(), depth ) ;
                    branch.onFalse().set( bottomJoin );

                // Add the StatPushRecovery and StatPopRecovery around the body
                    bottom =  addBreakAndContinueHandling( topJoin.next(),
                                                           bodyTop,
                                                           loopBottom,
                                                           bottomJoin,
                                                           topJoin,
                                                           label,
                                                           n.getCoords(),
                                                           depth ) ;
                // And link it in
                    bottom.set( branch );
                    bottom = bottomJoin.next() ;
            }
            break;

            case JJTFORSTATEMENT: { // similar to while loop but trickier becuase of optionals
                SimpleNode firstPart = ((SimpleNode)n.jjtGetChild(0)).getBoolean()
                                     ? (SimpleNode) n.jjtGetChild(0).jjtGetChild(0)
                                     : null ;
                SimpleNode secondPart = ((SimpleNode)n.jjtGetChild(1)).getBoolean()
                                      ? (SimpleNode) n.jjtGetChild(1).jjtGetChild(0)
                                      : null ;
                SimpleNode thirdPart = (SimpleNode)n.jjtGetChild(2) ;
                SimpleNode body = (SimpleNode)n.jjtGetChild(3) ;

                enterScope(n.getDecl(), depth);

                // Code the first part which could be missing, a sequence of declarations,
                // or a sequence of expressions
                    StatementNodeLink bottomOfFirstPart ;
                    if( firstPart == null ) {
                        bottomOfFirstPart = top ; }
                    else if( firstPart.getBoolean() ) {
                        // Children are variable declarator nodes
                        bottomOfFirstPart = processChildSequence(firstPart, top, vms) ; }
                    else {
                        bottomOfFirstPart = top ;
                        for( int i=0 ; i<firstPart.jjtGetNumChildren() ; ++i ) {
                            SimpleNode e = (SimpleNode) firstPart.jjtGetChild(i) ;
                            StatDo doNode = new StatDo( e.getCoords(), depth, eWalker.process(e, vms) ) ;
                            bottomOfFirstPart.set( doNode ) ;
                            bottomOfFirstPart = doNode.next() ; } }

                // Code the second part.
                    StatementNodeLink onTrue ;
                    StatementNodeLink onFalse ;
                    if( secondPart == null ) {
                        StatJoin topJoin = new StatJoin( n.getCoords(), currentDepth() ) ;
                        bottomOfFirstPart.set( topJoin );
                        onTrue = topJoin.next() ;
                        onFalse = new StatementNodeLink() ; }
                    else {
                        ExpressionNode decision = eWalker.ensureBoolean(secondPart, vms) ;
                        StatBranch whileNode = new StatBranch( secondPart.getCoords(), currentDepth(),
                                                               decision ) ;
                        bottomOfFirstPart.set( whileNode );
                        onTrue = whileNode.onTrue() ;
                        onFalse = whileNode.onFalse() ; }

                // Code the third part
                    StatementNodeLink topOfThirdPart = new StatementNodeLink() ;
                    StatementNodeLink bottomOfThirdPart = topOfThirdPart ;
                    for( int i=0 ; i<thirdPart.jjtGetNumChildren() ; ++i ) {
                        SimpleNode e = (SimpleNode) thirdPart.jjtGetChild(i) ;
                        StatDo doNode = new StatDo( e.getCoords(), currentDepth(), eWalker.process( e, vms ) ) ;
                        bottomOfThirdPart.set( doNode ) ;
                        bottomOfThirdPart = doNode.next() ; }

                // Loop back to the the top.
                    bottomOfThirdPart.set( bottomOfFirstPart.get() );

                // Push the label on the break and continue label stacks
                    if( label==null ) label = (labelCounter++) + "" ;
                    breakLabelStack.push( label ) ;
                    breakMustUseNameStack.push( new Boolean( false ) ) ;
                    continueLabelStack.push( label ) ;

                // Code the body
                    StatementNodeLink topOfBody = new StatementNodeLink() ;
                    StatementNodeLink bottomOfBody = new StatementNodeLink() ;
                    bottomOfBody = process(body, topOfBody, vms ) ;

                // Pop the labels
                    breakLabelStack.pop( ) ;
                    continueLabelStack.pop( ) ;
                    breakMustUseNameStack.pop( ) ;

                // Join node at the bottom
                    StatJoin join = new StatJoin( n.getCoords(), depth ) ;
                    onFalse.set( join );

                // Add StatPushRecovery and StatPopRecovery around the body
                    StatementNodeLink newBodyOfBottom
                           =  addBreakAndContinueHandling( onTrue,
                                                           topOfBody,
                                                           bottomOfBody,
                                                           join,
                                                           topOfThirdPart.get(),
                                                           label,
                                                           n.getCoords(),
                                                           currentDepth() ) ;
                // Hook up remaining loose ends.
                    newBodyOfBottom.set( topOfThirdPart.get() ) ;

                // Set bottom
                    bottom = join.next() ;

                exitScope();
            }
            break;

            case JJTRETURNSTATEMENT: {
                // If there is a result expression assign it to an ExpResult
                    if( functionType == null ) {
                        Assert.error( "return can not be used in an initialization block" );
                        bottom = null ; }
                    else if (n.getBoolean()) {
                        TypeNode retType = functionType.returnType() ;
                        ExpressionNode returnAssignment
                          = eWalker.returnExpression( n.jjtGetChild(0), retType, vms ) ;
                        StatDo ddo = new StatDo( n.getCoords(), depth, returnAssignment ) ;
                        top.set( ddo );
                        bottom = ddo.next() ; }
                    else {
                        Assert.error( functionType.returnType() == TyVoid.get(),
                                      "return must have value" );
                        bottom = top ; }

                // Now jump to the return node of the routine.
                    StatJump jump = new StatJump( n.getCoords(), depth, "return" ) ;
                    bottom.set( jump );
                    bottom = new StatementNodeLink() ;
            }
            break;

            case JJTTRYSTATEMENT: {
                boolean hasFinally = n.getBoolean() ;
                Node block = n.jjtGetChild(0) ;
                Node catchesList = n.jjtGetChild(1) ;
                SimpleNode finallyBlock = hasFinally ? (SimpleNode) n.jjtGetChild(2) : null ;

                // Start with 1 or 2 push recovery nodes
                    bottom = top ;
                    FinallyRecoveryFactory frf = null ;
                    if( hasFinally ) {
                        StatPushRecovery outerPushRecovery = new StatPushRecovery( n.getCoords(), depth ) ;
                        frf = new FinallyRecoveryFactory() ;
                        outerPushRecovery.addRecovery( frf );
                        top.set( outerPushRecovery ) ;
                        bottom = outerPushRecovery.next() ; }
                    StatPushRecovery innerPushRecovery = new StatPushRecovery( n.getCoords(), depth ) ;
                    bottom.set( innerPushRecovery ) ;
                    bottom = innerPushRecovery.next() ;

                // Now process the body
                    bottom = process( block, bottom, vms ) ;

                // Pop recovery node and a join node
                    StatPopRecovery innerPop = new StatPopRecovery( n.getCoords(), depth ) ;
                    bottom.set( innerPop ) ;
                    bottom = innerPop.next() ;
                    StatJoin innerJoin = new StatJoin( n.getCoords(), depth ) ;
                    bottom.set( innerJoin ) ;
                    bottom = innerJoin.next() ;

                // Now process the catches
                    for( int i=0 ; i < catchesList.jjtGetNumChildren() ; ++i ) {
                        SimpleNode jjtCatch = (SimpleNode) catchesList.jjtGetChild(i) ;
                        SimpleNode jjtParameter = (SimpleNode) jjtCatch.jjtGetChild(0) ;
                        Assert.error( eWalker.checkForThrowable(jjtParameter.getDecl().getType()),
                                      "Parameter for catch must be throwable" ) ;
                        SimpleNode jjtBlock = (SimpleNode) jjtCatch.jjtGetChild(1) ;
                        CatchRecoveryFactory crf = new CatchRecoveryFactory( jjtParameter.getDecl().getType() ) ;
                        innerPushRecovery.addRecovery( crf ) ;
                        enterScope(jjtCatch.getDecl(), depth);
                        StatementNodeLink catchBottom
                          = processParameter( jjtParameter, 0, crf.next() ) ;
                        catchBottom = process( jjtBlock, catchBottom, vms ) ;
                        StatEndCatch endCatch = new StatEndCatch( jjtCatch.getCoords(), depth ) ;
                        catchBottom.set( endCatch );
                        catchBottom = endCatch.next() ;
                        catchBottom.set( innerJoin ) ;
                        exitScope() ; }

                if( hasFinally ) {
                    // Need a StatPopRecovery, a StatStartFinally, and a StatJoin
                        StatPopRecovery outerPopRecovery = new StatPopRecovery( n.getCoords(), depth ) ;
                        bottom.set( outerPopRecovery ) ;
                        bottom = outerPopRecovery.next() ;
                        StatStartFinally startFinally = new StatStartFinally( n.getCoords(), depth ) ;
                        bottom.set( startFinally ) ;
                        bottom = startFinally.next() ;
                        StatJoin outerJoin = new StatJoin( n.getCoords(), depth ) ;
                        frf.next().set( outerJoin ) ;
                        bottom.set( outerJoin ) ;
                        bottom = outerJoin.next() ;

                    // Now code the body of the finally
                        bottom = process( finallyBlock, bottom, vms ) ;

                    // End with a StatEndFinally
                        StatEndFinally endFinally = new StatEndFinally( n.getCoords(), depth ) ;
                        bottom.set( endFinally );
                        bottom = endFinally.next() ;  }
            }
            break ;

            case JJTTHROWSTATEMENT: {
                ExpressionNode expr = eWalker.ensurePointerToThrowable( n.jjtGetChild(0), vms) ;
                StatThrow statThrow = new StatThrow( n.getCoords(),
                                                     depth,
                                                     expr) ;
                top.set( statThrow ) ;
                bottom = statThrow.next() ;
            }
            break ;

            case JJTSYNCHRONIZEDSTATEMENT:
                Assert.apology("Can't handle synchronized yet");
                bottom = null ;
            break;

            case JJTEXPRESSIONSTATEMENT: {
                StatDo statDo = new StatDo(n.getCoords(), currentDepth(), eWalker.process(n.jjtGetChild(0), vms));
                top.set(statDo);
                bottom = statDo.next();
            }
            break;
            
            case JJTASSERTSTATEMENT: {
                // TODO Complete the assert statement
                ExpressionNode decision = eWalker.ensureBoolean(n.jjtGetChild(0), vms) ;
                if( n.getBoolean() ) eWalker.process(n.jjtGetChild(1), vms) ;
            }

            case JJTSWITCHSTATEMENT: {
                // Set the label
                    if( label==null ) label = (labelCounter++) + "" ;
                // First push a recovery group
                    StatPushRecovery push = new StatPushRecovery( n.getCoords(), depth ) ;
                    JumpRecoveryFactory jrf = new JumpRecoveryFactory( "break "+label ) ;
                    push.addRecovery( jrf ) ;
                    top.set( push );
                    bottom = push.next() ;

                // Now a StatSwitch
                    ExpressionNode expr = eWalker.ensureSwitchable( n.jjtGetChild(0), vms ) ;
                    StatementNodeLink originalDefaultLink = new StatementNodeLink();
                    StatSwitch statSwitch = new StatSwitch(n.getCoords(), depth, expr, originalDefaultLink );
                    bottom.set(statSwitch);
                    bottom = new StatementNodeLink() ;

                // Push the label on the break label stacks
                    breakLabelStack.push( label ) ;
                    breakMustUseNameStack.push( new Boolean( false ) ) ;

                // Process the body
                    switchStack.push(statSwitch);

                    for( int childNum = 1 ; childNum < n.jjtGetNumChildren() ; ++childNum ) {
                        SimpleNode child = (SimpleNode) n.jjtGetChild( childNum ) ;
                        SimpleNode switchLabel = (SimpleNode) child.jjtGetChild(0) ;
                        Assert.apology( depth==currentDepth(),
                                        "Sorry, the TM does not support variable declarations that span multiple cases");
                        if( switchLabel.getBoolean() ) {
                            // Not a default node. Has an expression.
                            // Note we do not check for duplicated cases.
                            ExpressionNode caseExpr = eWalker.ensureSwitchable( switchLabel.jjtGetChild(0), vms ) ;
                            Assert.error( caseExpr.is_integral_constant(),
                                          "Case label must be an integral constant" ) ;
                            eWalker.checkAssignableCaseExpr( caseExpr.get_integral_constant_value(), expr.get_type() ) ;
                            statSwitch.newCase(caseExpr.get_integral_constant_value(), bottom); }
                        else {
                            Assert.error( ! statSwitch.hasDefault(), "More than one default for switch" );
                            statSwitch.addDefault( bottom ); }
                        bottom = processChildSequence( child, bottom, 1, vms); }

                    switchStack.pop();

                // Pop the label
                    breakLabelStack.pop() ;
                    breakMustUseNameStack.pop() ;

                // Now pop the recovery group
                    StatPopRecovery pop = new StatPopRecovery( n.getCoords(), depth ) ;
                    bottom.set( pop ) ;
                    bottom = pop.next() ;

                // If the switch has no default then the pop node becomes the default destination
                    originalDefaultLink.set( pop ) ;

                // Finally a join node is the destination for breaks
                    StatJoin join = new StatJoin( n.getCoords(), depth ) ;
                    bottom.set( join );
                    bottom = join.next() ;
                    jrf.next().set( join );
            }
            break;

            case JJTEMPTYSTATEMENT: {
                bottom = top;
            }
            break;

            case JJTBREAKSTATEMENT: {
                String breakLabel ;
                if( n.getBoolean() ) {
                    breakLabel = n.getString() ;
                    search : {
                        for( int i = breakLabelStack.size()-1 ; i >= 0 ; --i ) {
                            if( breakLabelStack.elementAt(i).equals( breakLabel ) )
                                break search ; }
                        Assert.error( "Label "+breakLabel+" is not defined" );
                    } }
                else {
                    // Find the top most label that is for a loop or switch
                    search : {
                        for( int i = breakLabelStack.size()-1 ; i >= 0 ; --i ) {
                            if( ! ((Boolean)breakMustUseNameStack.elementAt(i)).booleanValue() ) {
                                breakLabel = (String) breakLabelStack.elementAt(i) ;
                                break search ; } }
                        Assert.error( "break must be inside a loop or switch." ) ;
                        breakLabel = null ;
                    } }
                StatJump jump = new StatJump( n.getCoords(), depth, "break "+breakLabel ) ;
                top.set( jump );

                bottom = new StatementNodeLink() ;
            }
            break;

            case JJTCONTINUESTATEMENT: {
                String continueLabel ;
                if( n.getBoolean() ) {
                    continueLabel = n.getString() ;
                    search : {
                        for( int i = continueLabelStack.size()-1 ; i >= 0 ; --i ) {
                            if( continueLabelStack.elementAt(i).equals( continueLabel ) )
                                break search ; }
                        Assert.error( "Label "+continueLabel+" is not defined" );
                    } }
                else {
                    Assert.error( continueLabelStack.size() > 0, "continue must be inside a loop" ) ;
                    continueLabel = (String) breakLabelStack.peek() ; }
                StatJump jump = new StatJump( n.getCoords(), depth, "continue "+continueLabel ) ;
                top.set( jump );

                bottom = new StatementNodeLink() ;
            }
            break;

            case JJTVARIABLEDECLARATOR: {
            	// The declaration is actually for the local scope wrapper
                symtab.enterScope(n.getDecl());     // On the symtab, we enter local scope
                // type info has been doubled into the local scope declaration
                TypeNode type = n.getDecl().getType();
                VarNode varNode = new VarNode(n.getName(), type);
                StatDecl decNode = new StatDecl(n.getCoords(), depth, varNode);
                incrDepth();        // Here we increase depth
                top.set(decNode);               	
                bottom = decNode.next();
                if (n.jjtGetNumChildren() > 0) {  // initializer
                    SimpleNode expression = (SimpleNode) n.jjtGetChild(0).jjtGetChild(0);
                    ExpressionNode expr = expression.id == JJTARRAYINITIALIZER ?
                    	eWalker.initArray(n, expression, vms, ((TyJavaArray)type.getBaseType()).getBaseElementType() ) :
                    		eWalker.initialize(n, expression, vms);
                    StatDo statDo = new StatDo( n.getCoords(), currentDepth(), expr);
                    bottom.set(statDo);
                    bottom = statDo.next();
                }
            }
            break;
            
            case  JJTUNMODIFIEDCLASSDECLARATION:
            case JJTUNMODIFIEDINTERFACEDECLARATION: {
            	Assert.apology("Type declarations not handled yet by statementWalker");
            	bottom = top;
            }
            break;
 

            case JJTARRAYINITIALIZER: {
                bottom = top;
                for (int i = 0; i < n.jjtGetNumChildren(); i++) {
                    SimpleNode kid = (SimpleNode)n.jjtGetChild(i);
                    if (n.id == JJTARRAYINITIALIZER)
                        bottom = process(kid,  bottom, vms);
                    else {
                        StatDo statDo = new StatDo(n.getCoords(), currentDepth(),
                        		eWalker.process(kid, vms));
                        bottom.set(statDo);
                        bottom = statDo.next();
                    }
                }
            }
            break;


            default :
                Assert.check( "Unexpected node kind in statement walker: id="+n.id ) ;
                bottom = null ;
            } }
        catch( TMException e ) {
            if( e.getSourceCoords() == SourceCoords.UNKNOWN ) {
                SourceCoords coords = n.getCoords() ;
                e.setSourceCoords( coords ) ; }
            throw e ; }
        catch( RuntimeException e ) {
             SourceCoords coords = n.getCoords( ) ;
             Debug.getInstance().msg(Debug.COMPILE, "Exception thrown while processing line "+coords);
             throw e ; }
        return bottom;
    }

    private StatementNodeLink processParameter( SimpleNode parameter, int position, StatementNodeLink top ) {
        VarNode varNode = new VarNode(
            parameter.getName(), parameter.getDecl().getType());
        StatDecl decNode = new StatDecl(parameter.getCoords(), currentDepth(), varNode);
        incrDepth();        // Here we increase depth
        top.set(decNode);
        StatDo statDo = new StatDo(parameter.getCoords(), currentDepth(),
                eWalker.processParameter(parameter, position) );
        decNode.next().set(statDo);
        return statDo.next();
    }

    private void enterScope(int startingDepth){
        Integer integer = new Integer(startingDepth);
        depthStack.push(integer);
    }

    private void enterScope(Declaration decl, int startingDepth){
        symtab.enterScope(decl);
        enterScope(startingDepth);
     }

    private int currentDepth(){
        return depthStack.isEmpty() ? 0 : ((Integer)depthStack.peek()).intValue();
    }

    private void incrDepth(){
       depthStack.push(new Integer( ((Integer)depthStack.pop()).intValue() + 1));

    }

    private void exitScope(){
        Assert.check(!depthStack.empty(), "Too many exit scopes in " + toString());
        depthStack.pop();
        symtab.exitScope();
    }

    private StatementNodeLink processChildSequence(SimpleNode n, StatementNodeLink top, VMState vms) {
        return processChildSequence(n, top, 0, vms);
    }

    private StatementNodeLink processChildSequence(SimpleNode n, StatementNodeLink top, int s, VMState vms) {
        StatementNodeLink bottom = top;
        for(int i = s; i < n.jjtGetNumChildren(); i++) {
            bottom = process(n.jjtGetChild(i), bottom, vms);
        }
        return bottom;
    }

    /** Add StatPushRecovery and StatPopRecovery nodes around a statement S
     * @param top  A link that is to be set to the StatPushRecovery
     * @param first Indicates the top of the statement S
     * @param last  Indicates the bottom of the statement S. When the statment is empty first==last.
     * @param breakTarget Statement to jump to on break. Must be outside of S.
     * @param continueTarget Statement to jump to on continue. Must be outside of S.
     * @param label. Label for break and continue.
     * @param coords.  Coordinates for the new nodes.
     * @param depth. Variable depth for the new nodes.
     * @return A link representing the bottom of the new statement.
     */
    private StatementNodeLink addBreakAndContinueHandling(
                  StatementNodeLink top,
                  StatementNodeLink first,
                  StatementNodeLink last,
                  StatementNode breakTarget,
                  StatementNode continueTarget,
                  String label,
                  SourceCoords coords,
                  int depth ) {

        // Create a StatPushRecovery node
            StatPushRecovery spr = new StatPushRecovery( coords, depth ) ;
            top.set( spr ) ;

        // Add RecoveryFactory objects to the spr node.
            JumpRecoveryFactory breakFactory = new JumpRecoveryFactory( "break "+label ) ;
            breakFactory.next().set( breakTarget );
            spr.addRecovery( breakFactory ) ;
            if( continueTarget != null ) {
                JumpRecoveryFactory continueFactory = new JumpRecoveryFactory( "continue "+label ) ;
                continueFactory.next().set( continueTarget ) ;
                spr.addRecovery( continueFactory ); }

        // Create a StatPopRecovery node
            StatPopRecovery pop = new StatPopRecovery(coords, depth) ;
            last.set( pop ) ;

        // Link the start of the body to the StatPushRecovery.
        // This must be done after last is set, since first==last is a possibility.
            spr.next().set( first.get() );

        return pop.next() ;
    }
    
    void generateMethodPrologueAndEpilogue(
            StatementNodeLink top,
            SourceCoords coords,
            int depth,
            VMState vms,
            StatementNodeLink bodyTop,
            StatementNodeLink bodyBottom ) {
        // a push recovery node at the top
        StatPushRecovery push = new StatPushRecovery( coords, depth ) ;
        JumpRecoveryFactory jrf = new JumpRecoveryFactory( "return" ) ;
        push.addRecovery( jrf );
        top.set( push );
        
        if( bodyBottom == bodyTop ) {
            top = push.next() ; }
        else {
            push.next().set( bodyTop.get() ) ;
            top = bodyBottom ; }
        
        // A pop recovery and return node at the bottom
        StatPopRecovery pop = new StatPopRecovery( coords, depth ) ;
        top.set( pop );
        top = pop.next() ;
        StatReturn returnNode = new StatReturn( coords, depth ) ;
        top.set( returnNode );
        jrf.next().set( returnNode ) ;
        
    }
    /**
     * @param n
     * @param bottom
     * @param theClass
     * @return
     */
    private StatementNodeLink makeObjectInitializationStatement(SourceCoords coords, StatementNodeLink bottom, TyClass theClass) {
        ExpressionNode exp = new tm.javaLang.ast.ExpInitializeObject(theClass) ;
        StatDo statDo = new StatDo(coords,currentDepth(), exp) ;
        bottom.set( statDo ) ;
        bottom = statDo.next() ;
        return bottom;
    }

    /**
     * @param vms
     * @param n
     * @param bodyTop
     * @return
     */
    private StatementNodeLink makeSuperCall(ExpressionNode exp,
            VMState vms,
            SourceCoords coords,
            StatementNodeLink bodyTop) {
        StatementNodeLink bottom;
        // Add a statement for the call
        StatDo statDo = new StatDo(coords,currentDepth(), exp) ;
        bodyTop.set( statDo ) ;
        bottom = statDo.next() ;
        return bottom;
    }
}