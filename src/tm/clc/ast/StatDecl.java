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

import tm.clc.datum.AbstractDatum;
import tm.clc.rtSymTab.RT_Symbol_Table;
import tm.interfaces.SourceCoords;
import tm.virtualMachine.VMState;


/** Adds one variable to the stack and the symbol table.

    Note that StatDecl is not concerned with initialization.
    Initialization can be done by a StatDo right after the
    StatDecl.
*/

public class StatDecl extends StatementNode {
    VarNode var ;
    StatementNodeLink nextLink ;

    public StatDecl(SourceCoords coords, int varDepth, VarNode v) {
        super("decl", coords, varDepth) ;
        nextLink = new StatementNodeLink() ;
        var = v ;
    }

    public StatementNodeLink next() { return nextLink ; }

    public VarNode getVar () { return var; }

    /* States:
       null
         |
         | Create and push a datum to represent the variable
         | Add the variable to the symbol table
         | Select the next node.
         V
       null
    */

    public void step( VMState vms) {

        trimVariables( varDepth, vms ) ;

        Clc_ASTUtilities util = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
        AbstractDatum varDatum = util.pushDatum( var.get_type(),
                                                 vms,
                                                 var.getName().getUnqualifiedName () ) ;
        RT_Symbol_Table symtab = ((RT_Symbol_Table) vms.getSymbolTable()) ;
        symtab.newVar( var.getName(), var.getName().getUnqualifiedName(), varDatum ) ;
        vms.top().setSelected( nextLink.get() ) ;
    }

    public void beVisited( StatementNodeVisitor visitor ) {
        visitor.visit( this ) ;
        nextLink.beVisited( visitor ) ;
    }

    public String toString( Hashtable h ) {
        return "    (" + h.get( this ) + ") StatDecl \""+var.getName()+"\" of type "+var.get_type()
              +" line="+getCoords()+" depth="+getVarDepth()+"\n"
              + "      ---> "+formatLink( nextLink, h )+"\n" ;
    }

    public boolean isStopWorthy() { return true ; }
}
