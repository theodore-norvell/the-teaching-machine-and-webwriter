/*
 * Created on 26-Apr-2006 by Theodore S. Norvell. 
 */
package tm.clc.datum;

import tm.backtrack.BTTimeManager;
import tm.clc.ast.TypeNode;
import tm.cpp.datum.DatumUtilities;
import tm.interfaces.Datum;
import tm.virtualMachine.Memory;
import tm.virtualMachine.VMState;

public class AbstractFloatDatum32 extends AbstractFloatDatum {

    public AbstractFloatDatum32(int add, int sz, Datum p, Memory m, String n,
            TypeNode tp, BTTimeManager timeMan) {
        super(add, sz, p, m, n, tp, timeMan);
    }



    public void putValue(double value) {
        float floatVal = (float) value ; // No exception on overflow!
        int bits = Float.floatToIntBits(floatVal);
        int a = address ;
        for (int j = 0; j < size; j++) {
            mem.putByte(a, (byte) (bits & 0xFF) ) ;
            bits = bits >> 8 ;
            ++a ; }
    }
    
    public double getValue() {
        int bits = 0 ;
        int a = address +size - 1;
        for( int j = 0; j < size ; ++j ) {
            bits = (bits << 8) | 0xFF & mem.getByte(a) ;
            --a ; }
        return Float.intBitsToFloat( bits ) ; }
    
    public String getValueString(){
        Float i = new Float( getValue() );
        return i.toString();
    }
    
    public boolean input( VMState vms ) {
        boolean success = DatumUtilities.inputFloat( vms, this ) ;
        return success ;
    }

}
