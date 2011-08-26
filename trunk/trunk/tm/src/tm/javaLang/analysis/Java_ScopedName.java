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

/*
 * Java_ScopedName.java
 *
 * Created on March 12, 2003, 2:23 PM
 */

package tm.javaLang.analysis;

import tm.clc.analysis.ScopedName;
import tm.utilities.Assert;


/** Represents a ScopedName in the Java Language
 * @author Michael Bruce-Lockhart
 */
public class Java_ScopedName extends ScopedName {
    
    /** This name has not been categorized */    
    public static final int UNCATEGORIZED = 0;
    /** a <I>PackageName</I> per JLS [6.5] */    
    public static final int PACKAGE = 1;
    /** a <I>TypeName</I> per JLS [6.5] */    
    public static final int TYPE = 2;
    /** an <I>ExpressionName</I> per JLS [6.5] */    
    public static final int EXPRESSION = 3;
    /** an <I>AmbiguousName</I> per JLS [6.5] */    
    public static final int AMBIGUOUS = 4;
    private static final int LIMIT = 5;
    
    private int category = UNCATEGORIZED; // Added by mpbl, april '03
    
    /** The name of the java.lang.Object class */
    public static Java_ScopedName JAVA_LANG_OBJECT = new Java_ScopedName( "java.lang.Object") ;
    /** The name of the java.lang.String class */
    public static Java_ScopedName JAVA_LANG_STRING = new Java_ScopedName( "java.lang.String") ;

    /**
     * Creates a new <code>Java_ScopedName</code> instance.
     * 
     * @param rawName either an initial terminal id, a simple name, or
     *  a string representation of a qualified name.
     */
    
    public Java_ScopedName (String name) { 
            super(name);
    }
    
    /**
     * Creates a new <code>Java_ScopedName</code> instance.
     *
     * @param elements the components of the name.
     */
    public Java_ScopedName (String elements[]){
    	super(elements);
    }


    
    /**
     * Copy constructor. 
     * @param orig a <code>ScopedName</code> value
     */
    public Java_ScopedName (ScopedName orig) { 
        super(orig);
    }
    
    /**
	 * Copy constructor. 
	 * @param orig a <code>ScopedName</code> value
	 */
    public Java_ScopedName (Java_ScopedName orig) { 
		super(orig);
   }
        
/**
* Implementation of <code>clone</code> using the copy constructor.
* @return a copy of this <code>ScopedName</code>
*/
    public synchronized Object clone () {
	return new Java_ScopedName (this);
    }

    /**
     * Get the language specific scope operator.
     * @return "."
     */
    public String getScopeOp(){return ".";} 

    /** Attach a language specific category to the name. Can only be set once and the
     * split point must already have been set.
     * @param cat the category as defined by JLS [6.5]
     */    
    public void setCategory(int cat){
        Assert.check(cat >= UNCATEGORIZED && cat < LIMIT, "invalid category in" + toString());
        if (category == UNCATEGORIZED) category = cat;
    }
    
    
    

    /**
     * Iretrieve the name's category?
     * @return the language specific category
     */
    public int getCategory () { return category; }


   
    
}
