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

import tm.clc.ast.Clc_ASTUtilities;
import tm.clc.ast.TyAbstractClassDeclared;
import tm.clc.ast.TyAbstractRef;
import tm.clc.ast.TypeNode;
import tm.clc.datum.*;
import tm.cpp.datum.*;
import tm.interfaces.TypeInterface;
import tm.interfaces.Datum;
import tm.utilities.Assert;
import tm.virtualMachine.Memory;
import tm.virtualMachine.VMState;

public class Cpp_ASTUtilities extends Clc_ASTUtilities {

     public final TyVoid voidType = TyVoid.get () ;
     public final TyBool boolType = TyBool.get () ;
     public final TyChar charType = TyChar.get () ;
     public final TyDouble doubleType = TyDouble.get () ;
     public final TyInt intType = TyInt.get () ;
     public final TyLong longType = TyLong.get () ;
//	 public final TyOStream ostreamType = new TyOStream() ; 
//	 public final TyIStream istreamType = new TyIStream() ; 

    public TypeNode getIntType() {
        return intType ;  }
        
    public TypeNode getBooleanType() {
        return boolType ; }
    
    public TypeNode getFloatingType() {
        return doubleType ; }
        
    public TyAbstractRef getRefType( TypeNode target_type ) {
        return new TyRef( target_type ) ; }
        
    public TypeNode getVoidType() {
        return voidType ;  }

    @Override
    public Class getNativeClass(AbstractDatum d) {
        return d.getNativeClass() ;
    }

    @Override
    public Object getNativeValue(AbstractDatum d, VMState vms) {
        if( isString(d) ) {
        	if (d instanceof PointerDatum){
	            PointerDatum ptr = (PointerDatum) d ;
	            if( ptr.isNull() ) {
	                return null ;
	            } else {
	                AbstractIntDatum pointee = (AbstractIntDatum) ptr.deref() ;
	                Assert.check( pointee != null, "C++ pointer to char has bad value") ;
	                int size = 0;
	                Memory theMem = vms.getMemory() ;
	                int addr = ptr.getValue();
	                while (theMem.getByte(addr+size) != 0){
	                    size++;
	                } 
	                char[] stringData = new char[size];
	                for (int i = 0; i < size; i++)
	                    stringData[i] = (char) theMem.getByte(addr+i);
	                return new java.lang.String(stringData);
	             }
        	}else { // must be arrayDatum since isString
        		ArrayDatum array = (ArrayDatum) d;
        		int size = array.getNumberOfElements();
        		if(size==0) {
        			return null;
        		} else { // adjust size if null char in array
        			for (int i = 0; i < size; i++)
        				if (array.getElement(i).getByte(0) == 0 ){
        					size = i;
        					break;
        				}
	                char[] stringData = new char[size];
	                for (int i = 0; i < size; i++)
	                    stringData[i] = (char)array.getElement(i).getByte(0);
	                return new java.lang.String(stringData);        			
        		}      		
        	}
        }
        return d.getNativeValue() ;
    }

    @Override
    public void putNativeValue(AbstractDatum d, Object nativeResult, VMState vms) {
        d.putNativeValue( nativeResult) ;
    }

    @Override
    public boolean isString(AbstractDatum d) {
    	if (d instanceof PointerDatum){
            PointerDatum ptr = (PointerDatum) d ;
            TypeInterface pointeeType = ptr.getPointeeType() ;
            if( pointeeType instanceof TyChar )
            	return true;
    	}
    	if (d instanceof ArrayDatum){
    		ArrayDatum array = (ArrayDatum) d;
            TypeInterface elementType = ((TyArray)array.getType()).getElementType();
            if (elementType instanceof TyChar)
            	return true;   		
    	}
        return false;
    }
}
