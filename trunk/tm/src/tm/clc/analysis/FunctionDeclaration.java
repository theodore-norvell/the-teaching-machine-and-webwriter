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

package tm.clc.analysis;




import java.util.*;

import tm.clc.ast.TyAbstractFun;
import tm.clc.ast.TypeNode;

/**
 * Represents a function declaration from the perspective of name resolution. 
 * <br>Function <code>Declarations</code> are identified by their parameter
 * lists as well as their names.
 *
 * @created Oct 12 2001
 * @author Derek Reilly
 */

public class FunctionDeclaration extends Declaration {

    private Vector pTypes = new Vector (); // an ordered list of parameter types
    // does a parameter have a default value ?
    private Vector pDefault = new Vector (); 

    private boolean ellipsis = false; // an elipsis in parameter list ?

    /* javadoc from base class */
    public FunctionDeclaration (LFlags cat, ScopedName name, Definition defn,
                                SpecifierSet specifiers, TyAbstractFun type) { 
        super (cat, name, defn, specifiers, type);
        buildParameterList (type);
        if(type != null ) ellipsis = type.endsWithElipsis() ;
    }
    
    public FunctionDeclaration (LFlags cat, ScopedName name) {
        this (cat, name, null, null, null);
    }



    /**
     * Adds a parameter to the list of parameters for this function.
     * <em>Note:</em> order of parameters implicit in calls to this method
     * @param pType a <code>TypeNode</code> representing the type of the
     * parameter
     * @param hasDefault does this parameter have a default argument ?
     */
    public void addParameter (TypeNode pType, boolean hasDefault) { 
        pTypes.addElement (pType);
        pDefault.addElement (new Boolean (hasDefault));
    }

    /**
     * Build the parameter list using the corresponding AST representation.
     * @param params a <code>NodeList</code> of <code>TypedNodeInterface</code> 
     * representing each function parameter in order
     */
    private void buildParameterList (TyAbstractFun tyFun) {
        // NOTE: Orgiginally this routine stripped the CVQ attributes from each parameter.
        // The way it did so was in error. (Using tn.setAttributes(0) had
        // the unfortunate side effect of stripping the CVQ attributes from every type that
        // shared the type node!) Also this is a C++ specific action.
        // The justification was
        ///   * This is to 
        ///   * permit proper runtime identification of functions.
        ///   * <p>The <code>FunctionDeclaration</code>, as the compile-time
        ///   * representation of the function, will retain the cvq information.
        // So if we are having trouble with runtime identification, may this is why.
        // TSN 2005 Oct 26.

        if (tyFun != null) {
            for (int i = 0, sz=tyFun.getParamCount() ; i < sz ; i++) {
                
                TypeNode tn = tyFun.getParamType (i);
                boolean hasDefault = false; // default values not yet supported
                addParameter (tn.getClone (), hasDefault);
                // The following code is axed. See note above. TSN 2005 Oct 28.
                //// // !! note : this next line is C++-specific. To-date all (..)
                //// //    declaration code has been language independent. 
                //// tn.setAttributes (0); // clear outermost attributes in tyFun
            }
        }
    }


    /**
     * Returns the number of parameters in this function
     * @return the number of parameters
     */
    public int getParameterCount () { return pTypes.size (); }

    /**
     * Returns the set of parameters in this function
     * @return the set of parameters
     */
    public Vector getParameters () { return pTypes; }

    /**
     * Returns the <code>TypeNode</code> corresponding to the parameter
     * type at the provided index, or <code>null</null> if the index is 
     * out of bounds
     * @param idx the index of the parameter
     * @return the parameter type at <code>idx</code> or <code>null</code>
     */
    public TypeNode getParameter (int idx) { 
        TypeNode param = null;
        if (pTypes.size () > idx) param = (TypeNode) pTypes.elementAt (idx);
        return param;
    }

    /**
     * Called to indicate that an ellipsis exists in the parameter list for
     * this function.
     */
    public void ellipsisInParamList () { ellipsis = true; }

    /**
     * Indicates whether this function has an ellipsis in the parameter list.
     * @return a boolean indicator
     */
    public boolean hasEllipsis () { return ellipsis; }

    /**
     * Indicates whether the function has default values for the parameters
     * positioned after the indicated index.
     * @param index the position after which we will test for the existence of
     * parameter default values
     * @return a boolean indicator
     */
    public boolean defaultValuesAfter (int index) { 
        for (int i = index + 1; i < pDefault.size (); i++) 
            if (pDefault.elementAt(i).equals (Boolean.FALSE)) return false;
        return true;
    }

    /** 
     * Gives the index of the first paramenter without a default value
     * @return the index of the first parameter without a default value
     */
    public int firstParamWithoutDefaultValue () {
        for (int i = 0; i < pDefault.size (); i++) 
            if (pDefault.elementAt(i).equals (Boolean.FALSE)) return i;
        return -1;

    }
}



