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

import tm.clc.datum.AbstractIntDatum;
import tm.interfaces.Datum;
import tm.interfaces.SourceCoords;
import tm.utilities.Assert;
import tm.virtualMachine.ExpressionEvaluation;
import tm.virtualMachine.VMState;

public class StatBranch extends StatementNode {
    StatementNodeLink trueLink, falseLink ;
    ExpressionNode exp ;

    public StatBranch (SourceCoords coords, int varDepth, ExpressionNode e) {
        super ("branch", coords, varDepth) ;
        trueLink = new StatementNodeLink () ;
        falseLink = new StatementNodeLink () ;
        exp = e ;
    }

    public StatementNodeLink onTrue () { return trueLink ; }

    public StatementNodeLink onFalse () { return falseLink ; }



    /* States:
        null  --- ready to begin
          |
          | Push a new expression evaluator.
          V
        Integer(0) ---- Expression has been computed
          |
          | Eliminate all temporaries used to eval the expression
          | Select the next node.
          V
        null
    */

    public void step( VMState vms) {
        if( vms.top().at(this) == null ) {

            // First step
            trimVariables( varDepth, vms ) ;
            vms.top().map(this, new Integer(0)) ;
            setScratchMark( vms ) ;
            ExpressionEvaluation ee = new ExpressionEvaluation( vms, exp ) ;
            vms.push( ee ) ; }

        else {

            // Second step

            Datum exprValue = vms.getExpressionResult() ;
            Assert.apology( exprValue instanceof AbstractIntDatum,
                            "Internal error, abstract int expected" ) ;
            AbstractIntDatum intExprValue = (AbstractIntDatum) exprValue ;
            boolean isTrue =  intExprValue.getValue() != 0 ;

            trimScratchVars( vms ) ;

            vms.top().map(this, null) ;

            if( isTrue ) {
                vms.top().setSelected( onTrue().get() ) ; }
            else {
                vms.top().setSelected( onFalse().get() ) ; } } }

    public void beVisited( StatementNodeVisitor visitor ) {
        visitor.visit( this ) ;
        trueLink.beVisited( visitor ) ;
        falseLink.beVisited( visitor ) ;
    }

    public String toString( Hashtable h ) {
        return "    (" + h.get( this ) + ") StatBranch line="+getCoords()+" depth="+getVarDepth()+"\n"
               +exp.ppToString(4, 80)
             + "      true ---> "+formatLink( trueLink, h )+"\n"
             + "      false --> "+formatLink( falseLink, h )+"\n" ;
    }
}