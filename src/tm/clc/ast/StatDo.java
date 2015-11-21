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

import tm.interfaces.SourceCoords;
import tm.utilities.Assert;
import tm.virtualMachine.ExpressionEvaluation;
import tm.virtualMachine.VMState;

public class StatDo extends StatementNode {
    ExpressionNode exp ;
    StatementNodeLink nextLink ;

    public StatDo (SourceCoords coords, int varDepth, ExpressionNode e) {
        super("do", coords, varDepth) ;
        nextLink = new StatementNodeLink() ;
        exp = e ;
    }

    public StatementNodeLink next() { return nextLink ; }

    public void step( VMState vms) {
        if( vms.top().at(this) == null ) {

            // First step
            trimVariables( varDepth, vms ) ;
            //
            // Special case: If there is no expression and the line number is
            // UNKNOWN,
            // go straight to the next node.  This is used for "dummy" nodes
            // generated to represent loop exits and branch convergences.
            //
            if( exp instanceof NoExpNode && getCoords() == SourceCoords.UNKNOWN ) {
                Assert.check("Obselete use of StatDo. Use StatJoin instead please");
                vms.top().map(this, null) ;
                vms.top().setSelected( nextLink.get() ) ; }

            else {

                vms.top().map(this, new Integer(0)) ;
                setScratchMark( vms ) ;
                ExpressionEvaluation ee = new ExpressionEvaluation( vms, exp ) ;
                vms.push( ee ) ; } }

        else {

            // Second step

            trimScratchVars( vms ) ;

            vms.top().map(this, null) ;
            vms.top().setSelected( nextLink.get() ) ; } }

    public void beVisited( StatementNodeVisitor visitor ) {
        visitor.visit( this ) ;
        nextLink.beVisited( visitor ) ;
    }

    public String toString( Hashtable h ) {
        return "    (" + h.get( this ) + ") StatDo line="+getCoords()+" depth="+getVarDepth()+"\n"
               +exp.ppToString(4, 80)
               + "      ---> "+formatLink( nextLink, h )+"\n" ;
    }
}
