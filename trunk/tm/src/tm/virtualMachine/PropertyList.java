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

package tm.virtualMachine;

import tm.backtrack.BTTimeManager;
import tm.backtrack.*;

/** A function from strings to Objects. */
public class PropertyList
{
    protected final BTTimeManager timeMan ;
    private final BTVector<String> names  ;
    private final BTVector<Object> properties ;
    
    public PropertyList(BTTimeManager tm ){
        timeMan = tm ;
        names = new BTVector<String>( tm ) ;
        properties = new BTVector<Object>( tm ) ;
    }
    
// Copy constructor - not full deep - creates new vectors but with refs to original properties
    protected PropertyList(PropertyList original){
        this( original.timeMan ) ;
        for( int i = 0, sz = names.size() ; i < sz ; ++i ) {
            names.addElement( original.names.elementAt(i) ) ;
            properties.addElement( original.properties.elementAt(i) ) ;
        }
    }
    
    public void setProperty( String name, Object info )
    {
        int i ;
        int sz = names.size() ;
        for( i=0 ; i < sz ; ++i ) {
            String nm = names.elementAt(i) ;
            if( nm.equals( name ) ) break ; }
        if( i == sz ) {
            names.addElement( name ) ;
            properties.addElement( info ) ; }
        else {
            properties.setElementAt( info, i ) ; }
    }
    
    public Object getProperty(String name)
    {
        int i ;
        int sz = names.size() ;
        for( i=0 ; i < sz ; ++i ) {
            String nm = (String) names.elementAt(i) ;
            if( nm.equals( name ) ) break ; }
        if( i == sz ) {
            return null ; }
        else {
            return properties.elementAt( i ) ; }
    }
}