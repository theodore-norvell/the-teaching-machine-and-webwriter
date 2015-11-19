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

package tm.clc.analysis;

public class CGRAndTest extends CGRTest {
    private CGRTest operand0, operand1 ;

    public CGRAndTest( CGRTest operand0, CGRTest operand1) {
        this.operand0 = operand0 ;
        this.operand1 = operand1 ;
    }

    public boolean applies(ExpressionPtr exp) {
        return operand0.applies(exp) && operand1.applies(exp) ;
    }
}