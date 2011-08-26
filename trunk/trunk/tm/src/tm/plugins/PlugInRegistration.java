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
 * Created on 7-Jul-2006 by Theodore S. Norvell. 
 */
package tm.plugins;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/** A class representing the registration of a plug in factory.
 * Each registration is basically a triple of three strings:
 *     <ul><li>jackName -- This is the name of a jack
 *         <li>factoryClassName -- This is the fully qualified name of the factory class for the plug-in.
 *         <ii>parameter -- This is a string passed as a parameter when the factory object is created.
 *     </ul>
 * 
 * @author theo
 */
public class PlugInRegistration implements Comparable<PlugInRegistration> {

    private String jackName ;
    private String factoryClassName ;
    private String parameter ;
    private Boolean isActive ;
    private PlugInFactory cachedFactoryObject = null;
    
    public PlugInRegistration(String jackName, String className, String parameter ) {
        this( jackName, className, parameter, true ) ;
    }
    
    public PlugInRegistration(String jackName, String className, String parameter, boolean isActive ) {
        this.jackName = jackName ;
        this.factoryClassName = className ;
        this.parameter = parameter ;
        this.isActive = isActive ;
    }

    /**
     * @return Returns the factoryClassName.
     */
    String getClassName() {
        return factoryClassName;
    }

    /**
     * @return Returns the jackName.
     */
    String getJackName() {
        return jackName;
    }

    /**
     * @return Returns the parameter.
     */
    String getParameter() {
        return parameter;
    }
    
    /**
     * Active registrations are used to make plug-ins
     * @return
     */
    boolean isActive() { return isActive ; }

    public int compareTo(PlugInRegistration o) {
        int result = jackName.compareTo( o.jackName ) ;
        if( result != 0 ) return result ;
        result = factoryClassName.compareTo( o.factoryClassName ) ;
        if( result != 0 ) return result ;
        result = parameter.compareTo( o.parameter ) ;
        if( result != 0 ) return result ;
        if( isActive == o.isActive ) return 0 ;
        return isActive ? -1 : +1 ;
    }
    
    public boolean equals(Object obj) {
        if( obj instanceof PlugInRegistration )
            return 0==compareTo( (PlugInRegistration) obj ) ;
        else
            return false ;
    }
    
    /**
     * <P><strong>Precondition:</strong>
     * <ul><li>isActive
     *     </li>
     * </ul>
     * @return
     * @throws PlugInNotFound
     */
    PlugInFactory createFactoryObject() throws PlugInNotFound {
        
        if( cachedFactoryObject != null ) 
            return cachedFactoryObject ;
        
        String className = getClassName() ;
        String parameter = getParameter() ;
        Class factoryClass ;
        try {
            factoryClass = Class.forName( className ) ;}
        catch( ClassNotFoundException e ) {
            throw new PlugInNotFound( "Class '"+className+"' not found.", e ) ; }
        Class stringClass ;
        try {
            stringClass = Class.forName( "java.lang.String" ) ; }
        catch( ClassNotFoundException e ) {
            throw new PlugInNotFound( "Class 'java.lang.String' not found.", e ) ; }
        
        Method getInstanceMethod ;
        try {
            getInstanceMethod = factoryClass.getMethod("createInstance", new Class[]{stringClass} ) ; }
        catch( NoSuchMethodException e) {
            throw new PlugInNotFound( "Method 'createInstance(String)' not found in class '"+className+"'.", e ) ; }
        
        int modifiers = getInstanceMethod.getModifiers() ;
        if( ! Modifier.isStatic( modifiers )) {
            throw new PlugInNotFound( "Method 'createInstance(String)' of class '"+className+"' is not static '"+className+"'." ) ;
        }
        if( ! Modifier.isPublic( modifiers )) {
            throw new PlugInNotFound( "Method 'createInstance(String)' of class '"+className+"' is not public '"+className+"'." ) ;
        }
        
        Object plugInFactory ;
        try {
            plugInFactory = getInstanceMethod.invoke( null, new Object[] {parameter} ) ; }
        catch( Throwable e) {
            throw new PlugInNotFound( "Method 'createInstance(String)' of class '"+className+"' fails.", e ) ; }
        
        if( !( plugInFactory instanceof PlugInFactory ) ) {
            throw new PlugInNotFound( "Method 'createInstance(String)' of class '"+className+"' returns an object that is not a PlugInFactory." ) ; }
        
        cachedFactoryObject = (PlugInFactory) plugInFactory ;
        return cachedFactoryObject ;
    }

}