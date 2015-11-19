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

import tm.clc.datum.AbstractDatum;
import tm.clc.datum.AbstractObjectDatum;
import tm.clc.datum.AbstractPointerDatum;
import tm.clc.rtSymTab.RT_Symbol_Table;
import tm.utilities.Assert;
import tm.virtualMachine.VMState;

/** Get a pointer to the this object.
*/
public class ExpThis extends DefaultExpressionNode
{

    int[] path ;

    /** Constructor.
        @param t The type of the node.
        t.getPointeeType() is the type of the recipient.

        @param image The pointer as it appears prior to evaluation.
    **/
    public ExpThis( TyAbstractPointer t, String image ) {
        this(t, image, new int[] {} ) ; }

    /** Constructor.
        @param t The type of the node.
        t.getPointeeType() is the type of the recipient.

        @param image The pointer as it appears prior to evaluation.

        @param path A path to select the subobject.
    **/
    public ExpThis( TyAbstractPointer t, String image, int[] path ) {
        super("ExpThis") ;
        set_type( t ) ;
        this.path = path ;
        set_syntax( new String[]{ image } ) ;
        set_selector(  SelectorAlways.construct() ) ;
        set_stepper( StepperExpThis.construct() ) ; }

     public  String formatNodeData() {
        String result = super.formatNodeData() + "[";
        for( int i=0 ; i < path.length ; ++i ) {
            result += " " + path[i] ;
            if( i < path.length -1 ) result += "," ;
            else result += "]" ; }
        return result ;
     }
}

class StepperExpThis extends StepperBasic {

    private static StepperExpThis singleton ;

    static StepperExpThis construct()  {
        if( singleton == null ) singleton = new StepperExpThis() ;
        return singleton ; }

    public AbstractDatum inner_step( ExpressionNode nd, VMState vms ) {
        ExpThis nd1 = (ExpThis) nd ;

        // Get the Symbol table
            RT_Symbol_Table symtab = (RT_Symbol_Table) vms.getSymbolTable() ;

        // Get the current recipient
            AbstractObjectDatum recipient = symtab.getTopRecipient() ;
            Assert.apology(recipient != null, "Error \"this\" not in method" ) ;

        // Find the appropriate subobject
            AbstractObjectDatum subObject = recipient.getSubObject( nd1.path ) ;

        // New datum on scratch
            Clc_ASTUtilities util
                = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
            AbstractPointerDatum d
                = (AbstractPointerDatum) util.scratchDatum(nd.get_type(), vms) ;
        // Give it a value
            d.putValue( subObject ) ;

         return d ; }
}