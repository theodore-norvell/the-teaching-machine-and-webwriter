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

import java.util.*;

public class NodeList {

	Vector lll ;

    public NodeList () { lll = new Vector(0) ; }

    public NodeList (Node c) {
		lll = new Vector(1) ;
		lll.addElement( c ) ; }

    public NodeList (NodeList nl) {
		lll = new Vector( nl.lll.size() ) ;
		for( int i = 0, sz = nl.lll.size() ; i < sz ; ++i ) {
    		lll.addElement( nl.lll.elementAt(i) ) ; } }

    public void addFirstChild( Node c )	{
		lll.insertElementAt( c, 0 ) ; }
        
    public void addLastChild( Node c ) {
		lll.addElement( c ) ; }

    public void addListAtEnd( NodeList xtl ) {
		for( int i=0, sz=xtl.length() ; i < sz ; ++i ) {
			lll.addElement( xtl.get(i) ) ; } }

    public boolean isEmpty() { return lll.size()==0 ; }

	public Node get(int i) { return (Node) lll.elementAt( i ) ; }

	public int length() { return lll.size() ; }

	public String toString() {
		String result ;
		if( isEmpty() ){ result = "[]" ; }
		else {
			result = "[" + lll.elementAt(0).toString() ;
			for( int i = 1, sz = lll.size() ; i < sz ; ++i ) {
				result = result+ ", " + lll.elementAt(i).toString() ; }
			result = result + "]" ; }
		return result ; }

	void prettyPrint(Vector result, int spaceLeft) {
		for( int i = 0, sz = lll.size() ; i < sz ; ++i ) {
			get(i).prettyPrint( result, spaceLeft ) ;
			//Need to add a , or a ]
			int len = result.size() ;
			String lastline = (String) result.elementAt(len-1) ;
			if( i+1 == sz ) {
				lastline += "]" ; }
			else {
				lastline += "," ; }
			result.setElementAt(lastline, len-1) ; } }
}
