package tm.clc.datum;

import tm.backtrack.BTTimeManager;
import tm.clc.ast.TypeNode;
import tm.interfaces.Datum;
import tm.interfaces.PointerInterface;
import tm.interfaces.TypeInterface;
import tm.utilities.Assert;
import tm.utilities.Debug;
import tm.virtualMachine.Memory;
import tm.virtualMachine.Store;

abstract public class AbstractPointerDatum
    extends tm.clc.datum.AbstractScalarDatum
    implements PointerInterface    
{
	Store theStore ; // The store containing any pointees.

	public AbstractPointerDatum(int a, int size, Datum p, Memory m, String n, TypeNode tp, Store str, BTTimeManager timeMan) {
		super(a, size, p, m, n, tp, timeMan);
		theStore = str ;
	}

	public void putValue( AbstractDatum pointee ) {
		putValue( pointee.getAddress() ) ; }

	public void putValue( int addr ) {
		for( int j = 0 ; j < size ; ++j ) {
			mem.putByte(address+j, (byte) (addr & 0xFF) ) ;
			addr = addr >> 8 ; } }

	public int getValue() {
		int addr = 0 ;
		for( int j = size-1 ; j >= 0 ; --j ) {
			addr = (addr<<8) | (0xFF & mem.getByte(address+j)) ; }
		return addr ; }

	public abstract TypeInterface getPointeeType() ; 

    /** Returns a datum of type getPointeeType(). */
	public Datum deref() {
		Datum d = theStore.chasePointer( getValue(), getPointeeType() ) ;
		//Debug.getInstance().msg( d==null ? "d is null" : "d is"+d.toString()) ;
	    return d ; }

    /** Returns a datum of type getPointeeType() or a descendant type. */
	public Datum derefVirtual() {
		return theStore.chasePointerVirtual( getValue(), getPointeeType() ) ; }
	
	public void defaultInitialize(){
		putValue(0);
	}


}