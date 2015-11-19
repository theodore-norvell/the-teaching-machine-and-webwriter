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

import java.util.ArrayList;
import java.util.Set;
import java.util.Vector;

import tm.interfaces.CodeLine;
import tm.interfaces.MarkUp;
import tm.interfaces.SelectionInterface;
import tm.interfaces.SourceCoords;
import tm.interfaces.TagSetInterface;


public class VMCodeLine implements CodeLine {
    private char[] chars ;
    private MarkUp[] markUp ;
    private SourceCoords coords ;
    /** A list of all tag sets that apply anywhere on this line */
    private Set<TagSetInterface> tagSets ;

    public VMCodeLine( StringBuffer buff, Vector<MarkUp> markUpVector, SourceCoords sc, Set<TagSetInterface> tagSets) {
        chars = new char[ buff.length() ] ;
        if( buff.length() > 0 ) // Work around jview bug!
            buff.getChars(0, buff.length(), chars, 0);

        markUp = new MarkUp[ markUpVector.size() ] ;
        for( int i = 0, sz = markUpVector.size() ; i < sz ; ++i ) {
            markUp[i] = markUpVector.elementAt(i) ; }

        coords = sc ;
        this.tagSets = tagSets ;
    }

    public char[] getChars() { return chars ; }

    public SourceCoords getCoords() { return coords ; }

    public MarkUp[] markUp() { return markUp ; }

    /** Return true if the selection expression is valid anywhere on this line. */
    public boolean isSelected( SelectionInterface currentSelection ) {
        for( TagSetInterface tagSet : tagSets ) {
            if( tagSet.selectionIsValid( currentSelection ) ) return true ;
        }
        return false ; }
    

    public String toString() {
        String ch = new String(chars) ;
        StringBuffer mu = new StringBuffer("[") ;
        for(int i=0 ; i < markUp.length ; ++i ) {
            if( i>0 ) mu.append(", ") ; mu.append( markUp[i] ) ; }
        mu.append("]") ;
        return "ClcCodeLine "+coords+" <"+ch+">, "+tagSets+", "+mu ; }
}