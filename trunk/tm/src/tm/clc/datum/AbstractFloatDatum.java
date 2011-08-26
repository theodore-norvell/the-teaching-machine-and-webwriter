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

package tm.clc.datum;

import tm.backtrack.BTTimeManager;
import tm.clc.ast.TypeNode;
import tm.interfaces.Datum;
import tm.virtualMachine.Memory;
import tm.utilities.Assert;




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

	protected AbstractFloatDatum(AbstractFloatDatum original){
		super(original);
	}

	public abstract void putValue(double v ) ;
	
	public abstract double getValue() ;

	public abstract String getValueString() ;
	
	public void defaultInitialize(){
		putValue(0.0);
	}
	
	public boolean isEqual(Datum another){
		Assert.check(another instanceof AbstractFloatDatum, "Can't check equality of a float to another type");
		AbstractFloatDatum theOther = (AbstractFloatDatum) another;
		return getValue() == theOther.getValue();
	}
 }
