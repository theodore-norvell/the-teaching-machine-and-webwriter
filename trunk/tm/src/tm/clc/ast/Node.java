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

public abstract class Node {
    String _name ;
    NodeList _children ;

    public Node(String name, NodeList children) {
    _name = name ;
    _children = children ; }

    public Node (String name) {
        this( name, new NodeList() ) ;
    }

    public Node(String name, Node a ) {
        this(name) ;
        _children.addFirstChild( a ) ; }

    public Node(String name, Node a, Node b ) {
        this(name, b) ;
        _children.addFirstChild( a ) ; }
    public Node(String name, Node a, Node b, Node c ) {
        this(name, b, c) ;
        _children.addFirstChild( a ) ; }

    public Node(String name, Node a, Node b, Node c, Node d ) {
        this(name, b, c, d) ;
        _children.addFirstChild( a ) ; }

    public Node(String name, Node a, NodeList others ) {
        _name = name ;
        _children = new NodeList( others ) ;
        _children.addFirstChild( a ) ; }

    public void addFirstChild( Node xx ) {
        _children.addFirstChild(xx) ; }

    public void addLastChild( Node xx ) {
        _children.addLastChild(xx) ; }

    public String name() { return _name ; }

    public NodeList children() { return _children ; }

    public Node child(int i) { return _children.get(i) ; }

    public int childCount() { return _children.length() ; }

    public String toString() {
        NodeList childrenForDump = getChildrenForDump() ;
        if( childrenForDump.isEmpty() ) {
            return formatNodeData() ; }
        else {
            return formatNodeData() + childrenForDump.toString() ; } }

    public String ppToString(int indent, int width) {
        String result = "" ;
        Vector V = new Vector() ;
        final String filler = blanks(indent);
        prettyPrint( V, width-indent ) ;
        Enumeration E = V.elements() ;
        while( E.hasMoreElements() ) {
            String s = (String) E.nextElement() ;
            result = result + filler + s + "\n" ; }
        return result ; }

    String formatNodeData() { return _name ; } ;

    void prettyPrint(Vector result, int spaceLeft ) {
        String s = toString() ;
        NodeList childrenForDump = getChildrenForDump() ;
        if( s.length() <= spaceLeft
        || childrenForDump.isEmpty() ) {
            result.addElement( s ) ; }
        else {
            String nodeData =  formatNodeData();
            result.addElement( nodeData + "[" ) ;
            int spc = spaceLeft - 3 ;
            Vector tempVect = new Vector() ;
            childrenForDump.prettyPrint( tempVect, spc ) ;
            String bbb = blanks(3) ;
            Enumeration tempEnum = tempVect.elements() ;
            while( tempEnum.hasMoreElements() ) {
                String ss = (String) tempEnum.nextElement() ;
                String sss = bbb+ss ;
                result.addElement(sss) ; } } }

    protected NodeList getChildrenForDump() {
        return _children ;
    }

    private String blanks( int len ) {
        StringBuffer bbb = new StringBuffer(len) ;
        for(int i=0 ; i < len ; ++i ) {
            bbb.append(' ') ; }
        return bbb.toString() ; }


}

