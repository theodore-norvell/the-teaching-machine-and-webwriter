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

/** Expression node for looking up a nonstatic member of the "this" object. 
<P> When evaluated, this node looks up an identifier
    in the run time symbol table and (when necessary)
    creates a reference to it in the scatch area.
*/ 
public class ExpThisMember extends DefaultExpressionNode
{

    int [] path ;
    String member_name ;
    ScopedName member_fqn ;
    
    /** Constructor.
        @param t The type of the node.
        t.getPointeeType() is the type of the
        variable.
        
        @param member_name The variable as it appears in the program text.
                The image will show up in the Expression Engine
                subwindow.
        
        @param path The path through the subobject tree to the
                    right subobject.
        @param member_fqn The fully quallified name of the member 
    **/ 
    public ExpThisMember( TypeNode t,
                     String member_name,
                     int[] path,
                     ScopedName member_fqn )
    {
        super("ExpThisMember") ;
        set_type( t ) ;
        this.path = path ;
        this.member_name = member_name ;
        this.member_fqn = member_fqn ;
        set_syntax( new String[]{ member_name } ) ;
        set_selector( SelectorThisMember.construct() ) ;
        set_stepper( StepperThisMember.construct() ) ; }        

     public  String formatNodeData() {
        String result = super.formatNodeData() + "[";
        for( int i=0 ; i < path.length ; ++i ) {
            result += " " + path[i] ;
            if( i < path.length -1 ) result += "," ;
            else result += "]" ; }
        return result ;
     }
}

class SelectorThisMember implements Selector {

    private static SelectorThisMember singleton ;
    
    static SelectorThisMember construct() {
        if( singleton == null ) singleton = new SelectorThisMember() ;
        return singleton ; }
   
    public void select(ExpressionNode nd, VMState vms) {
        ExpThisMember nd1 = (ExpThisMember) nd ;
        vms.top().setSelected( nd ) ;
        
        // Highlight the symbol table entry.
            RT_Symbol_Table symtab = (RT_Symbol_Table) vms.getSymbolTable() ;
            symtab.setRecipientVarHighlight( nd1.path, nd1.member_fqn, STEntry.HIGHLIGHTED ) ; }
}
                
class StepperThisMember implements Stepper {

    private static StepperThisMember singleton ;
    
    static StepperThisMember construct() {
        if( singleton == null ) singleton = new StepperThisMember() ;
        return singleton ; }
        
    public void step( ExpressionNode nd, VMState vms ) {
        ExpThisMember nd1 = (ExpThisMember) nd ;
        
        // The node shouldn't already be mapped.
            Assert.check( vms.top().at( nd ) == null ) ;
        // Clear the selection
            vms.top().setSelected( null ) ;
        // Look it up and unhighlight the symbol table entry
            RT_Symbol_Table symtab = (RT_Symbol_Table) vms.getSymbolTable() ;
            symtab.setRecipientVarHighlight( nd1.path, nd1.member_fqn, STEntry.PLAIN ) ;
            
        // Get the Datum from the symbol table	
            AbstractDatum varDatum = symtab.getDatumFromRecipient( nd1.path, nd1.member_fqn ) ;
            
        // Make a new reference in the scratch region.
            Clc_ASTUtilities util = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
            AbstractRefDatum ref = util.scratchRef( vms, varDatum, nd1.member_name ) ;
            vms.top().map(nd, ref) ;
            
        // Check that the type is right.
            Assert.check( ((AbstractDatum)(vms.top().at(nd))).getType().equal_types( nd.get_type() ) ) ; /*OK*/
    }
}