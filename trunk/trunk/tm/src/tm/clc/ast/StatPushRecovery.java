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

import java.util.*;

import tm.interfaces.SourceCoords;
import tm.virtualMachine.VMState;

/**
 * <p>Title: The Teaching Machine</p>
 * <p>Description: </p>
 * <p>Company: Memorial University</p>
 * @author Theodore Norvell
 * @version 1.0
 */

public class StatPushRecovery extends StatementNode {
    StatementNodeLink nextLink = new StatementNodeLink() ;
    Vector /*of Recovery Factory*/ factoryList = new Vector() ;

    public StatPushRecovery(SourceCoords coords, int varDepth) {
        super("push recovery", coords, varDepth);
    }

    public void addRecovery( ClcRecoveryFactory r ) {
        factoryList.addElement( r ) ; }

    public StatementNodeLink next() {
        return nextLink ; }

    public void step( VMState vms) {
        trimVariables( varDepth, vms ) ;

        vms.pushRecoveryGroup( factoryList ) ;

        vms.top().map(this, null) ;
        vms.top().setSelected( nextLink.get() ) ;
    }

    public void beVisited( StatementNodeVisitor visitor ) {
        visitor.visit( this ) ;
        nextLink.beVisited( visitor ) ;
        for( int i = 0, sz = factoryList.size() ; i < sz ; ++i ) {
           ClcRecoveryFactory rec = (ClcRecoveryFactory) factoryList.elementAt( i ) ;
           StatementNodeLink link =  rec.next() ;
           link.beVisited( visitor ) ; }
    }

    public String toString( Hashtable h ) {
        String str = "    (" + h.get( this ) + ") StatPushRecovery line="+getCoords()+" depth="+getVarDepth()+"\n"
          + "      ---> "+formatLink( nextLink, h )+"\n" ;
       for( int i = 0, sz = factoryList.size() ; i < sz ; ++i ) {
           ClcRecoveryFactory rec = (ClcRecoveryFactory) factoryList.elementAt( i ) ;
           StatementNodeLink link =  rec.next() ;
           str +=  "       "+rec.getDescription()+" ---> "+formatLink( link, h )+"\n" ; }
       return str ;
    }
}