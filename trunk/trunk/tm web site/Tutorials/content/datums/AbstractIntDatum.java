package tm.clc.datum;

import tm.backtrack.BTTimeManager;
import tm.clc.ast.TypeNode;
import tm.interfaces.Datum;
import tm.virtualMachine.Memory;
import tm.utilities.Assert;

/*
COPYRIGHT (C) 1997--2001 by Michael Bruce-Lockhart & Theodore Norvell.  The associated software is 
released to students for educational purposes only and only for the duration
of the course in which it is handed out. No other use of the software, either
commercial or non-commercial, may be made without the express permission of
the author. 
*/


/*=========================================================================
Class: AbstractInt

Overview:
This abstract class groups all integral types..
===========================================================================
*/ 
 

 public abstract class AbstractIntDatum extends AbstractScalarDatum {

	public AbstractIntDatum(int add, int sz, Datum p, Memory m, String n, TypeNode tp, BTTimeManager timeMan) {
		super(add,sz,p, m, n, tp, timeMan);
	}

	public void putValue(long v ) {
		int a = address ;
		for( int i=0 ; i < size ; ++i) {
			mem.putByte( a, (byte) (v & 0xFF) ) ;
			v = v >> 8 ;
			a += 1 ; }
	}
	
	public void putNativeValue(Object obj){
		Assert.check(obj instanceof Integer);
		putValue(((Integer)obj).longValue());
	}
	

	public long getValue() {
		long v = getUnsignedValue() ;
		// Sign extend.
		v = v << (8*(8-size)) ;
		v = v >> (8*(8-size)) ;
		return v ;
	}
	
	final public long getUnsignedValue() {
		long v = 0 ;
		int a = address  ; // Locn of LSB
		// Copy size bytes from mem to the size LSBs of v.
		for( int i=0 ; i < size ; ++i ) {
			long byteVal = 0xFF & (long)mem.getByte(a) ;
			v = v | byteVal << 8*i ;
			++a ; }
	    return v ;
	}
	    

	public String getValueString(){
		Long i = new Long( getValue() );
		return i.toString();
	}
	
	public void defaultInitialize(){
		putValue(0);
	}
}