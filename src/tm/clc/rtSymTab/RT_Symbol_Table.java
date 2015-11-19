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

package tm.clc.rtSymTab;

import java.util.* ;

import tm.backtrack.BTStack;
import tm.backtrack.BTTimeManager;
import tm.clc.analysis.ScopedName;
import tm.clc.ast.AbstractFunctionDefn;
import tm.clc.ast.StatementNode;
import tm.clc.ast.StatementNodeLink;
import tm.clc.ast.Stepper;
import tm.clc.ast.TyAbstractClass;
import tm.clc.datum.AbstractDatum;
import tm.clc.datum.AbstractObjectDatum;
import tm.interfaces.Datum;
import tm.interfaces.STEntry;
import tm.interfaces.ViewableST;
import tm.utilities.Assert;
import tm.utilities.Visitor;
import tm.virtualMachine.Store;
import tm.virtualMachine.VMState;


public class RT_Symbol_Table implements ViewableST {

    private BTTimeManager timeMan ;

    private Hashtable<Object, AbstractFunctionDefn> functions ;

    private Vector<Object> mainFunctionKeys = new Vector() ;
    private Vector<TyAbstractClass> mainFunctionClasses = new Vector() ;

    private Frame globalFrame ;

    private BTStack<AbstractObjectDatum> recipients ; // A stack of recipient objects, null for conventional calls.

    /** A stack of frames representing the "this" objects. */
    private BTStack<Frame> recipientFrames ;


    /** frames --- is used to map names to datums.
       Each run-time scope (frame) is represented by a Frame object which is
       essentially a stack of variable entries. Each variable entry maps a name
       to a Datum.
       <P>   The Frames are linked so each frame points to the one below it on the
       stack representing the statically enclosing scope. For frames associated
       with nonmember functions, this will be the global frame. For member
       functions, this will be the frame representing the recipient objects
       data members.  For frames representing the objects this will be the global
       frame. </p>
       <P> Some invariants:
       <UL><li> recipientFrames is a substack of frames </li>
           <li> The number of nonnull entries in recipients equals
                the number of entries in recipientFrames </li>
       </UL>
    */
    private BTStack<Frame> frames ;


    /** Constructor */
    public RT_Symbol_Table(BTTimeManager tm) {
        timeMan = tm ;
        functions = new Hashtable<Object, AbstractFunctionDefn>() ;
        globalFrame = new Frame(timeMan, null ) ;
        frames = new BTStack<Frame>( timeMan ) ;
        recipientFrames = new BTStack<Frame>( timeMan ) ;
        recipients = new BTStack<AbstractObjectDatum>( timeMan ) ;
        frames.push( globalFrame ) ;
    }

    /** Add function definitions
     *  @param def The definition, including a key field. */
    public void addFunctionDefinition( AbstractFunctionDefn def ) {
        functions.put( def.getKey(), def ) ;
    }

    /** Retrieve function definition. Return null if none found. */
    public AbstractFunctionDefn getFunctionDefn( Object key ) {
        return functions.get( key ) ;  }
    
    public void addMainFunction( AbstractFunctionDefn def, TyAbstractClass tyClass ) {
         mainFunctionKeys.addElement( def.getKey() );
         mainFunctionClasses.addElement( tyClass ) ;
    }

    public Vector<Object> getMainFunctionKeys() {
        return mainFunctionKeys ; }
    
    public Vector<TyAbstractClass> getMainFunctionClasses() {
        return mainFunctionClasses ; }

    /** Static global variables */
    public void addStaticGlobal( ScopedName index, String name, AbstractDatum d)  {
        VarEntry entry = new VarEntry( index, name, d, timeMan ) ;
        globalFrame.addVar( entry ) ; }

    /** How many vars are in the global frame. */
    public int varsInGlobalFrame() {
        return globalFrame.varsInFrame() ; }

    private Vector<String> initializationChainName = new Vector<String>() ;
    private Vector<StatementNodeLink> initializationChainStart = new Vector<StatementNodeLink>() ;
    private Vector<StatementNodeLink> initializationChainEnd = new Vector<StatementNodeLink>() ;

    /** Add a new chain of initialization statements.
    Each statement will be used to build a FunctionEvaluation
    and so it should end with a StatReturn.
    */
    public void newInitializationChain( String chainName ) {
        for( int i=0, sz=initializationChainName.size(); i<sz ; ++i ){
            Assert.check( !chainName.equals( initializationChainName.elementAt(i) ) ) ; }
        initializationChainName.addElement(chainName) ;
        StatementNodeLink link = new StatementNodeLink() ;
        initializationChainStart.addElement( link ) ;
        initializationChainEnd.addElement( link ) ;
    }
    
    

    /** Add a new statement to an initialization chain.
    <P>   Typically only one statement is added at a time.  In this
    case the last_link be the next link of the statement added,
    however a complex graph of statement nodes may also be added
    provided it has a single entry (represented
    by statement) and a single exit (represented by last_link). </P>
    <P> Note that each chain should be terminated by a
        StatReturn. (When adding a StatReturn, the last_link
        parameter can be set to null.)</P>
    @param chainName the name of the chain to be added to.  Must already have been created.
    @param statement the statement to add.
    @param last_link a link out of the statement to add.
    */
    public void addInitializationStatement(
            String chainName,
            StatementNode statement,
            StatementNodeLink last_link ) {

        int i = findInitializationChain(chainName);
        StatementNodeLink link = initializationChainEnd.elementAt(i) ;
        link.set( statement ) ;
        initializationChainEnd.removeElementAt(i) ;
        initializationChainEnd.insertElementAt(last_link, i) ;
    }


    /** Get the index of an initialization chain.
     * @param chainName
     * @return
     */
    private int findInitializationChain(String chainName) {
        int i=0, sz=initializationChainName.size() ;
        for( ; i<sz ; ++i ) {
            if( chainName.equals( initializationChainName.elementAt(i) ) ) {
                break ; } }
        Assert.check( i<sz ) ;
        return i;
    }
    
    public StatementNodeLink getInitializationChain( String chainName ) {
        int i = findInitializationChain( chainName ) ;
        return initializationChainStart.elementAt(i) ; }

    /** Returns an enumeration of statement links */
    public Enumeration getInitializationChains() {
        return new Enumeration() {
            private int i=0 ;
            public boolean hasMoreElements() {
                return i < initializationChainStart.size() ; }
            public Object nextElement() {
                return initializationChainStart.elementAt(i++) ; } } ; }

    /** Begin scope of function parameters for a method call
      * @param recipient null if this is not a nonstatic method call
      */
    public void enterFunction( AbstractObjectDatum recipient ) {

      // Push the recipient onto the recipient stack
          recipients.push( recipient ) ;

      Frame outerFrame ;
      if( recipient != null ) {
        // We push a Frame to represent the recipient object.
            final Frame recipientFrame = new Frame( timeMan, globalFrame ) ;
            frames.push( recipientFrame ) ;
            recipientFrames.push( recipientFrame ) ;

          // Now we add the all the fields including fields
          // of the subobjects, by doing a depth-first walk of
          // the subobject tree.
            recipient.walkSubObjectTree( new Visitor<AbstractObjectDatum>() {
                public void visit( AbstractObjectDatum theSubObject ) {
                    TyAbstractClass tyClass = (TyAbstractClass) theSubObject.getType() ;
                    for( int i=0 ; i < theSubObject.numberOfFields() ; ++i ) {
                        ScopedName scnm = tyClass.getField(i).getName() ;
                        String nm = theSubObject.getDisplayNameForField(i) ;
                        AbstractDatum field = (AbstractDatum) theSubObject.getField(i) ;
                        VarEntry ve = new VarEntry(scnm, nm, field, timeMan) ;
                        recipientFrame.addVar( ve ) ; }
                }
            } ) ;
            outerFrame = recipientFrame ; }
        else {
            outerFrame = globalFrame ; }

        // Now we add a frame for the parameters and locals
            Frame localFrame = new Frame( timeMan, outerFrame ) ;
            frames.push( localFrame ) ;
        /*DBG*/ // System.out.println( "Entering method current Depth = " + varsInCurrentFrame() ) ;
    }

    /* Begin Scope of variables local to a function */
    //public void pushLocalFrame() {
    //    Frame top = (Frame)frames.top() ;
    //    Frame newFrame = new Frame(timeMan, top ) ;
    //    frames.push( newFrame ) ;
    //}

    /** End Scope */
    //public void popFrame() {
        // All local variable must have been destroyed.
    //	Assert.check( ((Frame)frames.top()).size() == 0 ) ;
    //	frames.pop() ; }

    /** Exit Function Scope */
    public void exitFunction() {
        // All parameters and local variables must have been destroyed.
        Assert.check( frames.top().size() == 0) ;
        frames.pop() ;

        // Remove the recipient (every call has a recipient, but it might be null).
        AbstractDatum recipient = recipients.top() ;
        recipients.pop() ;

        // If it was a method call, then remove the
        // frame representing the object.
        if( recipient != null ) {
            frames.pop() ;
            recipientFrames.pop() ; } }

    public int getFunctionCallDepth() {
        return frames.size() ; }

    /** Get the top recipient object ( "this" ) */
    public AbstractObjectDatum getTopRecipient() {
        return recipients.top() ; }

    /** Add a new auto variable to the current stack */
    public void newVar(ScopedName index, String name, AbstractDatum d ) {
        VarEntry entry = new VarEntry( index, name, d, timeMan ) ;
        Frame currentFrame = frames.top() ;
        currentFrame.addVar( entry ) ; }

    /** How many vars are in the current top frame */
    public int varsInCurrentFrame() {
        Frame currentFrame = frames.top() ;
        return currentFrame.varsInFrame() ; }

    /** Delete one variable */
    public void deleteVar() {
        Frame currentFrame = frames.top() ;
        currentFrame.deleteVar() ; }

    /** Get a variable by its index. Returns null if not found */
    private VarEntry lookupVarByIndex( ScopedName index ) {
        Frame f = frames.top() ;
        VarEntry entry = null ;
        while( f != null ) {
            entry = f.lookupByIndex( index ) ;
            if( entry != null ) break ;
            else f = f.staticLink() ; }
        return entry ; }

    private VarEntry lookupRecipientVar(  int[] path, ScopedName scoped_name ) {
        // Get the recipient
            Assert.apology( recipients.top() != null, "Internal error. Method code in non method" ) ;
            AbstractObjectDatum recipient = recipients.top() ;

        // Get its type
            Assert.check( recipient.getType() instanceof TyAbstractClass ) ;
            TyAbstractClass tyClass = (TyAbstractClass) recipient.getType() ;

        // Convert path to a number
        /** @todo When the path contains -1, it doesn't make sense to convert
         * the path to a position. */
            int i = tyClass.convertPathAndScopedNameToPosition( path, scoped_name ) ;
            Assert.apology( i != -1, "Internal error, Bad member name" ) ;

        // Get the frame corresponding to the "this" object
            Frame fr = recipientFrames.top() ;
            VarEntry ve = fr.lookupByNumber(i) ;

        return ve ; }

    /** Control the highlighting of an entry */
    public void setVarHighlight( ScopedName index, int value ) {
        VarEntry entry = lookupVarByIndex( index ) ;
        Assert.apology(entry!=null, "Id "+index+" not in symbol table" ) ;
        entry.putHighlight( value ) ; }

    /** Control the highlighting of a data member */
    public void setRecipientVarHighlight( int[] path, ScopedName scoped_name, int value  ) {
        VarEntry entry = lookupRecipientVar( path, scoped_name ) ;
        Assert.apology(entry!=null, "Id "+scoped_name+" not in symbol table" ) ;
        entry.putHighlight( value ) ; }

    /** Get a variable by name. Returns null if not found */
    public AbstractDatum getDatum( ScopedName index ) {
        VarEntry entry = lookupVarByIndex( index ) ;
        Assert.apology(entry!=null, "Id "+index+" not in symbol table" ) ;
        return entry.getDatum() ; }

    /** Get a variable by name. Returns null if not found */
    public AbstractDatum getDatumFromRecipient( int[] path, ScopedName scoped_name ) {
        VarEntry entry = lookupRecipientVar( path, scoped_name ) ;
        Assert.apology(entry!=null, "Id "+scoped_name+" not in symbol table" ) ;
        return entry.getDatum() ; }


    public void trimVariables( int depth, VMState vms ) {
        /*DBG*/ // System.out.println( "Triming Variables: depth = " + depth + " current = " + varsInCurrentFrame() ) ;
        Store sto = vms.getStore() ;
        while( varsInCurrentFrame() > depth ) {
            /*DBG*/ // System.out.println( "depth = " + depth + " current " + symtab.varsInCurrentFrame() ) ;
            /* TO BE ADDED: must call the delete method, if the variable is
               has one */
            deleteVar() ; // Remove entry from symbol table
            sto.pop() ; } } // Remove the variable from the stack

    /** getEntry -- see the ViewableST interface. */
    public STEntry getEntry(int index) {
        /*DBG System.out.println("SymbolTable.getEntry("+index+")") ;*/
        /* Note this shows the whole symbol table including
           frames that contain invisible variables */
        Assert.check( 0 <= index && index < size() ) ;
        int level = 0 ;
        Frame f ;
        while(true) {
            f = frames.get( level ) ;
            if ( index < f.size() ) break ;
            index -= f.size() ;
            level += 1 ; }
        return f.lookupByNumber( index ).getSTEntry() ; }


    /** getEntryAt --- see the ViewableST interface */
    public STEntry getEntryAt(int address) {
        // HMM HOW TO MAKE THIS MORE EFFICIENT.
        for( int level = frames.size() - 1 ; level >= 0 ; --level ) {
            Frame f = frames.get( level ) ;
            for( int i = 0, sz = f.size() ; i < sz ; ++i ) {
                VarEntry entry = f.lookupByNumber( i ) ;
                Datum d = entry.getDatum() ;
                int daddr = d.getAddress() ;
                if( daddr <= address && address < daddr + d.getNumBytes() ) {
                    return entry.getSTEntry() ; } } }
        return null ; }

    /** size --- the number of entries that getEntry can return */
    public int size() {
        int count = 0 ;
        for( int i=0 , sz = frames.size() ; i < sz ; ++i ) {
            Frame f = frames.get(i) ;
            count += f.size() ; }
        return count ; }
}
