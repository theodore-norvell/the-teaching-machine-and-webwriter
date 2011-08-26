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
import tm.virtualMachine.VMState;


public class StatReturn extends StatementNode {

    public StatReturn (SourceCoords coords, int varDepth) {
        super("return", coords, varDepth) ;
    }

    /* States:
        null
          |
          | Deallocate all local variables and parameters.
          | Remove the call from the symbol table
          | Remove the function evaluation
          V
          null
    */

    public void step( VMState vms) {
        // Get the symbol table
            RT_Symbol_Table symtab = (RT_Symbol_Table) vms.getSymbolTable() ;

        // Kill all local variables
            trimVariables( 0, vms ) ;

        // Remove the function's frame(s) from the symbol table.
            symtab.exitFunction() ;

        // Remove the mark on the stack
            vms.getStore().unsetStackMark();

        // Pop the argument list stack
            vms.popArgumentList() ;

        // Remove the Function Evaluation from the evaluation stack
            vms.pop() ; }

    public void beVisited( StatementNodeVisitor visitor ) {
        visitor.visit( this ) ;
    }

    public String toString( Hashtable h ) {
        return "    (" + h.get( this ) + ") StatReturn line="+getCoords()+" depth="+getVarDepth()+"\n";
    }

}
