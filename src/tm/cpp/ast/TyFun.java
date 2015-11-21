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

package tm.cpp.ast;

import java.util.Vector ;

import tm.clc.ast.NodeList;
import tm.clc.ast.TyAbstractFun;


public class TyFun extends TyAbstractFun implements TyCpp {
	
    public TyFun( Vector param_types, boolean endsWithElipsis ) {
		super( param_types, endsWithElipsis ) ; }

	public String getTypeString() { 
	    return "fcn returning " + returnType().getTypeString() ; }
	
	public String typeId() { return typeId( "", false ) ; }
	
	public String typeId( String seed, boolean lastWasLeft ) {
	    
	    int count=getParamCount() ;
	    if( count == 0 ) {
	        seed += "()" ; }
	    else {
	        seed += "(" ;
	        for( int i=0 ; i<count ; ++i ) {
	            seed += ((TyCpp)getParamType(i)).typeId( "", false ) ;
	            if( i+1 < count ) seed += ", " ; }
	        seed += ")" ; }
	        
	    if( lastWasLeft ) seed = "("+seed+")" ;
	    
	    return ((TyCpp)returnType()).typeId( seed, false ); 
	}
}
