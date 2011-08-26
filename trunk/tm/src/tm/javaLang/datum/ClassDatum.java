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
 * Created on 9-May-2005
 *
 */
package tm.javaLang.datum;

import tm.backtrack.BTTimeManager;
import tm.backtrack.BTVar;
import tm.clc.ast.TyAbstractClass;
import tm.clc.datum.AbstractObjectDatum;
import tm.interfaces.Datum;
import tm.virtualMachine.Memory;

/**
 * @author mpbl
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/*******************************************************************************
Class: ClassDatum

Overview:
This class represents the meta object associated with each class. As a practical
matter, all static fields of the class are the fields of the class object.

*******************************************************************************/

public class ClassDatum extends AbstractObjectDatum  {
    
    private BTVar<Boolean> hasBeenInitialized ;
    
    public boolean isInitialized() {
        return hasBeenInitialized.get().booleanValue() ; }
    
    public void setInitialized() {
        hasBeenInitialized.set( Boolean.TRUE ) ; }
    

    public ClassDatum(int add, Datum p, Memory m, String n, 
                       TyAbstractClass tp, BTTimeManager timeMan) {
        super(add, p, m, n, tp, timeMan);
        hasBeenInitialized = new BTVar<Boolean>( timeMan ) ;
        hasBeenInitialized.set( Boolean.FALSE ) ;
    }

    protected ClassDatum(ClassDatum original){
    	super(original);
    }
    
    public Datum copy(){
    	return new ClassDatum(this);
    }

    public String getValueString(){
        return type.getTypeString()+"{..}";
    }

}
