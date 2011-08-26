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
import tm.clc.ast.TyAbstractPointer;
import tm.clc.datum.AbstractDatum;
import tm.clc.datum.AbstractObjectDatum;
import tm.clc.datum.AbstractPointerDatum;
import tm.clc.datum.AbstractRefDatum;
import tm.utilities.Assert;
import tm.virtualMachine.VMState;

/** Implementation of C++'s . operator.
*/
public class OpMember extends DefaultExpressionNode
{
    String operator_image ;
    String member_name ;
    int [] path ;
    ScopedName member ;
    
    public OpMember( TypeNode t,
               String operator_image, String member_name,
               int[] path,
               ScopedName member,
               ExpressionNode operand )

    {
        super("OpMember", operand) ;
        
        Assert.check( t instanceof TyAbstractRef ) ;
        Assert.check( operand.get_type() instanceof TyAbstractPointer ) ;
        
        set_syntax( new String[]{ "", operator_image+member_name } ) ;
 
        this.operator_image = operator_image ;
        this.member_name = member_name ;
        this.path = path ;
        this.member = member ;
        
        set_type( t ) ;
        set_selector( SelectorLeftToRight.construct() ) ;
        set_stepper( StepperMember.construct() ) ;
    }
}

class StepperMember extends StepperBasic {

    private static StepperMember singleton ;
    
    static StepperMember construct() {
        if( singleton == null ) singleton = new StepperMember() ;
        return singleton ; }
        
    public AbstractDatum inner_step( ExpressionNode nd, VMState vms ) {
        // Find the object
	        Object xd = vms.top().at( nd.child_exp(0) ) ;
	    	Assert.check( xd instanceof AbstractPointerDatum ) ;
	    	AbstractPointerDatum xpd = (AbstractPointerDatum) xd ;
	    	AbstractDatum rd = (AbstractDatum) xpd.deref() ;
	    	Assert.check( rd instanceof AbstractObjectDatum ) ;
	    	AbstractObjectDatum object = (AbstractObjectDatum) rd ;
	    	    
	    // Find the field
	        AbstractDatum field
	           = object.getFieldByName( ((OpMember)nd).path,
	                                    ((OpMember)nd).member ) ;
	    	    
	    // New datum on scratch
		     Clc_ASTUtilities util
	    	    = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
	    	 AbstractRefDatum d
		        = (AbstractRefDatum) util.scratchDatum(nd.get_type(), vms) ;

		// Give it a value
		    d.putValue( field ) ;
		    String name = xpd.getValueString()
		                + ((OpMember)nd).operator_image
		                + ((OpMember)nd).member_name ;
		    d.putValueString( name ) ;
		        
		return d ; }
}