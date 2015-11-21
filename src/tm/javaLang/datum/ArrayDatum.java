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

package tm.javaLang.datum;

import tm.backtrack.BTTimeManager;
import tm.clc.analysis.ScopedName;
import tm.clc.ast.TypeNode;
import tm.clc.datum.AbstractArrayDatum;
import tm.clc.datum.AbstractDatum;
import tm.interfaces.Datum;
import tm.javaLang.ast.TyJavaArray;
import tm.javaLang.datum.IntDatum;
import tm.utilities.Assert;
import tm.virtualMachine.Memory;

/** Datum for Java arrays. 
 * */

public class ArrayDatum extends ObjectDatum  implements AbstractArrayDatum {

    public ArrayDatum(int add, Memory m, TyJavaArray tp, int numberOfElements, BTTimeManager timeMan) {
        super(add, null, m, "", tp, numberOfElements, timeMan );
    }

    protected ArrayDatum(ArrayDatum original){
    	super(original);
    }
    
    public Datum copy(){
    	return new ArrayDatum(this);
    }

    public IntDatum getLength() { return (IntDatum)getChildAt(1); }


    /** 
     * @see tm.clc.datum.AbstractArrayDatum#getNumberOfElements()
     */
    public int getNumberOfElements() {
        return (int) getLength().getValue();
    }

    /** 
     * @see tm.clc.datum.AbstractArrayDatum#getElement(int)
     */
    public AbstractDatum getElement(int i) {
        return (AbstractDatum) getChildAt(i+2) ;
    }
/**
 * Over-ride of AbstractObjectDatum for special case of arrays. Only a length field
 */    
    public AbstractDatum getFieldByName(ScopedName fieldName) {
            if( fieldName.getTerminalId().equals("length") ) 
                return getLength();
        Assert.apology( "Field "+fieldName+" not found in object" ) ;
        return null ; }


    /**
     * @param elements
     */
    public void setNumberOfElements(int elements) {
        // TODO Auto-generated method stub
        
    }
    
    public boolean equals(Object o){
    	if(!(o instanceof ArrayDatum)) return false;
    	ArrayDatum arrayDatum = (ArrayDatum) o;
    	if (arrayDatum.getNumberOfElements() != this.getNumberOfElements() )return false;
    	for (int i = 0; i < getNumberOfElements(); i++)
    		if(! getElement(i).equals(arrayDatum.getElement(i))) return false;
    	return true;
    }

}