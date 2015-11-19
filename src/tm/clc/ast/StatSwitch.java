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
import java.util.Vector ;

import tm.clc.datum.AbstractIntDatum;
import tm.interfaces.Datum;
import tm.interfaces.SourceCoords;
import tm.utilities.Assert;
import tm.virtualMachine.ExpressionEvaluation;
import tm.virtualMachine.VMState;

public class StatSwitch extends StatementNode {
    private ExpressionNode exp ;
    private Vector constantVector ;
    private Vector linkVector ;
    private StatementNodeLink defaultLink ;
    boolean hasExplicitDefault = false ;

    public StatSwitch(SourceCoords coords, int varDepth, ExpressionNode e, StatementNodeLink nextLink) {
        super("switch", coords, varDepth) ;
        constantVector = new Vector() ;
        linkVector = new Vector() ;
        defaultLink = nextLink ;
        exp = e ; }

    public void newCase( long value, StatementNodeLink link )
    {   constantVector.addElement( new Long(value) ) ;
        linkVector.addElement( link ) ; }

    public boolean hasDefault() { return hasExplicitDefault ; }

    public void addDefault( StatementNodeLink link ) {
        defaultLink = link ;
        hasExplicitDefault = true ; }

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

            long switchValue  ;
            if( exprValue instanceof AbstractIntDatum ) {
                switchValue = ((AbstractIntDatum) exprValue).getValue() ; }
            else {
                Assert.apology( "switch expression must be an integral value\n" ) ;
                switchValue = 0 ; }

            trimScratchVars( vms ) ;

            vms.top().map(this, null) ;

            // Search for match
            int j = -1 ;
            for( int i = 0, sz = constantVector.size() ; i < sz ; ++i ) {
                long caseValue = ((Long) constantVector.elementAt( i )).longValue() ;
                if( switchValue == caseValue ) {
                    j = i ;
                    break ; } }

            // Select a link
            StatementNodeLink link ;
            if( j != -1 ) link = (StatementNodeLink) linkVector.elementAt( j ) ;
            else  link = defaultLink ;

            // Select the node the link points to
            vms.top().setSelected( link.get() ) ; } }

    public void beVisited( StatementNodeVisitor visitor ) {
        visitor.visit( this ) ;
        for( int i = 0, sz = constantVector.size() ; i < sz ; ++i ) {
            StatementNodeLink link = (StatementNodeLink) linkVector.elementAt( i ) ;
            link.beVisited( visitor ); }
        defaultLink.beVisited( visitor ) ;
    }

    public String toString( Hashtable h ) {
        String str = "    (" + h.get( this ) + ") StatSwitch line="+getCoords()+" depth="+getVarDepth()+"\n"
                     +exp.ppToString(4, 80);
        for( int i = 0, sz = constantVector.size() ; i < sz ; ++i ) {
            long caseValue = ((Long) constantVector.elementAt( i )).longValue() ;
            StatementNodeLink link =  (StatementNodeLink) linkVector.elementAt( i ) ;
            str +=  "      "+ caseValue + " ---> "+formatLink( link, h )+"\n" ; }
        return str ;
    }
}
