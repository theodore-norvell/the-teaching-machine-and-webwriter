package tm.clc.datum;

import tm.backtrack.BTTimeManager;
import tm.clc.ast.TypeNode;
import tm.interfaces.Datum;
import tm.virtualMachine.Memory;

/*
COPYRIGHT (C) 1997--2001 by Michael Bruce-Lockhart & Theodore Norvell.  The associated software is 
released to students for educational purposes only and only for the duration
of the course in which it is handed out. No other use of the software, either
commercial or non-commercial, may be made without the express permission of
the author. 
*/


/*=========================================================================
Class: AbstractFloatDatum

Overview:
This abstract class groups all floating point types..
===========================================================================
*/ 
 

 public abstract class AbstractFloatDatum extends AbstractScalarDatum {

	public AbstractFloatDatum(int add, int sz, Datum p, Memory m, String n, TypeNode tp, BTTimeManager timeMan) {
		super(add,sz,p, m, n, tp, timeMan);
	}

	public abstract void putValue(double v ) ;
	
	public abstract double getValue() ;

	public abstract String getValueString() ;
	
	public void defaultInitialize(){
		putValue(0.0);
	}
 }
