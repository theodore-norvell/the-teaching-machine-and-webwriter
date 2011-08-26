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

/*
 * Created on 22-Aug-2005 by Theodore S. Norvell. 
 */
package tm.javaLang;

import java.util.Hashtable;

import javax.swing.text.html.HTML;

import tm.clc.analysis.Declaration;
import tm.clc.analysis.ScopedName;
import tm.clc.ast.Stepper;
import tm.javaLang.ast.Java_StepperBuiltIn;
import tm.javaLang.ast.Java_StepperBuiltInGeneric;
import tm.utilities.Assert;

/** Build steppers for built-in (native) functions.
 * @author theo
 */
public class BuiltInStepperFactory {

    private static Hashtable<String,Stepper> ht = new Hashtable<String,Stepper>( ) ;
    
    /**
     * @param decl The declaration of the method
     * @return
     */
    public static Stepper makeStepper(Declaration decl) {
        ScopedName fqn = decl.getName() ;
        String fullName = fqn.getName() ;
        String methodName = fqn.getTerminalId();
        fqn.removeTerminalId();
        String className = fqn.getName();
        fqn.append(methodName);
        
        Stepper stepper = ht.get( fullName ) ;
        
        if( stepper != null ) return stepper ;

        if (methodName.equals("read") && className.equals("java.io.InputStream") ) {
            stepper = new Java_StepperBuiltIn( Java_StepperBuiltIn.GET  ) ; }
        else if (methodName.equals("print") && className.equals("java.io.PrintStream")) {
            stepper = new Java_StepperBuiltIn( Java_StepperBuiltIn.OUTPUT  ) ; }
        else if (className.equals("tm.scripting.ScriptManager")) {
        	if(methodName.equals("relay")  ) {
	            stepper = new Java_StepperBuiltIn( Java_StepperBuiltIn.SCRIPT_RELAY  ) ; }
        	else if(methodName.equals("relayRtnInt")  ) {
	            stepper = new Java_StepperBuiltIn( Java_StepperBuiltIn.SCRIPT_RELAY_RTN_INT ) ; }
        	else if(methodName.equals("relayRtnDouble")  ) {
	            stepper = new Java_StepperBuiltIn( Java_StepperBuiltIn.SCRIPT_RELAY_RTN_DOUBLE ) ; }
	        else if (methodName.equals("snapShot")) {
	    //    	System.out.println("SCRIPT_SNAPSHOT stepper");
	            stepper = new Java_StepperBuiltIn( Java_StepperBuiltIn.SCRIPT_SNAPSHOT  ) ; }
	        else if (methodName.equals("setReference") ) {
	            //    	System.out.println("SCRIPT_REFERENCE stepper");
	            stepper = new Java_StepperBuiltIn( Java_StepperBuiltIn.SCRIPT_REFERENCE  ) ; }
	        else if (methodName.equals("compareReference")) {
	            //    	System.out.println("SCRIPT_COMPARE_REF stepper");
	                    stepper = new Java_StepperBuiltIn( Java_StepperBuiltIn.SCRIPT_COMPARE_REF  ) ; }

	        else stepper = new Java_StepperBuiltInGeneric(className, methodName);
        }
        else {
            stepper = new Java_StepperBuiltInGeneric(className, methodName); }
        
        ht.put( fullName, stepper ) ;
        
        return stepper ;
    }

}
