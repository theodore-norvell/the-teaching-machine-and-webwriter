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
import tm.interfaces.Datum;
import tm.utilities.Assert;
import tm.virtualMachine.VMState;

/** Convert a sub object to a containing superobject. */

public class OpDownConversion extends DefaultExpressionNode
{
    String prefix_operator_image ;
    int [] path ;
    
    public OpDownConversion(TypeNode t,
                     String prefix_operator_image,
                     int[] path, 
                     ExpressionNode operand )
    {
        super("OpDownConversion", operand) ;
        
        Assert.check( t instanceof TyAbstractPointer ) ;
        Assert.check( operand.get_type() instanceof TyAbstractPointer ) ;
        
        set_syntax( new String[]{ prefix_operator_image, "" } ) ;
 
        this.prefix_operator_image = prefix_operator_image ;
        this.path = path ;
        
        set_type( t ) ;
        set_selector( SelectorLeftToRight.construct() ) ;
        set_stepper( StepperDownConversion.construct() ) ;
    }
}

class StepperDownConversion extends StepperBasic {

    private static StepperDownConversion singleton ;
    
    static StepperDownConversion construct() {
        if( singleton == null ) singleton = new StepperDownConversion() ;
        return singleton ; }
        
    public AbstractDatum inner_step( ExpressionNode nd, VMState vms ) {
        // Find the object
	        Object xd = vms.top().at( nd.child_exp(0) ) ;
	    	Assert.check( xd instanceof AbstractPointerDatum ) ;
	    	AbstractPointerDatum xpd = (AbstractPointerDatum) xd ;
	    	AbstractDatum rd = (AbstractDatum) xpd.deref() ;
	    	Assert.check( rd instanceof AbstractObjectDatum ) ;
	    	AbstractObjectDatum object = (AbstractObjectDatum) rd ;
	    	    
	    // Find the superobject
	        AbstractObjectDatum superobject = object ;
	        int [] path = ((OpDownConversion)nd).path ;
	        for( int i=0, sz = path.length ; i < sz ; ++i ) {
	            Datum parent = superobject.getParent() ;
	            Assert.apology(parent != null, "Internal error, bad down conversion" ) ;
	            Assert.apology(parent instanceof AbstractObjectDatum, "Internal error, bad down conversion" ) ;
	            superobject = (AbstractObjectDatum) parent ; }
	    	    
	    // New datum on scratch
		     Clc_ASTUtilities util
	    	    = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
	    	 AbstractPointerDatum d
		        = (AbstractPointerDatum) util.scratchDatum(nd.get_type(), vms) ;

		// Give it a value
		    d.putValue( superobject ) ;
		
		// For references, give it a name too.
		    if( d instanceof AbstractRefDatum ) {
		        String name = xpd.getValueString()
		                    + ((OpDownConversion)nd).prefix_operator_image
		                    +  xpd.getValueString() ;
		        ((AbstractRefDatum)d).putValueString( name ) ; }
		        
		return d ; }
}