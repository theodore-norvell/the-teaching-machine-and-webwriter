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

/** An expression node for plain old function calls. */
public class OpFuncCall  extends OpAbsFuncCall  {
    public OpFuncCall ( TypeNode t,
                   String function_name,
                   boolean operator_syntax,
                   Object function_key,
                   NodeList arguments ) {
        super( "OpFuncCall", false, false, t, function_name, null, operator_syntax,
               false, function_key, false, null, null, arguments, null ) ;
    }
}
