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

import java.util.Vector ;

import tm.clc.analysis.ScopedName;
import tm.clc.ast.TypeNode;
import tm.clc.ast.TypeNodeLink;
import tm.clc.datum.AbstractDatum;
import tm.interfaces.TypeInterface;
import tm.utilities.Assert;
import tm.virtualMachine.MemRegion;
import tm.virtualMachine.VMState;


public abstract class TyAbstractFun extends TypeNode  {
	
    private Vector   param_types ;
    
    private boolean ellipsis = false ;

    private TypeNodeLink returnTypeLink ;

    public TyAbstractFun(Vector param_types ) {
        this( param_types, false ) ;
    }
    public TyAbstractFun( Vector param_types, boolean endsWithEllipsis ) {
        super() ;
        returnTypeLink = new TypeNodeLink() ;
        this.param_types = param_types ;
        ellipsis = endsWithEllipsis ; }

    public void addToEnd( TypeNode l ) {
            returnTypeLink.addToEnd( l ) ; }

	//public NodeList paramList() {return param_types ; }

    public TypeNode returnType() {return returnTypeLink.get() ; }
    
    public boolean endsWithElipsis() { return ellipsis ; }
    
    public int getParamCount() {
        return param_types.size() ; }
   
    public TypeNode getParamType(int i) {
        return (TypeNode) param_types.elementAt(i) ; }

	public AbstractDatum makeMemberDatum(VMState vms, int address, AbstractDatum parent, String name) {
		Assert.apology("Attempt to make a datum of function type" ) ;
		return null ; }
	
	public AbstractDatum makeDatum(VMState vms, MemRegion mr, String name) {
		Assert.apology("Attempt to make a datum of function type" ) ;
		return null ; }

	public boolean equal_types( TypeInterface t ) {
		Assert.apology("Comparing function types not supported") ;
		return false ;}
	
	public int getNumBytes() { 
	    Assert.apology("Size of a function type requested") ;
	    return 0 ; }
}
