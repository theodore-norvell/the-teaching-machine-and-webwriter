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

import java.util.Hashtable;

import tm.interfaces.SourceCoords;
import tm.virtualMachine.VMState;

/**
 * <p>Title: The Teaching Machine</p>
 * <p>Description: </p>
 * <p>Company: Memorial University</p>
 * @author Theodore Norvell
 * @version 1.0
 */

public class StatEndFinally  extends StatementNode {
    StatementNodeLink nextLink = new StatementNodeLink() ;

    public StatEndFinally (SourceCoords coords, int varDepth) {
        super("end finally", coords, varDepth);
    }

    public StatementNodeLink next() {
        return nextLink ; }

    public void step( VMState vms) {
        trimVariables( varDepth, vms ) ;
        vms.top().map(this, null) ;
        boolean rethrown = vms.endFinally() ;
        if( ! rethrown ) vms.top().setSelected( nextLink.get() ) ;
    }

    public void beVisited( StatementNodeVisitor visitor ) {
        visitor.visit( this ) ;
        nextLink.beVisited( visitor ) ;
    }

    public String toString( Hashtable h ) {
        return "    (" + h.get( this ) + ") StatEndFinally line="+getCoords()+" depth="+getVarDepth()+"\n"
               + "      ---> "+formatLink( nextLink, h )+"\n" ;
    }
}
