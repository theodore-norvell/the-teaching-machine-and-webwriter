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

import tm.clc.ast.TyAbstractPointer;
import tm.clc.datum.AbstractDatum;
import tm.clc.datum.AbstractObjectDatum;
import tm.clc.datum.AbstractPointerDatum;
import tm.clc.datum.AbstractRefDatum;
import tm.utilities.Assert;
import tm.virtualMachine.VMState;

/** Convert an object to a subobject (a base class) */

public class OpUpConversion extends DefaultExpressionNode
{
    String prefix_operator_image ;
    int [] path ;
    
    public OpUpConversion(TypeNode t,
                     String prefix_operator_image,
                     int[] path, 
                     ExpressionNode operand )
    {
        super("OpUpConversion", operand) ;
        
        Assert.check( t instanceof TyAbstractPointer ) ;
        Assert.check( operand.get_type() instanceof TyAbstractPointer ) ;
        
        set_syntax( new String[]{ prefix_operator_image, "" } ) ;
 
        this.prefix_operator_image = prefix_operator_image ;
        this.path = path ;
        
        set_type( t ) ;
        set_selector( SelectorLeftToRight.construct() ) ;
        set_stepper( StepperUpConversion.construct() ) ;
    }
}

class StepperUpConversion extends StepperBasic {

    private static StepperUpConversion singleton ;
    
    static StepperUpConversion construct() {
        if( singleton == null ) singleton = new StepperUpConversion() ;
        return singleton ; }
        
    public AbstractDatum inner_step( ExpressionNode nd, VMState vms ) {
        // Find the object
	        Object xd = vms.top().at( nd.child_exp(0) ) ;
	    	Assert.check( xd instanceof AbstractPointerDatum ) ;
	    	AbstractPointerDatum xpd = (AbstractPointerDatum) xd ;
	    	AbstractDatum rd = (AbstractDatum) xpd.deref() ;
	    	Assert.check( rd instanceof AbstractObjectDatum ) ;
	    	AbstractObjectDatum object = (AbstractObjectDatum) rd ;
	    	    
	    // Find the subobject
	        AbstractObjectDatum subobject
	           = object.getSubObject( ((OpUpConversion)nd).path ) ;
	    	    
	    // New datum on scratch
		     Clc_ASTUtilities util
	    	    = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
	    	 AbstractPointerDatum d
		        = (AbstractPointerDatum) util.scratchDatum(nd.get_type(), vms) ;

		// Give it a value
		    d.putValue( subobject ) ;
		
		// For references, give it a name too.
		    if( d instanceof AbstractRefDatum ) {
		        String name = xpd.getValueString()
		                    + ((OpUpConversion)nd).prefix_operator_image
		                    +  xpd.getValueString() ;
		        ((AbstractRefDatum)d).putValueString( name ) ; }
		        
		return d ; }
}