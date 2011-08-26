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

package tm.javaLang.ast;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import tm.clc.ast.Clc_ASTUtilities;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.OpAbsFuncCall;
import tm.clc.ast.Stepper;
import tm.clc.ast.TyAbstractClassDeclared;
import tm.clc.datum.AbstractDatum;
import tm.clc.datum.AbstractPointerDatum;
import tm.interfaces.TypeInterface;
import tm.javaLang.analysis.Java_ScopedName;
import tm.javaLang.datum.ArrayDatum;
import tm.javaLang.datum.CharDatum;
import tm.javaLang.datum.ObjectDatum;
import tm.javaLang.datum.PointerDatum;
import tm.utilities.Assert;
import tm.virtualMachine.VMState;

/**
 * A Generic stepper for handling calls to native library methods
 *
 * @author mpbl and tsn
 */

public class Java_StepperBuiltInGeneric implements Stepper {
	
	private Class theClass;		// the library class containing the method
	private String methodName;
	
	/**
	 *  Constructor.
	 * @param className the name of the library class containing the method
	 * @param methodName the name of the method
	 */
	public Java_StepperBuiltInGeneric( String className, String methodName) {
    	try {
    		theClass = Class.forName(className);
        	this.methodName = methodName;
    	} catch (ClassNotFoundException e){
    		Assert.error("Unable to find class" + className);
    	}
    }
    
    public void step( ExpressionNode nd, VMState vms ) {
        // The node shouldn't already be mapped.
        Assert.check( vms.top().at( nd ) == null ) ;

        Clc_ASTUtilities util
            = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
        
        // Clear the selection
        vms.top().setSelected( null ) ;
        
        Assert.check(nd instanceof OpAbsFuncCall);
        boolean staticCall = ((OpAbsFuncCall)nd).isStatic();
        int start = 0;
        Object nativeRecipient = null;
        
        if (!staticCall) {
        	start = 1;
        	AbstractDatum recipient = (AbstractDatum)vms.top().at( nd.child_exp(0));
        	nativeRecipient = util.getNativeValue(recipient, vms);
        }
        
        int args = nd.childCount() - start;
        // Get values of operands
        Class argClasses[] = new Class[args];
        Object argValues[] = new Object[args];
        
        for (int i = 0; i < argClasses.length; i++) {
        	AbstractDatum arg = (AbstractDatum)vms.top().at( nd.child_exp(i + start) );
        	argClasses[i] = util.getNativeClass(arg);
        	argValues[i] = util.getNativeValue(arg, vms);
        }
        Object nativeResult = null;
        Method method = null;
        try {
			method = theClass.getMethod(methodName, argClasses);
		} catch (NoSuchMethodException e) {
			Assert.error("Unable to find method " + methodName);
		} catch (SecurityException e){
				Assert.error("Security violation.");
		}
		try {
			nativeResult = method.invoke(nativeRecipient, argValues);
		} catch (IllegalAccessException e){
			Assert.error("Illegal access of " + methodName);			
		} catch (IllegalArgumentException e) {
			Assert.error("Illegal arguments for " + methodName);			
		} catch (InvocationTargetException e) {
            //@TODO We should turn this into a TM Exception somehow.
            e.getCause().printStackTrace( System.err ) ;
			Assert.apology("Native method " + methodName +" threw an exception.+" +
                    "The TM does not yet support this." );			
		}
        	
        AbstractDatum d
            = util.scratchDatum(nd.get_type(), vms);

        util.putNativeValue( d, nativeResult, vms ) ;
    
        // Map it.
        vms.top().map(nd, d);
    }
 }