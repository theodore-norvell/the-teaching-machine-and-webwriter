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

package tm.clc.ast;

import tm.clc.datum.AbstractDatum;
import tm.virtualMachine.MemRegion;
import tm.virtualMachine.VMState;

abstract public class TyAbstractArray extends TypeNode
{
    // elementType -- The type of the elements
    protected TypeNodeLink elementTypeLink = new TypeNodeLink() ;

	public TyAbstractArray( ) {
		super() ;
    }

    public void addToEnd( TypeNode l ) {
        elementTypeLink.addToEnd( l ) ; }
   
    public TypeNode getElementType() { return elementTypeLink.get() ; }

	public AbstractDatum makeDatum(VMState vms, MemRegion mr, String name ) {
		int address = mr.findSpace( getNumBytes() ) ;
	    AbstractDatum d = makeMemberDatum( vms, address, null, name ) ;
		vms.getStore().addDatum(d) ;
		return d ; }
}