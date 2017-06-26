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

import tm.clc.datum.AbstractRefDatum;
import tm.displayEngine.OldExpressionDisplay;
import tm.interfaces.Datum;
import tm.interfaces.StateInterface ;
import tm.utilities.Assert;
import tm.virtualMachine.VMState;

abstract public class DefaultExpressionNode extends ExpressionNode
{

    private static char red = StateInterface.EXP_START_VALUE ;
    private static char underline = StateInterface.EXP_START_SELECTED ;
    private static char blue = StateInterface.EXP_START_LVALUE ;
    private static char endEffect = StateInterface.EXP_END ;

    private Selector selector ;

    private Stepper  stepper ;

    private String [] syntax = null ;

    public DefaultExpressionNode(String name, NodeList children) {
        super(name, children) ; }

    public DefaultExpressionNode(String name) {
        super(name) ; }

    public DefaultExpressionNode(String name, Node a ) {
        super(name, a) ; }

    public DefaultExpressionNode(String name, Node a, Node b ) {
        super(name, a, b) ; }

    public DefaultExpressionNode(String name, Node a, Node b, Node c ) {
        super(name, a, b, c) ; }

    public DefaultExpressionNode(String name, Node a, Node b, Node c, Node d ) {
        super(name, a, b, c, d) ; }

    public DefaultExpressionNode(String name, Node a, NodeList args ) {
        super(name, a, args) ; }

    public void set_syntax( String [] syntax ) {
        this.syntax = syntax ; }

    public void set_stepper( Stepper stepper ) {
        this.stepper = stepper ; }

    public void set_selector( Selector selector ) {
        this.selector = selector ; }

    public void step( VMState vms ) {
        Assert.check(stepper != null) ;
        stepper.step(this, vms) ;
    }

    public void select( VMState vms ) {
        Assert.check(selector != null) ;
        selector.select( this, vms ) ;
    }

    public String toString(VMState vms) {
        return toString( vms, syntax, _children ) ; }

    final protected String toString(VMState vms, NodeList children) {
        return toString( vms, syntax, children ) ; }

    final protected String toString(VMState vms,
                              String [] syntax,
                              NodeList children ) {

        Assert.check(syntax != null) ;
        Assert.check( syntax.length == 1+children.length() ) ;

        // What datum is associated with this node?
        String res ;
        Datum d = (Datum) vms.top().at( this ) ;
        if( d != null ) {
            if( d instanceof AbstractRefDatum ) {
                // References
                res = blue + d.getValueString() + endEffect ; }
            else {
                // Values
                res = red + d.getValueString() + endEffect ; }  }
        else {
            res = "" ;
            int i ;
            for(i=0 ; i < children.length() ; ++i ) {
                ExpressionNode thisChild
                    = (ExpressionNode) children.get(i) ;
                res += syntax[i] + thisChild.toString(vms) ; }
            res += syntax[i] ; }

         if( this == vms.top().getSelected() )
             return underline + res + endEffect ;
         else
             return res ;
    }

     public  String formatNodeData() {
        String data = _name  +":"+get_type().typeId()
                             +" "+dump_syntax() ;
        if( isUninteresting() ) data += " uninteresting" ;
        return  data ;
     }

     String dump_syntax() {
        StringBuffer b = new StringBuffer() ;
        for( int i=0, sz= syntax.length ; i<sz ; ++i ) {
            if( i!=0 ) b.append("$") ;
            b.append(syntax[i]) ; }
        return b.toString() ;
     }
}