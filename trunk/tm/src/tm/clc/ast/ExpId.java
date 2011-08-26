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

import tm.clc.analysis.ScopedName;
import tm.clc.datum.AbstractDatum;
import tm.clc.datum.AbstractRefDatum;
import tm.clc.rtSymTab.RT_Symbol_Table;
import tm.interfaces.STEntry;
import tm.utilities.Assert;
import tm.virtualMachine.VMState;

/** Class for variable look up.
<P> When evaluated, this node looks up an identifier
    in the run time symbol table and (when necessary)
    creates a reference to it in the scratch area.
*/
public class ExpId extends DefaultExpressionNode
{
    String image ;
    ScopedName index ;

    /** Constructor.
        @param t The type of the node.
        t.getPointeeType() is the type of the
        variable.

        @param image The variable as it appears in the program text.
                The image will show up in the Expression Engine
                subwindow.

        @param index The key to use in the runtime symbol table to
                look up the variable.
    **/
    public ExpId( TyAbstractRef t, String image, ScopedName index ) {
        super("ExpId") ;
        this.image = image ;
        this.index = index ;
        set_type( t ) ;
        set_syntax( new String[]{ image } ) ;
        set_selector( SelectorExpId.construct() ) ;
        set_stepper( StepperExpId.construct() ) ; }


    /** Constructor.
        @param t The type of the node.
        t.getPointeeType() is the type of the
        variable.

        @param image The variable as it appears in the program text.
                The image will show up in the Expression Engine
                subwindow.

        @param index The key to use in the runtime symbol table to
                look up the variable.

        @param child This is an expression to be evaluated first. Used for Java
                  access to a static field. e.g.  (foo()).some_static_field
    **/
    public ExpId( TyAbstractRef t, String image, ScopedName index,  ExpressionNode child ) {
        super("ExpId", child) ;
        this.image = image ;
        this.index = index ;
        set_type( t ) ;
        set_syntax( new String[]{ "", "."+image } ) ;
        set_selector( SelectorExpId.construct() ) ;
        set_stepper( StepperExpId.construct() ) ; }

    public ScopedName getIndex () { return index; }
}


class SelectorExpId implements Selector {

    private static SelectorExpId singleton ;

    static SelectorExpId construct() {
        if( singleton == null ) singleton = new SelectorExpId() ;
        return singleton ; }

    public void select(ExpressionNode nd, VMState vms) {
        // Try to find an unmapped child.
            int f = -1 ;
            for(int i = 0 ; i < nd.childCount() ; ++ i ) {
                if( vms.top().at(nd.child_exp(i)) == null ) {
                    f = i ; break ; } }

        if( f == -1 ) {
            // None found, all children are mapped. Select this one
                ExpId nd1 = (ExpId) nd ;
                vms.top().setSelected( nd ) ;

                // Highlight the symbol table entry.
                RT_Symbol_Table symtab = (RT_Symbol_Table) vms.getSymbolTable() ;
                symtab.setVarHighlight( nd1.index, STEntry.HIGHLIGHTED ) ; }
        else {
            // Recurse on first unmapped child
                nd.child_exp(f).select( vms ) ; } }
}

class StepperExpId implements Stepper {

    private static StepperExpId singleton ;

    static StepperExpId construct() {
        if( singleton == null ) singleton = new StepperExpId() ;
        return singleton ; }

    public void step( ExpressionNode nd, VMState vms ) {
        ExpId nd1 = (ExpId) nd ;
        // The node shouldn't already be mapped.
            Assert.check( vms.top().at( nd ) == null ) ;
        // Clear the selection
            vms.top().setSelected( null ) ;

        // Look it up and unhighlight the symbol table entry
            RT_Symbol_Table symtab = (RT_Symbol_Table) vms.getSymbolTable() ;
            symtab.setVarHighlight( nd1.index, STEntry.PLAIN ) ;

        // Get the datum from the symbol table
            AbstractDatum varDatum = symtab.getDatum( nd1.index ) ;

        // Make a new reference in the scratch region.
            Clc_ASTUtilities util
                = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
            AbstractRefDatum ref = util.scratchRef( vms, varDatum, nd1.image ) ;
            vms.top().map(nd, ref) ;

        // Check that the type is right.
            Assert.check( ((AbstractDatum)(vms.top().at(nd))).getType().equal_types( nd.get_type() ) ) ; /*OK*/
    }
}
