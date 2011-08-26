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
 * Created on Jun 8, 2005
 *
 */
package tm.clc.ast;

import java.util.Hashtable;

/** Print a graph of statement nodes.
 * @author theo
 *
 */
public class StatementPrinter {
    private static class Numberer implements StatementNodeVisitor {
        private Hashtable h ;
        private int count = 0 ;
        
        Numberer( Hashtable h ) { this.h = h ; }
        
        public boolean visited( StatementNode nd ) {
            return h.containsKey( nd ) ; }
        
        public void visit( StatementNode nd ) {
            h.put( nd, new Integer( count++ ) ) ; }
    }
    
    private static class Printer implements StatementNodeVisitor {
        private Hashtable v = new Hashtable() ;
        private Hashtable h ;
        private StringBuffer buf ;
        
        Printer( Hashtable h, StringBuffer buf ) {
            this.h = h ;
            this.buf = buf ;}
        
        public boolean visited( StatementNode nd ) {
            return v.containsKey( nd ) ; }
        
        public void visit( StatementNode nd ) {
            buf.append( nd.toString( h ) ) ;
            v.put( nd, new Integer( 0 ) ) ; }
        }
    
    static public void FormatStatement( StringBuffer buf, StatementNodeLink link ) {
        Hashtable h = new Hashtable() ;
        link.beVisited( new Numberer( h ) ) ;
        link.beVisited( new Printer( h, buf ) ) ; }
}
