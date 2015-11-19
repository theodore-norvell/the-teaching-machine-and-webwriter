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
 * Created on 2009-06-01 by Theodore S. Norvell. 
 */
package tm.clc.parser;

import java.util.regex.*;

import tm.utilities.Assert;

public class CommonParserHelper {
    static Pattern pat = Pattern.compile("/\\*\\#/?[Tt]\\s*(([a-zA-Z])*)\\s*\\*/") ;
    
    public static String extractTagName(String image ) {
        Matcher mat = pat.matcher(image) ;
        if( mat.find() ) {
            return mat.group(1) ;
        }
        else {
            Assert.check( "CommonParserHelper.extractTagName fails to match") ;
            return null ;
        }
    }
}
