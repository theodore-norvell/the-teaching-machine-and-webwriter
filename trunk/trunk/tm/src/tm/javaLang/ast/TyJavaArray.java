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

package tm.javaLang.ast;

import tm.backtrack.BTTimeManager;
import tm.clc.ast.TyAbstractClass;
import tm.clc.ast.TypeNode;
import tm.clc.ast.TypeNodeLink;
import tm.clc.ast.VarNode;
import tm.clc.datum.AbstractDatum;
import tm.clc.datum.AbstractObjectDatum;
import tm.interfaces.TypeInterface;
import tm.javaLang.analysis.Java_ScopedName;
import tm.javaLang.datum.ArrayDatum;
import tm.javaLang.datum.IntDatum;
import tm.utilities.Assert;
import tm.virtualMachine.MemRegion;
import tm.virtualMachine.Memory;
import tm.virtualMachine.Store;
import tm.virtualMachine.VMState;

/** Java style arrays.
*/

public class TyJavaArray extends TyAbstractClass implements TyJava {
    private static final Java_ScopedName lengthName = new Java_ScopedName( "length" ) ;
    // elementType -- The type of the elements
    protected TypeNodeLink elementTypeLink = new TypeNodeLink() ;

    public TyJavaArray(String name, TyClass objectClass) {
        super( name );
        addSuperClass( objectClass ) ;
        VarNode field = new tm.clc.ast.VarNode(lengthName, TyInt.get() ) ;
        addField( field ) ;
    }

    public void addToEnd( TypeNode l ) {
        elementTypeLink.addToEnd( l ) ; }
   
    public TypeNode getElementType() { return elementTypeLink.get() ; }
    
    public TypeNode getBaseElementType(){
    	TypeNode possible = getElementType();
    	if (possible instanceof TyPointer) {
    		TypeNode target = ((TyPointer)possible).getPointeeType();
    		if (target instanceof TyJavaArray)
    			return ((TyJavaArray)target).getBaseElementType();
    	}
    	return possible;
    }

    public AbstractDatum makeMemberDatum(VMState vms, int address,
                                         AbstractDatum parent, String name) {
        Assert.check(false);
        return null;
    }
    
    /** Should not be called */
    public int getNumBytes() {
        Assert.check( false ) ;
        return 0 ; }
    
    /** Should not be called */
    protected AbstractObjectDatum constructDatum(int add, AbstractDatum p, Memory m, String n, BTTimeManager timeMan) {
        Assert.check(false);
        return null ; }


    public ArrayDatum makeArrayDatum(VMState vms, int elements) {
    /*DBG System.out.println("TyJavaArray.makeMemberDatum at "+address);//*/
        Assert.check(elements >= 0);
        
        // Obtain Space
            Store store = vms.getStore();
            MemRegion mr = store.getHeap();
            TyAbstractClass subObjectType = getSuperClass( 0 ) ;
            int size = subObjectType.getNumBytes() + IntDatum.size + elements * getElementType().getNumBytes();
            int address = mr.findSpace(size);
        
        // Create and add the datum
            ArrayDatum d = new ArrayDatum(address, vms.getMemory(), this, elements, vms.getTimeManager());
            d.setNumberOfElements( elements ) ;
            vms.getStore().addDatum(d);

        int birthOrder = 0 ;
        int currentAddress = address ;
        
        // Create and add a sub object for the super class.
            String subObjectName = subObjectType.getTypeString() ;
            AbstractDatum subObject = subObjectType.makeMemberDatum
                ( vms, currentAddress, d, "."+subObjectName ) ;
            d.addSubObject( 0, birthOrder, (AbstractObjectDatum) subObject ) ;
            subObject.defaultInitialize() ;
            currentAddress += subObject.getNumBytes() ;
            birthOrder += 1 ;
        
        // Create and add a field for the length field
            TyInt intType = TyInt.get();
            IntDatum lengthField = (IntDatum) intType.makeMemberDatum(vms, address,                                                                 d, "length");
            d.addField( 0, birthOrder, lengthField ) ;
            lengthField.putValue(elements);
            currentAddress += lengthField.getNumBytes() ;
            birthOrder += 1 ;

        // Add fields for the array elements.
            for(int i = 0; i<elements; i++) {
                /*DBG System.out.println("Adding an element at "+elementAddress); */
                AbstractDatum f = getElementType().makeMemberDatum
                       (vms,
                        currentAddress,
                        d,
                        "[" + i + "]" );
                d.addField(i+1, birthOrder, f) ;
                f.defaultInitialize() ;
                currentAddress += getElementType().getNumBytes();
                birthOrder += 1 ; }
        
        // And that's that.
            return d;
    }

    public String getTypeString() {
        return "array of " + getElementType().getTypeString();
    }

    public String typeId() {
        return "[" +((TyJava)getElementType()).elementId();
    }

    public String elementId(){
        Assert.check(false);
        return "";
    }

    public boolean equal_types(TypeInterface t) {
        if(t instanceof TyJavaArray) {
            TyJavaArray passed = (TyJavaArray) t;
            if(passed.getElementType().equal_types(getElementType())) {
                return true ; } }
        return false;
    }
    
    public boolean isReachableByWideningFrom(TyJava fromType) {
        return false;
    }    
    
    
}