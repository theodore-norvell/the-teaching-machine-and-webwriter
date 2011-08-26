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

import java.util.Hashtable ;

import tm.clc.rtSymTab.RT_Symbol_Table;
import tm.interfaces.SourceCoords;
import tm.languageInterface.StatementInterface;
import tm.utilities.Assert;
import tm.virtualMachine.Store;
import tm.virtualMachine.VMState;


public abstract class StatementNode extends Node implements StatementInterface {

    protected int varDepth ; // The number of local variables currenty declared

    private SourceCoords coords ; // The line number of the statement

    public StatementNode(String name, SourceCoords cs, int vd) {
        super(name) ;
        coords = cs ;
        varDepth = vd ; }

    public SourceCoords getCoords() { return coords ; }

    public int getVarDepth () { return varDepth; }

    public void select( VMState vms )  {
        /* Statement selection should not happen */
        Assert.check(false) ; }



    /**   This is where variables are removed when they go out
          of scope.
        <P>	  When we start executing a node, there may be local variables
            on the stack that have just gone out of scope. These must be
            deleted. Therefore it is important to call this routine on the
            first step of any Statement node. With varDepth as the depth
            parameter. */

    protected void trimVariables( int depth, VMState vms ) {
        RT_Symbol_Table symtab = (RT_Symbol_Table) vms.getSymbolTable() ;
        symtab.trimVariables( depth, vms ); }

    protected void setScratchMark( VMState vms ) {
        Store sto = vms.getStore() ;
        sto.setScratchMark() ; }

    protected void trimScratchVars( VMState vms ) {
        Store sto = vms.getStore() ;
        int mark = sto.getScratchMark() ;
        while( sto.scratchSize() > mark ) {
            /* TO BE ADDED: must call the delete method, if the variable is
               has one */
            sto.popScratch() ; }
        sto.unsetScratchMark() ; }

    private boolean uninteresting = false ;

    public void setUninteresting( boolean ui ) { uninteresting = ui ; }

    public boolean isUninteresting() { return uninteresting ; }

    public boolean isStopWorthy() { return false ; }

    abstract public void beVisited( StatementNodeVisitor visitor ) ;

    abstract public String toString( Hashtable h ) ;
    
    protected String formatLink( StatementNodeLink lnk, Hashtable h ) {
        StatementNode stmt = lnk.get() ;
        if( stmt == null ) return "NULL" ;
        else return "(" + h.get(stmt) + ")" ;
    }
}