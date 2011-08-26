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

package tm.configuration;


public class StringTokenizer {
    private String myString;
    int cursor;
    
    public StringTokenizer(String or){
        myString = or.trim();
        cursor = 0;
    }
    
    public void reset(){
        cursor = 0;
    }
    
    public String nextToken(){
        if (cursor < myString.length()) {
            int start = cursor;
            while (cursor < myString.length()) {
                if (isWhiteSpace(myString.charAt(cursor))) break;
                cursor++;
            }
            String token = myString.substring(start,cursor);
            advanceToNextToken();
            return token;
        } else return null;
    }
    
    public String theRest(){
        return myString.substring(cursor);
    }

    /*  Tokens must start with non-white space
    */
    private void advanceToNextToken(){
        while (cursor < myString.length()) {
            if (!isWhiteSpace(myString.charAt(cursor))) return;
            cursor++;
        }
    }
    
    private boolean isWhiteSpace(char c){
    	return c == ' ' || c == '\t';
    }
}
        