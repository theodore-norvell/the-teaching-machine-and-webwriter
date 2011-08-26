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
import tm.clc.analysis.Declaration;
import tm.clc.analysis.ScopedName;
import tm.clc.ast.TyAbstractClassDeclared;
import tm.clc.ast.TyAbstractPointer;
import tm.clc.ast.VarNode;
import tm.clc.datum.AbstractDatum;
import tm.clc.datum.AbstractObjectDatum;
import tm.clc.rtSymTab.RT_Symbol_Table;
import tm.javaLang.datum.ClassDatum;
import tm.javaLang.datum.ObjectDatum;
import tm.utilities.Assert;
import tm.virtualMachine.Memory;
import tm.virtualMachine.VMState;

/** Represents a class in Java.
 * 
 * @author theo
 *
 */
public class TyClass extends TyAbstractClassDeclared implements TyJava {
	
	private TyMetaClass myMetaClass;
    
    private TyClass myDirectSuperClass ;
    
    private String staticInitializationChainName ;
    
    private String dynamicInitializationChainName ;

    public TyClass(ScopedName nm, ScopedName fqn, Declaration decl) {
        super(nm.getName(), fqn, decl);
        myMetaClass = new TyMetaClass(nm);
    }

    protected AbstractObjectDatum constructDatum(int add, AbstractDatum p,
                                                 Memory m, String n, BTTimeManager timeMan) {
        return new ObjectDatum(add, p, m, n, this, timeMan);
    }

    public String typeId() { return getFullyQualifiedName().getName(); }

    public String elementId(){
    	Assert.check(false);
    	return null;
    	}
    
    public TyMetaClass getMetaClass(){ return myMetaClass;}
    
    public void setDefined(){
    	super.setDefined();
		myMetaClass.setDefined();
    }
    
    
    public void addStaticField(VarNode field ) {
	    myMetaClass.addField(field);
    }
    
    public boolean isReachableByWideningFrom(TyJava fromType) {

        if (!(fromType instanceof TyClass)) return false;
        
        TyClass subClass = (TyClass)fromType;  // Checking if fromType is a subClass of this
        for (int i = 0; i < subClass.superClassCount(); i++) {
            TyClass superClass = (TyClass)subClass.getSuperClass(i);
            if (this.equal_types(superClass) || // If I am one of the superclasses
                this.isReachableByWideningFrom(superClass) )   // or it is widening to go from superclass to me
                    return true;
        }
        return false;
    }
    
    public void setStaticInitializationChainName(String staticInitializationChainName) {
        this.staticInitializationChainName = staticInitializationChainName ; }

    public void setDynamicInitializationChainName(String dynamicInitializationChainName) {
        this.dynamicInitializationChainName = dynamicInitializationChainName ; }
    
    public String getStaticInitializationChainName() {
        return staticInitializationChainName ; }
    
    public String getDynamicInitializationChainName() {
        return dynamicInitializationChainName ; }

    /** Set the direct superclass.
     * Should be called for any TyClass other than those
     * representing interfaces and that representing 
     * java.lang.Object.
     */
    public void setDirectSuperClass(TyClass parent) {
        myDirectSuperClass = parent ; 
    }
    
    /**
     * @return the direct superclass if there is one. null if this is
     * an interface or is java.lang.Object.
     */
    public TyClass getDirectSuperClass() {
        return myDirectSuperClass;
    }
    
    private ClassDatum findClassDatum( VMState vms ) {
        RT_Symbol_Table rtSymTab = (RT_Symbol_Table) vms.getSymbolTable() ;
        return (ClassDatum) rtSymTab.getDatum( getFullyQualifiedName() ) ;
    }

    /** Determine whether the static initialization code for a
     * class has been run yet, or not. 
     * @param vms
     * @return whether the class has been initialized
     */
    public boolean isInitialized(VMState vms) {
        return findClassDatum( vms ).isInitialized() ;
    }

    /** Indicate that the class has been initialized.
     * @param vms
     */
    public void setInitialized(VMState vms) {
        findClassDatum( vms ).setInitialized() ;
    }
}