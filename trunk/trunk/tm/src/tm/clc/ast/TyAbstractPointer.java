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

import tm.interfaces.TypeInterface;

abstract public class TyAbstractPointer extends TypeNode
{

    protected TypeNodeLink pointeeTypeLink = new TypeNodeLink() ; 

    public TyAbstractPointer() {
        super() ;
        // Note the link is not set. Should be set by a call to addToEnd.
    }

    public TyAbstractPointer(TypeNode pointeeTp) {
        this() ;
        pointeeTypeLink.set( pointeeTp );
    }

    public TypeNode getPointeeType() { return pointeeTypeLink.get() ; }
    
    // Added 2003.08.22 by mpbl
    public TypeNode getBaseType(){return getPointeeType().getBaseType();}

    public void addToEnd( TypeNode l ) {
        pointeeTypeLink.addToEnd( l ) ; }
        
	public boolean equal_types( TypeInterface t ) {
		return t instanceof TyAbstractPointer
		    && getPointeeType().equal_types( ((TyAbstractPointer)t).getPointeeType() ) ; }

}