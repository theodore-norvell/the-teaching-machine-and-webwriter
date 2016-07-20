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

package tm.cpp.datum;

import tm.backtrack.BTTimeManager;
import tm.clc.ast.TypeNode;
import tm.clc.datum.AbstractArrayDatum;
import tm.clc.datum.AbstractDatum;
import tm.interfaces.Datum;
import tm.utilities.Assert;
import tm.virtualMachine.Memory;

public class ArrayDatum extends AbstractDatum implements AbstractArrayDatum
{

    protected AbstractDatum[] element;
    protected int elementSize ; // Size of elements in bytes.
    
    public ArrayDatum(int add, int noe, int el_size, Datum p, Memory m, String n, TypeNode tp, BTTimeManager timeMan) {
        super(add, noe*el_size, p, m, n, tp, timeMan);

        element = new AbstractDatum[noe] ;
        elementSize = el_size ;}
    
    public int getNumChildren() {
        return element.length ; }

    public Datum getChildAt(int i) {
        return element[i] ; }
    
    public String getChildLabelAt(int i) {
        Assert.check(i>=0 && i < element.length) ;
        return Integer.toString(i) ; }
    
    public int getNumberOfElements() {
        return element.length ; }
    
    public AbstractDatum getElement( int i ) {
        return element[i] ; }

    public void putElement(int i, AbstractDatum d ) {
        Assert.check( d.getNumBytes()==elementSize ) ;
        Assert.check( d.getParent() == this ) ;
        element[i] = d ;
        d.setBirthOrder(i) ; }
    
    public String getValueString(){
        return "array";
    }
    
    /**
     * @see tm.interfaces.Datum#defaultInitialize()
     */
    public void defaultInitialize() {
        for (int d = 0; d < element.length; d++)
            element[d].defaultInitialize();     
    }
    
}
