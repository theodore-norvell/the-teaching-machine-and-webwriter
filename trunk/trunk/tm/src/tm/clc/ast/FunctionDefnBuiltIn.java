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
package tm.clc.ast;


/** Represent a function that is built in to the TM.
 * @author theo
 *
 */
public class FunctionDefnBuiltIn extends AbstractFunctionDefn {

    protected Stepper stepper ;
    
    /**
     * @param name
     * @param type
     */
    public FunctionDefnBuiltIn(Object key, Stepper stepper) {
        super(key);
        this.stepper = stepper ;
    }
    
    /**
     * @return Returns the stepper.
     */
    public Stepper getStepper() {
        return stepper;
    }
    
    public String toString() {
        StringBuffer buf = new StringBuffer() ;
        buf.append( "Built in " ) ;
        buf.append( super.toString() ) ;
        buf.append( "stepper: "); buf.append( stepper ) ;
        return buf.toString() ;
    }
}
