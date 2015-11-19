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

//** An expression node for function member calls. */
public class OpMemberCall extends OpAbsFuncCall
{
    public OpMemberCall ( TypeNode t,
        String function_name,
        String operator,
        boolean use_operator_syntax,
        boolean suppress_recipient,
        Object function_key,
        boolean isVirtual,
        ExpressionNode recipient,
        int [] path, 
        NodeList arguments ) {
        this( t, function_name, operator, use_operator_syntax,
               suppress_recipient, function_key, isVirtual, recipient, path,
               arguments, null ) ;
    }
    
    public OpMemberCall ( TypeNode t,
        String function_name,
        String operator,
        boolean use_operator_syntax,
        boolean suppress_recipient,
        Object function_key,
        boolean isVirtual,
        ExpressionNode recipient,
        int [] path, 
        NodeList arguments,
        ExpressionNode repetitions ) {
        super( "OpMemberCall", true, false, t, function_name, operator, use_operator_syntax,
               suppress_recipient, function_key, isVirtual, path, recipient, arguments,
               repetitions ) ;
        //System.out.println("Building OpMemberCall to " + function_name ) ;
        //System.out.println("   Recipient is\n" + recipient.ppToString(3, 80) ) ;
        //System.out.println("   argument list has" + arguments.length() + " members" ) ;
    }
}