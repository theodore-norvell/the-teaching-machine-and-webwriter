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
 * Created on 26-Apr-2006 by Theodore S. Norvell. 
 */
package tm.clc.datum;

import tm.backtrack.BTTimeManager;
import tm.clc.ast.TypeNode;
import tm.interfaces.Datum;
import tm.javaLang.datum.DatumUtilities;
import tm.virtualMachine.Memory;
import tm.virtualMachine.VMState;

public class AbstractFloatDatum64 extends AbstractFloatDatum {
    public static final int size = 8 ;
    
    public AbstractFloatDatum64(int add, int sz, Datum p, Memory m, String n,
            TypeNode tp, BTTimeManager timeMan) {
        super(add, sz, p, m, n, tp, timeMan);
    }

	protected AbstractFloatDatum64(AbstractFloatDatum64 original){
		super(original);
	}

    public void putValue(double v ) {
        long bits = Double.doubleToLongBits(v);
        int a = address ;
        for (int j = 0; j < size; j++) {
            mem.putByte(a, (byte) (bits & 0xFF) ) ;
            bits = bits >> 8 ;
            ++a ; }
    }
    
    public double getValue() {
        long bits = 0 ;
        int a = address +size - 1;
        for( int j = 0; j < size ; ++j ) {
            bits = (bits << 8) | 0xFF & mem.getByte(a) ;
            --a ; }
        return Double.longBitsToDouble( bits ) ; }
    
    public String getValueString(){
        Double i = new Double( getValue() );
        return i.toString();
    }
    
    public boolean input(VMState vms) {
        boolean success = DatumUtilities.inputFloat(vms, this);
        return success;
    }

    public Datum copy(){
    	return new AbstractFloatDatum64(this);
    }
}
