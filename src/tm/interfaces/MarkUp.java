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

package tm.interfaces;

public class MarkUp {

    public int column ;

    public int command ;
    
    public TagSetInterface tagSet ;

    public MarkUp( int col, short comm ) {
        column = col ;
        command = comm ; }

    public MarkUp( int col, short comm, TagSetInterface ts ) {
        column = col ;
        command = comm ;
        tagSet = ts ;}

    public static final short NORMAL = 0,
                              KEYWORD = 1,
                              COMMENT = 2,
                              PREPROCESSOR = 3,
                              CONSTANT = 4,
                              CHANGE_TAG_SET = 5 ;

    public String toString() {
        return "("+column+", "+command+")" ; }
}