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

/** An expression node for calls to this object. */
public class OpThisMemberCall extends OpAbsFuncCall
{
    public OpThisMemberCall ( TypeNode t,
        String function_name,
        Object function_key,
        boolean isVirtual,
        int [] path,
        NodeList arguments ) {
        super( "OpThisMemberCall", true, true, t, function_name, null, false,
               false, function_key, isVirtual, path, null, arguments, null ) ;
    }
}