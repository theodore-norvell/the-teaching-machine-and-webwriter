package tm.clc.datum;

import tm.backtrack.BTTimeManager;
import tm.clc.ast.TyAbstractRef;
import tm.clc.ast.TypeNode;
import tm.interfaces.Datum;
import tm.interfaces.TypeInterface;
import tm.virtualMachine.Memory;
import tm.virtualMachine.Store;

/*
COPYRIGHT (C) 1997--2001 by Michael Bruce-Lockhart & Theodore Norvell.
The associated software is 
released to students for educational purposes only and only for the duration
of the course in which it is handed out. No other use of the software, either
commercial or non-commercial, may be made without the express permission of
the author. 
*/


/* Class: AbstractRefDatum.
   Language independant representation of references of all sorts.
   References are used both for variables of reference type
   (as in C++) but also in a lot of places where the virtual
   machine has done a lookup and needs to keep a pointer to
   an object.
*/
abstract public class AbstractRefDatum extends AbstractPointerDatum {

	public static final int size = 4 ;

	public AbstractRefDatum(int a, Datum p, Memory m, String n, TypeNode tp, Store str, BTTimeManager timeMan) {
		super(a, size, p, m, n, tp, str, timeMan);
    }

	public AbstractRefDatum(int a, Memory m, String n, TypeNode tp, Store str, BTTimeManager timeMan) {
		this(a, null, m, n, tp, str, timeMan);
	}

	public boolean isNull() {return 0==getValue() ;}

	abstract public void putValueString( String str ) ;

	abstract public String getValueString() ;

	public TypeInterface getPointeeType() { return ((TyAbstractRef) type).getPointeeType() ; }
}